package lipnus.com.realworld.main;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.ResponseBody.Scenario;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class MainActivity extends AppCompatActivity {


    //뷰페이저
    @BindView(R.id.main_pager)
    ViewPager mainPager;

    ViewPager_Adapter mainPagerAdapter;

    @BindView(R.id.main_title_iv)
    ImageView titleIv;

    @BindView(R.id.tabview)
    TabLayout tabview;

    int pagerPosition; //(0,1,2)

    RetroClient retroClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        tabview.setupWithViewPager(mainPager); //탭뷰
        tabview.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabview,12,12);
            }
        });

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏
        initSetting();
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        postScenario();
    }


    public void onClick_Scenario(View v){
        postScenario();
    }


    public void initSetting(){

        Glide.with(this)
                .load(R.drawable.maintitle)
                 .centerCrop()
                .into(titleIv);
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    //서버에서 시나리오를 받아옴
    public void postScenario(){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postScenarios(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e("EVEV", "onError(), " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                List<Scenario> data = (List<Scenario>) receivedData;

                Log.e("EVEV", "onSuccess(), " + String.valueOf(code));
//                scenarioArray(data);
                GlobalApplication.scenarioList = data;
                pagerSetting();

            }

            @Override
            public void onFailure(int code) {
                Log.e("EVEV", "onFailure(), " + String.valueOf(code));
            }
        });
    }



    //뷰페이저 세팅
    public void pagerSetting(){
        mainPagerAdapter = new ViewPager_Adapter( getSupportFragmentManager() );
        mainPager.setAdapter(mainPagerAdapter);

        //페이지
        mainPager.setCurrentItem(pagerPosition);

        //뷰페이저에 변화가 있을때의 리스너
        mainPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //뷰페이저가 넘어갈때마다 pagerPosition에다가 뷰페이저의 현재위치를 저장
                pagerPosition = position;
                Log.d("SSCC", "페이지번호: "+ pagerPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }






    //받은 시나리오 데이터들을 정렬(call by reference)
    public void scenarioArray(List<Scenario> scenarioList){

        int listSize = scenarioList.size();
        int nowIndex= listSize;

        Log.e("EVEV", "사이즈: " + listSize);


        //어디까지 클리어했는지 확인
        for(int i=0; i<listSize; i++){
            Scenario scenario = scenarioList.get(i);

            if(scenario.lastPlayed == null){
                nowIndex = i;
                scenario.lastPlayed="진행중";
                break;
            }
        }

        //아직 클리어 하지 않은 부분
        for(int i=nowIndex+1; i<listSize; i++){
            Scenario scenario = scenarioList.get(i);
            scenario.name = "LOCKED";
        }

    }


    //탭뷰 인디케이터 크기 줄이기
    public void setIndicator (TabLayout tabs,int leftDip,int rightDip){
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
