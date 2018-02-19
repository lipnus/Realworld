package lipnus.com.realworld.mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.SimpleFunction;
import lipnus.com.realworld.main.ListViewAdapter;
import lipnus.com.realworld.main.MainActivity;
import lipnus.com.realworld.retro.ResponseBody.Mission;
import lipnus.com.realworld.retro.ResponseBody.Scenario;
import lipnus.com.realworld.retro.ResponseBody.ScenarioDetail;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class MissionActivity extends AppCompatActivity {

    @BindView(R.id.mission_title_iv) ImageView titleIv;

    @BindView(R.id.mission_back_iv) ImageView backIv;

    @BindView(R.id.mission_title_tv) TextView titleTv;

    @BindView(R.id.mission_subtitle_tv) TextView subtitleTv;

    @BindView(R.id.mission_listview)
    ListView listview;

    MissionListViewAdapter adapter;


    RetroClient retroClient;

    int scenarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //리스트뷰
        adapter = new MissionListViewAdapter();
        listview.setAdapter(adapter);

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        scenarioId = iT.getExtras().getInt("scenarioId");
        postScenarioDetail(scenarioId);
    }

    public void onClick_mission_back(View v){
        Intent iT = new Intent(this, MainActivity.class);
        startActivity(iT);
        finish();
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

        //상단타이틀
        titleTv.setText( data.name );
        subtitleTv.setText( data.name + " - 미션목록");

        //상단이미지
        Glide.with(this)
                .load(GlobalApplication.imgPath + data.coverImageUrl)
                .centerCrop()
                .into(titleIv);
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);


        //정렬(call by reference이므로 걍 이렇게 하면 됨)
        missionArray(data.missions);

    }


    //받은 시나리오 데이터들을 정렬(call by reference)
    public void missionArray(List<Mission> missionList){

        int listSize = missionList.size();
        int nowIndex= listSize;

        Log.e("EVEV", "사이즈: " + listSize);


        //어디까지 클리어했는지 확인
        for(int i=0; i<listSize; i++){
            Mission mission = missionList.get(i);

            if(mission.succeededAt == null){
                nowIndex = i;
                mission.succeededAt="진행중";
                mission.name = "#" + (i+1) + ". " + mission.name;

                adapter.addItem(mission.name, mission.succeededAt, "now");
                break;
            }else{
                adapter.addItem(mission.name, mission.succeededAt, "open");
            }
        }

        //아직 클리어 하지 않은 부분들 처리
        for(int i=nowIndex+1; i<listSize; i++){
            Mission mission = missionList.get(i);
            mission.name = "#" + (i+1) + ". " + "LOCKED";

            adapter.addItem(mission.name, mission.succeededAt, "locked");
        }

        adapter.notifyDataSetChanged();
    }
}
