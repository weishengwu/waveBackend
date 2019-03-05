import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SignIn {
    UserList userlist;    
    public SignIn(){
        userlist = loadJsonIntoUserList();
    }
    /**
    * Loads the users from the users.json file into userlist object using GSON
    *
    * @return the populated user list
    */
    public UserList loadJsonIntoUserList() {
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
    
    public String Login(String username, String password) throws Exception{
		try {
			User validUser = checkCredentials(username, password);
			JsonObject obj = new JsonObject();
			if (validUser != null) {
				obj.addProperty("username", validUser.getUserName());
			}
			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return (new JsonObject().toString());
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
    public User checkCredentials(String username, String password) {
        for (int i = 0; i < userlist.getList().size(); i++) {
            if (username.equals(userlist.getList().get(i).getUserName()) && password.equals(userlist.getList().get(i).getPassword())) {
                return userlist.getList().get(i);
            }
        }
        return null;
    }
}