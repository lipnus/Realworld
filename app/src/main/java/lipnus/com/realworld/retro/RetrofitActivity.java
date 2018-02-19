package lipnus.com.realworld.retro;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.ResponseBody.ResponseGet2;
import lipnus.com.realworld.retro.ResponseBody.Scenario;
import lipnus.com.realworld.retro.ResponseBody.TokenGet;

public class RetrofitActivity extends AppCompatActivity {

    TextView resonseTv;

    String codeResult;
    String resultStr;


    String LOG = "SSSS";
    RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        resonseTv = (TextView) findViewById(R.id.connect_response_tv);
        retroClient = RetroClient.getInstance(this).createBaseApi();
    }


    public void onClick_post(View v){
        Toast.makeText(this, "POST Clicked", Toast.LENGTH_SHORT).show();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("input1", "aaa");
        parameters.put("input2", "bbb");

        retroClient.postFirst(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, t.toString());

                codeResult = "Error";
                resultStr ="Error";


                setTextView();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ResponseGet2 data = (ResponseGet2) receivedData;

                codeResult = String.valueOf(code);
                resultStr = data.in1;;


                setTextView();
            }

            @Override
            public void onFailure(int code) {

                codeResult = String.valueOf(code);
                resultStr ="Failure";

                setTextView();
            }
        });
    }

    public void onClick_post_tocken(View v){
        Toast.makeText(this, "POST Clicked", Toast.LENGTH_SHORT).show();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("client_id", "l7xx119653b3a36b4dc4be4206419bea131d");
        parameters.put("mobile_no", "01027151024");

        retroClient.postAuthorize(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, t.toString());

                codeResult = "Error";
                resultStr ="Error";

                setTextView();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                TokenGet data = (TokenGet) receivedData;

                codeResult = String.valueOf(code);
                resultStr = "access_token: " + data.access_token;
                Log.d("RRWW", data.access_token);
                setTextView();
            }

            @Override
            public void onFailure(int code) {

                codeResult = String.valueOf(code);
                resultStr ="Failure";
                setTextView();
            }
        });
    }

    public void onClick_post_scenario(View v){
        Toast.makeText(this, "POST Clicked", Toast.LENGTH_SHORT).show();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", "dd4076af-0e79-11e8-b608-0cc47a9ce276");

        retroClient.postScenarios(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, t.toString());

                codeResult = "Error";
                resultStr ="Error";
                setTextView();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                List<Scenario> data = (List<Scenario>) receivedData;

                codeResult = String.valueOf(code);
                resultStr = data.get(0).id;

                setTextView();
            }

            @Override
            public void onFailure(int code) {

                codeResult = String.valueOf(code);
                resultStr ="Failure";


                setTextView();
            }
        });
    }


    public void setTextView(){

        String resultText = "codeResult: " + codeResult  + "\n" + resultStr;
        resonseTv.setText( resultText );
    }
}
