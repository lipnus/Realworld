package lipnus.com.realworld.submenu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.NavigationMenu;
import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.ResponseBody.Coupon;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class CouponDetailActivity extends AppCompatActivity {


    @BindView(R.id.coupon_description_tv)
    TextView descriptionTv;

    @BindView(R.id.coupon_title_iv)
    ImageView titleIv;

    @BindView(R.id.coupon_qr_iv)
    ImageView qrIv;

    @BindView(R.id.coupon_title_tv)
    TextView titleTv;

    NavigationMenu navigationMenu;
    RetroClient retroClient;
    int couponId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        ButterKnife.bind(this);

        //애니매이션 없음
        overridePendingTransition(0, 0);
        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        couponId = iT.getExtras().getInt("couponId", 0);

        //네이게이션
        View thisView = this.getWindow().getDecorView() ;
        navigationMenu = new NavigationMenu(this, thisView,1);
        postCoupons();
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

    //받은 전체쿠폰 중 선택한 쿠폰데이터를 찾아서 화면에 뿌려줌
    public void setCoupons(List<Coupon> coupons) {

        //선택한 쿠폰을 찾는다
        for (int i = 0; i < coupons.size(); i++) {
            if (coupons.get(i).couponId == couponId) {

                titleTv.setText( coupons.get(i).title );
                descriptionTv.setText( coupons.get(i).description);

                Glide.with(this)
                        .load( GlobalApplication.imgPath + coupons.get(i).image)
                        .into(titleIv);
                titleIv.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(this)
                        .load( GlobalApplication.imgPath + coupons.get(i).qrcode )
                        .into(qrIv);
                qrIv.setScaleType(ImageView.ScaleType.FIT_XY);

                break;
            }
        }//for
    }

}
