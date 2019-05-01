import java.io.IOException;

import com.google.gson.*;

public interface MapReduceInterface {
  public void map(String key, JsonObject value, DFS context, String file) throws IOException;
  public void reduce(String key, JsonObject values, DFS context, String file) throws IOException;
}