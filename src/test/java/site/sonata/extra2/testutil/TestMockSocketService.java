/**
 * [[[LICENSE-NOTICE]]]
 */
package site.sonata.extra2.testutil;

import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static site.sonata.extra2.util.NullUtil.nnChecked;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.ParametersAreNonnullByDefault;

import org.testng.annotations.Test;

import site.sonata.extra2.concurrent.exception.WAInterruptedException;
import site.sonata.extra2.concurrent.exception.WATimeoutException;
import site.sonata.extra2.exception.AssertionException;
import site.sonata.extra2.testutil.MockSocketData;
import site.sonata.extra2.testutil.MockSocketService;
import site.sonata.extra2.testutil.TestUtil;

/**
 * Tests for {@link MockSocketService}
 *
 * @author Sergey Olefir
 */
@ParametersAreNonnullByDefault
public class TestMockSocketService
{
	/** self-documenting */
	@Test
	public void testAssertNoConnectedSocketMocks() throws Exception
	{
		MockSocketService service = new MockSocketService(100);
		
		service.assertNoConnectedSocketMocks();
		
		service.connectSocket("addr", 123);
		try
		{
			service.assertNoConnectedSocketMocks();
			assert false;
		} catch (AssertionException e)
		{
			// ok
		}
		
		service.getAndClearAllConnectedSocketMocks();
		service.assertNoConnectedSocketMocks();
	}
	
	/** self-documenting */
	@Test
	public void testConnectSocket() throws Exception
	{
		MockSocketService service = new MockSocketService(100);
		
		service.assertNoConnectedSocketMocks();
		
		try (Socket mockSocket = service.connectSocket("addr", 123))
		{
			verify(mockSocket).connect(new InetSocketAddress("addr", 123), 0);
			
			MockSocketData socketData = service.getTheOnlyConnectedSocketMock();
			assertEquals(socketData.getMockSocket(), mockSocket);
		}
		
		assertEquals(service.getAndClearAllConnectedSocketMocks().size(), 1);
		
		service.assertNoConnectedSocketMocks();
	}
	
	/** self-documenting */
	@Test
	public void testConnectSocketWithTimeout() throws Exception
	{
		MockSocketService service = new MockSocketService(100);
		
		service.assertNoConnectedSocketMocks();
		
		try (Socket mockSocket = service.connectSocket("addr", 123, 4567))
		{
			verify(mockSocket).connect(new InetSocketAddress("addr", 123), 4567);
			
			MockSocketData socketData = service.getAndClearTheOnlyConnectedSocketMock();
			assertEquals(socketData.getMockSocket(), mockSocket);
		}
		
		service.assertNoConnectedSocketMocks();
	}
	
	/** self-documenting */
	@SuppressWarnings("resource")
	@Test
	public void testGetAllConnectedSocketMocks() throws Exception
	{
		MockSocketService service = new MockSocketService(100);
		
		assertEquals(service.getAllConnectedSocketMocks().size(), 0);
		service.assertNoConnectedSocketMocks();
		
		try (Socket mockSocket = service.connectSocket("addr", 123))
		{
			verify(mockSocket).connect(new InetSocketAddress("addr", 123), 0);
			assertEquals(service.getAllConnectedSocketMocks().size(), 1);
			assertEquals(service.getAllConnectedSocketMocks().getFirst().getMockSocket(), mockSocket);
			
			Socket mockSocket2 = service.connectSocket("addr2", 234);
			assertEquals(service.getAllConnectedSocketMocks().size(), 2);
			assertEquals(service.getAllConnectedSocketMocks().getFirst().getMockSocket(), mockSocket);
			assertEquals(service.getAllConnectedSocketMocks().getLast().getMockSocket(), mockSocket2);
			
			Socket mockSocket3 = service.connectSocket("addr3", 345);
			LinkedBlockingDeque<MockSocketData> allMocks = service.getAllConnectedSocketMocks();
			for (Socket ms : new Socket[] {mockSocket, mockSocket2, mockSocket3})
			{
				assertSame(nnChecked(allMocks.pollFirst()).getMockSocket(), ms);
			}
			
			assertSame(service.getAllConnectedSocketMocks(), allMocks);
			assertEquals(service.getAllConnectedSocketMocks().size(), 0);
		}
		
		service.assertNoConnectedSocketMocks();
	}
	
	
	/** self-documenting */
	@SuppressWarnings("resource")
	@Test
	public void testGetAllConnectedSocketMocksClone() throws Exception
	{
		MockSocketService service = new MockSocketService(100);
		
		assertEquals(service.getAllConnectedSocketMocksClone().size(), 0);
		service.assertNoConnectedSocketMocks();
		
		try (Socket mockSocket = service.connectSocket("addr", 123))
		{
			verify(mockSocket).connect(new InetSocketAddress("addr", 123), 0);
			assertEquals(service.getAllConnectedSocketMocksClone().size(), 1);
			assertEquals(service.getAllConnectedSocketMocksClone().getFirst().getMockSocket(), mockSocket);
			
			Socket mockSocket2 = service.connectSocket("addr2", 234);
			assertEquals(service.getAllConnectedSocketMocksClone().size(), 2);
			assertEquals(service.getAllConnectedSocketMocksClone().getFirst().getMockSocket(), mockSocket);
			assertEquals(service.getAllConnectedSocketMocksClone().getLast().getMockSocket(), mockSocket2);
			
			Socket mockSocket3 = service.connectSocket("addr3", 345);
			LinkedBlockingDeque<MockSocketData> allMocks = service.getAllConnectedSocketMocksClone();
			for (Socket ms : new Socket[] {mockSocket, mockSocket2, mockSocket3})
			{
				assertSame(nnChecked(allMocks.pollFirst()).getMockSocket(), ms);
			}
			
			assertNotSame(service.getAllConnectedSocketMocksClone(), allMocks);
			assertEquals(service.getAllConnectedSocketMocksClone().size(), 3);
		}
	}
	
	/** self-documenting */
	@SuppressWarnings("resource")
	@Test
	public void testGetAndClearAllConnectedSocketMocks() throws Exception
	{
		MockSocketService service = new MockSocketService(100);
		
		assertEquals(service.getAndClearAllConnectedSocketMocks().size(), 0);
		service.assertNoConnectedSocketMocks();
		
		try (Socket mockSocket = service.connectSocket("addr", 123))
		{
			verify(mockSocket).connect(new InetSocketAddress("addr", 123), 0);
			LinkedBlockingDeque<MockSocketData> mocks = service.getAndClearAllConnectedSocketMocks();
			service.assertNoConnectedSocketMocks();
			assertEquals(mocks.size(), 1);
			assertEquals(mocks.getFirst().getMockSocket(), mockSocket);
			
			Socket mockSocket2 = service.connectSocket("addr2", 234);
			Socket mockSocket3 = service.connectSocket("addr3", 345);
			mocks = service.getAndClearAllConnectedSocketMocks();
			service.assertNoConnectedSocketMocks();
			assertEquals(mocks.size(), 2);
			assertEquals(mocks.getFirst().getMockSocket(), mockSocket2);
			assertEquals(mocks.getLast().getMockSocket(), mockSocket3);
			
			assertNotSame(service.getAllConnectedSocketMocks(), mocks);
			assertEquals(service.getAndClearAllConnectedSocketMocks().size(), 0);
		}
		
		service.assertNoConnectedSocketMocks();
	}
	
	
	/** self-documenting */
	@Test
	public void testGetAndClearTheOnlyConnectedSocketMock() throws Exception
	{
		MockSocketService service = new MockSocketService(100);
		
		try
		{
			service.getAndClearTheOnlyConnectedSocketMock();
			assert false;
		} catch (NoSuchElementException e)
		{
			// good
		}
		service.assertNoConnectedSocketMocks();
		
		try (Socket mockSocket = service.connectSocket("addr", 123))
		{
			verify(mockSocket).connect(new InetSocketAddress("addr", 123), 0);
			MockSocketData mockSocketData = service.getAndClearTheOnlyConnectedSocketMock();
			service.assertNoConnectedSocketMocks();
			assertEquals(mockSocketData.getMockSocket(), mockSocket);
			
			service.connectSocket("addr2", 234);
			@SuppressWarnings("resource") Socket mockSocket3 = service.connectSocket("addr3", 345);
			try
			{
				mockSocketData = service.getAndClearTheOnlyConnectedSocketMock();
				assert false;
			} catch (IllegalStateException e)
			{
				assert e.getMessage().contains("[2] connected mock sockets instead of exactly one") : e;
			}
			
			// test getting back to one works
			while (service.getAllConnectedSocketMocks().size() > 1)
				service.getAllConnectedSocketMocks().remove();
			assertSame(service.getAndClearTheOnlyConnectedSocketMock().getMockSocket(), mockSocket3);
		}
		
		service.assertNoConnectedSocketMocks();
	}
	
	
	/** self-documenting */
	@SuppressWarnings("resource")
	@Test
	public void testGetLastConnectedSocketMock() throws Exception
	{
		MockSocketService service = new MockSocketService(100);
		
		try
		{
			service.getLastConnectedSocketMock();
			assert false;
		} catch (NoSuchElementException e)
		{
			// good
		}
		service.assertNoConnectedSocketMocks();
		
		try (Socket mockSocket = service.connectSocket("addr", 123))
		{
			service.getLastConnectedSocketMock();
			
			verify(mockSocket).connect(new InetSocketAddress("addr", 123), 0);
			assertSame(service.getLastConnectedSocketMock().getMockSocket(), mockSocket);
			
			Socket mockSocket2 = service.connectSocket("addr2", 234);
			verify(mockSocket2).connect(new InetSocketAddress("addr2", 234), 0);
			assertSame(service.getLastConnectedSocketMock().getMockSocket(), mockSocket2);
			
			Socket mockSocket3 = service.connectSocket("addr3", 345);
			verify(mockSocket3).connect(new InetSocketAddress("addr3", 345), 0);
			assertSame(service.getLastConnectedSocketMock().getMockSocket(), mockSocket3);
			
			assertEquals(service.getAllConnectedSocketMocks().size(), 3);
		}
	}
	
	
	/** self-documenting */
	@SuppressWarnings("resource")
	@Test
	public void testGetTheOnlyConnectedSocketMock() throws Exception
	{
		MockSocketService service = new MockSocketService(100);
		
		try
		{
			service.getTheOnlyConnectedSocketMock();
			assert false;
		} catch (NoSuchElementException e)
		{
			// good
		}
		service.assertNoConnectedSocketMocks();
		
		try (Socket mockSocket = service.connectSocket("addr", 123))
		{
			verify(mockSocket).connect(new InetSocketAddress("addr", 123), 0);
			assertSame(service.getTheOnlyConnectedSocketMock().getMockSocket(), mockSocket);
			
			Socket mockSocket2 = service.connectSocket("addr2", 234);
			verify(mockSocket2).connect(new InetSocketAddress("addr2", 234), 0);
			try
			{
				service.getTheOnlyConnectedSocketMock();
				assert false;
			} catch (IllegalStateException e)
			{
				assert e.toString().contains("[2] connected mock sockets instead of exactly one") : e;
			}
			
			Socket mockSocket3 = service.connectSocket("addr3", 345);
			verify(mockSocket3).connect(new InetSocketAddress("addr3", 345), 0);
			try
			{
				service.getTheOnlyConnectedSocketMock();
				assert false;
			} catch (IllegalStateException e)
			{
				assert e.toString().contains("[3] connected mock sockets instead of exactly one") : e;
			}
			
			// Test getting back to 'exactly one' works.
			service.getAllConnectedSocketMocks().remove();
			service.getAllConnectedSocketMocks().remove();
			assertSame(service.getTheOnlyConnectedSocketMock().getMockSocket(), mockSocket3);

			
			assertEquals(service.getAllConnectedSocketMocks().size(), 1);
		}
	}
	
	
	/** self-documenting */
	@SuppressWarnings("resource")
	@Test
	public void testWaitForTheOnlyConnectedSocketMock() throws Exception
	{
		TestUtil.runWithTimeLimit(5000, () -> {
			
			MockSocketService service = new MockSocketService(100);
			
			try
			{
				service.waitForAndClearTheOnlyConnectedSocketMock(1000);
				assert false;
			} catch (WATimeoutException e)
			{
				// good
			}
			service.assertNoConnectedSocketMocks();
			
			try (Socket mockSocket = service.connectSocket("addr", 123))
			{
				verify(mockSocket).connect(new InetSocketAddress("addr", 123), 0);
				assertSame(service.waitForAndClearTheOnlyConnectedSocketMock(100).getMockSocket(), mockSocket);
				service.assertNoConnectedSocketMocks();
				
				service.connectSocket("addr2", 234);
				Socket mockSocket3 = service.connectSocket("addr3", 345);
				try
				{
					service.waitForAndClearTheOnlyConnectedSocketMock(100);
					assert false;
				} catch (IllegalStateException e)
				{
					assert e.toString().contains("[2] connected mock sockets instead of one or none") : e;
				}
				
				// Test getting back to 'exactly one' works.
				service.getAllConnectedSocketMocks().remove();
				assertSame(service.waitForAndClearTheOnlyConnectedSocketMock(100).getMockSocket(), mockSocket3);
				service.assertNoConnectedSocketMocks();
	
				// Test asynchronous socket connection (the 'wait for' part).
				{
					TestUtil.runAsynchronously(() -> {Thread.sleep(2000);service.connectSocket("addr4", 456);});
					long start = System.currentTimeMillis();
					MockSocketData mockSocketData = service.waitForAndClearTheOnlyConnectedSocketMock(3000);
					long duration = System.currentTimeMillis() - start;
					assert duration > 1000 : duration;
					assert duration < 3000 : duration;
					verify(mockSocketData.getMockSocket()).connect(new InetSocketAddress("addr4", 456), 0);
				}
				
				// Test 'waitFor' interruption.
				{
					final Thread testThread = Thread.currentThread();
					TestUtil.runAsynchronously(() -> {Thread.sleep(2000); testThread.interrupt();});
					long start = System.currentTimeMillis();
					try
					{
						service.waitForAndClearTheOnlyConnectedSocketMock(3000);
						assert false;
					} catch (WAInterruptedException e)
					{
						// good.
					}
					long duration = System.currentTimeMillis() - start;
					assert duration > 1000 : duration;
					assert duration < 3000 : duration;
				}
			}
			
			service.assertNoConnectedSocketMocks();
		});
	}
}
