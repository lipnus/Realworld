package lipnus.com.realworld.retro.ResponseBody;

import java.util.List;

/**
 * Created by LIPNUS on 2018-03-02.
 */

public class QuestDetail {

    public int id;
    public int type;
    public String image;
    public String text;
    public String answer;


    public QuestDetail(int id, int type, String image, String text, String answer) {
        this.id = id;
        this.type = type;
        this.image = image;
        this.text = text;
        this.answer = answer;
    }



}
