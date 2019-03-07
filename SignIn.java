import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.io.FileOutputStream;


public class SignIn {
    UserList userlist;    
    public SignIn(){
        userlist = ReadFile.loadJsonIntoUserList();
    }
    public void refreshUserList() {
        userlist = ReadFile.loadJsonIntoUserList();
    }
    public String Login(String username, String password) throws Exception {
        System.out.println("Entered username: " + username);
        System.out.println("Entered password: " + password);
        try {
            User validUser = checkCredentials(username, password);
            JsonObject obj = new JsonObject();
            if (validUser != null) {
                //pack return jsonobject with music list and playlist info
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String musicListString = gson.toJson(ReadFile.loadJsonIntoMusicList());
                JsonObject musicListJson = new Gson().fromJson(musicListString, JsonObject.class);
                String userString = gson.toJson(validUser);
                JsonObject userJson = new Gson().fromJson(userString, JsonObject.class);
                obj.add("musiclist", musicListJson);
                obj.add("user", userJson);
            }
            return obj.toString();
        } 
        catch (Exception e) {
            System.out.println("Not a valid user... returning empty json object");
            return (new JsonObject()).toString();
        }
    }
    
    public String SignUp(String username, String password) throws Exception {
        try {
            boolean userExist = checkUserName(username);
            JsonObject obj = new JsonObject();
            
            if(!userExist)
            {
                addUser(username, password);
                obj.addProperty("IsSignedUp", userExist);
            }
            return obj.toString();
        } 
        catch (Exception e) {
            System.out.println("User already exists... returning empty json object");
            return (new JsonObject()).toString();
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
    
    public boolean checkUserName(String username) {
        for (int i = 0; i < userlist.getList().size(); i++)
        {
            
            if(username.equals(userlist.getList().get(i).getUserName()))
            {
                return true;
            }
        }
        return false;
    }

    public void addUser(String name, String password) {
        User newUser = new User(name, password);        
        userlist.addToList(newUser);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(userlist);
        JsonObject jObj = new Gson().fromJson(strJson, JsonObject.class);
        
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
    
    
}