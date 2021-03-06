import com.google.gson.annotations.SerializedName;
import java.io.*;
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
    public void addSongs(MusicList m) {
        for(int i = 0; i < m.size(); i++) {
            list.add(m.get(i));
        }
    }
    public Music getSong(String id) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getSongID().equals(id))
                return list.get(i);
        }
        return null;
    }
    // public String returnSongs(String s) {
    //     int start = Integer.parseInt(s);
    //     Gson gson = new GsonBuilder().setPrettyPrinting().create();
    //     JsonObject retObject = new JsonObject();
    //     JsonArray musicArray = new JsonArray();
    //     for (int i = start; i < start+100; i++) {
    //         String musicString = gson.toJson(list.get(i));
    //         JsonObject musicJson = new Gson().fromJson(musicString, JsonObject.class);
    //         musicArray.add(musicJson);
    //         if (i == list.size() - 1) {
    //             retObject.addProperty("keepPulling", "false");
    //             break;
    //         }
    //         else if (i == start+100-1) {
    //             retObject.addProperty("keepPulling", "true");
    //         }
    //     }
    //     retObject.addProperty("currentIndex", Integer.toString(start+100));
    //     retObject.add("musiclist", musicArray);
    //     return retObject.toString();
    // }
    public String returnSongs(String s) {
        int start = Integer.parseInt(s)*100;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject retObject = new JsonObject();
        JsonArray musicArray = new JsonArray();
        for (int i = start; i < start+100; i++) {
            if (i == list.size() - 1)
                break;
            String musicString = gson.toJson(list.get(i));
            JsonObject musicJson = new Gson().fromJson(musicString, JsonObject.class);
            musicArray.add(musicJson);
        }
        retObject.add("musiclist", musicArray);
        return retObject.toString();
    }
    // public String searchSong(String query) {
    //     String artist = "";
    //     String song = "";
    //     Gson gson = new GsonBuilder().setPrettyPrinting().create();
    //     JsonObject retObject = new JsonObject();
    //     JsonArray musicArray = new JsonArray();
    //     if (query.contains("-")) {
    //         artist = query.split("-")[0].trim();
    //         song = query.split("-")[1].trim();
    //     }
    //     else {
    //         artist = "fdsaifhdsiofhdihafidsoafa";
    //         song = "djadsajdsahofiahiofahfoias";
    //     }
    //     for(int i = 0; i < list.size(); i++) {
    //         if (    (   list.get(i).getArtistName().toLowerCase().contains(artist) &&  list.get(i).getSongTitle().contains(song)   )
    //                 ||  list.get(i).getArtistName().toLowerCase().contains(query)
    //                 ||  list.get(i).getSongTitle().toLowerCase().contains(query)     ) {
    //             String musicString = gson.toJson(list.get(i));
    //             JsonObject musicJson = new Gson().fromJson(musicString, JsonObject.class);    
    //             musicArray.add(musicJson);
    //         }
    //     }
    //     retObject.add("musiclist", musicArray);
    //     return retObject.toString();
    // }
    public String searchSong(String query) {
        try {
            File file = new File("assets/search/" + query.toLowerCase() + ".json");
            if (file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                String myJson = ReadFile.inputStreamToString(inputStream);
                return myJson;
            }
            else {
                JsonObject retObject = new JsonObject();
                retObject.add("musiclist", new JsonArray());
                return retObject.toString();            
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject retObject = new JsonObject();
        retObject.add("musiclist", new JsonArray());
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
