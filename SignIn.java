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
        System.out.println("Entered username: " + username);
        System.out.println("Entered password: " + password);
        try {
           User validUser = checkCredentials(username, password);
           JsonObject obj = new JsonObject();
           if (validUser != null) {
            obj.addProperty("username", validUser.getUserName());
        }
        return obj.toString();
    } catch (Exception e) {
        System.out.println("Not a valid user... returning empty json object");
        return (new JsonObject()).toString();
    }
}

public String SignUp(String username, String password) throws Exception
{

//                if(file.exists()) {
//                    UserList newUserList =  updateUserList(file);
//
//                    userExist = checkUserName(inputUserName.getText().toString(), newUserList.getList(), v);
//                    //Log.d("ADDUSER", "IN SIGN IN" +newUserList.toString());
//                }
//                else {

    System.out.println("Entered username: " + username);
    System.out.println("Entered password: " + password);
    try {
        //User validUser = checkCredentials(username, password);
        boolean userExist = checkUserName(username);
        JsonObject obj = new JsonObject();

        if(!userExist)
        {
//                    
//                   
          addUser(username, password, "users.json", userlist);
          obj.addProperty("IsSignedUp", userExist);
          
//                    
//                    signUp(v);
      }
    return obj.toString();
} catch (Exception e) {
    System.out.println("User already exists... returning empty json object");
    return (new JsonObject()).toString();
}
//                }
//
//                // Since user does not exist yet, add to local memory

//                else {
//                    Toast.makeText(SignUpActivity.this, "Username already exist, please try another name", Toast.LENGTH_LONG).show();
//                }
}



public UserList updateUserList(File file) {
    UserList userTemp = null;
    try {
            //Log.d("ADDUSER", "file exists IN SIGN IN");
        InputStream inputStream = new FileInputStream(file);
        String myJson = inputStreamToString(inputStream);
        userTemp = new Gson().fromJson(myJson, UserList.class);
        inputStream.close();

            //Log.d("ADDUSER", userList.toString());
        return userTemp;
        
    } catch (IOException e) {
        e.printStackTrace();
    }
    return userTemp;
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
    public void addUser(String name, String password, String json , UserList userlist) {
        User newUser = new User();
        newUser.setUserName(name);
        newUser.setPassword(password);
        //newUser.setJson(json);

        userlist.addToList(newUser);

        //TODO: Add playlist add here
        /**
        final String path2 = getFilesDir().getAbsolutePath() + "/playlists.json";
        final File file2 = new File(path2);
        if(file2.exists()) {
            PlaylistHandler newPlaylistHandler =  updatePlaylistHandler(file2);
            addUserToPlaylist(newPlaylistHandler, name);
        }
        else {
            addUserToPlaylist(playlistHandler, name);
        }
        **/

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(userlist);
        JsonObject jObj = new Gson.fromJson(strJson, JsonObject.class);
        JsonArray playlists = new JsonArray();
        jObj.add("playLists", playlists);
        strJson = jObj.toString();

        try {
            String filePath =  "assets/users.json";
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(strJson.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            //Log.d("ADDUSER", "OUTPUTTED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}