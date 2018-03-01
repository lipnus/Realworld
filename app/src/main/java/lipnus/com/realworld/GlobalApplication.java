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

    public static String access_tocken = "e23898f5-1d73-11e8-b608-0cc47a9ce276";
    public static String imgPath = "http://210.180.118.59:8061/images/";

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
