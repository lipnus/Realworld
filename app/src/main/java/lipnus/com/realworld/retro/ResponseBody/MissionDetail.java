package lipnus.com.realworld.retro.ResponseBody;

import java.util.List;

/**
 * Created by Sunpil on 2018-02-22.
 */

public class MissionDetail {

    public int id;
    public String name;
    public int score;
    public int maxScore;
    public String image;
    public String SucceededAt;
    public String content;
    public List<Quest> quests;
    public String video;

    public MissionDetail(int id, String name, int score, int maxScore, String image, String succeededAt, String content, List<Quest> quests, String video) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.image = image;
        SucceededAt = succeededAt;
        this.content = content;
        this.quests = quests;
        this.video = video;
    }
}
