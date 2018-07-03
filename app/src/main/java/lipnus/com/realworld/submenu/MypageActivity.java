package lipnus.com.realworld.submenu;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.NavigationMenu;
import lipnus.com.realworld.R;
import lipnus.com.realworld.mission.MissionListViewAdapter;
import lipnus.com.realworld.retro.ResponseBody.Coupon;
import lipnus.com.realworld.retro.ResponseBody.Scenario;
import lipnus.com.realworld.retro.ResponseBody.UserInfo;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class MypageActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT = 2;

    @BindView(R.id.mypage_profile_iv)
    ImageView profileIv;

    @BindView(R.id.mypage_name_tv)
    TextView nameTv;

    @BindView(R.id.mypage_listview)
    ListView listView;

    @BindView(R.id.mypage_scrollview)
    ScrollView scrollView;

    Mypage_ListViewAdapter adapter;
    NavigationMenu navigationMenu;
    RetroClient retroClient;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        ButterKnife.bind(this);

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //애니매이션 없음
        overridePendingTransition(0, 0);

        View thisView = this.getWindow().getDecorView() ;
        navigationMenu = new NavigationMenu(this, thisView,2);
        imageSetting();

        //리스트뷰
        adapter = new Mypage_ListViewAdapter();
        listView.setAdapter(adapter);

        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,0);
            }
        },200);

        postScenario();
        postUserInfo();

    }

    public void imageSetting(){
        //프사

        String profileImg = setting.getString("profileImg", "0");
        if(profileImg.equals("0")){
            Glide.with(this)
                    .load( R.drawable.whoareyou )
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(profileIv);
                    profileIv.setScaleType(ImageView.ScaleType.FIT_XY);
        }else{
            Glide.with(this)
                    .load( profileImg )
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(profileIv);
            profileIv.setScaleType(ImageView.ScaleType.FIT_XY);
        }

    }

    public void postUserInfo(){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postUserInfo(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e("sss", "onError(), " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                UserInfo data = (UserInfo) receivedData;
                setUserInfo(data);
            }

            @Override
            public void onFailure(int code) {
                Log.e("sss", "onFailure(), " + String.valueOf(code));
            }
        });
    }

    public void setUserInfo(UserInfo userInfo){
        if(userInfo.name.equals("")){
            nameTv.setText("이름미설정");
        }else{
            nameTv.setText(userInfo.name);
        }

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
                setScoreList(data);

            }

            @Override
            public void onFailure(int code) {
                Log.e("EVEV", "onFailure(), " + String.valueOf(code));
            }
        });
    }

    public void setScoreList(List<Scenario> scenarios){

        for(int i=0; i<scenarios.size(); i++){
            adapter.addItem(scenarios.get(i).name, scenarios.get(i).score, scenarios.get(i).maxScore);
        }
        adapter.notifyDataSetChanged();

    }

    public void onClick_mypage_profile(View v){

        Intent iT = new Intent(Intent.ACTION_PICK);
        iT.setType("image/*");
        startActivityForResult(iT, GALLERY_INTENT);
    }

    //사진첩 호출의 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            try {

                //프레퍼런스
                editor.putString("profileImg", data.getData().toString());
                editor.commit();

                //프사
                Glide.with(this)
                        .load( data.getData() )
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(profileIv);
                profileIv.setScaleType(ImageView.ScaleType.FIT_XY);

                Log.e("HHTT", "경로인가: " + data.getData() );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//if

    }

}
