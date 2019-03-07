import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Music implements Serializable {
    class Artist {
        @SerializedName("name")
        String artistName;
    }
    class Song {
        @SerializedName("id")
        String songID;
        @SerializedName("title")
        String songTitle;
    }

    //Initialize classes
    @SerializedName("artist")
    Artist artist;
    @SerializedName("song")
    Song song;

    // Getters and setters
    public void setArtistName(String n) {
        this.artist.artistName = n;
    }
    public String getArtistName() {
        return this.artist.artistName;
    }
    public void setSongID(String n) {
        this.song.songID = n;
    }
    public String getSongID() {
        return this.song.songID;
    }
    public void setSongTitle(String n) {
        this.song.songTitle = n;
    }
    public String getSongTitle() {
        return this.song.songTitle;
    }
    public String toString() {
        String results = "";
        results += "\n"
                + String.format("%" + 40 + "s", "ID: " + this.getSongID())
                + String.format("%" + 55 + "s", "Title: " + this.getSongTitle())
                + String.format("%" + 40 + "s", "Artist: " + this.getArtistName());
        return results;
    }
}

