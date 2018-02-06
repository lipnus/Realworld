package lipnus.com.realworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import lipnus.com.realworld.direction.CompassActivity;
import lipnus.com.realworld.main.MainActivity;
import lipnus.com.realworld.qrcode.QrcodeActicity;
import lipnus.com.realworld.quest.QuestActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
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
}
