import java.io.*;
import java.net.*;
import org.json.simple.*;

public class Server {
	// Declare variables
	private static final int PORT = 1234;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket, outPacket;
	private static byte[] buffer;
	private static SignIn login;

	public static void main(String[] args) {
		System.out.println("Opening Port...");
		// Attempt to start server
		try {
			datagramSocket = new DatagramSocket(PORT);
			System.out.println("Datagram socket properly opened at port " + PORT);
			System.out.println("******************************************************************* \n");
		} catch (SocketException sockEx) {
			System.out.println("Unable To Open...");
			System.exit(1);
		}
		login = new SignIn();
		//System.out.print(login.getUserList().getList());
		handleClient(); // wait for message from client
	}

	/**
	 * Handles the client request and sends a message back to the client
	 */
	private static void handleClient() {
		try {
			String messageIn;
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

				/* AT THIS POINT, MESSAGE HAS BEEN RECIEVED AND IS STORED IN "messageIn" */

				// check credentials
				try {
					buffer = handleSignIn(messageIn);
				} catch(Exception e) {
					e.printStackTrace();
				}
				// marshall json array to send back
				outPacket = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);

				// This section of code for sending the UDP packets
				// messageOut = "message" + numRequests + ": " + messageIn;
				// outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(),
				// clientAddress, clientPort);
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

	public static byte[] handleSignIn(String mIn) throws Exception{
		try {
			String username = mIn.split(",")[0];
			String password = mIn.split(",")[1];
			User validUser = login.checkCredentials(username, password, login.getUserList().getList());
			JSONObject obj = new JSONObject();
			if (validUser != null) {
				obj.put("username", validUser.getUserName());
			}
			return obj.toString().getBytes("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}