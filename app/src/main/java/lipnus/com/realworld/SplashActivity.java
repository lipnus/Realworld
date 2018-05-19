package lipnus.com.realworld;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lipnus.com.realworld.main.Main2Activity;
import lipnus.com.realworld.retro.ResponseBody.TokenGet;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class SplashActivity extends AppCompatActivity {

    String mobileNumber; //원래는 전화번호를 썼으나 이제 안씀
    String deviceUuidStr=""; //시작 전 처리
    RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        getUuid();

        //약간의 딜레이를 준다
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                if(!deviceUuidStr.equals("")){
                    postAuthorize(deviceUuidStr);
                }else{
                    getUuid();
                }
            }
        }, 2000);

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
                Toast.makeText(getApplicationContext(), "Permission 실패\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("Permission 요청")
                .setDeniedMessage("권한 거부")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
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
        }, 1000);
    }

    //토큰받기
    public void postAuthorize(String mobileNumber){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("client_id", "l7xx119653b3a36b4dc4be4206419bea131d");
        parameters.put("mobile_no", mobileNumber);

        retroClient.postAuthorize(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {

                Log.e("EVEV", "onError(), " + t.toString());
                Toast.makeText(getApplicationContext(), "서버접속실패", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                TokenGet data = (TokenGet) receivedData;
                GlobalApplication.access_tocken = data.access_token;

                Intent iT = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(iT);
                finish();
            }

            @Override
            public void onFailure(int code) {
                Log.e("EVEV", "onFailure(), " + String.valueOf(code));
                Toast.makeText(getApplicationContext(), "서버접속실패", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    //uuid 얻기
    public void getUuid(){

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

            //서버로 전송
            deviceUuidStr = deviceUuid.toString();
            Log.e("DDEE", "디바이스uuid: " + deviceUuidStr);

        }
    }






    public void onClick_splash(View v){
        if(!deviceUuidStr.equals("")){
            postAuthorize(deviceUuidStr);
        }else{
            getUuid();
        }
    }

    //전화번호얻기
    public String getMoible_no(){

        String phoneNum="0";

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            requirePermission();
        }else{
            TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            phoneNum = telManager.getLine1Number();

            if(phoneNum.startsWith("+82")){
                phoneNum = phoneNum.replace("+82", "0");
            }
        }

        Log.e("PPNN", "전화번호: " + phoneNum);
        return phoneNum;
    }

}
