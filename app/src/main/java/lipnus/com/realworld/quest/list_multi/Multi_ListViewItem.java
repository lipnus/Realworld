package lipnus.com.realworld.quest.list_multi;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 * 생각해보니, MiniQuest Class랑 같아서 만들필요가 없지만 앞에것들도 이렇게 해와서 형태 유지
 *
 */

public class Multi_ListViewItem {

    public int order;
    public String text;
    public String answer;
    public String userAnswer;

    public Multi_ListViewItem(int order, String text, String answer) {
        this.order = order;
        this.text = text;
        this.answer = answer;
        this.userAnswer = "";
    }
}