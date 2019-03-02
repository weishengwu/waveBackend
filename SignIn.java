import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class SignIn {
    private UserList userList;
    private String path;
    private File file;

    public SignIn() {
        // Get path for local memory
        path = "assets/users.json";
        file = new File(path);    
        userList = loadJsonIntoUserList();
    }

    public UserList getUserList() {
        return userList;
    }
    
    /**
    * Updates userList using local memory json file
    *
    * @param file - file of local json
    * @return UsrList - new updated user list
    */
    public UserList updateUserList(File file) {
        UserList userTemp = null;
        try {
            if (file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                String myJson = inputStreamToString(inputStream);
                userTemp = new Gson().fromJson(myJson, UserList.class);
                inputStream.close();
                
                //Log.d("ADDUSER", userList.toString());
                
                return userTemp;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userTemp;
    }
    
    /**
    * Loads the users from the users.json file into userlist object using GSON
    *
    * @return the populated user list
    */
    public UserList loadJsonIntoUserList() {
        try {
            InputStream inputStream = new FileInputStream(file);
            String myJson = inputStreamToString(inputStream);
            UserList userList = new Gson().fromJson(myJson, UserList.class);
            return userList;
        } catch (IOException e) {
            return null;
        }
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
    
    
    /**
    * Checks if username and password that user inputted matches a user profile provided from the json file
    *
    * @param username The username the user inputted
    * @param password The password the user inputted
    * @param userlist The user list which contains all the users
    * 
    */
    public User checkCredentials(String username, String password, List<User> userlist) {
        for (int i = 0; i < userlist.size(); i++) {
            if (username.equals(userlist.get(i).getUserName()) && password.equals(userlist.get(i).getPassword())) {
                return userlist.get(i);
            }
        }
        return null;
    }

    public static byte[] handleSignIn(String mIn) throws Exception{
		try {
			String username = mIn.split(",")[0];
			String password = mIn.split(",")[1];
			User validUser = checkCredentials(username, password, login.getUserList().getList());
			JsonObject obj = new JsonObject();
			if (validUser != null) {
				obj.put("username", validUser.getUserName());
			}
			return obj.toString().getBytes("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return (new JSONObject()).toString().getBytes("utf-8");
		}
	}
}