import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

public class SongHandler {

    public SongHandler() {
    }

    public String getSongFragment(String songID, String frag) {
        JsonObject ret = new JsonObject();
        SongDispatcher songdispatcher = new SongDispatcher();
        long i = Long.parseLong(frag);
        int filesize = 0;
        int fragmentSize = 8192;
        ret.addProperty("currentIndex", i+1);
        ret.addProperty("keepPulling", "true");
        if (fragmentSize*(i+1) > filesize)
            ret.addProperty("keepPulling", "false");
        try {
            filesize = songdispatcher.getFileSize(songID);
            ret.addProperty("data", songdispatcher.getSongChunk(songID, i));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException et) {
            et.printStackTrace();
        }
        return ret.toString();
    }
}