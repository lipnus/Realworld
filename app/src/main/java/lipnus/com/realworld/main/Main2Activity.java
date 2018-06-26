package lipnus.com.realworld.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.mission.MissionActivity;
import lipnus.com.realworld.retro.ResponseBody.Banners;
import lipnus.com.realworld.retro.ResponseBody.Scenario;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.main2_title_iv) ImageView titleIv;
    @BindView(R.id.main2_listview) ListView listView;
    ListViewAdapter adapter;

    @BindView(R.id.main2_banner_iv) ImageView bannerIv;
    RetroClient retroClient;

    String bannerLink = "https://www.mystery-trail.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        initSetting();//기본세팅들은 다 여기 때려박음
        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        postScenario();
        postBanners();
    }

    public void initSetting(){

        Glide.with(this)
                .load(R.drawable.title)
                .into(titleIv);
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);

        adapter = new ListViewAdapter();
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setMessage("종료하시겠어요?");

        builder.setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //무반응
                    }
                });
        builder.show();
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
                GlobalApplication.scenarioList = data;
                scenarioSetting(data);
            }

            @Override
            public void onFailure(int code) {
                Log.e("EVEV", "onFailure(), " + String.valueOf(code));
            }
        });
    }

    public void scenarioSetting(List<Scenario> data){

         for(int i=0; i < data.size(); i++){
            Scenario scenario = data.get(i);
            adapter.addItem(Integer.parseInt(scenario.id), scenario.name, scenario.lastPlayed, scenario.accomplished, scenario.lastPlayed);
        }

        adapter.notifyDataSetChanged();


        //리스트뷰의 클릭리스너
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem scenario = (ListViewItem)parent.getAdapter().getItem(position);

                Log.e("CCC", "위치: " + position + " / 내용: " + scenario.date );

                //처음인 경우만 시놉시스를 보여준다
                Intent iT;
                if(scenario.lastPlayed==null){
                    iT = new Intent(getApplicationContext(), SynopsisActivity.class);
                }else{
                    iT = new Intent(getApplicationContext(), MissionActivity.class);
                }

                GlobalApplication.nowMission = scenario.id;
                iT.putExtra("scenarioId", scenario.id);
                startActivity(iT);
            }
        });
    }


    //서버에서 배너를 받아옴
    public void postBanners(){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);
        parameters.put("position", 0);

        retroClient.postBanners(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e("EVEV", "onError(), " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                List<Banners> data = (List<Banners>) receivedData;
                setBanners(data);

//                Log.e("sibal", data.imageUrl);
            }

            @Override
            public void onFailure(int code) {
                Log.e("banner", "onFailure(), " + String.valueOf(code));
            }
        });
    }

    public void setBanners(List<Banners> banners){

        bannerLink = banners.get(0).targetUrl;

        Glide.with(this)
                .load(GlobalApplication.imgPath + banners.get(0).imageUrl)
                .into(bannerIv);
        bannerIv.setScaleType(ImageView.ScaleType.FIT_XY);

    }


    public void onClick_banner(View v){
        String url = bannerLink;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);


    }
}
