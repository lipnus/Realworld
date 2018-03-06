package lipnus.com.realworld.quest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import java.util.HashMap;

import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.mission.MissionDetailActivity;
import lipnus.com.realworld.retro.ResponseBody.QuestResult;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class SuccessActivity extends SlidingActivity {


    ImageView dragIv;
    RetroClient retroClient;

    TextView titleTv, contentTv;

    String answer;
    int questId;
    int missionTo=-1;


    @Override
    public void init(Bundle savedInstanceState) {
        setContent(R.layout.activity_success);
        disableHeader();
        enableFullscreen();

        dragIv = findViewById(R.id.drag_iv);
        titleTv = findViewById(R.id.success_title_tv);
        contentTv = findViewById(R.id.success_content_tv);
        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        questId = iT.getExtras().getInt("questId", 0);
        answer = iT.getExtras().getString("answer", "");

        postQuestResult(answer);

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
    }

    public void postQuestResult(String answer){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);
        parameters.put("answer", answer);

        retroClient.postQuestResult(questId, parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                QuestResult data = (QuestResult) receivedData;
                setQuestResult(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setQuestResult(QuestResult data){

        contentTv.setText( data.content);
        missionTo = data.missionTo;
    }


    public void onClick_success_next(View v){

        if(missionTo!=-1){

//            기존 미션디테일창 끔
            MissionDetailActivity mdActivity = (MissionDetailActivity) MissionDetailActivity.MDActivity;
            mdActivity.finish();

            Toast.makeText(getApplicationContext(), "다음미션으로 이동", Toast.LENGTH_LONG).show();

            Intent iT = new Intent(getApplicationContext(), MissionDetailActivity.class);
            iT.putExtra("missionId", missionTo);
            startActivity(iT);
            finish();


        }

    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(5);
    }
}

