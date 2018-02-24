package lipnus.com.realworld.mission;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.ResponseBody.MissionDetail;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;
import com.klinker.android.sliding.SlidingActivity;

public class MissionDetailActivity extends SlidingActivity {

    TextView titleTv, contentTv;
    YouTubePlayerView youTubePlayerView;

    RetroClient retroClient;
    int missionId;

    @Override
    public void init(Bundle savedInstanceState) {
        setContent(R.layout.activity_mission_detail);

//        setTitle("Mission");
//        setImage(R.drawable.main);
        disableHeader();
        enableFullscreen();

        titleTv = findViewById(R.id.detail_title_tv);
        contentTv = findViewById(R.id.detail_content_tv);
        youTubePlayerView = findViewById(R.id.detail_youtube);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        missionId = iT.getExtras().getInt("missionId", 0);
        postMissionDetail(missionId);

        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        String videoId = "D76OSIdqtak";
                        initializedYouTubePlayer.loadVideo(videoId, 0);
                    }
                });
            }
        }, true);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }


    public void postMissionDetail(int missionId){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postMissionsDetail(missionId, parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                MissionDetail data = (MissionDetail) receivedData;

                Log.e("CCC", "미션상세데이터: " + data.content);
                setMission(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setMission(MissionDetail data){
        titleTv.setText("#" + missionId + ". "+ data.name);
        contentTv.setText(data.content);
    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(5);
    }

}

