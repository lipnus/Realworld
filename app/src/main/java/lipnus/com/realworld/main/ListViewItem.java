package lipnus.com.realworld.main;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class ListViewItem {

    private String titile;
    private String date;

    public ListViewItem(String titile, String date) {
        this.titile = titile;
        this.date = date;
    }

    public String getTitile() {
        return titile;
    }

    public String getDate() {
        return date;
    }

}