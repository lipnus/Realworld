package lipnus.com.realworld.main;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class ListViewItem {

    public String title;
    public String date;
    public boolean accomplished;

    public ListViewItem(String title, String date, boolean accomplished) {
        this.title = title;
        this.date = date;
        this.accomplished = accomplished;
    }
}