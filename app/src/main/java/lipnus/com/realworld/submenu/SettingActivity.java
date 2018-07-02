package lipnus.com.realworld.submenu;


import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.NavigationMenu;
import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.ResponseBody.Coupon;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class SettingActivity extends AppCompatActivity {

    RetroClient retroClient;
    NavigationMenu navigationMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        //애니매이션 없음
        overridePendingTransition(0, 0);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏


        //네이게이션
        View thisView = this.getWindow().getDecorView() ;
        navigationMenu = new NavigationMenu(this, thisView,3);


    }

    public void onClick_setting_term(View v){

        String url ="https://doctorhamel.com/terms_of_service.pdf";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);



    }

    public void onClick_setting_personal(View v){

        String url ="https://doctorhamel.com/privacy.pdf";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

    }

    public void onClick_setting_as(View v){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("plain/text");
        // email setting 배열로 해놔서 복수 발송 가능
        String[] address = {"ehelp@doctorhamel.com"};
        email.putExtra(Intent.EXTRA_EMAIL, address);
        email.putExtra(Intent.EXTRA_SUBJECT,"미스테리트레일 불편신고");
        email.putExtra(Intent.EXTRA_TEXT,"");
        startActivity(email);
    }





}
