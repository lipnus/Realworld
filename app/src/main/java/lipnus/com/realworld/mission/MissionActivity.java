package lipnus.com.realworld.mission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.NavigationMenu;
import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.ResponseBody.Mission;
import lipnus.com.realworld.retro.ResponseBody.ScenarioDetail;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class MissionActivity extends AppCompatActivity {



    @BindView(R.id.mission_title_iv) ImageView titleIv;
    @BindView(R.id.mission_back_iv) ImageView backIv;
    @BindView(R.id.mission_title_tv) TextView titleTv;
    @BindView(R.id.mission_subtitle_tv) TextView subtitleTv;
    @BindView(R.id.mission_listview) ListView listView;
    @BindView(R.id.mission_scrollView) ScrollView scrollView;
    MissionListViewAdapter adapter;
    RetroClient retroClient;

    public static int scenarioId; //네비게이션에서 재사용된다

    ScenarioDetail sDetail; //클릭시 미션 아이디를 찾기 위해서

    public static Activity missionActivity;
    NavigationMenu navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏
        missionActivity = MissionActivity.this;


        //리스트뷰
        adapter = new MissionListViewAdapter();
        listView.setAdapter(adapter);

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        scenarioId = iT.getExtras().getInt("scenarioId");

        //네이게이션
        View thisView = this.getWindow().getDecorView() ;
        navigationMenu = new NavigationMenu(this, thisView,0);

        //스크롤뷰 조정
        scrollViewControl();

        //백버튼
        Glide.with(this)
                .load(R.drawable.back)
                .into(backIv);
        backIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        adapter.cleaarItem();
        postScenarioDetail(scenarioId);
    }

    public void onClick_mission_back(View v){
        finish();
    }

    public void scrollViewControl(){

        //onScrollChangedListener은 SDK 23이상에서만 동작한다 23버전 이하에서는 꿩대신 닭으로 onTouchListener를 사용
        if(Build.VERSION.SDK_INT >=23 ){
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    scrollChanged();
                }
            });
        }else { //버전이 낮은경우
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                private ViewTreeObserver observer;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scrollChanged();
                    return false;
                }
            });
        }
    }

    public void scrollChanged(){

        try{
            float scrollY = scrollView.getScrollY();

            //최상단부 메뉴 그림의 스크롤에 따른 변환효과
                titleIv.setScaleX(1 + (scrollY / 1000));
                titleIv.setScaleY(1 + (scrollY / 1000));

            //뒤로가기 화살표 스르륵
            if(scrollY<300){
                backIv.setAlpha( (300-scrollY)/300);
            }


        }catch(Exception e){Log.d("DDD", "오류: " + e);}
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

                sDetail = data; //다음 리스트뷰로 가는 아이디 때문에..
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



        setListViewHeightBasedOnChildren(listView, 100);
        adapter.notifyDataSetChanged();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,0);
            }
        });


        //리스트뷰의 클릭리스너
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("CCC", "MissionAcitivty 미션id: " + sDetail.missions.get(position).id);

                MissionListViewItem mission = (MissionListViewItem)parent.getAdapter().getItem(position);
                int missionId = sDetail.missions.get(position).id;

                if(mission.state.equals("open") || mission.state.equals("now")){
                    Intent iT = new Intent(getApplicationContext(), MissionDetailActivity.class);
                    iT.putExtra("missionId", missionId);
                    startActivity(iT);
                }else{
                    Toast.makeText(getApplicationContext(), "LOCKED", Toast.LENGTH_LONG).show();
                }


            }
        });
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

                String missionName = "#" + mission.id + ". " + mission.name;
                adapter.addItem(missionName, mission.succeededAt, "now");
                break;

            }else{
                String missionName = mission.name;
                adapter.addItem(missionName, mission.succeededAt, "open");
            }
        }

        //아직 클리어 하지 않은 부분들 처리
        for(int i=nowIndex+1; i<listSize; i++){
            Mission mission = missionList.get(i);
            mission.name = "#" + mission.id + ". " + "LOCKED";

            adapter.addItem(mission.name, mission.succeededAt, "locked");
        }
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
