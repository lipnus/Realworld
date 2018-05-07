package lipnus.com.realworld.retro.ResponseBody;

/**
 * Created by LIPNUS on 2018-05-04.
 */

public class Inventory {
    public int id;
    public String name;
    public String iconUrl;
    public String obtainedAt;

    public Inventory(int id, String name, String iconUrl, String obtainedAt) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.obtainedAt = obtainedAt;
    }
}
