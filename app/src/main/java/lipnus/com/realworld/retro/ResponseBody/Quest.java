package lipnus.com.realworld.retro.ResponseBody;

/**
 * Created by Sunpil on 2018-02-22.
 */

public class Quest {

    public int id;
    public int type;
    public String label;
    public int locked;

    public Quest(int id, int type, String label, int locked) {
        this.id = id;
        this.type = type;
        this.label = label;
        this.locked = locked;
    }

}
