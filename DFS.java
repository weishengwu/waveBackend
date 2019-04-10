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
        // Long guid;
        // Long size;
        // public PagesJson()
        // {
        //     guid = (long) 0;
        //     size = (long) 0;
        // }
        // // getters
        // public Long getGUID() {
        //     return guid;
        // }
        // public Long getSize() {
        //     return size;
        // }
        // // setters
        // public void setGUID(Long guid) {
        //     this.guid = guid;
        // }
        // public void setSize(Long size) {
        //     this.size = size;
        // }
        Long guid;
        int size;
        public PagesJson()
        {
            guid = (long) 0;
            size = 0;
        }

        // getters
        public Long getGUID()
        {
            return guid;
        }

        public int getSize()
        {
            return size;
        }

        // setters
        public void setGUID(Long guid)
        {
            this.guid = guid;
        }
        public void setSize(int size)
        {
            this.size = size;
        }
    };
    
    public class FileJson 
    {
        // String name;
        // Long size;
        // ArrayList<PagesJson> pages;
        // public FileJson()
        // {
            
        // }
        // // getters
        // public String getName() {
        //     return name;
        // }
        // public Long getSize() {
        //     return size;
        // }
        // public ArrayList<PagesJson> getPages() {
        //     return pages;
        // }
        // // setters
        // public void setName(String name) {
        //     this.name = name;
        // }
        // public void setSize(Long size) {
        //     this.size = size;
        // }
        // // public void setPages()
        String name;
        Long   size;
        int   numberOfItems;
        int   itemsPerPage;
        ArrayList<PagesJson> pages;
        public FileJson()
        {
            this.name = "not set";
            this.size = (long) 0;
            this.numberOfItems = 0;
            this.itemsPerPage =  0;
            this.pages = new ArrayList<PagesJson>();
        }
        // getters
        public String getName()
        {
            return this.name;
        }
        public Long getSize()
        {
            return this.size;
        }
        public int getNumberOfItems()
        {
            return this.numberOfItems;
        }
        public int getItemsPerPage()
        {
            return this.itemsPerPage;
        }
         public int getNumberOfPages()
        {
            return this.pages.size();
        }
        public ArrayList<PagesJson> getPages()
        {
            return this.pages;
        } 
        public PagesJson getPage(int i)
        {
            return pages.get(i);
        }


        // setters
        public void setName(String name)
        {
            this.name = name;
        }
        public void setSize(Long size)
        {
            this.size = size;
        }
        public void setPages(ArrayList<PagesJson> pages)
        {
            this.pages = new ArrayList<PagesJson>(); 
        	for(int i = 0 ; i < pages.size(); i++)
        	{
        		this.pages.add(pages.get(i));
        	}
        } 
        public void addPage(PagesJson page)
        {
            this.pages.add(page);
            this.size += page.getSize();
        }
		public void addPage(Long guid, int page_size)
        {
            PagesJson page = new PagesJson();		//metadata
            page.setGUID(guid);         			//metadata
            page.setSize(page_size);    			//metadata

            this.addPage(page);
        }
    };
    
    public class FilesJson 
    {
        // List<FileJson> file;
        // Long size;
        // public FilesJson() 
        // {
            
        // }
        
        // // getters
        // public Long getSize(){
        //     return file.size();
        // }
        
        // // setters
        // public void setSize(Long size)
        // {
        //     this.size = size;
        // }
        List<FileJson> files;
         public FilesJson() 
         {
             files = new ArrayList<FileJson>();
         }

        // getters
         public FileJson getFile(int i)
         {
            return this.files.get(i);
         }

        // setters
         public void addFile(FileJson file)
         {
            this.files.add(file);
         }

         public int size()
         {
            return files.size();
         }

         public void deleteFile(String fileName)
         {
         	int index_to_remove = 0;
         	for(int i = 0 ; i < files.size(); i++)
         	{
         		if(files.get(i).getName().equals(fileName))
         		{
					index_to_remove = i;
         		}
         	}

         	files.remove(index_to_remove);
         }
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
        FilesJson files = readMetaData();
        for(int i = 0 ; i < files.size(); i++)
        {
            listOfFiles += files.getFile(i).name + "\n";
        }

        //System.out.println(TAG + ":files.size() == " + files.size());//DEBUG
        if(files.size() == 0 ){return "Empty";}
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
        
    }
    
    /**
    * Read block pageNumber of fileName 
    *
    * @param filename Name of the file
    * @param pageNumber number of block. 
    */
    public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception
    {
        FilesJson metadata = readMetaData();
		long guid = (long) -1;

		for (int i = 0; i < metadata.getSize(); i++) {
			FileJson filejson = metadata.getFile(i);

            if (filejson.getName().equals(fileName)) {
				guid = filejson.getPage(pageNumber).getGUID();
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
    public void append(String filename, RemoteInputFileStream data) throws Exception
    {
		Long guid = md5(filename + System.currentTimeMillis());

		FilesJson metadata = readMetaData();

        // add data to page
		// TODO*******************************************************************************************************************************************************

		// locate peer
		ChordMessageInterface peer = chord.locateSuccessor(guid);

		peer.put(guid, data);
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


