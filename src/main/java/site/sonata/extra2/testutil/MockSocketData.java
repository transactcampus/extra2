/**
 * [[[LICENSE-NOTICE]]]
 */
package site.sonata.extra2.testutil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.annotation.ParametersAreNonnullByDefault;

import org.javatuples.Pair;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.sonata.extra2.exception.AssertionException;

/**
 * Encloses all data relevant to mock socket.
 *
 * @author Sergey Olefir
 */
@ParametersAreNonnullByDefault
@RequiredArgsConstructor
public class MockSocketData
{
	/**
	 * InetAdd
	 */
	public static final InetAddress MOCK_SOCKET_INET_ADDRESS;
	static
	{
		try
		{
			MOCK_SOCKET_INET_ADDRESS = InetAddress.getByAddress(new byte[] {98, 76, 54, 32});
		} catch (UnknownHostException e)
		{
			throw new AssertionException(e);
		}
	}
	
	/**
	 * 'Warms up' services -- particularly mock framework -- this should make
	 * timing execution of socket mock stuff much more reliable.
	 * 
	 * @see MockSocketService#warmUp()
	 */
	/*package*/ static void warmUp()
	{
		mock(Socket.class); // this is an actual warm-up routine
	}
	
	/**
	 * Socket mock.
	 */
	@Getter
	private final Socket mockSocket;
	
	/**
	 * Input stream for reading data sent via {@link Socket#getOutputStream()}
	 */
	@Getter
	private final RevivableInputStream inputStream;
	
	/**
	 * Output stream for sending data to be read via {@link Socket#getInputStream()}
	 */
	@Getter
	private final RevivableOutputStream outputStream;
	
	/**
	 * Control for {@link Socket#getInputStream()} stream -- may be used to 
	 * artificially interrupt reads via kill() etc. 
	 */
	@Getter
	private final RevivableInputStream controlForSocketInput;
	
	/**
	 * Control for {@link Socket#getOutputStream()} stream -- may be used to 
	 * artificially interrupt writes via kill() etc. 
	 */
	@Getter
	private final RevivableOutputStream controlForSocketOutput;
	
	/**
	 * Factory.
	 * 
	 * @param bufferSize buffer size for internal buffers -- note that there are
	 * 		at least two different buffers + data that is in-flight, so the
	 * 		actual size of data that can be 'in the pipes' might be roughly
	 * 		3 times as much as this size
	 */
	@SuppressWarnings("resource")
	public static MockSocketData createSocket(int bufferSize) throws IOException
	{
		Socket mockSocket = mock(Socket.class);
		Pair<RevivableInputStream, RevivableOutputStream> socketInPipe = TestUtil.createKillableBytePipe(bufferSize);
		Pair<RevivableInputStream, RevivableOutputStream> socketOutPipe = TestUtil.createKillableBytePipe(bufferSize);
		when(mockSocket.getInputStream()).thenReturn(socketInPipe.getValue0());
		when(mockSocket.getOutputStream()).thenReturn(socketOutPipe.getValue1());
		when(mockSocket.getInetAddress()).thenReturn(MOCK_SOCKET_INET_ADDRESS);
		
		return new MockSocketData(mockSocket, socketOutPipe.getValue0(), socketInPipe.getValue1(), 
			socketInPipe.getValue0(), socketOutPipe.getValue1());
	}
}
