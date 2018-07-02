package lipnus.com.realworld;

import android.app.Application;
import android.content.Intent;

import java.util.List;

import lipnus.com.realworld.retro.ResponseBody.Scenario;

/**
 * Created by Sunpil on 2018-02-18.
 */

public class GlobalApplication extends Application {

    public static List<Scenario> scenarioList = null;
//    public static String access_tocken = "dd4076af-0e79-11e8-b608-0cc47a9ce276";

    public static String access_tocken = "";
    public static String serverPath = "https://apitest.doctorhamel.com:443";
    public static String imgPath = "https://apitest.doctorhamel.com:443/images/";
    public static String missionImgPath =""; //인벤토리에도 그림이 떠야하니 저장해뒀다가 씀

    public static String clientId = "l7xx119653b3a36b4dc4be4206419bea131d";
    public static String uuid = "";

    public static String tempEmail = "";
    public static String tempPassword = "";

    public static Intent profileImg = null;

    //유저의 현재위치
    public static volatile double user_latitude = 0;
    public static volatile double user_longitude = 0;


    //현재 진행중인 시나리오
    public static int nowMission = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    //서버에서 보낸 걸 디자인 형식에 맞게 표시되게 만든다
    public static String dateTrans(String dateStr){

//        String dateStr = "Fri, 16 Mar 2018 22:03:30 GMT";

        String day = dateStr.substring(5,7);
        String year = dateStr.substring(12,16);
        String monthStr = dateStr.substring(8,11);
        String month="0";

        String[] monthList  = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for(int i=0; i<12; i++){
            if(monthStr.equals(monthList[i])){
                month = Integer.toString(i+1);
            }
        }
        return year + "/" + month + "/" + day;
    }

    //위치정보 입력
    public static void setLocation(double user_latitude, double user_longitude) {
        GlobalApplication.user_latitude = user_latitude;
        GlobalApplication.user_longitude = user_longitude;
    }
}
