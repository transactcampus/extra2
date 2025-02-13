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
package io.github.solf.extra2.file;

import static io.github.solf.extra2.util.NullUtil.nnChecked;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Provides an interface to read file line-by-line and provides support for
 * 'pushing back' one or more lines to be read by subsequent {@link #readLine()}
 * <p>
 * Externally it all looks just like a collection of lines.
 * <p>
 * NOT thread-safe.
 * <p>
 * Implementation is strange because it started life as {@link IncludingFileReader}
 * (which is now a subclass)
 *
 * @author Sergey Olefir
 */
@NonNullByDefault
public class SimpleFileReader implements Closeable
{
	/**
	 * File inclusion pattern.
	 */
	private static final Pattern fileInclusionPattern = Pattern.compile("^ *\\[\\[(.*)\\]\\] *$");
		
	/**
	 * Stack of readers currently open (top-most is currently being read).
	 */
	private final Stack<Container> readerStack = new Stack<Container>();
	
	/**
	 * Internal {@link Deque} used to support 'push line' functionality.
	 */
	private final Deque<String> pushedStringsDeque = new LinkedBlockingDeque<String>();
	
	/**
	 * Whether file-inclusion functionality is active (if not active, then
	 * 'inclusion' lines are returned as-is without actually including anything).
	 */
	@Getter
	private boolean including;
	
	/**
	 * Line that was last read (by e.g. previous call to {@link #readLine()}).
	 * <p>
	 * Is null if no lines were read yet or end-of-file has been reached.
	 */
	@Getter
	@Nullable
	private String lastReadLine = null;
	
	/**
	 * Container for file & reader.
	 */
	@AllArgsConstructor
	protected static class Container
	{
		public final File file;
		public final BufferedReader reader;
	}
	
	/**
	 * Constructor.
	 */
	public SimpleFileReader(String fileName) throws IllegalStateException
	{
		this(fileName, false);
	}
	
	/**
	 * Constructor.
	 */
	public SimpleFileReader(File file) throws IllegalStateException
	{
		this(file, false);
	}
	
	/**
	 * Constructor.
	 */
	protected SimpleFileReader(String fileName, boolean useInclusion) throws IllegalStateException
	{
		this(new File(fileName), useInclusion);
	}
	
	/**
	 * Constructor.
	 */
	protected SimpleFileReader(File file, boolean useInclusion) throws IllegalStateException
	{
		this.including = useInclusion;
		readerStack.push(createContainer(file));
	}
	
	/**
	 * Creates file reader.
	 */
	protected static BufferedReader createReader(File file) throws IllegalStateException
	{
		if (!file.exists())
			throw new IllegalStateException("File doesn't exist: " + file);
		if (!file.isFile())
			throw new IllegalStateException("Not a file: " + file);
		
		try
		{
			return new BufferedReader(new FileReader(file));
		} catch( FileNotFoundException e )
		{
			throw new IllegalStateException("IOError: " + e, e);
		}
	}
	
	/**
	 * Creates container for a given file.
	 */
	protected static Container createContainer(File file) throws IllegalStateException
	{
		return new Container(file, createReader(file));
	}
	
	/**
	 * Creates container based on given (probably relative) file name and container
	 * that is used to determine current directory.
	 */
	@SuppressWarnings("unused")
	protected Container createContainerForIncludedFile(Container parentContainer, String fileName) throws IllegalStateException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Reads next line.
	 * 
	 * @return next line or null if no more lines
	 */
	@Nullable
	public String readLine() throws IllegalStateException
	{
		lastReadLine = internalReadLine();
		
		return lastReadLine;
	}
	
	/**
	 * Reads all remaining lines and separates them by '\n' (last line doesn't
	 * get '\n' at the end).
	 * <p>
	 * Throws exception if there are no lines to read.
	 * <p>
	 * After using this method you don't need to close the reader (it's auto-closed),
	 * though you still may do so.
	 */
	public String readAllRemainingLines() throws IllegalStateException
	{
		StringBuilder sb = new StringBuilder(2048);
		
		boolean firstLine = true;
		while (true)
		{
			String line = readLine();
			if (line == null)
				break;
			
			if (firstLine)
				firstLine = false;
			else
				sb.append('\n');
			
			sb.append(line);
		}
		
		if (firstLine)
			throw new IllegalStateException("There were no lines available.");
		
		return sb.toString();
	}
	
	/**
	 * Reads next line.
	 * 
	 * @return next line or null if no more lines
	 */
	@Nullable
	private String internalReadLine() throws IllegalStateException
	{
		String line = pushedStringsDeque.pollFirst();
		if (line != null)
			return line;
		
		Container container;
		try
		{
			while (true)
			{
				if (readerStack.isEmpty())
					return null; // Finished reading entire stack.
				
				container = readerStack.peek();
				line = container.reader.readLine(); 
				if (line != null)
					break;
				
				// Finished reading file.
				container.reader.close();
				readerStack.pop();
			}
		} catch (IOException e)
		{
			throw new IllegalStateException("IOError: " + e, e);
		}
		
		if (including)
		{
			Matcher matcher = fileInclusionPattern.matcher(line);
			if (matcher.matches())
			{
				// Handle file inclusion.
				String includedFileName = nnChecked(matcher.group(1));
				readerStack.push(createContainerForIncludedFile(container, includedFileName));
				
				// Recursively re-invoke so we read line from the included file.
				return readLine();
			}
		}
		
		return line;
	}

	
	/**
	 * Reads next line with trimming
	 * 
	 * @return next line or null if no more lines
	 */
	@Nullable
	public String readLineWithTrim() throws IllegalStateException
	{
		String line = readLine();
		if (line != null)
			line = line.trim();
		
		return line;
	}
	
	/**
	 * Line that was last read (by e.g. previous call to {@link #readLine()}) with
	 * trimming.
	 * <p>
	 * Is null if no lines were read yet or end-of-file has been reached.
	 */
	@Nullable
	public String getLastReadLineWithTrim() throws IllegalStateException
	{
		String line = getLastReadLine();
		if (line != null)
			line = line.trim();
		
		return line;
	}
	
	/**
	 * Closes this reader.
	 * If you've read all lines, you don't need to close it (though you still can).
	 */
	@Override
	public void close() throws IllegalStateException
	{
		while(!readerStack.isEmpty())
		{
			try
			{
				readerStack.pop().reader.close();
			} catch( IOException e )
			{
				throw new IllegalStateException("IOError: " + e, e);
			}
		}
	}
	
	/**
	 * 'Pushes' string back into the reader to be read by the subsequent
	 * {@link #readLine()} calls (before any lines from the actual underlying
	 * file are read).
	 * <p>
	 * This method pushes the new line in front of any of the previously pushed
	 * lines -- i.e. it will be read before the already-pushed lines.
	 */
	public SimpleFileReader pushFirst(String line)
	{
		pushedStringsDeque.addFirst(line);
		
		return this;
	}
	
	/**
	 * 'Pushes' string back into the reader to be read by the subsequent
	 * {@link #readLine()} calls (before any lines from the actual underlying
	 * file are read).
	 * <p>
	 * This method pushes the new line at the end of any of the previously pushed
	 * lines -- i.e. it will be read after the already-pushed lines.
	 */
	public SimpleFileReader pushLast(String line)
	{
		pushedStringsDeque.addLast(line);
		
		return this;
	}
	
	
	/**
	 * 'Pushes' last read string back into the reader to be read by the subsequent
	 * {@link #readLine()} calls (before any lines from the actual underlying
	 * file are read).
	 * <p>
	 * In other words it 'reverts' the last {@link #readLine()}
	 * <p>
	 * This method pushes the new line at the end of any of the previously pushed
	 * lines -- i.e. it will be read after the already-pushed lines.
	 */
	public SimpleFileReader pushLastReadLine()
	{
		String line = getLastReadLine();
		if (line != null)
			pushLast(line);
		
		return this;
	}
}
