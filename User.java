import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;

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

    @Override
    public String toString()
    {
        return ("Username: " + username + ", Password: " + password + " ");
    }
}
