import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.*;
import java.io.InputStream;
import java.util.*;


/* JSON Format

{"file":
  [
     {"name":"MyFile",
      "size":128000000,
      "pages":
      [
         {
            "guid":11,
            "size":64000000
         },
         {
            "guid":13,
            "size":64000000
         }
      ]
      }
   ]
} 
*/



public class DFS {
  public void runMapReduce(PagesJson[] fileInput, PagesJson[] fileOutput) {
    int size = 0; // If the remote methods are in Chord, then size is a
    //  variable of Chord
    DFS peer; //GUID for successor~Tony
    int interval = 0;
    int guid = 0;
    int[] counter = {};
    String fileName = "";
    //chord.successor.onChordSize(guid, 1); // Obtain the number of nodes
    while (size > 0) { // Obtained from onNetworkSize after a full cycle
      interval = 1936 / size; // Assuming 38 characters A-Z, 0-9, _, +.
      //1936 = 38*38
    }
    int lower = 0;
    try {
      create(fileName);
      for (int i = 0; i < (size - 1); i++) { //for i in [range(0, size-1]
        long page = md5(fileName + i);
        double lowerBoundInterval = (Math.floor(lower / 38)) + (lower % 38);
        //appendEmptyPage(fileName, page, lowerBoundInterval);
        lower += interval;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    // mapreducer is an instance of the class that
    // implements MapReduceInterface
    for (PagesJson page: fileInput) {
      counter[guid]++;
      //peer = locateSuccessor(page.guid);
      //peer.mapContext(page.guid, mapreducer, this, fileOutput + ".map");
    }
    while (counter[guid] != 0) { //wait until counter[fileInput] == 0
      //bulkTree(fileInput);
      for (PagesJson page: fileInput) {
        long pages = md5(fileName);
        //long peer = locateSuccessor(page.guid);
        //peer.bulk(page);
      }
      //createFile(fileOutput, interval, size);
      try {
        create(fileName);
        for (int i = 0; i < (size - 1); i++) { //for i in [range(0, size-1]
          long page = md5(fileName + i);
          double lowerBoundInterval = (Math.floor(lower / 38)) + (lower % 38);
          //appendEmptyPage(fileName, page, lowerBoundInterval);
          lower += interval;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    for (PagesJson page: fileOutput) { //for each page in fileOutput + ".map";
      counter[guid]++;
      //peer = locateSuccessor(page.guid);
      //peer.reduceContext(page.guid, mapreducer, this, fileOutput);
    }
    while (counter[guid] != 0) { //wait until counter[fileInput] == 0
      //bulkTree(fileOutput);
      for (PagesJson page: fileInput) {
        long pages = md5(fileName);
        //long peer = locateSuccessor(page.guid);
        //peer.bulk(page);
      }
    }
  }

  public class PagesJson {
    Long guid;
    Long size;
    public PagesJson() {
      guid = (long) - 1;
      size = (long) 0;
    }
    // getters
    public Long getGUID() {
      return guid;
    }
    public Long getSize() {
      return size;
    }
    // setters
    public void setGUID(Long guid) {
      this.guid = guid;
    }
    public void setSize(Long size) {
      this.size = size;
    }
  };

  public class FileJson {
    String name;
    Long size;
    ArrayList < PagesJson > pages;
    public FileJson() {
      name = "empty";
      size = (long) 0;
      pages = new ArrayList < PagesJson > ();
    }
    // getters
    public String getName() {
      return name;
    }
    public Long getSize() {
      return size;
    }
    public ArrayList < PagesJson > getPages() {
      return pages;
    }
    public PagesJson getPage(int i) {
      return pages.get(i);
    }
    public int getNumPages() {
      return pages.size();
    }
    // setters
    public void setName(String name) {
      this.name = name;
    }
    public void setSize(Long size) {
      this.size = size;
    }
    public void setPages(ArrayList < PagesJson > pages) {
      this.pages = new ArrayList < PagesJson > ();
      for (int i = 0; i < pages.size(); i++) {
        this.pages.add(pages.get(i));
      }
    }
    public void addPage(Long guid, Long pageSize) {
      PagesJson newPage = new PagesJson();
      newPage.setGUID(guid);
      newPage.setSize(pageSize);
      pages.add(newPage);
      size += pageSize;
    }
  };

  public class FilesJson {
    List < FileJson > file;
    public FilesJson() {
      file = new ArrayList < FileJson > ();
    }
    // getters
    public FileJson getFile(int i) {
      return file.get(i);
    }
    public int getSize() {
      return file.size();
    }
    // setters
    public void addFile(FileJson fileToAdd) {
      file.add(fileToAdd);
    }
    //deleter
    public boolean deleteFile(String filename) {
      for (int i = 0; i < file.size(); i++) {
        if (file.get(i).getName().equals(filename)) {
          file.remove(i);
          return true;
        }
      }
      return false;
    }
  };


  int port;
  Chord chord;


  private static long md5(String objectName) {
    try {
      MessageDigest m = MessageDigest.getInstance("MD5");
      m.reset();
      m.update(objectName.getBytes());
      BigInteger bigInt = new BigInteger(1, m.digest());
      return Math.abs(bigInt.longValue());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();

    }
    return 0;
  }



  public DFS(int port) throws Exception {


    this.port = port;
    long guid = md5("" + port);
    chord = new Chord(port, guid);
    Files.createDirectories(Paths.get(guid + "/repository"));
    Files.createDirectories(Paths.get(guid + "/tmp"));
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        chord.leave();
      }
    });

  }


  /**
   * Join the chord
   *
   */
  public void join(String Ip, int port) throws Exception {
    chord.joinRing(Ip, port);
    chord.print();
  }


  /**
   * leave the chord
   *
   */
  public void leave() throws Exception {
    chord.leave();
  }

  /**
   * print the status of the peer in the chord
   *
   */
  public void print() throws Exception {
    chord.print();
  }

  /**
   * readMetaData read the metadata from the chord
   *
   */
  public FilesJson readMetaData() throws Exception {
    FilesJson filesJson = null;
    try {
      Gson gson = new Gson();
      long guid = md5("Metadata");
      ChordMessageInterface peer = chord.locateSuccessor(guid);
      RemoteInputFileStream metadataraw = peer.get(guid);
      metadataraw.connect();
      Scanner scan = new Scanner(metadataraw);
      scan.useDelimiter("\\A");
      String strMetaData = scan.next();
      System.out.println(strMetaData);
      filesJson = gson.fromJson(strMetaData, FilesJson.class);
    } catch (NoSuchElementException ex) {
      filesJson = new FilesJson();
    }
    return filesJson;
  }

  /**
   * writeMetaData write the metadata back to the chord
   *
   */
  public void writeMetaData(FilesJson filesJson) throws Exception {
    long guid = md5("Metadata");
    ChordMessageInterface peer = chord.locateSuccessor(guid);

    Gson gson = new Gson();
    peer.put(guid, gson.toJson(filesJson));
  }

  /**
   * Change Name
   *
   */
  public void move(String oldName, String newName) throws Exception {
    // Write Metadata
    FileJson file = new FileJson(); // metadata we want
    FilesJson metadata = readMetaData(); // All metadata

    for (int i = 0; i < metadata.getSize(); i++) {
      if (metadata.getFile(i).getName().equals(oldName)) {
        file = metadata.getFile(i);

        //Changes metadata and writes back to Chord
        file.setName(newName);
        writeMetaData(metadata);
        return;
      }
    }
    System.out.println("file not found: " + file); // DEBUG
  }


  //WRITEPAGEDATA MIGHT BE NEEDED****************************************************************************************************************************************


  /**
   * List the files in the system
   *
   * @param filename Name of the file
   */
  public String lists() throws Exception {
    String listOfFiles = "";

    FilesJson files = readMetaData();
    for (int i = 0; i < files.getSize(); i++) {
      listOfFiles += files.getFile(i).getName() + "\n";
    }

    if (files.getSize() == 0) {
      return "Empty DFS";
    }

    return listOfFiles;
  }

  /**
   * create an empty file 
   *
   * @param filename Name of the file
   */
  public void create(String fileName) throws Exception {
    //Create file entry
    FileJson fileJson = new FileJson();
    fileJson.setName(fileName);
    // Write file entry to Metadata
    FilesJson metadata = readMetaData();
    metadata.addFile(fileJson);
    writeMetaData(metadata);

    try {
      if (fileName.contains("music")) {
        String path = "assets/music.json";
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        String myJson = inputStreamToString(inputStream);
        MusicList musicList = new Gson().fromJson(myJson, MusicList.class);

        final int MUSIC_PER_PAGE = 50;

        MusicList writeTemp = new MusicList();
        for (int i = 0; i < musicList.size(); i++) {
          writeTemp.addSong(musicList.get(i));

          if ((i + 1) % MUSIC_PER_PAGE == 0 || i == musicList.size() - 1) {
            JsonObject musicJson = new Gson().fromJson(new Gson().toJson(writeTemp), JsonObject.class);
            String musicListWrite = musicJson.toString();
            //write file here
            new Gson().toJson(musicJson, new FileWriter("assets/dfs_temp/temp" + i + ".json"));
            //append here
            RemoteInputFileStream appendStream = new RemoteInputFileStream("assets/dfs_temp/temp" + i + ".json");
            append(fileName, appendStream);
            writeTemp = new MusicList();
          }
        }

      } else if (fileName.contains("user")) {
        String path = "assets/users.json";
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        String myJson = inputStreamToString(inputStream);
        PrintWriter writer = new PrintWriter("assets/dfs_temp/temp.txt", "UTF-8");
        writer.println(myJson);
        writer.close();
        // UserList userList = new Gson().fromJson(myJson, UserList.class);
        // JsonObject userJson = new Gson().fromJson(new Gson().toJson(userList), JsonObject.class);
        // String userListWrite = userJson.toString();
        // //write file here
        // new Gson().toJson(userJson, new FileWriter("assets/dfs_temp/temp.json"));
        // System.out.println("\n\n\n\n\n" + userJson.toString() + "\n\n\n\n\n");
        //append here
        RemoteInputFileStream appendStream = new RemoteInputFileStream("assets/dfs_temp/temp.txt");
        append(fileName, appendStream);
      } else {
        System.out.println("Command Failed!");
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("\nSuccessfully added " + fileName + " to the metadata\n");
  }

  /**
   * delete file 
   *
   * @param filename Name of the file
   */
  public void delete(String fileName) throws Exception {
    FilesJson metadata = readMetaData();
    FileJson file = new FileJson();

    // find file
    for (int i = 0; i < metadata.getSize(); i++) {
      if (metadata.getFile(i).getName().equals(fileName)) {
        file = metadata.getFile(i);

        // delete all pages of file
        for (int j = 0; j < file.getNumPages() - 1; j++) {
          Long guid = file.getPage(j).getGUID();
          ChordMessageInterface peer = chord.locateSuccessor(guid);
          peer.delete(guid);
        }

        metadata.deleteFile(fileName);
        writeMetaData(metadata);
        return;
      }
    }
    System.out.println("\nSuccessfully deleted " + fileName + " from the metadata\n");
  }

  /**
   * Read block pageNumber of fileName 
   *
   * @param filename Name of the file
   * @param pageNumber number of block. 
   */
  public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception {
    FilesJson metadata = readMetaData();
    long guid = (long) - 1;

    for (int i = 0; i < metadata.getSize(); i++) {
      FileJson filejson = metadata.getFile(i);

      if (filejson.getName().equals(fileName)) {
        guid = filejson.getPage(pageNumber).getGUID();
        System.out.println("GUID" + guid);
        break;
      }
    }
    ChordMessageInterface peer = chord.locateSuccessor(guid);
    return peer.get(guid);
  }

  /**
   * Add a page to the file                
   *
   * @param filename Name of the file
   * @param data RemoteInputStream. 
   */
  public void append(String filename, RemoteInputFileStream data) throws Exception {
    Long guid = md5(filename + System.currentTimeMillis());
    FilesJson metadata = readMetaData();

    for (int i = 0; i < metadata.getSize(); i++) {
      if (metadata.getFile(i).getName().equals(filename)) {

        FileJson file = metadata.getFile(i);

        //Changes metadata and writes back to Chord
        file.addPage(guid, (long) data.toString().getBytes().length);
        writeMetaData(metadata);
        break;
      }
    }

    // locate peer
    ChordMessageInterface peer = chord.locateSuccessor(guid);
    peer.put(guid, data);
  }


  /**
   * Reads a file using inputstream
   *
   * @param inputStream a file to read from
   * @return a string of the read in file
   */
  public String inputStreamToString(InputStream inputStream) {
    try {
      byte[] bytes = new byte[inputStream.available()];
      inputStream.read(bytes, 0, bytes.length);
      String json = new String(bytes);
      return json;
    } catch (IOException e) {
      return null;
    }
  }


}
