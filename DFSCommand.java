import java.io.*;
import java.util.Scanner;
import com.google.gson.*;

//import DFS.FileJson;
//import DFS.FilesJson;
//fuck tony 
//what duh nani 

public class DFSCommand {
	DFS dfs;

	public DFSCommand(int p, int portToJoin) throws Exception {
		dfs = new DFS(p);

		if (portToJoin > 0) {
			System.out.println("Joining " + portToJoin);
			dfs.join("127.0.0.1", portToJoin);
		}

		menu(); // its suppose to?
		// its supposed to print multiple times , in the while loop it goes
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		String line = buffer.readLine();
		// System.out.println("Result " +result);
		// while (!line.contains("quit")) {
		while(!line.contains("quit")) {
			String[] result = line.split("\\s");
			System.out.println("Result " +line);	//DEBUG
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
					pageNumber = Integer.parseInt(result[2]);

				}
				System.out.println("reading page #" + pageNumber);

				//Remote Input File Stream
				RemoteInputFileStream dataraw = dfs.read(pathName, pageNumber);
				dataraw.connect();

				//Scanner
				Scanner scan = new Scanner(dataraw);
				scan.useDelimiter("\\A");
				// scan.useDelimiter("\0");
				String data = scan.next();
				System.out.println("\n\n\n-------------------------------------\n\n");
				System.out.println(data); // DEBUG
				System.out.println("\n\n\n-------------------------------------\n\n");

				//Convert from json to ArrayList
				if (result[1].equals("music")) {
					MusicList page = new MusicList();
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					page = gson.fromJson(data, MusicList.class);
					
					for (int i = 0; i < page.size(); i++)
					{
						System.out.println("\n\n\n-------------------------------------\n\n");
						//System.out.println("\t\t" + page.getItem(i).song.title);

						System.out.println("\t" + page.getMusicList().get(i).getSongTitle());
						System.out.println("\t" + page.size());

						System.out.println("\n\n\n-------------------------------------\n\n");
					}

					
				}


				else if (result[1].equals("user")) {
					UserList page = new UserList();
					Gson gson = new Gson();
					page = gson.fromJson(data, UserList.class);
					
						//System.out.println("\t\t" + page.getItem(i).song.title);
						for(int i = 0; i < page.getList().size(); i++)
						{
							System.out.println("\n\n\n-------------------------------------\n\n");
							System.out.println("\t" + page.getList().get(i));
							System.out.println("\n\n\n-------------------------------------\n\n");
						}
					
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

				
				if (result[1].equals("music")) {
					MusicList page = new MusicList();
					Gson gson = new Gson();
					page = gson.fromJson(data, MusicList.class);
					
					for (int i = 0; i < page.size(); i++)
					{
						//System.out.println("\t\t" + page.getItem(i).song.title);
						System.out.println("\t" + page.getMusicList().get(i).getSongTitle());
						System.out.println("\t" + page.size());
					}

					
				}


				else if (result[1].equals("user")) {
					UserList page = new UserList();
					Gson gson = new Gson();
					page = gson.fromJson(data, UserList.class);
					
						//System.out.println("\t\t" + page.getItem(i).song.title);
						for(int i = 0; i < page.getList().size(); i++)
						{
							System.out.println("\t" + page.getList().get(i));

						}
					
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

				int pageNumber = 0;

				//Remote Input File Stream
				RemoteInputFileStream dataraw = dfs.read(pathName, pageNumber);
				dataraw.connect();

				//Scanner
				Scanner scan = new Scanner(dataraw);
				scan.useDelimiter("\\A");
				String data = scan.next();
				//System.out.println("gg" + data); // DEBUG
	

				//print each catalogItem song.title
				if (result[1].equals("music")) {
					MusicList page = new MusicList();
					Gson gson = new Gson();
					page = gson.fromJson(data, MusicList.class);
					
					for (int i = 0; i < page.size(); i++)
					{
						//System.out.println("\t\t" + page.getItem(i).song.title);
						System.out.println("\t" + page.getMusicList().get(i).getSongTitle());
						System.out.println("\t" + page.size());
					}

					
				}
				else if (result[1].equals("user")) {
					UserList page = new UserList();
					Gson gson = new Gson();
					page = gson.fromJson(data, UserList.class);
					
						//System.out.println("\t\t" + page.getItem(i).song.title);
						for(int i = 0; i < page.getList().size(); i++)
						{
							System.out.println("\t" + page.getList().get(i));

						}
					
				}
			} 
			if (result[0].equals("append")) {
				// dfs.append();
				System.out.println("Successfully appended!"); 
			}
			// Command is "move 'oldFileName' 'newFileName'" 
			if (result[0].equals("move")) {
				// todo
				dfs.move(result[1],result[2]); // need old file and new filename
			}
			menu();
			line = buffer.readLine();
		}
		// User interface:
		// join, ls, touch, delete, read, tail, head, append, move
	}

	public void menu() {
		System.out.println("Choose from the following commands:");
		// System.out.println("join");
		System.out.println("ls");
		System.out.println("touch");
		System.out.println("delete");
		System.out.println("read");
		System.out.println("tail");
		System.out.println("head");
		System.out.println("move");
		System.out.println("quit");
	}
	
	public DFS getDFS() {
		return dfs;
	}

}