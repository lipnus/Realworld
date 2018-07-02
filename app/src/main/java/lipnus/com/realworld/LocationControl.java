package lipnus.com.realworld;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by Sunpil on 2017-03-23.
 *
 * 사용자의 위치를 받아오는 클래스
 *
 */

public class LocationControl {

    //현재위치를 구하는데 필요한 지오코더
    LocationManager lm;

    //생성자
    public LocationControl(Context ct){
        //현재위치를 구하는데 필요한 지오코더
        lm = (LocationManager) ct.getSystemService(Context.LOCATION_SERVICE); //LocationManager 객체를 얻어온다

        //연재위치 구하기
        WhereAreyou();
    }


    //현재위치좌표를 구한다
    public void WhereAreyou(){
        try{

            Log.d("GPGP", "위치정보 수신중..");

            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);

        }catch(SecurityException ex){}

    }

    //위치 리스너
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("GPGP", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

            Log.e("GPGP", "위치정보 : " + provider + "\n위도 : " + latitude + "\n경도 : " + longitude
                    + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);

            //GlobalApplication에 현재의 위치 저장
            GlobalApplication.setLocation(latitude, longitude);



            lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
        }
        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat2 목표지점 위도
     * @param lon2 목표지점 경도
     * @param unit 거리 표출단위
     * @return
     */
    public static int distance(double lat2, double lon2, String unit) {

        //GlobalApplication에 저장된 사용자의 위치값
        double lat1 = GlobalApplication.user_latitude;
        double lon1 = GlobalApplication.user_longitude;

        //위도경도를 받지 못할경우 초깃값인 0,0으로 유지되며, 이럴 경우 -1을 반환
        if (lat1 == 0){
            return -1;
        }

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (int)(dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    //지금 위치에서 해당 목표까지 걸어서(35m/분) 몇분거리인지 표시
    public static String distanceMinute(double lat, double lon){

        String rV;

        int distance = distance(lat, lon, "meter");
        double needTime = (double)distance / 35;

        //소수 첫째자리까지만 표시
        DecimalFormat df =new DecimalFormat("#");

        if(distance != -1){
            rV = df.format(needTime) + "분 거리";
        }else{
            rV = "";
        }

        //해당 위치에 있을때 표시
        if(df.format(needTime).equals("0")){
            rV = "바로그곳!";
        }

        if( needTime > 80){
            rV = "안암동";
        }

        return rV;
    }


    //지금 위치에서 해당 목표까지 걸어서(35m/분) 몇분거리인지 표시(정수형)
    public static int distanceMinuteInt(double lat, double lon){

        String rV;

        int distance = distance(lat, lon, "meter");
        double needTime = (double)distance / 35;

        //소수 첫째자리까지만 표시
        DecimalFormat df =new DecimalFormat("#");

        if(distance != -1){
            rV = df.format(needTime);
        }else{
            rV = "0";
        }

        int returnValue = Integer.parseInt(rV);
        return returnValue;
    }
}
