/**
 * Copyright Sergey Olefir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.solf.extra2.concurrent.retry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.DefaultLocation;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.javatuples.Pair;

import io.github.solf.extra2.concurrent.retry.RetryAndRateLimitService.RRLAfterRequestAttemptFailedDecision;
import io.github.solf.extra2.concurrent.retry.RetryAndRateLimitService.RRLDelayQueueProcessingDecision;
import io.github.solf.extra2.concurrent.retry.RetryAndRateLimitService.RRLEntry;
import io.github.solf.extra2.concurrent.retry.RetryAndRateLimitService.RRLMainQueueProcessingDecision;

/**
 * Events listener interface for {@link RetryAndRateLimitService}
 *
 * @author Sergey Olefir
 */
//Exclude TYPE_ARGUMENT as we will allow null return values.
@NonNullByDefault({DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE, DefaultLocation.FIELD, DefaultLocation.TYPE_BOUND, DefaultLocation.ARRAY_CONTENTS})
public interface RRLEventListener<@Nonnull Input, Output>
{
	/**
	 * Reports an assertion error in the code.
	 */
	public void errorAssertionError(@Nullable RRLEntry<Input, Output> entry, String message);
	
	/**
	 * Request was rejected (not added to processing).
	 * <p>
	 * Classified as 'error' because it seems like something that 'should not happen'.
	 * 
	 * @param errMsg error message reported to the client
	 */
	public void errorRequestRejected(Input request, long timeLimitMs, long delayBeforeFirstAttempMs, String errMsg);

	/**
	 * Reports SPI method exception.
	 */
	public void errorSpiMethodException(@Nullable RRLEntry<Input, Output> entry, Throwable t);
	/**
	 * Reports event listener method exception.
	 * zzz if entry is always null, remove it as arg?
	 */
	public void errorEventListenerMethodException(@Nullable RRLEntry<Input, Output> entry, Throwable t);

	/**
	 * Reports unexpected {@link InterruptedException}
	 */
	public void errorUnexpectedInterruptedException(InterruptedException e, String message);
	
	/**
	 * Reports unexpected {@link RuntimeException}
	 */
	public void errorUnexpectedRuntimeException(RuntimeException e, String message);
	
	/**
	 * Final failure of request processing (request never completed without
	 * errors).
	 * 
	 * @param t if not null, then last attempt failure was caused by this
	 * 		{@link Throwable}; if null, then {@link RRLMainQueueProcessingDecision#FINAL_FAILURE}
	 * 		was returned by {@link #spiMainQueueProcessingDecision(RRLEntry, boolean, boolean)}
	 */
	public void requestFinalFailure(RRLEntry<Input, Output> entry, @Nullable Throwable t);
	
	/**
	 * Final timeout of request processing (request never completed without
	 * errors before and now it is too late to try).
	 * 
	 * @param remainingValidityTime how long is remaining until request is still
	 * 		valid; in case of timeout this is often expected to be negative
	 * 		or zero/close-to-zero (negative means request is past validity time)
	 */
	public void requestFinalTimeout(RRLEntry<Input, Output> entry, long remainingValidityTime);
	
	/**
	 * Final timeout of request processing (request never completed without
	 * errors before and now it is too late to try).
	 * 
	 * @param remainingValidityTime how long is remaining until request is still
	 * 		valid; in case of timeout this is often expected to be negative
	 * 		or zero/close-to-zero (negative means request is past validity time)
	 */
	public void requestExecuting(RRLEntry<Input, Output> entry, int attemptNumber, long remainingValidityTime);
	
	/**
	 * Request succeeded.
	 */
	public void requestSuccess(RRLEntry<Input, Output> entry, Output result, int attemptNumber, long requestDuration);
	
	/**
	 * Request attempt failed.
	 */
	public void requestAttemptFailed(RRLEntry<Input, Output> entry, Exception exception, int attemptNumber, long requestDuration);
	
	/**
	 * Request attempt failed.
	 */
	public void requestAttemptFailedDecision(RRLEntry<Input, Output> entry, Pair<RRLAfterRequestAttemptFailedDecision, Long> decision);
	
	/**
	 * Request added to processing.
	 */
	public void requestAdded(RRLEntry<Input, Output> entry);
	
	/**
	 * Request removed from processing -- either due to completion or any of
	 * possible errors/timeouts/etc.
	 * <p>
	 * This kind of duplicates {@link #requestSuccess(RRLEntry, Object, int, long)}
	 * etc. but sometimes it is more convenient to handle stuff in one place.
	 */
	public void requestRemoved(RRLEntry<Input, Output> entry);
	
	
	/**
	 * Reports main queue processing decision for the item (note that decision
	 * process is potentially invoked multiple times per item -- every operation
	 * that potentially takes time (reserving a thread, obtaining a ticket)
	 * results in a new decision afterwards.
	 */
	public void mainQueueProcessingDecision(@Nullable RRLEntry<Input, Output> entry,
		Pair<RRLMainQueueProcessingDecision, Long> decision, long itemProcessingSince);
	
	/**
	 * Reports main queue has obtained a thread required for request processing
	 * for specific item.
	 * aaa must handle case when thread was NOT obtained
	 */
	public void mainQueueThreadObtained(@Nullable RRLEntry<Input, Output> entry,
		long itemProcessingSince, long timeTakenToObtainThread);
	
	/**
	 * Reports main queue has obtained a thread required for request processing
	 * for specific item.
	 */
	public void mainQueueTicketObtainAttempt(@Nullable RRLEntry<Input, Output> entry,
		long itemProcessingSince, boolean ticketObtained, long timeTakenVirtualMs);
	
	/**
	 * Reports main queue has completed processing of an item.
	 */
	public void mainQueueProcessingCompleted(@Nullable RRLEntry<Input, Output> entry,
		long itemProcessingSince, long timeTakenVirtualMs);
	
	
	/**
	 * Reports delay queue item about to enter potential delay step (i.e. when
	 * delay queue processing retrieved an item and needs to decide what to do
	 * with it, including potentially sleeping). 
	 */
	public void delayQueueItemBeforeDelayStep(@Nullable RRLEntry<Input, Output> entry, 
		long queueDelayMs, long remainingDelay);
	
	/**
	 * Reports delay queue processing decision after single delay step.
	 */
	public void delayQueueDecisionAfterDelayStep(@Nullable RRLEntry<Input, Output> entry, long queueDelayMs, 
		RRLDelayQueueProcessingDecision decision, long sleptFor, long remainingDelay);
}
