package lipnus.com.realworld.user;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.main.ScenarioActivity;
import lipnus.com.realworld.retro.ResponseBody.Join;
import lipnus.com.realworld.retro.ResponseBody.TokenGet;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class JoinActivity extends AppCompatActivity {

    @BindView(R.id.join_email_et) EditText emailEt;
    @BindView(R.id.join_pw_et) EditText pwEt;
    @BindView(R.id.join_pw_confirm_et) EditText pwConfirmEt;
    @BindView(R.id.join_mobile_et) EditText mobileEt;
    @BindView(R.id.join_name_et) EditText nameEt;

    RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏
    }

    public void onClick_join_finish(View v){
        inputCheck();
    }

    public void inputCheck(){

        String resultStr = "";

        //조건충족 체크
        if(emailEt.getText().toString().equals("")){
            resultStr = "이메일을 입력해주세요";
        }else if(!emailEt.getText().toString().contains("@")){
            resultStr = "이메일 형식이 올바르지 않습니다";
        }else if(!pwEt.getText().toString().equals( pwConfirmEt.getText().toString() )) {
            resultStr = "비밀번호가 일치하지 않습니다";
        }else if(pwEt.getText().toString().length() < 6 ) {
            resultStr = "6자리 이상의 비밀번호를 입력하세요";
        }else if(mobileEt.getText().toString().equals("")){
            resultStr = "전화번호를 입력해주세요";
        }else if(mobileEt.getText().toString().length() < 10){
            resultStr = "올바른 전화번호를 입력해주세요";
        }else{
            getUuid();
        }

        if(!resultStr.equals("")) Toast.makeText(getApplicationContext(), resultStr, Toast.LENGTH_LONG).show();
    }



    //로그인
    public void postJoin(String uuid){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("client_id", GlobalApplication.clientId);
        parameters.put("mobile_id", uuid);
        parameters.put("provider", "self");
        parameters.put("login", emailEt.getText().toString());
        parameters.put("password", pwEt.getText().toString());
        parameters.put("mobile_no", mobileEt.getText().toString());
        parameters.put("name", nameEt.getText().toString());

        retroClient.postJoin(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {

                Log.e("EVEV", "onError(), " + t.toString());
                Toast.makeText(getApplicationContext(), "회원가입오류", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Join data = (Join) receivedData;
                Toast.makeText(getApplicationContext(), data.name + "가입이 완료되었습니다!", Toast.LENGTH_LONG).show();
                GlobalApplication.tempEmail = emailEt.getText().toString();
                GlobalApplication.tempPassword = pwEt.getText().toString();
                finish();
            }

            @Override
            public void onFailure(int code) {
                Log.e("EVEV", "onFailure(), " + String.valueOf(code));
                Toast.makeText(getApplicationContext(), "회원가입오류", Toast.LENGTH_LONG).show();
            }
        });
    }



    //권한 인증
    public void requirePermission(){

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                permissionOk();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permission 요청에 실패했습니다\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("Permission을 요청합니다")
                .setDeniedMessage("권한 거부")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE)
                .check();
    }

    //퍼이션 완료
    public void permissionOk(){
        //약간의 딜레이를 준다
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                getUuid();
            }
        }, 100);
    }

    //uuid 얻기
    public void getUuid(){

        //앞에서 해놓은게 있으면 그걸 쓰고 아니면 새로 구한다
        if(!GlobalApplication.uuid.equals("")){
            postJoin(GlobalApplication.uuid);
        }else{

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

            if(permissionCheck== PackageManager.PERMISSION_DENIED){
                requirePermission();

            }else{
                final TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                final String tmDevice, tmSerial, androidId;
                tmDevice = "" + tm.getDeviceId();
                tmSerial = "" + tm.getSimSerialNumber();
                androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
                String deviceId = deviceUuid.toString();

                postJoin(deviceId);
            }

        }


    }

    @Override
    public void onBackPressed() {
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


}
