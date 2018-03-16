package lipnus.com.realworld.mission;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class MissionListViewItem {

    public String titile;
    public String date;
    public String state;

    public MissionListViewItem(String titile, String date, String state) {
        this.titile = titile;
        this.date = date;
        this.state = state;
    }


}