package lipnus.com.realworld.retro.ResponseBody;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public class TokenGet {

    public final String error;
    public final String access_token;

    public TokenGet(String error, String access_token) {
        this.error = error;
        this.access_token = access_token;
    }
}
