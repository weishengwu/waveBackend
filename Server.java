import java.io.*;
import java.net.*;

public class Server {
	// Declare variables
	private static final int PORT = 1234;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket, outPacket;
	private static byte[] buffer;

	public static void main(String[] args) {
		System.out.println("Opening Port...");
		try {
			datagramSocket = new DatagramSocket(PORT);
			System.out.println("Datagram socket properly opened at port " + PORT);
			System.out.println("******************************************************************* \n");
		} catch (SocketException sockEx) {
			System.out.println("Unable To Open...");
			System.exit(1);
		}
		handleClient();
	}

	/** 
	 * Handles the client request and sends a message back to the client 
	 */
	private static void handleClient() {
		try {
			String messageIn;
			String messageOut;
			InetAddress clientAddress = null;
			int clientPort;
			int numRequests = 1;

			do {
				// This section of code for receiving the UDP packets
				buffer = new byte[256];
				inPacket = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(inPacket);
				clientAddress = inPacket.getAddress();
				clientPort = inPacket.getPort();
				messageIn = new String(inPacket.getData(), 0, inPacket.getLength());
				System.out.println("Request #" + numRequests);
				System.out.println("Incoming client request from " + clientAddress + " at port " + clientPort);
				System.out.println("Message: " + messageIn);

				// This section of code for sending the UDP packets
				messageOut = "message" + numRequests + ": " + messageIn;
				outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort);
				datagramSocket.send(outPacket);
				numRequests++;
				System.out.println("Successfully sent response back to client at address " + clientAddress + "\n");
				System.out.println("******************************************************************* \n");
			} while (true);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			System.out.println("\n Closing connection...");
			datagramSocket.close();
		}
	}
}