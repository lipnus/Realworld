package lipnus.com.realworld;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import lipnus.com.realworld.mission.MissionActivity;
import lipnus.com.realworld.submenu.CouponActivity;
import lipnus.com.realworld.submenu.MypageActivity;
import lipnus.com.realworld.submenu.SettingActivity;

/**
 * Created by LIPNUS on 2018-02-19.
 */

public class NavigationMenu {

    Context context;
    View view;
    int position;

    ImageView[] menuIv = new ImageView[4];
    TextView[] menuTv = new TextView[4];
    LinearLayout[] menuLr = new LinearLayout[4];


    public NavigationMenu(final Context context, View view, int position) {
        this.context = context;
        this.view = view;
        this.position = position;

        menuIv[0] = view.findViewById(R.id.navitaion_menu_iv1);
        menuIv[1] = view.findViewById(R.id.navitaion_menu_iv2);
        menuIv[2] = view.findViewById(R.id.navitaion_menu_iv3);
        menuIv[3] = view.findViewById(R.id.navitaion_menu_iv4);


        menuTv[0] = view.findViewById(R.id.navitaion_menu_tv1);
        menuTv[1] = view.findViewById(R.id.navitaion_menu_tv2);
        menuTv[2] = view.findViewById(R.id.navitaion_menu_tv3);
        menuTv[3] = view.findViewById(R.id.navitaion_menu_tv4);


        menuLr[0] = view.findViewById(R.id.navitaion_menu_lr1);
        menuLr[1] = view.findViewById(R.id.navitaion_menu_lr2);
        menuLr[2] = view.findViewById(R.id.navitaion_menu_lr3);
        menuLr[3] = view.findViewById(R.id.navitaion_menu_lr4);


        //이미지 적용
        Glide.with(context)
                .load( R.drawable.menu1 )
                .into(menuIv[0]);
        menuIv[0].setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(context)
                .load( R.drawable.menu2 )
                .into(menuIv[1]);
        menuIv[1].setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(context)
                .load( R.drawable.menu3)
                .into(menuIv[2]);
        menuIv[2].setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(context)
                .load( R.drawable.menu4)
                .into(menuIv[3]);
        menuIv[3].setScaleType(ImageView.ScaleType.FIT_XY);


        //현재위치에 따른 개별설정
        menuTv[position].setTextColor(Color.parseColor("#3f1874"));

        switch (position) {
            case 0:
                Glide.with(context)
                        .load(R.drawable.menu1_on)
                        .into(menuIv[0]);
                menuIv[0].setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            case 1:
                Glide.with(context)
                        .load(R.drawable.menu2_on)
                        .into(menuIv[1]);
                menuIv[1].setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            case 2:
                Glide.with(context)
                        .load(R.drawable.menu3_on)
                        .into(menuIv[2]);
                menuIv[2].setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            case 3:
                Glide.with(context)
                        .load(R.drawable.menu4_on)
                        .into(menuIv[3]);
                menuIv[3].setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }//switch문



        //클릭이벤트
        menuLr[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iT = new Intent(context, MissionActivity.class);
                iT.putExtra("scenarioId", MissionActivity.scenarioId);
                context.startActivity(iT);
//                iT.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MissionActivity missionActivity = (MissionActivity) MissionActivity.missionActivity;
                missionActivity.finish();
            }
        });

        menuLr[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iT = new Intent(context, CouponActivity.class);
//                iT.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(iT);

            }
        });

        menuLr[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iT = new Intent(context, MypageActivity.class);
//                iT.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(iT);
            }
        });

        menuLr[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iT = new Intent(context, SettingActivity.class);
//                iT.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(iT);
            }
        });
    }

    public void finishOtherActivity(){
        MissionActivity missionActivity = (MissionActivity) MissionActivity.missionActivity;
        missionActivity.finish();


    }


}
