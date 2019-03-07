import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

public class EditUser {
    private UserList userlist;
    public EditUser() {
        userlist = ReadFile.loadJsonIntoUserList();
    }
    public void refreshUserList() {
        userlist = ReadFile.loadJsonIntoUserList();
    }
    public String addPlaylist(String uName, String pName) {
        userlist.addPlaylist(uName, pName);
        ReadFile.writeUserListToJson(userlist);
        refreshUserList();
        return returnUpdatedUser(userlist.getUser(uName));
    }
    public String deletePlaylist(String uName, String pName) {
        userlist.deletePlaylist(uName, pName);
        ReadFile.writeUserListToJson(userlist);
        refreshUserList();
        return returnUpdatedUser(userlist.getUser(uName));
    }
    public String addSongToPlaylist(String uName, String pName, String sID) {
        userlist.addSongToPlaylist(uName, pName);
        ReadFile.writeUserListToJson(userlist);
        refreshUserList();
        return returnUpdatedUser(userlist.getUser(uName));
    }
    public String deleteSongFromPlaylist(String uName, String pName, String sID) {
        userlist.deleteSongFromPlaylist(uName, pName, sID);
        ReadFile.writeUserListToJson(userlist);
        refreshUserList();
        return returnUpdatedUser(userlist.getUser(uName));
    }
    public String returnUpdatedUser(User user) {
        String userString = gson.toJson(user);
        JsonObject userJson = new Gson().fromJson(userString, JsonObject.class);
        return userJson.toString();
    }
}