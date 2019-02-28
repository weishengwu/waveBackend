import java .util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserList implements Serializable
{
    @SerializedName("users")
    private List<User> list = new ArrayList<User>();

    public List<User> getList()
    {
        return this.list;
    }

    public void setList(List<User> list)
    {
        this.list = list;
    }

    public void addToList(User user)
    {
        list.add(user);
    }
    @Override
    public String toString()
    {
        String results = "";
        for(User user : list)
        {
            results += user.toString();
        }
        return results;
    }
}