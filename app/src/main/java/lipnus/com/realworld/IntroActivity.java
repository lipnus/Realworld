package lipnus.com.realworld;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import lipnus.com.realworld.direction.CompassActivity;
import lipnus.com.realworld.main.MainActivity;
import lipnus.com.realworld.mission.MissionActivity;
import lipnus.com.realworld.qrcode.QrcodeActicity;
import lipnus.com.realworld.quest.QuestActivity;
import lipnus.com.realworld.retro.RetrofitActivity;
import lipnus.com.realworld.volley.VolleyActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("위치권한, 카메라권한")
                .setDeniedMessage("권한 거부")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

    }

    public void onClick_direction(View v){
        Intent iT = new Intent(this, CompassActivity.class);
        startActivity(iT);
    }

    public void onClick_qrcode(View v){
        Intent iT = new Intent(this, QrcodeActicity.class);
        startActivity(iT);
    }

    public void onClick_quest(View v){
        Intent iT = new Intent(this, QuestActivity.class);
        startActivity(iT);
    }

    public void onClick_main(View v){
        Intent iT = new Intent(this, MainActivity.class);
        startActivity(iT);
    }

    public void onClick_retrofit(View v){
        Intent iT = new Intent(this, RetrofitActivity.class);
        startActivity(iT);
    }

    public void onClick_volley(View v){
        Intent iT = new Intent(this, VolleyActivity.class);
        startActivity(iT);
    }

    public void onClick_mission(View v){
        Intent iT = new Intent(this, MissionActivity.class);
        startActivity(iT);
    }
}
