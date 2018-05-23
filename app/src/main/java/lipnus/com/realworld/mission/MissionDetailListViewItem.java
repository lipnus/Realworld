package lipnus.com.realworld.mission;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class MissionDetailListViewItem {

    public int questId;
    public int type;
    public String label;

    public MissionDetailListViewItem(int questId, int type, String label) {
        this.questId = questId;
        this.type = type;
        this.label = label;
    }
}