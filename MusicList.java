import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.GsonBuilder;


public class MusicList implements Serializable{
    @SerializedName("music")
    private ArrayList<Music> list = new ArrayList<Music>();

    public MusicList() {
        list = new ArrayList<Music>();
    }

    public ArrayList<Music> getMusicList()
    {
        return this.list;
    }
    public Music get(int i) {
        return this.list.get(i);
    }
    public int size() {
        return this.list.size();
    }
    public void addSong(Music m) {
        list.add(m);
    }
    public Music getSong(String id) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getSongID().equals(id))
                return list.get(i);
        }
        return null;
    }
    public String returnSongs(String s) {
        int start = Integer.parseInt(s);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject retObject = new JsonObject();
        JsonArray musicArray = new JsonArray();
        for (int i = start; i < start+100; i++) {
            String musicString = gson.toJson(list.get(i));
            JsonObject musicJson = new Gson().fromJson(musicString, JsonObject.class);
            musicArray.add(musicJson);
            if (i == list.size() - 1) {
                retObject.addProperty("keepPulling", "false");
                break;
            }
            else if (i == start+100-1) {
                retObject.addProperty("keepPulling", "true");
            }
        }
        retObject.addProperty("currentIndex", Integer.toString(start+100));
        retObject.add("musiclist", musicArray);
        return retObject.toString();
    }
    @Override
    public String toString() {
        String results = "";
        for(Music m : list) {
            results += m.toString() + "\n";
        }
        return results;
    }
}
