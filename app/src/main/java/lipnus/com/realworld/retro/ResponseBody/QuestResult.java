package lipnus.com.realworld.retro.ResponseBody;

/**
 * Created by LIPNUS on 2018-03-02.
 */

public class QuestResult {

    public boolean result;

    public String content;
    public int obtainedItem;
    public int missionTo;
    public int missionUnlock;

    public String correctAnswer;

    public QuestResult(boolean result, String contact, int obtainedItem, int missionTo, int missionUnlock, String correctAnswer) {
        this.result = result;
        this.content = contact;
        this.obtainedItem = obtainedItem;
        this.missionTo = missionTo;
        this.missionUnlock = missionUnlock;
        this.correctAnswer = correctAnswer;
    }
}
