package lipnus.com.realworld.mission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.LocationControl;
import lipnus.com.realworld.NavigationMenu;
import lipnus.com.realworld.R;
import lipnus.com.realworld.main.ScenarioActivity;
import lipnus.com.realworld.main.SynopsisActivity;
import lipnus.com.realworld.retro.ResponseBody.Mission;
import lipnus.com.realworld.retro.ResponseBody.ScenarioDetail;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;
import lipnus.com.realworld.submenu.MypageActivity;

public class MissionActivity extends AppCompatActivity {



    @BindView(R.id.mission_title_iv) ImageView titleIv;
    @BindView(R.id.mission_back_iv) ImageView backIv;
    @BindView(R.id.mission_subtitle_tv) TextView subtitleTv;
    @BindView(R.id.mission_pic_title_iv) TextView picTitleTv;

    @BindView(R.id.mission_listview) ListView listView;
    @BindView(R.id.mission_scrollView) ScrollView scrollView;
    MissionListViewAdapter adapter;
    RetroClient retroClient;

    public static int scenarioId; //네비게이션에서 재사용된다

//    ScenarioDetail sDetail; //클릭시 미션 아이디를 찾기 위해서

    public static Activity missionActivity;
    NavigationMenu navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏
        missionActivity = MissionActivity.this;

//        checkGPS();

        //애니매이션 없음
        overridePendingTransition(R.anim.fadein, 0);

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
                .centerCrop()
                .into(backIv);
//        backIv.setScaleType(ImageView.ScaleType.CENTER_CROP);


    }



    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.e("GGGG", "리스트 재실행");
        adapter.cleaarItem();
        postScenarioDetail(scenarioId);
    }

    public void onClick_mission_back(View v){
        Intent iT = new Intent(this, ScenarioActivity.class);
        iT.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iT);
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

//                sDetail = data; //다음 리스트뷰로 가는 아이디 때문에..
                setSenario(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSenario(ScenarioDetail data){

        //시작지점인지 알려줌
        alertStartLocation(data.beginLoc);



        //미션이름 표시
        picTitleTv.setText(data.name);
        subtitleTv.setText( "미션목록");

        //미션이미지를 저장해둔다(인벤토리에서 써야해서..)
        GlobalApplication.missionImgPath = GlobalApplication.imgPath + data.coverImageUrl;

        //상단이미지
        Glide.with(this)
                .load(GlobalApplication.imgPath + data.coverImageUrl)
                .into(titleIv);
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);


        //리스트에 삽입
        for(int i=0; i<data.missions.size(); i++){
            Mission mission = data.missions.get(i);
            adapter.addItem(mission.id, mission.locked, mission.name, mission.succeededAt);
        }



        //리스트뷰 크기설정
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

                MissionListViewItem missionItem = (MissionListViewItem)parent.getAdapter().getItem(position);

                if( missionItem.looked==false ){ // 진행중, 해결한 미션
                    Intent iT = new Intent(getApplicationContext(), MissionDetailActivity.class);
                    iT.putExtra("missionId", missionItem.id);
                    startActivity(iT);
                }
                else if(missionItem.looked==true){ //잠겨있는 미션
                    Toast.makeText(getApplicationContext(), "LOCKED", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onClick_mission_synopsis(View v){
        Intent iT = new Intent(this, SynopsisActivity.class);
        iT.putExtra("scenarioId", scenarioId);
        startActivity(iT);
        finish();
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


    //시작지점인지 알려줌
    public void alertStartLocation(String beginLoc){

        if(GlobalApplication.user_latitude != 0){
            Log.e("HHTT", "위치는: "+ GlobalApplication.user_latitude);
        }

    }

    //gps가 켜져있는지 확인하고 현재위치를 수집
    public boolean checkGPS() {

        //현재위치를 구하는데 필요한 지오코더
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPS) {
            Log.e("GPGP", "gps 켜져있음");

            //위치정보 업데이트
            LocationControl gl = new LocationControl( getApplicationContext() );
            return true;

        } else {
            Toast.makeText(getApplication(), "GPS를 켜주세요!", Toast.LENGTH_LONG).show();
            Log.e("GPGP", "gps꺼짐");

            //위치정보 설정창 띄우기
            //여기서는 강요는 하지 않음
//            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
        return false;
    }



    public int convertDpToPixel(float dp, Context context){

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);

        return (int)px;

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent iT = new Intent(this, ScenarioActivity.class);
        iT.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iT);
    }
}
