package lipnus.com.realworld.submenu;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class Mypage_ListViewItem {

    public String scenarioTitle;
    public int score;
    public int maxScore;


    public Mypage_ListViewItem(String scenarioTitle, int score, int maxScore) {
        this.scenarioTitle = scenarioTitle;
        this.score = score;
        this.maxScore = maxScore;
    }
}