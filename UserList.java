import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserList implements Serializable
{
    @SerializedName("users")
    private ArrayList<User> list = new ArrayList<User>();

    public ArrayList<User> getList()
    {
        return this.list;
    }
    public void setList(ArrayList<User> list)
    {
        this.list = list;
    }
    public void addToList(User user)
    {
        list.add(user);
    }
    public void addPlaylist(String uName, String pName) {
        for (User u: list) {
            if (u.getUserName().equals(uName))
                u.addPlaylist(pName);
        }
    }
    public User.Playlist deletePlaylist(String uName, String pName) {
        for (User u: list) {
            if (u.getUserName().equals(uName))
                return u.deletePlaylist(pName);
        }
        return null;
    }
    public void addSongToPlaylist(String uName, String pName, String sID) {
        for (User u: list) {
            if (u.getUserName().equals(uName))
                u.addSongToPlaylist(pName, sID);
        }
    }
    public String deleteSongFromPlaylist(String uName, String pName, String sID) {
        for (User u: list) {
            if (u.getUserName().equals(uName))
                return u.deleteSongFromPlaylist(pName, sID);
        }
        return null;
    }

    @Override
    public String toString()
    {
        String results = "";
        for(User user : list)
        {
            results += user.toString();
        }
        return results;
    }
}