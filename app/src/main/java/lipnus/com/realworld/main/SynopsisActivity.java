package lipnus.com.realworld.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.mission.MissionActivity;
import lipnus.com.realworld.retro.ResponseBody.ScenarioDetail;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class SynopsisActivity extends AppCompatActivity {

    RetroClient retroClient;
    public static int scenarioId; //네비게이션에서 재사용된다
    @BindView(R.id.synopsis_back_iv) ImageView backIv;
    @BindView(R.id.synopsis_backgroud_iv) ImageView backgroudIv;
    @BindView(R.id.synopsis_tv) TextView synopsisTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synopsis);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        scenarioId = iT.getExtras().getInt("scenarioId");

        //서버호출
        postScenarioDetail(scenarioId);

        //백버튼
        Glide.with(this)
                .load(R.drawable.back)
                .into(backIv);
        backIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }



    public void postScenarioDetail(int scenarioId){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postScenariosDetail(scenarioId, parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ScenarioDetail data = (ScenarioDetail) receivedData;
                setSynopsis(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSynopsis(ScenarioDetail data){

        Glide.with(this)
                .load(GlobalApplication.imgPath + data.coverImageUrl)
                .into(backgroudIv);
        backgroudIv.setScaleType(ImageView.ScaleType.FIT_XY);

        //텍스트뷰에 html적용
        if(Build.VERSION.SDK_INT >= 24){
            synopsisTv.setText(Html.fromHtml(data.synopsis, Html.FROM_HTML_MODE_COMPACT));
        }else{
            synopsisTv.setText(Html.fromHtml(data.synopsis));
        }

    }


    public void onClick_synopsis(View v){
        Intent iT = new Intent(this, MissionActivity.class);
        iT.putExtra("scenarioId", scenarioId);
        startActivity(iT);
        finish();
    }


    public void onClick_back(View v){
        Intent iT = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(iT);
        finish();
    }
}
