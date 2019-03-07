import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.io.Serializable;

public class User implements Serializable {
    public class Song {
        @SerializedName("ID")
        String songID;
        public Song(String id) {
            songID = id;
        }
        public String getSongID() {
            return songID;
        }
    }
    public class Playlist {
        @SerializedName("plName")
        private String playlistName;
        @SerializedName("song")
        private ArrayList<Song> songIDs = new ArrayList<Song>();

        public Playlist(String p) {
            playlistName = p;
        }
        public String getPlaylistName() {
            return playlistName;
        }
        public ArrayList<String> getPlaylistSongIDs() {
            ArrayList<String> ret = new ArrayList<String>();
            for (Song s: songIDs) {
                ret.add(s.getSongID());
            }
            return ret;
        }
        public void addSong(String sID) {
            songIDs.add(new Song(sID));
        }
        @Override
        public String toString() {
            String retString = "";
            retString += playlistName + "\n";
            for (Song s: songIDs) {
                retString += s.getSongID() + "\n";
            }
            return retString;
        }
    }


    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("playLists")
    private ArrayList<Playlist> listOfPlaylists = new ArrayList<Playlist>();

    public User(String user, String pw) {
        username = user;
        password = pw;
        listOfPlaylists = new ArrayList<Playlist>();
    }

    public void setUserName(String name)
    {
        this.username = name;
    }
    public String getUserName()
    {
        return this.username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getPassword()
    {
        return this.password;
    }
    public void addPlaylist(String pName) {
        listOfPlaylists.add(new Playlist(pName));
    }
    public void addSongToPlaylist(String pName, String songID) {
        getPlaylist(pName).addSong(songID);
    }
    public Playlist getPlaylist(String pName) {
        for (Playlist p: listOfPlaylists) {
            if (p.getPlaylistName().equals(pName))
            return p;
        }
        return null;
    }
    public ArrayList<Playlist> getListOfPlaylists() {
        return listOfPlaylists;
    }

    @Override
    public String toString()
    {
        String retString = "";
        retString += "username: " + username + "\n";
        retString += "password: " + password + "\n";
        for (Playlist p: listOfPlaylists) {
            retString += p.toString();
        }
        return retString;
    }
}
