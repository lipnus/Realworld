package lipnus.com.realworld.retro.ResponseBody;

/**
 * Created by LIPNUS on 2018-02-19.
 */

public class Mission {

    public int id;
    public String name;
    public int score;
    public int maxScore;
    public String succeededAt;

    public Mission(int id, String name, int score, int maxScore, String succeededAt) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.succeededAt = succeededAt;
    }

}
