package lipnus.com.realworld.submenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import lipnus.com.realworld.main.SynopsisActivity;
import lipnus.com.realworld.mission.MissionActivity;
import lipnus.com.realworld.mission.MissionDetailActivity;
import lipnus.com.realworld.mission.MissionListViewAdapter;
import lipnus.com.realworld.mission.MissionListViewItem;
import lipnus.com.realworld.retro.ResponseBody.Inventory;
import lipnus.com.realworld.retro.ResponseBody.Mission;
import lipnus.com.realworld.retro.ResponseBody.ScenarioDetail;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class InventoryActivity extends AppCompatActivity {

    @BindView(R.id.inventory_title_iv)
    ImageView titleIv;
    @BindView(R.id.inventory_back_iv) ImageView backIv;
    @BindView(R.id.inventory_subtitle_tv)
    TextView subtitleTv;
    @BindView(R.id.inventory_listview)
    ListView listView;
    @BindView(R.id.inventory_scrollView)
    ScrollView scrollView;

    InventoryListViewAdapter adapter;
    RetroClient retroClient;

    public static Activity inventoryActivity;
    NavigationMenu navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏
        inventoryActivity = InventoryActivity.this;

        //애니매이션 없음
        overridePendingTransition(0, 0);

        //리스트뷰
        adapter = new InventoryListViewAdapter();
        listView.setAdapter(adapter);

         //네이게이션
        View thisView = this.getWindow().getDecorView() ;
        navigationMenu = new NavigationMenu(this, thisView,1);

        //스크롤뷰 조정
        scrollViewControl();

        postInventory();

        //백버튼
        Glide.with(this)
                .load(R.drawable.back)
                .into(backIv);
        backIv.setScaleType(ImageView.ScaleType.FIT_XY);

        //상단이미지
        Glide.with(this)
                .load(GlobalApplication.missionImgPath)
                .centerCrop()
                .into(titleIv);
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);


    }



    public void onClick_inventory_back(View v){
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


    public void postInventory(){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postInventory(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                List<Inventory> data = (List<Inventory>) receivedData;
                setInventory(data);

                Log.e("DDDD", "data: " + data.get(0).name);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setInventory(List<Inventory> data){

        //리스트에 삽입
        for(int i=0; i<data.size(); i++){
            Inventory inventory = data.get(i);
            adapter.addItem(inventory.id, inventory.name, inventory.iconUrl, inventory.obtainedAt);
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
//
//
//        //리스트뷰의 클릭리스너
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                MissionListViewItem missionItem = (MissionListViewItem)parent.getAdapter().getItem(position);
//
//                if( missionItem.looked==false ){ // 진행중, 해결한 미션
//                    Intent iT = new Intent(getApplicationContext(), MissionDetailActivity.class);
//                    iT.putExtra("missionId", missionItem.id);
//                    startActivity(iT);
//                }
//                else if(missionItem.looked==true){ //잠겨있는 미션
//                    Toast.makeText(getApplicationContext(), "LOCKED", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
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