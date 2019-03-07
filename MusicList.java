import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class MusicList implements Serializable{
    @SerializedName("music")
    private ArrayList<Music> list = new ArrayList<Music>();

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
    public Music getSong(String id) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getSongID().equals(id))
                return list.get(i);
        }
        return null;
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
