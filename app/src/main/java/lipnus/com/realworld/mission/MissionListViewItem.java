package lipnus.com.realworld.mission;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class MissionListViewItem {

    public int id;
    public boolean looked;
    public String name;
    public String date;


    public MissionListViewItem(int id, boolean looked, String name, String date) {
        this.id = id;
        this.looked = looked;
        this.name = name;
        this.date = date;
    }
}