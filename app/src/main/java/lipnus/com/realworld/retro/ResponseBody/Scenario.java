package lipnus.com.realworld.retro.ResponseBody;

/**
 * Created by Sunpil on 2018-02-16.
 */

public class Scenario {
    public String id;
    public String name;
    public int score;
    public int maxScore;
    public String lastPlayed;
    public boolean accomplished;

    public Scenario(String id, String name, int score, int maxScore, String lastPlayed, boolean accomplished) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.lastPlayed = lastPlayed;
        this.accomplished = accomplished;
    }



//• id: 시나리오의 ID
    //• name: 시나리오의 이름
    //• score: 사용자가 획득한 점수
    //• maxScore: 최대로 획득 가능한 점수
    //• lastPlayed: 마지막 플레이한 일시

}
