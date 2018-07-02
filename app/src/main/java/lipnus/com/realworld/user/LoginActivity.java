package lipnus.com.realworld.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.main.ScenarioActivity;
import lipnus.com.realworld.retro.ResponseBody.TokenGet;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_email_et) EditText emailEt;
    @BindView(R.id.login_password_et) EditText pwEt;

    RetroClient retroClient;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();
    }



    public void onClick_login(View v){

        if(inputCheck()){ postLogin(emailEt.getText().toString(), pwEt.getText().toString()); }
    }


    public void onClick_join(View v){
        Intent iT = new Intent(getApplicationContext(), JoinActivity.class);
        startActivity(iT);
    }



    //올바르게 입력했는지 체크
    public boolean inputCheck(){

        if(emailEt.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
            return false;
        }else if(pwEt.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(!GlobalApplication.tempEmail.equals("")){
            emailEt.setText( GlobalApplication.tempEmail );
        }

        if(!GlobalApplication.tempPassword.equals("")){
            pwEt.setText( GlobalApplication.tempPassword );
        }
    }

    //로그인
    public void postLogin(String id, String pw){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("client_id", "l7xx119653b3a36b4dc4be4206419bea131d");
        parameters.put("id", id);
        parameters.put("password", pw);
        parameters.put("provider", "self");

        retroClient.postLogin(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {

                Log.e("EVEV", "onError(), " + t.toString());
                Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                TokenGet data = (TokenGet) receivedData;
                GlobalApplication.access_tocken = data.access_token;

                saveToken(data.access_token);

                Toast.makeText(getApplicationContext(), "미스테리트레일에 오신 것을 환영합니다", Toast.LENGTH_LONG).show();

                Intent iT = new Intent(getApplicationContext(), ScenarioActivity.class);
                startActivity(iT);
                finish();
            }

            @Override
            public void onFailure(int code) {
                Log.e("EVEV", "onFailure(), " + String.valueOf(code));
                Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 올바르지 않습니다", Toast.LENGTH_LONG).show();
            }
        });
    }

    //받은 토큰을 프레퍼런스에 저장
    public void saveToken(String token){

        Log.e("HHTT", "토큰 프레퍼런스에 저장");
        //프레퍼런스
        editor.putString("access_token", token);
        editor.commit();

        GlobalApplication.access_tocken = token;

    }
}
