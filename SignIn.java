import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

public class SignIn {
    UserList userlist;    
    public SignIn(){
        userlist = ReadFile.loadJsonIntoUserList();
    }
    public void refreshUserList() {
        userlist = ReadFile.loadJsonIntoUserList();
    }
    public String Login(String username, String password) throws Exception {
        try {
            User validUser = checkCredentials(username, password);
            JsonObject obj = new JsonObject();
            if (validUser != null) {
                //pack return jsonobject with music list and playlist info
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String userString = gson.toJson(validUser);
                JsonObject userJson = new Gson().fromJson(userString, JsonObject.class);
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
                refreshUserList();
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
        ReadFile.writeUserListToJson(userlist);
    }
    
    
}