package lipnus.com.realworld.quest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import java.util.HashMap;

import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.main.Main2Activity;
import lipnus.com.realworld.main.MainActivity;
import lipnus.com.realworld.mission.MissionActivity;
import lipnus.com.realworld.mission.MissionDetailActivity;
import lipnus.com.realworld.retro.ResponseBody.QuestResult;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class SuccessActivity extends AppCompatActivity {


    ImageView dragIv;
    RetroClient retroClient;
    TextView titleTv, contentTv;
    TextView detailBtn;

    String answer;
    int questId;
    int missionTo=-1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.bind(this);

        dragIv = findViewById(R.id.drag_iv);
        titleTv = findViewById(R.id.success_title_tv);
        contentTv = findViewById(R.id.success_content_tv);
        detailBtn = findViewById(R.id.detail_btn_tv);

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


//        contentTv.setText( Html.fromHtml(data.content) );

        WebView web = (WebView) findViewById(R.id.webapp);

        // 자바스크립트 허용
        web.getSettings().setJavaScriptEnabled(true);

        //웹뷰 크기
        web.setInitialScale(250);

        // 스크롤바 없애기
        web.setHorizontalScrollBarEnabled(true);
        web.setVerticalScrollBarEnabled(false);
        web.setBackgroundColor(0);

        web.loadData(data.content, "text/html", "UTF-8");

        if(data.missionTo==null){
            detailBtn.setText("미션리스트로 이동");
            missionTo = -1;
        }else{
            detailBtn.setText("다음미션으로 이동");
            missionTo = Integer.parseInt(data.missionTo);
        }

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
        }else{

            Intent iT = new Intent(getApplicationContext(), MissionActivity.class);
            iT.putExtra("scenarioId", GlobalApplication.nowMission);
            iT.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(iT);
            finish();
        }
    }


}

