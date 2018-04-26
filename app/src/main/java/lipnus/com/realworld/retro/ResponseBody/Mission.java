package lipnus.com.realworld.retro.ResponseBody;

/**
 * Created by LIPNUS on 2018-02-19.
 */

public class Mission {

    public int id;
    public boolean locked;
    public int maxScore;
    public String name;
    public int order;
    public int score;
    public String succeededAt;

    public Mission(int id, boolean locked, int maxScore, String name, int order, int score, String succeededAt) {
        this.id = id;
        this.locked = locked;
        this.maxScore = maxScore;
        this.name = name;
        this.order = order;
        this.score = score;
        this.succeededAt = succeededAt;
    }
}
