package lipnus.com.realworld.submenu;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.HashMap;
import java.util.List;

import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.NavigationMenu;
import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.ResponseBody.Coupon;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class CouponActivity extends AppCompatActivity {

    GridView gridView;
    GridViewAdapter adapter;
    RetroClient retroClient;
    NavigationMenu navigationMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        //애니매이션 없음
        overridePendingTransition(0, 0);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        gridView = findViewById(R.id.gridView);
        adapter = new GridViewAdapter( getDisplayWidth() );
        gridView.setAdapter(adapter);

        //네이게이션
        View thisView = this.getWindow().getDecorView() ;
        navigationMenu = new NavigationMenu(this, thisView,1);

        initGridView();
        postCoupons();
    }

    public void initGridView(){

        //클릭리스너
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                GridViewItem item = (GridViewItem)adapter.getItem(position);

                Intent iT = new Intent(getApplicationContext(), CouponDetailActivity.class);
                iT.putExtra("couponId", item.couponId);
                startActivity(iT);

                Log.e("HHTT", "쿠폰아이디 출력: " + adapter.getCount() );
                Log.e("HHTT", "쿠폰아이디 출력: " + adapter.getItem(0) );

            }
        });
    }

    //서버에서 쿠폰을 받아옴
    public void postCoupons(){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postCoupons(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e("EVEV", "onError(), " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                List<Coupon> data = (List<Coupon>) receivedData;
                setCoupons(data);

//                Log.e("sibal", data.imageUrl);
            }

            @Override
            public void onFailure(int code) {
                Log.e("banner", "onFailure(), " + String.valueOf(code));
            }
        });
    }

    //받은 데이터를 이용하여 쿠폰을 그리드뷰에 뿌림
    public void setCoupons(List<Coupon> coupons){


        for(int i=0; i<coupons.size(); i++){
            adapter.addItem(coupons.get(i).couponId, coupons.get(i).title, coupons.get(i).description, GlobalApplication.imgPath+coupons.get(i).image);
            Log.e("HHTT", "쿠폰아이디 입력: " + coupons.get(i).couponId );
        }
        adapter.notifyDataSetChanged();

    }



    //화면의 가로크기를 반환한다
    public int getDisplayWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }


}
