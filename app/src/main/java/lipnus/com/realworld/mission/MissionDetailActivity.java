package lipnus.com.realworld.mission;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.quest.ChoiceActivity;
import lipnus.com.realworld.quest.MultiInputActivity;
import lipnus.com.realworld.quest.QrcodeActicity;
import lipnus.com.realworld.quest.WordActivity;
import lipnus.com.realworld.retro.ResponseBody.MissionDetail;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class MissionDetailActivity extends AppCompatActivity {


    @BindView(R.id.detail_title_tv)
    TextView titleTv;
    @BindView(R.id.detail_title_iv) ImageView titleIv;
    @BindView(R.id.detail_content_tv) TextView contentTv;
    @BindView(R.id.detail_drag_iv) ImageView dragIv;
    @BindView(R.id.detail_listview) ListView listView;

    MissionDetailListViewAdapter adapter;
    RetroClient retroClient;
    int missionId;

    YouTubePlayerView youTubePlayerView;

    public static Activity MDActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);
        ButterKnife.bind(this);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        MDActivity = MissionDetailActivity.this;

        //리스트뷰
        adapter = new MissionDetailListViewAdapter();
        listView.setAdapter(adapter);

        //리스트뷰의 클릭리스너
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MissionDetailListViewItem mDetail = (MissionDetailListViewItem)adapter.getItem(position);
                int questId = mDetail.questId;
                int questType = mDetail.type;

                if(mDetail.locked == 1){
                    Toast.makeText(getApplicationContext(), "LOCKED", Toast.LENGTH_LONG).show();
                }else{
                    Intent iT;
                    Log.d("CCDD", "type=" + questType);

                    if(questType== 5){//4지선다
                        iT = new Intent(getApplicationContext(), ChoiceActivity.class);
                    }else if(questType==2){//QR코드
                        iT = new Intent(getApplicationContext(), QrcodeActicity.class);
                    }else if(questType==6){//다중주관식
                        iT = new Intent(getApplicationContext(), MultiInputActivity.class);
                    }
                    else{
                        iT = new Intent(getApplicationContext(), WordActivity.class);
                    }

                    iT.putExtra("questId", questId);
                    startActivity(iT);
                }
            }
        });

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        missionId = iT.getExtras().getInt("missionId", 0);
        postMissionDetail(missionId);


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
        titleTv.setText(data.name);
        contentTv.setText(data.content);

        //영상처리
        if(data.video != null){

            youTubePlayerView.setVisibility(View.VISIBLE);

            String urlPath[] = data.video.split("/");
            final String youtubeId = urlPath[urlPath.length-1];

            youTubePlayerView.initialize(new YouTubePlayerInitListener() {
                @Override
                public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
                    initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            String videoId = youtubeId;
                            initializedYouTubePlayer.loadVideo(videoId, 0);
                        }
                    });
                }
            }, true);
        }

        //이미지
        Glide.with(this)
                .load(GlobalApplication.imgPath + data.image)
                .into(titleIv);
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);

        //쿼스트 버튼
        for(int i=0; i<data.quests.size(); i++){
            adapter.addItem(data.quests.get(i).id, data.quests.get(i).type, data.quests.get(i).label, data.quests.get(i).locked);
        }

        //리스트뷰 크기설정
        setListViewHeightBasedOnChildren(listView, 100);
        adapter.notifyDataSetChanged();
    }

    //리스트뷰 크기
    public void setListViewHeightBasedOnChildren(ListView listView, int paddingBottom) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int dpHeight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        int pxHeight = convertDpToPixel(dpHeight,this);

        params.height = dpHeight*2 + paddingBottom;

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public int convertDpToPixel(float dp, Context context){

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);

        return (int)px;

    }
}

