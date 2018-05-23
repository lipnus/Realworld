package lipnus.com.realworld.quest;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.ResponseBody.QuestDetail;
import lipnus.com.realworld.retro.ResponseBody.QuestResult;
import lipnus.com.realworld.retro.RetroCallback;
import lipnus.com.realworld.retro.RetroClient;

public class QrcodeActicity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

//    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;

    @BindView(R.id.qrcode_resultTv)
    TextView resultTextView;

    @BindView(R.id.drag_iv)
    ImageView dragIv;

    @BindView(R.id.qr_overlap_iv)
    ImageView qrOverlapIv;

    RetroClient retroClient;

    String answer;
    int questId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏

        // 호출할 때 같이 보낸 값 받아옴
//        Intent iT = getIntent();
//        questId = iT.getExtras().getInt("questId", 0);

        //상단이미지
        Glide.with(this)
                .load(R.drawable.dragdown)
                .into(dragIv);
        dragIv.setScaleType(ImageView.ScaleType.FIT_XY);

        dragIv.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.BounceInDown)
                        .duration(1500)
                        .playOn(dragIv);
            }
        });

        Glide.with(this)
                .load(R.drawable.qr)
                .into(qrOverlapIv);
        qrOverlapIv.setScaleType(ImageView.ScaleType.FIT_XY);

        qrInit();
    }

    public void qrInit(){

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        resultTextView.setText(text);

        Toast.makeText(getApplicationContext(), "잘못된 QR코드입니다", Toast.LENGTH_LONG).show();
        YoYo.with(Techniques.Tada)
                .duration(1000)
                .playOn(qrOverlapIv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }


    public void postQuestDetail(int questId){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);

        retroClient.postQuestDetail(questId, parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                QuestDetail data = (QuestDetail) receivedData;
//                setQuest(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "Fialure: " + String.valueOf(code), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void postQuestResult(String answerStr){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", GlobalApplication.access_tocken);
        parameters.put("answer", answerStr);


        retroClient.postQuestResult(questId, parameters, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "잘못된 QR코드입니다", Toast.LENGTH_LONG).show();
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(qrOverlapIv);
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                QuestResult data = (QuestResult) receivedData;
                setCheckAnswer(data);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "잘못된 QR코드입니다", Toast.LENGTH_LONG).show();
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(qrOverlapIv);
            }
        });
    }

    //정답체크
    public void setCheckAnswer(QuestResult data){

        if(data.result){
            //정답
            Intent iT = new Intent(getApplicationContext(), SuccessActivity.class);
            iT.putExtra("questId", questId);
            iT.putExtra("answer", answer);
            startActivity(iT);

            finish();
        }

        else{
            Toast.makeText(getApplicationContext(), "잘못된 QR코드입니다", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .playOn(qrOverlapIv);
        }
    }

}