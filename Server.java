import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class Server {
	// Declare variables
	private static final int PORT = 1234;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket, outPacket;
	private static byte[] buffer;
	private static SignIn login;
	private static HashMap<String,JsonObject> attendedReq;
	private static Dispatcher dispatcher;

	public static void main(String[] args) {
		attendedReq = new HashMap<String,JsonObject>();		//Hashmap of current requests
		dispatcher = new Dispatcher();						//Dispatcher
		
		
		//register objects and methods here
		SignIn signIn = new SignIn();
		dispatcher.registerObject(signIn, "SignIn");
		MusicList musiclist = ReadFile.loadJsonIntoMusicList();
		dispatcher.registerObject(musiclist, "MusicList");
		EditUser editUser = new EditUser();
		dispatcher.registerObject(editUser, "EditUser");


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

		receive(); // wait for message from client

	}

	/**
	 * Handles the client request and sends a message back to the client
	 */
	private static void receive() {
		try {
			String messageIn;
			InetAddress clientAddress = null;
			int clientPort;

			do {
				// This section of code for receiving the UDP packets
				buffer = new byte[256];
				inPacket = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(inPacket);
				clientAddress = inPacket.getAddress();
				clientPort = inPacket.getPort();

				messageIn = new String(inPacket.getData(), 0, inPacket.getLength());
				//System.out.println("Request #" + numRequests);
				//System.out.println("Incoming client request from " + clientAddress + " at port " + clientPort);
				System.out.println("Message: " + messageIn);
				/* AT THIS POINT, MESSAGE HAS BEEN RECIEVED AND IS STORED IN "messageIn" */
				JsonObject jsonIn = new Gson().fromJson(messageIn,JsonObject.class);

				JsonObject ret = null;

				String callSemantic = new String((jsonIn).get("call-semantics").getAsString());


				if (jsonIn.get("object").getAsString().equals("SongHandler")) {
					String songID = jsonIn.get("param").getAsJsonObject().get("songID").getAsString();
					Runnable r = new SongHandler(songID, clientAddress);
					new Thread(r).start();
				}
				else {
					if(callSemantic.equals("at-most-one"))
					{
						if (attendedReq.containsKey(jsonIn.get("requestID").getAsString())) 
						{
							send(attendedReq.get(jsonIn.get("requestID").getAsString()), clientAddress, clientPort);
						}
						else
						{
							ret = new Gson().fromJson((dispatcher.dispatch(messageIn)).get("ret").getAsString(),JsonObject.class);
							attendedReq.put(jsonIn.get("requestID").toString(), ret);
							send(ret, clientAddress, clientPort);
						}
					}
					else
					{
						ret = new Gson().fromJson((dispatcher.dispatch(messageIn)).get("ret").getAsString(),JsonObject.class);
						send(ret, clientAddress, clientPort);
					}



					//System.out.println("test  " + Arrays.asList(attendedReq)); 

					
					System.out.println("Successfully sent response back to client at address " + clientAddress + "\n");
					System.out.println("******************************************************************* \n");
				}
			} while (true);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			System.out.println("\n Closing connection...");
			datagramSocket.close();
		}
	}


	public static void send(JsonObject ret, InetAddress clientAddress, int clientPort)
	{
		try
		{
			System.out.println(ret.toString());
			buffer = ret.toString().getBytes();
				// marshall json array to send back
			outPacket = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
			datagramSocket.send(outPacket);

		}
		catch(IOException ioEx)
		{
			ioEx.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		

	}
}
