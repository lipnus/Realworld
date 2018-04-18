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

/**
 * Created by LIPNUS on 2018-02-19.
 */

public class NavigationMenu {

    Context context;
    View view;
    int position;


    ImageView[] menuIv = new ImageView[3];

    TextView[] menuTv = new TextView[3];

    LinearLayout[] menuLr = new LinearLayout[3];

    public NavigationMenu(final Context context, View view, int position) {
        this.context = context;
        this.view = view;
        this.position = position;

        menuIv[0] = view.findViewById(R.id.navitaion_menu_iv1);
        menuIv[1] = view.findViewById(R.id.navitaion_menu_iv2);
        menuIv[2] = view.findViewById(R.id.navitaion_menu_iv3);

        menuTv[0] = view.findViewById(R.id.navitaion_menu_tv1);
        menuTv[1] = view.findViewById(R.id.navitaion_menu_tv2);
        menuTv[2] = view.findViewById(R.id.navitaion_menu_tv3);

        menuLr[0] = view.findViewById(R.id.navitaion_menu_lr1);
        menuLr[1] = view.findViewById(R.id.navitaion_menu_lr2);
        menuLr[2] = view.findViewById(R.id.navitaion_menu_lr3);

        //이미지 적용
        Glide.with(context)
                .load( R.drawable.navi_1_off )
                .into(menuIv[0]);
        menuIv[0].setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(context)
                .load( R.drawable.navi_2_off )
                .into(menuIv[1]);
        menuIv[1].setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(context)
                .load( R.drawable.navi_3_off )
                .into(menuIv[2]);
        menuIv[2].setScaleType(ImageView.ScaleType.FIT_XY);


        //현재위치에 따른 개별설정
        menuTv[position].setTextColor(Color.parseColor("#2cc1e0"));

        switch (position) {
            case 0:
                Glide.with(context)
                        .load(R.drawable.navi_1_on)
                        .into(menuIv[0]);
                menuIv[0].setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            case 1:
                break;

            case 2:
                break;
        }//switch문



        //클릭이벤트
        menuLr[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iT = new Intent(context, MissionActivity.class);
                iT.putExtra("scenarioId", MissionActivity.scenarioId);
                context.startActivity(iT);
                finishOtherActivity();
            }
        });

        menuLr[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "준비중입니다", Toast.LENGTH_LONG).show();

            }
        });

        menuLr[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "준비중입니다", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void finishOtherActivity(){
        MissionActivity missionActivity = (MissionActivity) MissionActivity.missionActivity;
        missionActivity.finish();
    }


}
