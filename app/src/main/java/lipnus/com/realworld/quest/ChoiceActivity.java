package lipnus.com.realworld.quest;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import lipnus.com.realworld.retro.ResponseBody.Hint;
import lipnus.com.realworld.retro.ResponseBody.QuestDetail;
import lipnus.com.realworld.retro.ResponseBody.QuestResult;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class ChoiceActivity extends AppCompatActivity {

    @BindView(R.id.drag_iv) ImageView dragIv;
    @BindView(R.id.choice_title_tv) TextView titleTv;
    @BindView(R.id.choice_hint_tv) TextView hintTv;
    @BindView(R.id.choice_listview) ListView listView;
    Choice_ListViewAdapter adapter;

    RetroClient retroClient;
    AudioManager  mAudioManager;

    int questId;
    Vibrator vibrator;

    int answerNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
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



        postQuestDetail(questId); //힌트
        postHints(questId); //4지선다
    }

    public void initListview(){

        //리스트뷰
        adapter = new Choice_ListViewAdapter();
        listView.setAdapter(adapter);

        //리스트뷰의 클릭리스너
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Choice_ListViewItem hintItem = (Choice_ListViewItem) parent.getAdapter().getItem(position);
                answerNumber = hintItem.id;

                postQuestResult(hintItem.id);
            }
        });
    }

    public void postQuestDetail(int questId){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postQuestDetail(questId, parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                QuestDetail data = (QuestDetail) receivedData;
                setQuest(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setQuest(QuestDetail data){

        //텍스트뷰에 html적용
        if(Build.VERSION.SDK_INT >= 24){
            hintTv.setText(Html.fromHtml(data.text, Html.FROM_HTML_MODE_COMPACT));
        }else{
            hintTv.setText(Html.fromHtml(data.text));
        }

    }



    //서버에서 4지선다를 받아옴
    public void postHints(int questId){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postHints(questId, parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                List<Hint> data = (List<Hint>) receivedData;
                setHints(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setHints(List<Hint> hints){

        Log.e("HHTT", "힌트: " + hints);

        for(int i=0; i<hints.size(); i++){
            adapter.addItem(hints.get(i).description, hints.get(i).id);
        }
        adapter.notifyDataSetChanged();

    }


    //서버로 정답을 보냄(정답이 아니면 500. Success하면 정답)
    public void postQuestResult(int answer){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);
        parameters.put("answer", Integer.toString(answer) ); //서버에서 이건 string으로 받는듯..

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
        Toast.makeText(getApplicationContext(), "잘못된 암호입니다.", Toast.LENGTH_LONG).show();
        if(mAudioManager.getRingerMode() != 0){
            vibrator.vibrate(300);
        }
        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(listView);
    }


    //정답체크
    public void MoveToSuccessActivity(){

        Intent iT = new Intent(getApplicationContext(), SuccessActivity.class);
        iT.putExtra("questId", questId);
        iT.putExtra("answer", Integer.toString(answerNumber) );
        startActivity(iT);
        finish();
    }





}
