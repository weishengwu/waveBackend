import java.io.IOException;
import com.google.gson.*;

public class Mapper implements MapReduceInterface {
  public void map(String key, JsonObject value, DFS context, String file) throws IOException {
    String newKey = key;
    JsonObject newValue = value;
    context.emit(newKey, newValue, file);
  }

  public void reduce(String key, JsonObject values, DFS context, String file) throws IOException {
    context.emit(key, values, file);
  }

  //public void map(String key, JsonObject value, DFS context, String file) throws IOException;
  //public void reduce(String key, JsonObject values, DFS context, String file) throws IOException;
}