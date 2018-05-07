package lipnus.com.realworld.submenu;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class InventoryListViewItem {

    public int id;
    public String name;
    public String iconUrl;
    public String obtainedAt;

    public InventoryListViewItem(int id, String name, String iconUrl, String obtainedAt) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.obtainedAt = obtainedAt;
    }

}