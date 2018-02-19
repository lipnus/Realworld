package lipnus.com.realworld.mission;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class MissionListViewItem {

    private String titile;
    private String date;
    private String state;

    public MissionListViewItem(String titile, String date, String state) {
        this.titile = titile;
        this.date = date;
        this.state = state;
    }

    public String getTitile() {
        return titile;
    }

    public String getDate() {
        return date;
    }

    public String getState() {
        return state;
    }
}