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
package io.github.solf.extra2.testutil;

import static io.github.solf.extra2.util.NullUtil.nullable;

import java.util.Arrays;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.DefaultLocation;
import org.eclipse.jdt.annotation.NonNullByDefault;

import io.github.solf.extra2.concurrent.ConsumerWithException;
import io.github.solf.extra2.concurrent.FunctionWithException;
import io.github.solf.extra2.concurrent.RunnableWithException;
import io.github.solf.extra2.concurrent.SupplierWithException;
import io.github.solf.extra2.util.TypeUtil;

/**
 * Additional assertions not available in TestNG asserts.
 *
 * @author Sergey Olefir
 */
@NonNullByDefault
public class AssertExtra
{
	/**
	 * Asserts that given actual value is greater than targetValue
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <T> void assertGreater(@Nonnull T actual, @Nonnull Comparable<T> targetValue, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		if (nullable(actual) == null)
			throw new IllegalArgumentException("'actual' is null");
		if (nullable(targetValue) == null)
			throw new IllegalArgumentException("'targetValue' is null");
		
		if (targetValue.compareTo(actual) >= 0)
			throw new IllegalStateException("actual value '" + actual + "' is not greater than '" + targetValue + "'" + (messages == null ? "" : ": " + Arrays.toString(messages)));
	}
	
	/**
	 * Asserts that given actual value is greater or equal than targetValue
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <T> void assertGreaterOrEqual(@Nonnull T actual, @Nonnull Comparable<T> targetValue, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		if (nullable(actual) == null)
			throw new IllegalArgumentException("'actual' is null");
		if (nullable(targetValue) == null)
			throw new IllegalArgumentException("'targetValue' is null");
		
		if (targetValue.compareTo(actual) > 0)
			throw new IllegalStateException("actual value '" + actual + "' is not greater or equal than '" + targetValue + "'" + (messages == null ? "" : ": " + Arrays.toString(messages)));
	}
	
	/**
	 * Asserts that given actual value is less than targetValue
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <T> void assertLess(@Nonnull T actual, @Nonnull Comparable<T> targetValue, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		if (nullable(actual) == null)
			throw new IllegalArgumentException("'actual' is null");
		if (nullable(targetValue) == null)
			throw new IllegalArgumentException("'targetValue' is null");
		
		if (targetValue.compareTo(actual) <= 0)
			throw new IllegalStateException("actual value '" + actual + "' is not less than '" + targetValue + "'" + (messages == null ? "" : ": " + Arrays.toString(messages)));
	}
	
	/**
	 * Asserts that given actual value is less than targetValue
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <T> void assertLessOrEqual(@Nonnull T actual, @Nonnull Comparable<T> targetValue, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		if (nullable(actual) == null)
			throw new IllegalArgumentException("'actual' is null");
		if (nullable(targetValue) == null)
			throw new IllegalArgumentException("'targetValue' is null");
		
		if (targetValue.compareTo(actual) < 0)
			throw new IllegalStateException("actual value '" + actual + "' is not less or equal than '" + targetValue + "'" + (messages == null ? "" : ": " + Arrays.toString(messages)));
	}
	
	
	
	
	
	
	/**
	 * Asserts that given actual value is greater than targetValue
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	public static <T> void assertGreater(@Nonnull T actual, @Nonnull Comparable<T> targetValue)
		throws IllegalArgumentException, IllegalStateException
	{
		assertGreater(actual, targetValue, (String[])null);
	}
	
	/**
	 * Asserts that given actual value is greater or equal than targetValue
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	public static <T> void assertGreaterOrEqual(@Nonnull T actual, @Nonnull Comparable<T> targetValue)
		throws IllegalArgumentException, IllegalStateException
	{
		assertGreaterOrEqual(actual, targetValue, (String[])null);
	}
	
	/**
	 * Asserts that given actual value is less than targetValue
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	public static <T> void assertLess(@Nonnull T actual, @Nonnull Comparable<T> targetValue)
		throws IllegalArgumentException, IllegalStateException
	{
		assertLess(actual, targetValue, (String[])null);
	}
	
	/**
	 * Asserts that given actual value is less than targetValue
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	public static <T> void assertLessOrEqual(@Nonnull T actual, @Nonnull Comparable<T> targetValue)
		throws IllegalArgumentException, IllegalStateException
	{
		assertLessOrEqual(actual, targetValue, (String[])null);
	}
	
	/**
	 * Asserts that the given block fails with a {@link Throwable} which is returned
	 * (throws exception if block doesn't fail with throwable).
	 */
	public static <@Nonnull R extends Throwable> R assertFails(RunnableWithException r)
		throws IllegalArgumentException, IllegalStateException
	{
		return assertFails(r, (String[])null);
	}
	
	/**
	 * Asserts that the given block fails with a {@link Throwable} which is returned
	 * (throws exception if block doesn't fail with throwable).
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <@Nonnull R extends Throwable> R assertFails(RunnableWithException r, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		if (nullable(r) == null)
			throw new IllegalArgumentException("Parameter code block must not be null.");
		
		try
		{
			r.run();
		} catch (Throwable e)
		{
			// this is expected, good
			return TypeUtil.coerce(e);
		}
		
		throw new IllegalStateException("code block didn't fail (didn't throw Throwable)" + (messages == null ? "" : ": " + Arrays.toString(messages)));
	}

	
	/**
	 * Asserts that the given block fails with a {@link Throwable} whose toString()
	 * contains given substring (throws exception if block doesn't fail with 
	 * throwable or it doesn't contain required substring).
	 */
	public static <@Nonnull R extends Throwable> R assertFailsWithSubstring(
		RunnableWithException r, String substring)
			throws IllegalArgumentException, IllegalStateException
	{
		return assertFailsWithSubstring(r, substring, (String[])null);
	}
	
	/**
	 * Asserts that the given block fails with a {@link Throwable} whose toString()
	 * contains given substring (throws exception if block doesn't fail with 
	 * throwable or it doesn't contain required substring).
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <@Nonnull R extends Throwable> R assertFailsWithSubstring(
		RunnableWithException r, String substring, String @Nullable... messages)
			throws IllegalArgumentException, IllegalStateException
	{
		R result = assertFails(r, messages);
		assertContains( result.toString(), substring, messages);
		
		return result;
	}

	
	/**
	 * Asserts that the given block fails with a {@link Throwable} whose toString()
	 * contains given substring (case-insensitive) (throws exception if block doesn't fail with 
	 * throwable or it doesn't contain required substring).
	 */
	public static <@Nonnull R extends Throwable> R assertFailsWithSubstringIgnoreCase(
		RunnableWithException r, String substring)
			throws IllegalArgumentException, IllegalStateException
	{
		return assertFailsWithSubstringIgnoreCase(r, substring, (String[])null);
	}
	
	/**
	 * Asserts that the given block fails with a {@link Throwable} whose toString()
	 * contains given substring (case-insensitive) (throws exception if block doesn't fail with 
	 * throwable or it doesn't contain required substring).
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <@Nonnull R extends Throwable> R assertFailsWithSubstringIgnoreCase(
		RunnableWithException r, String substring, String @Nullable... messages)
			throws IllegalArgumentException, IllegalStateException
	{
		R result = assertFails(r, messages);
		assertContainsIgnoreCase( result.toString(), substring, messages);
		
		return result;
	}

	/**
	 * Assert that given code block doesn't throw any exceptions.
	 * <p>
	 * Useful to e.g. wrap checked-exception-throwing blocks so that they don't
	 * throw any checked exception.s
	 */
	public static void assertPasses(RunnableWithException r, String @Nullable... messages)
	{
		if (nullable(r) == null)
			throw new IllegalArgumentException("Parameter code block must not be null.");
		
		assertPasses(() -> {r.run(); return null;}, messages);
	}

	/**
	 * Assert that given code block doesn't throw any exceptions.
	 * <p>
	 * Useful to e.g. wrap checked-exception-throwing blocks so that they don't
	 * throw any checked exception.s
	 */
	public static <T> T assertPasses(Callable<T> c, String @Nullable... messages)
	{
		if (nullable(c) == null)
			throw new IllegalArgumentException("Parameter code block must not be null.");
		
		try
		{
			return c.call();
		} catch (Exception e)
		{
			throw new IllegalStateException("code block failed: " + (((messages == null) || (messages.length == 0)) ? "" : ": " + Arrays.toString(messages) + ": ")  + e, e);
		}
	}
	
	/**
	 * Asserts that haystack (via toString()) contains needle.
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static void assertContains(@Nullable Object haystack, @Nullable String needle)
		throws IllegalArgumentException, IllegalStateException
	{
		assertContains(haystack, needle, (String[])null);
	}
	
	/**
	 * Asserts that haystack (via toString()) contains needle.
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static void assertContains(@Nullable Object haystack, @Nullable String needle, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		assertContainsWithFlag(true, haystack, needle, messages);
	}
	
	/**
	 * Asserts that haystack (via toString()) contains needle (case-insensitive).
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 */
	public static void assertContainsIgnoreCase(@Nullable Object haystack, @Nullable String needle)
	{
		assertContainsIgnoreCase(haystack, needle, (String[])null);
	}
	
	/**
	 * Asserts that haystack (via toString()) contains needle (case-insensitive).
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static void assertContainsIgnoreCase(@Nullable Object haystack, @Nullable String needle, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		assertContainsWithFlagIgnoreCase(true, haystack, needle, messages);
	}

	
	/**
	 * Asserts that haystack (via toString()) contains needle (or not, depending on flag).
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 * 
	 * @param mustContain a flag -- 'true' is for 'must contain', 'false' for
	 * 		'must NOT contain'
	 * @param message additional informational messages, may be null
	 */
	public static void assertContainsWithFlag(boolean mustContain, @Nullable Object haystack, @Nullable String needle, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		if ((haystack == null) || (needle == null))
			throw new IllegalArgumentException("Haystack and needle must be non null, got: haystack[" + haystack + "] and needle [" + needle + "]" + (messages == null ? "" : ": " + Arrays.toString(messages)));
		
		if (haystack.toString().contains(needle) != mustContain)
		{
			Throwable t = null;
			if (haystack instanceof Throwable)
				t = (Throwable)haystack;
			throw new IllegalStateException("haystack[" + haystack + "] " + (mustContain ? "doesn't contain" : "contains") + " needle [" + needle + "]" + (messages == null ? "" : ": " + Arrays.toString(messages)), t);
		}
	}
	
	/**
	 * Asserts that haystack (via toString()) contains needle (or not, depending on flag) -- 
	 * case-insensitive.
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 * 
	 * @param mustContain a flag -- 'true' is for 'must contain', 'false' for
	 * 		'must NOT contain'
	 * @param message additional informational messages, may be null
	 */
	public static void assertContainsWithFlagIgnoreCase(boolean mustContain, @Nullable Object haystack, @Nullable String needle, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		assertContainsWithFlag(mustContain, haystack == null ? null : haystack.toString().toLowerCase(), needle == null ? null : needle.toLowerCase(), messages);		
	}


	/**
	 * Asserts that haystack (via toString()) does NOT contain needle.
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static void assertNotContains(@Nullable Object haystack, @Nullable String needle)
		throws IllegalArgumentException, IllegalStateException
	{
		assertNotContains(haystack, needle, (String[])null);
	}
	
	/**
	 * Asserts that haystack (via toString()) does NOT contain needle.
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static void assertNotContains(@Nullable Object haystack, @Nullable String needle, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		assertContainsWithFlag(false, haystack, needle, messages);
	}
	
	/**
	 * Asserts that haystack (via toString()) does NOT contain needle (case-insensitive).
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 */
	public static void assertNotContainsIgnoreCase(@Nullable Object haystack, @Nullable String needle)
	{
		assertNotContainsIgnoreCase(haystack, needle, (String[])null);
	}
	
	/**
	 * Asserts that haystack (via toString()) does NOT contain needle (case-insensitive).
	 * <p>
	 * If either or both argument are null, that's a failure too.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static void assertNotContainsIgnoreCase(@Nullable Object haystack, @Nullable String needle, String @Nullable... messages)
		throws IllegalArgumentException, IllegalStateException
	{
		assertContainsWithFlagIgnoreCase(false, haystack, needle, messages);
	}
	
	/**
	 * Asserts that given actual value is between given bounds (with flags).
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param mustBeBetween if true, then must be between; false MUST NOT be between
	 * @param inclusiveBounds if boundary values are included in the range
	 * @param message additional informational messages, may be null
	 */
	public static <T> void assertBetweenWithFlags(
		boolean mustBeBetween, @Nonnull T actual, 
		@Nonnull Comparable<T> lowerBound, @Nonnull Comparable<T> upperBound, 
		boolean inclusiveBounds, String @Nullable... messages)
			throws IllegalArgumentException, IllegalStateException
	{
		if (mustBeBetween)
		{
			if (inclusiveBounds)
			{
				assertGreaterOrEqual(actual, lowerBound, messages);
				assertLessOrEqual(actual, upperBound, messages);
			}
			else
			{
				assertGreater(actual, lowerBound, messages);
				assertLess(actual, upperBound, messages);
			}
		}
		else
		{
			// Using exceptions here isn't the most elegant way, but it's the most expedient
			if (inclusiveBounds)
			{
				IllegalStateException first = null;
				try
				{
					assertLess(actual, lowerBound, messages);
				} catch (IllegalStateException e)
				{
					first = e;
				}
				
				try
				{
					assertGreater(actual, upperBound, messages);
				} catch (IllegalStateException e)
				{
					// Both asserts must fail for this to fail
					if (first != null)
						throw first;
				}
			}
			else
			{
				IllegalStateException first = null;
				try
				{
					assertLessOrEqual(actual, lowerBound, messages);
				} catch (IllegalStateException e)
				{
					first = e;
				}
				
				try
				{
					assertGreaterOrEqual(actual, upperBound, messages);
				} catch (IllegalStateException e)
				{
					// Both asserts must fail for this to fail
					if (first != null)
						throw first;
				}
			}
		}
	}
	
	
	/**
	 * Asserts that given actual value is between given bounds (bounds are inclusive).
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <T> void assertBetweenInclusive(
		@Nonnull T actual, 
		@Nonnull Comparable<T> lowerBound, @Nonnull Comparable<T> upperBound, 
		String @Nullable... messages)
			throws IllegalArgumentException, IllegalStateException
	{
		assertBetweenWithFlags(true, actual, lowerBound, upperBound, true, messages);
	}
	
	/**
	 * Asserts that given actual value is between given bounds (bounds are inclusive).
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	public static <T> void assertBetweenInclusive(
		@Nonnull T actual, 
		@Nonnull Comparable<T> lowerBound, @Nonnull Comparable<T> upperBound)
			throws IllegalArgumentException, IllegalStateException
	{
		assertBetweenInclusive(actual, lowerBound, upperBound, (String[])null);
	}
	
	/**
	 * Asserts that given actual value is NOT between given bounds (bounds are inclusive).
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <T> void assertNotBetweenInclusive(
		@Nonnull T actual, 
		@Nonnull Comparable<T> lowerBound, @Nonnull Comparable<T> upperBound, 
		String @Nullable... messages)
			throws IllegalArgumentException, IllegalStateException
	{
		assertBetweenWithFlags(false, actual, lowerBound, upperBound, true, messages);
	}
	
	/**
	 * Asserts that given actual value is NOT between given bounds (bounds are inclusive).
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	public static <T> void assertNotBetweenInclusive(
		@Nonnull T actual, 
		@Nonnull Comparable<T> lowerBound, @Nonnull Comparable<T> upperBound)
			throws IllegalArgumentException, IllegalStateException
	{
		assertNotBetweenInclusive(actual, lowerBound, upperBound, (String[])null);
	}
	
	
	/**
	 * Asserts that given actual value is between given bounds (bounds are exclusive).
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <T> void assertBetweenExclusive(
		@Nonnull T actual, 
		@Nonnull Comparable<T> lowerBound, @Nonnull Comparable<T> upperBound, 
		String @Nullable... messages)
			throws IllegalArgumentException, IllegalStateException
	{
		assertBetweenWithFlags(true, actual, lowerBound, upperBound, false, messages);
	}
	
	/**
	 * Asserts that given actual value is between given bounds (bounds are exclusive).
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	public static <T> void assertBetweenExclusive(
		@Nonnull T actual, 
		@Nonnull Comparable<T> lowerBound, @Nonnull Comparable<T> upperBound)
			throws IllegalArgumentException, IllegalStateException
	{
		assertBetweenExclusive(actual, lowerBound, upperBound, (String[])null);
	}
	
	/**
	 * Asserts that given actual value is NOT between given bounds (bounds are exclusive).
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param message additional informational messages, may be null
	 */
	public static <T> void assertNotBetweenExclusive(
		@Nonnull T actual, 
		@Nonnull Comparable<T> lowerBound, @Nonnull Comparable<T> upperBound, 
		String @Nullable... messages)
			throws IllegalArgumentException, IllegalStateException
	{
		assertBetweenWithFlags(false, actual, lowerBound, upperBound, false, messages);
	}
	
	/**
	 * Asserts that given actual value is NOT between given bounds (bounds are exclusive).
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	public static <T> void assertNotBetweenExclusive(
		@Nonnull T actual, 
		@Nonnull Comparable<T> lowerBound, @Nonnull Comparable<T> upperBound)
			throws IllegalArgumentException, IllegalStateException
	{
		assertNotBetweenExclusive(actual, lowerBound, upperBound, (String[])null);
	}
	
	/**
	 * Asserts that at least of the given paths completes successfully (without
	 * exception); at least one path must be specified.
	 * <p>
	 * Paths are evaluated until one of them succeeds, everything after that is
	 * skipped.
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	public static int assertAnyOk(RunnableWithException... paths)
		throws IllegalArgumentException, IllegalStateException
	{
		if (paths.length == 0)
			throw new IllegalArgumentException("Must provide at least one path, got: " + Arrays.toString(paths));
		
		Throwable last = null;
		int index = 0;
		for (RunnableWithException path : paths)
		{
			index++;
			
			try
			{
				path.run();
				
				return index; // success, return path index
			} catch (Throwable e)
			{
				// This path failed, try another one
				last = e;
			}
		}
		
		throw new IllegalStateException("All paths failed, last exception: " + last, last);
	}
	
	/**
	 * Evaluates given expression and (with the evaluated value) 
	 * asserts that at least of the given paths completes successfully (without
	 * exception); at least one path must be specified.
	 * <p>
	 * Paths are evaluated until one of them succeeds, everything after that is
	 * skipped.
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	@SafeVarargs
	//Exclude TYPE_ARGUMENT as we will allow null return values.
	@NonNullByDefault({DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE, DefaultLocation.FIELD, DefaultLocation.TYPE_BOUND, DefaultLocation.ARRAY_CONTENTS})
	public static <V> int assertAnyOkWithEval(Callable<V> expresionToEval,
		ConsumerWithException<V>... paths)
		throws IllegalArgumentException, IllegalStateException
	{
		final V value;
		try
		{
			value = expresionToEval.call();
		} catch (Throwable e)
		{
			throw new IllegalStateException("Expression evaluation failed: " + e, e);
		}
		
		RunnableWithException[] runnables = new @Nonnull RunnableWithException[paths.length];
		for (int i = 0; i < paths.length; i++)
		{
			final ConsumerWithException<V> path = paths[i];
			runnables[i] = () -> path.accept(value);
		}
		
		try
		{
			return assertAnyOk(runnables);
		} catch (Throwable e)
		{
			throw new IllegalStateException("assertAnyOkWithEval failed for value [" + value + "]: " + e, e);
		}
	}
	
	/**
	 * With the given value 
	 * asserts that at least of the given paths completes successfully (without
	 * exception); at least one path must be specified.
	 * <p>
	 * Paths are evaluated until one of them succeeds, everything after that is
	 * skipped.
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	@SafeVarargs
	//Exclude TYPE_ARGUMENT as we will allow null return values.
	@NonNullByDefault({DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE, DefaultLocation.FIELD, DefaultLocation.TYPE_BOUND, DefaultLocation.ARRAY_CONTENTS})
	public static <V> int assertAnyOkWithValue(V value,
		ConsumerWithException<V>... paths)
		throws IllegalArgumentException, IllegalStateException
	{
		return assertAnyOkWithEval(() -> value, paths);
	}
	
	/**
	 * Asserts that at least of the given paths completes successfully (without
	 * exception AND returns true); at least one path must be specified.
	 * <p>
	 * Paths are evaluated until one of them succeeds, everything after that is
	 * skipped.
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	@SafeVarargs
	public static int assertAnyTrue(SupplierWithException<Boolean>... paths)
		throws IllegalArgumentException, IllegalStateException
	{
		
		RunnableWithException[] runnables = new @Nonnull RunnableWithException[paths.length];
		for (int i = 0; i < paths.length; i++)
		{
			SupplierWithException<Boolean> path = paths[i];
			runnables[i] = () -> {if (!Boolean.TRUE.equals(path.get())) throw new IllegalStateException("got false");};
		}
		
		return assertAnyOk(runnables);
	}
	
	/**
	 * Asserts that at least of the given paths completes successfully (without
	 * exception AND returns true); at least one path must be specified.
	 * <p>
	 * Paths are evaluated until one of them succeeds, everything after that is
	 * skipped.
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param msg additional message to add to exception in case of failure
	 */
	@SafeVarargs
	public static int assertAnyTrue(String msg, SupplierWithException<Boolean>... paths)
		throws IllegalStateException
	{
		try
		{
			return assertAnyTrue(paths);
		} catch (Throwable e)
		{
			throw new IllegalStateException(msg + ": " + e, e);
		}
	}
	
	/**
	 * Evaluates given expression and (with the evaluated value) 
	 * asserts that at least of the given paths completes successfully (without
	 * exception AND returns true); at least one path must be specified.
	 * <p>
	 * Paths are evaluated until one of them succeeds, everything after that is
	 * skipped.
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	@SafeVarargs
	//Exclude TYPE_ARGUMENT as we will allow null return values.
	@NonNullByDefault({DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE, DefaultLocation.FIELD, DefaultLocation.TYPE_BOUND, DefaultLocation.ARRAY_CONTENTS})
	public static <V> int assertAnyTrueWithEval(Callable<V> expresionToEval, FunctionWithException<V, @Nonnull Boolean>... paths)
		throws IllegalStateException
	{
		final V value;
		try
		{
			value = expresionToEval.call();
		} catch (Throwable e)
		{
			throw new IllegalStateException("Expression evaluation failed: " + e, e);
		}
		
		@SuppressWarnings("unchecked") SupplierWithException<Boolean>[] suppliers = new @Nonnull SupplierWithException[paths.length];
		for (int i = 0; i < paths.length; i++)
		{
			FunctionWithException<V, Boolean> path = paths[i];
			suppliers[i] = () -> path.apply(value);
		}
		
		try
		{
			return assertAnyTrue(suppliers);
		} catch (Throwable e)
		{
			throw new IllegalStateException("assertAnyTrueWithEval failed for value [" + value + "]: " + e, e);
		}
	}
	
	/**
	 * With the given value 
	 * asserts that at least of the given paths completes successfully (without
	 * exception AND returns true); at least one path must be specified.
	 * <p>
	 * Paths are evaluated until one of them succeeds, everything after that is
	 * skipped.
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 */
	@SafeVarargs
	//Exclude TYPE_ARGUMENT as we will allow null return values.
	@NonNullByDefault({DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE, DefaultLocation.FIELD, DefaultLocation.TYPE_BOUND, DefaultLocation.ARRAY_CONTENTS})
	public static <V> int assertAnyTrueWithValue(V value, FunctionWithException<V, @Nonnull Boolean>... paths)
		throws IllegalStateException
	{
		return assertAnyTrueWithEval(() -> value, paths);
	}
	
	/**
	 * Evaluates given expression and (with the evaluated value) 
	 * asserts that at least of the given paths completes successfully (without
	 * exception AND returns true); at least one path must be specified.
	 * <p>
	 * Paths are evaluated until one of them succeeds, everything after that is
	 * skipped.
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param msg additional message to add to exception in case of failure
	 */
	@SafeVarargs
	//Exclude TYPE_ARGUMENT as we will allow null return values.
	@NonNullByDefault({DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE, DefaultLocation.FIELD, DefaultLocation.TYPE_BOUND, DefaultLocation.ARRAY_CONTENTS})
	public static <V> int assertAnyTrueWithEval(String msg, Callable<V> expresionToEval, FunctionWithException<V, @Nonnull Boolean>... paths)
		throws IllegalStateException
	{
		try
		{
			return assertAnyTrueWithEval(expresionToEval, paths);
		} catch (Throwable e)
		{
			throw new IllegalStateException(msg + ": " + e, e);
		}
	}
	
	/**
	 * With the given value 
	 * asserts that at least of the given paths completes successfully (without
	 * exception AND returns true); at least one path must be specified.
	 * <p>
	 * Paths are evaluated until one of them succeeds, everything after that is
	 * skipped.
	 * <p>
	 * For wider compatibility this throws exception rather than assertion error.
	 * 
	 * @param msg additional message to add to exception in case of failure
	 */
	@SafeVarargs
	//Exclude TYPE_ARGUMENT as we will allow null return values.
	@NonNullByDefault({DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE, DefaultLocation.FIELD, DefaultLocation.TYPE_BOUND, DefaultLocation.ARRAY_CONTENTS})
	public static <V> int assertAnyTrueWithValueAndMessage(String msg, V value, FunctionWithException<V, @Nonnull Boolean>... paths)
		throws IllegalStateException
	{
		return assertAnyTrueWithEval(msg, () -> value, paths);
	}
}
