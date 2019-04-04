import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.Gson;
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


public class DFS
{
    
    
    public class PagesJson
    {
        Long guid;
        Long size;
        public PagesJson()
        {
            
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
    
    public class FileJson 
    {
        String name;
        Long size;
        ArrayList<PagesJson> pages;
        public FileJson()
        {
            
        }
        // getters
        public String getName() {
            return name;
        }
        public Long getSize() {
            return size;
        }
        public ArrayList<PagesJson> getPages() {
            return pages;
        }
        // setters
        public void setName(String name) {
            this.name = name;
        }
        public void setSize(Long size) {
            this.size = size;
        }
        // public void setPages()
    };
    
    public class FilesJson 
    {
        List<FileJson> file;
        public FilesJson() 
        {
            
        }
        // getters
        // setters
    };
    
    
    int port;
    Chord chord;
    
    
    private long md5(String objectName)
    {
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1,m.digest());
            return Math.abs(bigInt.longValue());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            
        }
        return 0;
    }
    
    
    
    public DFS(int port) throws Exception
    {
        
        
        this.port = port;
        long guid = md5("" + port);
        chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid+"/repository"));
        Files.createDirectories(Paths.get(guid+"/tmp"));
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
    public void join(String Ip, int port) throws Exception
    {
        chord.joinRing(Ip, port);
        chord.print();
    }
    
    
    /**
    * leave the chord
    *
    */ 
    public void leave() throws Exception
    {        
        chord.leave();
    }
    
    /**
    * print the status of the peer in the chord
    *
    */
    public void print() throws Exception
    {
        chord.print();
    }
    
    /**
    * readMetaData read the metadata from the chord
    *
    */
    public FilesJson readMetaData() throws Exception
    {
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
            filesJson= gson.fromJson(strMetaData, FilesJson.class);
        } catch (NoSuchElementException ex)
        {
            filesJson = new FilesJson();
        }
        return filesJson;
    }
    
    /**
    * writeMetaData write the metadata back to the chord
    *
    */
    public void writeMetaData(FilesJson filesJson) throws Exception
    {
        long guid = md5("Metadata");
        ChordMessageInterface peer = chord.locateSuccessor(guid);
        
        Gson gson = new Gson();
        peer.put(guid, gson.toJson(filesJson));
    }
    
    /**
    * Change Name
    *
    */
    public void move(String oldName, String newName) throws Exception
    {
        // TODO:  Change the name in Metadata
        // Write Metadata
    }
    
    
    /**
    * List the files in the system
    *
    * @param filename Name of the file
    */
    public String lists() throws Exception
    {
        String listOfFiles = "";
        
        return listOfFiles;
    }
    
    /**
    * create an empty file 
    *
    * @param filename Name of the file
    */
    public void create(String fileName) throws Exception
    {
        // TODO: Create the file fileName by adding a new entry to the Metadata
        // Write Metadata
        
        
        
    }
    
    /**
    * delete file 
    *
    * @param filename Name of the file
    */
    public void delete(String fileName) throws Exception
    {
        
        
    }
    
    /**
    * Read block pageNumber of fileName 
    *
    * @param filename Name of the file
    * @param pageNumber number of block. 
    */
    public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception
    {
        return null;
    }
    
    /**
    * Add a page to the file                
    *
    * @param filename Name of the file
    * @param data RemoteInputStream. 
    */
    public void append(String filename, RemoteInputFileStream data) throws Exception
    {
        
    }
    
    
    /**
    * Add a page to the file                
    *
    * @param filename Name of the file
    * @param data RemoteInputStream. 
    */
    public void write(String filename, RemoteInputFileStream data) throws Exception
    {
        
    }
    
}


