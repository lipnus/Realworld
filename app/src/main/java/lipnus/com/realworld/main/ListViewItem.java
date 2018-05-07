package lipnus.com.realworld.main;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class ListViewItem {

    public int id;
    public String title;
    public String date;
    public boolean accomplished;
    public String lastPlayed;

    public ListViewItem(int id, String title, String date, boolean accomplished, String lastPlayed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.accomplished = accomplished;
        this.lastPlayed = lastPlayed;
    }
}