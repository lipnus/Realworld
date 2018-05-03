package lipnus.com.realworld;

import android.app.Application;

import java.util.List;

import lipnus.com.realworld.retro.ResponseBody.Scenario;

/**
 * Created by Sunpil on 2018-02-18.
 */

public class GlobalApplication extends Application {

    public static List<Scenario> scenarioList = null;
//    public static String access_tocken = "dd4076af-0e79-11e8-b608-0cc47a9ce276";

    public static String access_tocken = "";
    public static String imgPath = "http://api.doctorhamel.com:8061/images/";

    @Override
    public void onCreate() {
        super.onCreate();
    }


    //서버에서 좆같은 형식으로 보낸 걸 디자인 형식에 맞게 표시되게 만든다
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
}
