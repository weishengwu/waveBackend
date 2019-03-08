import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

public class ReadFile {
    /**
     * Loads the music from the music.json file into musicList object using GSON
     * @return the populated music list
     */
    public static MusicList loadJsonIntoMusicList() {
        String path = "assets/music.json";
        File file = new File(path);    
        try {
            InputStream inputStream = new FileInputStream(file);
            String myJson = inputStreamToString(inputStream);
            MusicList musicList  = new Gson().fromJson(myJson, MusicList.class);
            return musicList;
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
    * Loads the users from the users.json file into userlist object using GSON
    *
    * @return the populated user list
    */
    public static UserList loadJsonIntoUserList() {
        String path = "assets/users.json";
        File file = new File(path);    
        try {
            InputStream inputStream = new FileInputStream(file);
            String myJson = inputStreamToString(inputStream);
            UserList userList = new Gson().fromJson(myJson, UserList.class);
            return userList;
        } catch (IOException e) {
            return null;
        }
    }
    public static void writeUserListToJson(UserList userlist) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(userlist);
        
        try {
            String filePath =  "assets/users.json";
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(strJson.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
    * Reads a file using inputstream
    *
    * @param inputStream a file to read from
    * @return a string of the read in file
    */
    public static String inputStreamToString(InputStream inputStream) {
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