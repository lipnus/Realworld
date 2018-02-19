package lipnus.com.realworld.volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import lipnus.com.realworld.R;

public class VolleyActivity extends AppCompatActivity {

    TextView volleyTv;

    //volley
    IVolleyResult mResultCallback = null;
    VolleyConnect volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        volleyTv = (TextView) findViewById(R.id.volley_tv);

        volleyCallback();
    }


    public void onClick_volleyPost(View v){
        volleyConnect();
    }

    public void onClick_volleyPost2(View v){
        volleyConnect2();
    }

    public void onClick_volleyPost3(View v){
        volleyConnect3();
    }


    public void volleyConnect(){
        Log.d("VOVO", "Volley Connect");
        String url = "http://210.180.118.59:8061/authorize";

        Map<String, String> params = new HashMap<>();
        params.put("client_id", "l7xx119653b3a36b4dc4be4206419bea131d");
        params.put("mobile_no", "01027151024");


        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }

    public void volleyConnect2(){
        Log.d("VOVO", "Volley Connect");
        String url = "https://jsonplaceholder.typicode.com/posts";

        Map<String, String> params = new HashMap<>();
        params.put("title", "foo");

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }

    public void volleyConnect3(){
        Log.d("VOVO", "Volley Connect");
        String url = "http://ec2-13-125-164-178.ap-northeast-2.compute.amazonaws.com:9000/test";

        Map<String, String> params = new HashMap<>();
        params.put("input1", "AAA");
        params.put("input2", "BBB");

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }





    void volleyCallback(){
        mResultCallback = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {

                Log.d("VOVO", "Response: " + response);
            }

            @Override
            public void notifyError(VolleyError error) {
//              reConnectDialog();
                Log.d("VOVO", "Volley Error: " + error );

                error.printStackTrace();
            }
        };
    }
}
