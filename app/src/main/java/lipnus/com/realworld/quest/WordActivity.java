package lipnus.com.realworld.quest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.mission.MissionDetailActivity;
import lipnus.com.realworld.retro.ResponseBody.MissionDetail;
import lipnus.com.realworld.retro.ResponseBody.QuestDetail;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class WordActivity extends SlidingActivity {

    EditText inputEt;
    ImageView dragIv;

    TextView titleTv, hintTv;
    LinearLayout editLr;
    RetroClient retroClient;

    String answer;
    int questId;

    @Override
    public void init(Bundle savedInstanceState) {
        setContent(R.layout.activity_word);
        disableHeader();
        enableFullscreen();

        inputEt = findViewById(R.id.word_et);
        titleTv = findViewById(R.id.word_title_tv);
        hintTv = findViewById(R.id.word_hint_tv);
        dragIv = findViewById(R.id.drag_iv);
        editLr = findViewById(R.id.word_edit_lr);



        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        questId = iT.getExtras().getInt("questId", 0);


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

        postQuestDetail(questId);
    }

    public void setEditText(){


        inputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String inputStr = inputEt.getText().toString();

                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE: //Done버튼 눌렀을 때

                        if(inputStr.equals( answer )){

                            //정답
                            Intent iT = new Intent(getApplicationContext(), SuccessActivity.class);
                            iT.putExtra("questId", questId);
                            iT.putExtra("answer", answer);
                            startActivity(iT);

                            finish();

                        }else{
                            Toast.makeText(getApplicationContext(), "잘못된 암호입니다", Toast.LENGTH_LONG).show();
                            YoYo.with(Techniques.Tada)
                                    .duration(700)
                                    .playOn(editLr);
                        }
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "기본", Toast.LENGTH_LONG).show();
                        return false;
                }
                return true;
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

        hintTv.setText( data.text );
        answer = data.answer;

        setEditText();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        //키보드 자동 띄우기
        inputEt.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager manager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                manager.showSoftInput(inputEt, 0);
            }
        }, 100);


    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(5);
    }
}
