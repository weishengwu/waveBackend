import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("playlistJson")
    String playlistJson;
    @SerializedName("login")
    Boolean login;


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
    public void setJson(String json)
    {
        this.playlistJson = json;
    }
    public String getJson()
    {
        return this.playlistJson;
    }
    public void setLoginCheck(Boolean loginCheck)
    {
        this.login = loginCheck;
    }
    public Boolean getLoginCheck()
    {
        return this.login;
    }

    @Override
    public String toString()
    {
        return ("Username: " + username + ", Password: " + password + " ");
    }
}
