import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.*;


public class Server {
	// Declare variables
	private static final int PORT = 1234;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket, outPacket;
	private static byte[] buffer;
	private static SignIn login;
	private static HashMap<String,JsonObject> attendedReq;
	private static Dispatcher dispatcher;
	private static ReadFile readfile;

	public static void main(String[] args) throws Exception {
		//Start DFSCommand first
		DFSCommand dfsCommand = null;
		if (args.length < 1) {
			throw new IllegalArgumentException("Parameter: <port> <portToJoin>");
		}
		if (args.length > 1) {
			dfsCommand = new DFSCommand(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} else {
			dfsCommand = new DFSCommand(Integer.parseInt(args[0]), 0);
		}

		//Start Server after DFSCommand

		ReadFile.sendDFS(dfsCommand.getDFS());
		attendedReq = new HashMap<String,JsonObject>();		//Hashmap of current requests
		dispatcher = new Dispatcher();						//Dispatcher
		readfile = new ReadFile();
		
		
		//register objects and methods here
		SignIn signIn = new SignIn();
		dispatcher.registerObject(signIn, "SignIn");
		MusicList musiclist = readfile.loadJsonIntoMusicList();
		dispatcher.registerObject(musiclist, "MusicList");
		EditUser editUser = new EditUser();
		dispatcher.registerObject(editUser, "EditUser");
		SongHandler songHandler = new SongHandler();
		dispatcher.registerObject(songHandler, "SongHandler");

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

				MyRunnable myRunnable = new MyRunnable(buffer, inPacket, clientAddress, clientPort);
				Thread t = new Thread(myRunnable);
				t.start();
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

	public static class MyRunnable implements Runnable{
		byte[] buffer;
		DatagramPacket inPacket;
		InetAddress clientAddress;
		int clientPort;
		String messageIn = "";

		public MyRunnable(byte[] buffer, DatagramPacket inPacket, InetAddress clientAddress, int clientPort) {
			this.buffer = buffer;
			this.inPacket = inPacket;
			this.clientAddress = clientAddress;
			this.clientPort = clientPort;
			messageIn =new String(inPacket.getData(), 0, inPacket.getLength());
			// System.out.println("Request #" + numRequests);
			// System.out.println("Incoming client request from " + clientAddress + " at
			// port " + clientPort);
			System.out.println("Message: " + messageIn);
			/* AT THIS POINT, MESSAGE HAS BEEN RECIEVED AND IS STORED IN "messageIn" */;
			// System.out.println("Request #" + numRequests);
			// System.out.println("Incoming client request from " + clientAddress + " at
			// port " + clientPort);
			//System.out.println("Message: " + messageIn);
			/* AT THIS POINT, MESSAGE HAS BEEN RECIEVED AND IS STORED IN "messageIn" */
		}

		public void run() {
			JsonObject jsonIn = new Gson().fromJson(messageIn, JsonObject.class);

			JsonObject ret;

			String callSemantic = new String((jsonIn).get("call-semantics").getAsString());

			if (callSemantic.equals("at-most-one")) {
				if (attendedReq.containsKey(jsonIn.get("requestID").getAsString())) {
					send(attendedReq.get(jsonIn.get("requestID").getAsString()), clientAddress, clientPort);
				} else {
					ret = new Gson().fromJson((dispatcher.dispatch(messageIn)).get("ret").getAsString(),JsonObject.class);
					attendedReq.put(jsonIn.get("requestID").toString(), ret);
					send(ret, clientAddress, clientPort);
				}
			} else {
				ret = new Gson().fromJson((dispatcher.dispatch(messageIn)).get("ret").getAsString(),JsonObject.class);
				send(ret, clientAddress, clientPort);
			}

			// System.out.println("test " + Arrays.asList(attendedReq));
			System.out.println(Thread.getAllStackTraces().keySet());
			System.out.println("Successfully sent response back to client at address " + clientAddress + "\n");
			System.out.println("***************************************************************** \n");
		}
	}

	/*					 ADDING DFSCOMMAND.JAVA HERE					 */
	class DFSCommand {
		DFS dfs;
	
		public DFSCommand(int p, int portToJoin) throws Exception {
			dfs = new DFS(p);
	
			if (portToJoin > 0) {
				System.out.println("Joining " + portToJoin);
				dfs.join("127.0.0.1", portToJoin);
			}
	
			menu();
	
			BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
			String line = buffer.readLine();
			// System.out.println("Result " +result);
			// while (!line.contains("quit")) {
			while(true) {
				String[] result = line.split("\\s");
				System.out.println("Result " +line);
				if (result[0].equals("join") && result.length > 1) {
					dfs.join("127.0.0.1", Integer.parseInt(result[1]));
				}
				if (result[0].equals("print")) {
					dfs.print();
				}
	
				if (result[0].equals("leave")) {
					dfs.leave();
				}
	
				if (result[0].equals("ls")) {
					// todo
	
					System.out.println(dfs.lists());
				}
	
				if (result[0].equals("touch")) {
					// todo
					if(result[1].equals("music"))
					{
						dfs.create("./assets/music.json");
					}
					else if (result[1].equals("user"))
					{
						dfs.create("./assets/users.json");
	
					}
					
	
				}
	
				if (result[0].equals("delete")) {
					if(result[1].equals("music"))
					{
						dfs.delete("./assets/music.json");
					}
					else if (result[1].equals("user"))
					{
						dfs.delete("./assets/users.json");
	
					}
					// todo
				}
			
				if (result[0].equals("read")) {
					// todo
					//default page number = 1
					int pageNumber = 1;
	
					String pathName = "";
	
					if(result[1].equals("music"))
					{
						pathName = "./assets/music.json";
					}
					else if (result[1].equals("user"))
					{
						pathName = "./assets/users.json";
	
					}
	
					
	
					//If page specified update pageNumber
					if (result.length > 1) {
						pageNumber = Integer.parseInt(result[1]);
	
					}
					System.out.println("reading page #" + pageNumber);
	
					//Remote Input File Stream
					RemoteInputFileStream dataraw = dfs.read(pathName, pageNumber);
					dataraw.connect();
	
					//Scanner
					Scanner scan = new Scanner(dataraw);
					scan.useDelimiter("\\A");
					String data = scan.next();
					//System.out.println(data); // DEBUG
	
					//Convert from json to ArrayList
					MusicList page = new MusicList();
					Gson gson = new Gson();
					page = gson.fromJson(data, MusicList.class);
	
					//print each catalogItem song.title
					for (int i = 0; i < page.size(); i++) {
						//System.out.println("\t\t" + page.getItem(i).song.title);
						System.out.println("\t" + page.getMusicList().get(i).getSongTitle());
					}
					System.out.println(":Print Complete.");
				}
	
				if (result[0].equals("tail")) {
					// todo
					int index = 0;
					
					String pathName = "";
	
					if(result[1].equals("music"))
					{
						index = 0;
						pathName = "./assets/music.json";
					}
					else if (result[1].equals("user"))
					{
						index = 1;
						pathName = "./assets/users.json";
	
					}
					
					//TODO: make sure get correct file based on music and users
					int pageNumber = dfs.readMetaData().getFile(index).getNumPages()-1;
					System.out.println("reading page #" + pageNumber);
	
					//Remote Input File Stream
					RemoteInputFileStream dataraw = dfs.read(pathName, pageNumber);
					dataraw.connect();
	
					//Scanner
					Scanner scan = new Scanner(dataraw);
					scan.useDelimiter("\\A");
					String data = scan.next();
					//System.out.println(data); // DEBUG
	
					//Convert from json to ArrayList
					MusicList page = new MusicList();
					Gson gson = new Gson();
					page = gson.fromJson(data, MusicList.class);
	
					//print each catalogItem song.title
					for (int i = 0; i < page.size(); i++) {
						//System.out.println("\t\t" + page.getItem(i).song.title);
						System.out.println("\t" + page.getMusicList().get(i).getSongTitle());
					}
					System.out.println(":Print Complete.");
				}
	
				if (result[0].equals("head")) {
					// todo
					
					String pathName = "";
	
					if(result[1].equals("music"))
					{
						
						pathName = "./assets/music.json";
					}
					else if (result[1].equals("user"))
					{
						
						pathName = "./assets/users.json";
	
					}
					
					//TODO: make sure get correct file based on music and users
					int pageNumber = 0;
					System.out.println("reading page #" + pageNumber);
	
					//Remote Input File Stream
					RemoteInputFileStream dataraw = dfs.read(pathName, pageNumber);
					dataraw.connect();
	
					//Scanner
					Scanner scan = new Scanner(dataraw);
					scan.useDelimiter("\\A");
					String data = scan.next();
					//System.out.println(data); // DEBUG
	
					//Convert from json to ArrayList
					MusicList page = new MusicList();
					Gson gson = new Gson();
					page = gson.fromJson(data, MusicList.class);
	
					//print each catalogItem song.title
					for (int i = 0; i < page.size(); i++) {
						//System.out.println("\t\t" + page.getItem(i).song.title);
						System.out.println("\t" + page.getMusicList().get(i).getSongTitle());
					}
					System.out.println(":Print Complete.");
				}
	
				if (result[0].equals("append")) {
					// todo
					
				}
	
				if (result[0].equals("move")) {
					// todo
				}
				line = buffer.readLine();
			}
			// User interface:
			// join, ls, touch, delete, read, tail, head, append, move
		}
	
		public void menu() {
			System.out.println("Menu");
			System.out.println("\n join");
			System.out.println("\n ls");
			System.out.println("\n touch");
			System.out.println("\n delete");
			System.out.println("\n read");
			System.out.println("\n tail");
			System.out.println("\n head");
			System.out.println("\n append");
			System.out.println("\n move");
			System.out.println("\n quit");
		}
		public DFS getDFS() {
			return dfs;
		}	
	}//END OF DFSCOMMAND	

	
}


