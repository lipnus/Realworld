package lipnus.com.realworld.main;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        tabview.setupWithViewPager(mainPager); //탭뷰

        pagerSetting();
        addData();
    }

    //뷰페이서 재설정
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

    //받은 데이터들을 표현
    public void addData(){

        Glide.with(this)
                .load("http://lipnus.ivyro.net/psh.jpg")
                 .centerCrop()
                .into(titleIv);
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);

    }
}
