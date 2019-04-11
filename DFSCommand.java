import java.io.*;
import java.util.Scanner;
import com.google.gson.*;

//import DFS.FileJson;
//import DFS.FilesJson;

public class DFSCommand {
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

	static public void main(String args[]) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException("Parameter: <port> <portToJoin>");
		}
		if (args.length > 1) {
			DFSCommand dfsCommand = new DFSCommand(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} else {
			DFSCommand dfsCommand = new DFSCommand(Integer.parseInt(args[0]), 0);
		}

		//Server server = new Server(dfsCommand.dfs, PORT);
	}
}
