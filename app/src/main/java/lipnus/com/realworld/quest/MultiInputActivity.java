package lipnus.com.realworld.quest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.quest.list_choice.Choice_ListViewAdapter;
import lipnus.com.realworld.quest.list_choice.Choice_ListViewItem;
import lipnus.com.realworld.quest.list_multi.Multi_ListViewAdapter;
import lipnus.com.realworld.quest.list_multi.Multi_ListViewItem;
import lipnus.com.realworld.retro.ResponseBody.MiniQuest;
import lipnus.com.realworld.retro.ResponseBody.QuestDetail;
import lipnus.com.realworld.retro.ResponseBody.QuestDetail_Multi;
import lipnus.com.realworld.retro.ResponseBody.QuestResult;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class MultiInputActivity extends AppCompatActivity {

    @BindView(R.id.drag_iv) ImageView dragIv;
    @BindView(R.id.multi_title_iv) ImageView titleIv;
    @BindView(R.id.multi_title_tv) TextView titleTv;
    @BindView(R.id.multi_hint_tv) TextView hintTv;
    @BindView(R.id.multi_listview) ListView listView;
    Multi_ListViewAdapter adapter;

    RetroClient retroClient;
    AudioManager  mAudioManager;

    int questId;
    Vibrator vibrator;

    QuestDetail_Multi questDetail;

    String answerText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        questId = iT.getExtras().getInt("questId", 0);

        //진동
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        //오디오상태 체크
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        //리스트뷰 세팅
        initListview();

        //상단이미지
        Glide.with(this)
                .load(R.drawable.dragdown)
                .into(dragIv);
        dragIv.setScaleType(ImageView.ScaleType.FIT_XY);

        dragIv.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.BounceInDown)
                        .duration(1500)
                        .playOn(dragIv);
            }
        });

        postQuestDetail_Multi(questId);
    }

    public void initListview(){

        //리스트뷰
        adapter = new Multi_ListViewAdapter();
        listView.setAdapter(adapter);

    }

    public void postQuestDetail_Multi(int questId){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postQuestDetail_Multi(questId, parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                QuestDetail_Multi data = (QuestDetail_Multi) receivedData;

                //맞으면 등록해주는거 때문에 전역에 하나 저장해둠
                questDetail = data;
                setQuest(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setQuest(QuestDetail_Multi data){
        Log.e("HHTT", "받은값: " + data);

        //html적용된 textview에 데이터입력
        if(Build.VERSION.SDK_INT >= 24){
            hintTv.setText(Html.fromHtml(data.text.description, Html.FROM_HTML_MODE_COMPACT));
        }else{
            hintTv.setText(Html.fromHtml(data.text.description));
        }

        //이미지 넣기
        Glide.with(this)
                .load(GlobalApplication.imgPath + data.text.image)
                .into(titleIv);
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);


        //리스트뷰에 넣기
        List<MiniQuest> miniQuest = data.text.quest;
        for(int i=0; i<miniQuest.size(); i++){
            adapter.addItem(miniQuest.get(i).order, miniQuest.get(i).text, miniQuest.get(i).answer);
        }

        //리스트뷰 사이즈
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = miniQuest.size() * convertDpToPixel(100, this);
        listView.setLayoutParams(params);
        listView.requestLayout();

        adapter.notifyDataSetChanged();
    }



    //정답체크
    public void postQuestResult(String answer){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);
        parameters.put("answer", answer );

        Log.e("HHTT", "서버로 보낸 정답: " + answer);


        retroClient.postQuestResult(questId, parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                wrongAnswer();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                QuestResult data = (QuestResult) receivedData;

                if(data.result){
                    MoveToSuccessActivity();
                }else{
                    wrongAnswer();
                }

            }

            @Override
            public void onFailure(int code) {
                wrongAnswer();
            }
        });
    }

    public void wrongAnswer(){

        Toast.makeText(getApplicationContext(), "잘못된 암호입니다", Toast.LENGTH_LONG).show();
        if(mAudioManager.getRingerMode() != 0){
            vibrator.vibrate(300);
        }
        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(listView);

        //맞은거 적으면 정렬은 해준다
        guideHint();
    }

    public void guideHint(){
        int answerCount = adapter.getCount();
        Boolean isAnswer;

        for(int i=0; i<answerCount; i++){
            isAnswer = false;
            Multi_ListViewItem item = (Multi_ListViewItem)adapter.getItem(i);

            for(int j=0; j<answerCount; j++){

                if(item.userAnswer.equals( questDetail.text.quest.get(j).answer )){
                    adapter.setAnswer(j, item.userAnswer);

                    if(i!=j){ adapter.setAnswer(i, "X"); }
                    isAnswer = true;
                }
            }
            if(!isAnswer){ adapter.setAnswer(i, "X"); }
        }
        adapter.notifyDataSetChanged();

    }


    public void MoveToSuccessActivity(){

        Intent iT = new Intent(getApplicationContext(), SuccessActivity.class);
        iT.putExtra("questId", questId);
        iT.putExtra("answer", answerText );
        startActivity(iT);
        finish();
    }


    public void onClick_multi_finish(View v){

        int count = adapter.getCount();
        String answerStr="";

        for(int i=0; i<count; i++){
            if(i!=0) answerStr += "-";

            Multi_ListViewItem item = (Multi_ListViewItem)adapter.getItem(i);
            answerStr += item.userAnswer;
        }

        //전역변수에 입력
        answerText = answerStr;

        //서버로 작성단 답안 텍스트 보냄
        postQuestResult(answerStr);

    }









    public int convertDpToPixel(float dp, Context context){

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);

        return (int)px;

    }

}
