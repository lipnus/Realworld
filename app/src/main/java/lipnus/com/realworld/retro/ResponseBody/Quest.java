package lipnus.com.realworld.retro.ResponseBody;

/**
 * Created by Sunpil on 2018-02-22.
 */

public class Quest {

    public int id;
    public int type;
    public String label;

    public Quest(int id, int type, String label) {
        this.id = id;
        this.type = type;
        this.label = label;
    }
}
