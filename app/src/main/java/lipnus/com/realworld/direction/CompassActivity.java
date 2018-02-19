package lipnus.com.realworld.direction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.eventBus.BusProvider;
import lipnus.com.realworld.R;
import lipnus.com.realworld.eventBus.LocationEvent;


public class CompassActivity extends AppCompatActivity {

    private static final String TAG = "CompassActivity";

    private ImageView arrowView;
    private Compass compass;

    double myLatitude, myLogitude;
    double targetLatitude, targetLongitude;

    int targetAngle = 0; //목표지점과 현재위치와의 각 차이

    @BindView(R.id.compass_story)
    TextView storyTv;

    GetLocation loca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);

        //오또
        BusProvider.getInstance().register(this);

        storyTv.setText("하멜이 찾아간 장소 근처에는 닫혀있는 상점들, 생선냄새드이 가득했다. " +
                "관광객이라면 쉽게 오지 않을것 같은 그길에 한 골목이 있었다.");

        //나침반 초기화
        setupCompass();

        //현재위치
        loca = new GetLocation( getApplicationContext() );

        targetLatitude = 37.590473;
        targetLongitude = 127.021486;

    }


    @Subscribe
    public void FinishLoad(LocationEvent mLocationEvent) {

        myLatitude = mLocationEvent.getMyLatitude();
        myLogitude = mLocationEvent.getMyLogitude();

        targetAngle = 360 - angle(myLatitude, myLogitude, targetLatitude, targetLongitude);
        Log.d("GPGP", "회전각: " + targetAngle);

        loca = new GetLocation( getApplicationContext() );
    }


    private void setupCompass() {
        arrowView = (ImageView) findViewById(R.id.main_image_hands);
        compass = new Compass(this);

        Compass.CompassListener cl = new Compass.CompassListener(){
            private float currentAzimuth;

            @Override
            public void onNewAzimuth(float azimuth) {
                Log.d(TAG, "will set rotation from " + currentAzimuth + " to "
                        + azimuth);

                azimuth = azimuth + targetAngle;

                Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                currentAzimuth = azimuth;

                an.setDuration(500);
                an.setRepeatCount(0);
                an.setFillAfter(true);

                arrowView.startAnimation(an);
            }
        };
        compass.setListener(cl);
    }

    //두 지점 사이의 각도를 구함(북극기준으로 시계방향)
    public int angle(double P1_latitude, double P1_longitude, double P2_latitude, double P2_longitude)
    {
        // 현재 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        double Cur_Lat_radian = P1_latitude * (3.141592 / 180);
        double Cur_Lon_radian = P1_longitude * (3.141592 / 180);


        // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        double Dest_Lat_radian = P2_latitude * (3.141592 / 180);
        double Dest_Lon_radian = P2_longitude * (3.141592 / 180);

        // radian distance
        double radian_distance = 0;
        radian_distance = Math.acos(Math.sin(Cur_Lat_radian) * Math.sin(Dest_Lat_radian) + Math.cos(Cur_Lat_radian) * Math.cos(Dest_Lat_radian) * Math.cos(Cur_Lon_radian - Dest_Lon_radian));

        // 목적지 이동 방향을 구한다.(현재 좌표에서 다음 좌표로 이동하기 위해서는 방향을 설정해야 한다. 라디안값이다.
        double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math.sin(Cur_Lat_radian) * Math.cos(radian_distance)) / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));        // acos의 인수로 주어지는 x는 360분법의 각도가 아닌 radian(호도)값이다.

        double true_bearing = 0;
        if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0)
        {
            true_bearing = radian_bearing * (180 / 3.141592);
            true_bearing = 360 - true_bearing;
        }
        else
        {
            true_bearing = radian_bearing * (180 / 3.141592);
        }

        return (int)true_bearing;
    }






    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        compass.stop();
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }






}