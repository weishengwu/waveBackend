import java.io.*;
import java.net.*;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class Server {
	// Declare variables
	private static final int PORT = 1234;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket, outPacket;
	private static byte[] buffer;
	private static SignIn login;
	private static HashMap<Integer,JsonObject> runningReq;
	private static Dispatcher dispatcher;

	public static void main(String[] args) {
		runningReq = new HashMap<Integer,JsonObject>();		//Hashmap of current requests
		dispatcher = new Dispatcher();						//Dispatcher
		
		
		//register objects and methods here
		SignIn signIn = new SignIn();
		dispatcher.registerObject(signIn, "SignIn");
		EditUser editUser = new EditUser();
		dispatcher.registerObject(editUser, "UserList");


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
				// JsonObject jsonIn = new Gson().fromJson(messageIn,JsonObject.class);



				

				//******************************** Add thread here*******************************************







				JsonObject jsonIn = new Gson().fromJson(messageIn,JsonObject.class);
				String requestID = jsonIn.get("requestID").getAsString();
				String callSem = jsonIn.get("call-semantics").getAsString();
				JsonObject ret = new Gson().fromJson((dispatcher.dispatch(messageIn)).get("ret").getAsString(),JsonObject.class);
				ret.addProperty("requestID", requestID);
				ret.addProperty("call-semantics", callSem);
				System.out.println(ret.toString());
				// check credentials
				try {
					buffer = ret.toString().getBytes();
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
}