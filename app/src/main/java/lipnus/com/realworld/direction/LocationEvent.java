package lipnus.com.realworld.direction;

/**
 * Created by Sunpil on 2018-02-05.
 */

public class LocationEvent {

    double myLatitude;
    double myLogitude;

    public LocationEvent(double myLatitude, double myLogitude) {
        this.myLatitude = myLatitude;
        this.myLogitude = myLogitude;
    }

    public double getMyLatitude() {
        return myLatitude;
    }

    public double getMyLogitude() {
        return myLogitude;
    }
}
