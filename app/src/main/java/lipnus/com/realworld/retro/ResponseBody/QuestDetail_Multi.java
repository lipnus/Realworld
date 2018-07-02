package lipnus.com.realworld.retro.ResponseBody;

import java.util.List;

/**
 * Created by LIPNUS on 2018-03-02.
 */

public class QuestDetail_Multi {

    public int id;
    public int image;
    public SubText text;
    public int type;

    public static class SubText{
        public String description;
        public String image;
        public List<MiniQuest> quest;
        public int type;
    }


}
