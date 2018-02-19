package lipnus.com.realworld;

import android.app.Application;

import java.util.List;

import lipnus.com.realworld.retro.ResponseBody.Scenario;

/**
 * Created by Sunpil on 2018-02-18.
 */

public class GlobalApplication extends Application {

    public static List<Scenario> scenarioList = null;

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
