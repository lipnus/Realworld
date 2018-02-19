package lipnus.com.realworld.mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.ResponseBody.ScenarioDetail;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class MissionActivity extends AppCompatActivity {

    @BindView(R.id.mission_title_iv)
    ImageView titleIv;

    @BindView(R.id.mission_back_iv)
    ImageView backIv;

    @BindView(R.id.mission_title_tv)
    TextView titleTv;

    @BindView(R.id.mission_subtitle_tv)
    TextView subtitleTv;

    RetroClient retroClient;

    int scenarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        scenarioId = iT.getExtras().getInt("scenarioId");
        postScenarioDetail(scenarioId);




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

                Toast.makeText(getApplicationContext(), "성공: " + data.id, Toast.LENGTH_SHORT).show();
                setSenario(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSenario(ScenarioDetail data){

        titleTv.setText( data.name );
        subtitleTv.setText( data.name + " - 미션목록");

        Log.e("MMSS", GlobalApplication.imgPath + data.coverImageUrl);

        Glide.with(this)
                .load(GlobalApplication.imgPath + data.coverImageUrl)
                .into(titleIv);
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);
//        Log.e("MMSS", "이미지: " + data.coverImgeUrl );
    }
}
