import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserList implements Serializable
{
    @SerializedName("users")
    private ArrayList<User> list = new ArrayList<User>();

    public ArrayList<User> getList()
    {
        return this.list;
    }

    public void setList(ArrayList<User> list)
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