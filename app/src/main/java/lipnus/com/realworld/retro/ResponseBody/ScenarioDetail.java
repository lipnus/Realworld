package lipnus.com.realworld.retro.ResponseBody;

import java.util.List;

/**
 * Created by LIPNUS on 2018-02-19.
 */

public class ScenarioDetail {

    public int id;
    public String name;
    public int score;
    public int maxScore;
    public String lastPlayed;
    public String coverImageUrl;
    public String synopsis;
    public List<mission> missions;


    //생성자
    public ScenarioDetail(int id, String name, int score, int maxScore, String lastPlayed, String coverImgeUrl, String synopsis, List<mission> missions) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.lastPlayed = lastPlayed;
        this.coverImageUrl = coverImgeUrl;
        this.synopsis = synopsis;
        this.missions = missions;
    }

    //하위클래스
    static class mission{
        public int id;
        public String name;
        public int score;
        public int maxScore;
        public String succeededAt;

        public mission(int id, String name, int score, int maxScore, String succeededAt) {
            this.id = id;
            this.name = name;
            this.score = score;
            this.maxScore = maxScore;
            this.succeededAt = succeededAt;
        }
    }
}
