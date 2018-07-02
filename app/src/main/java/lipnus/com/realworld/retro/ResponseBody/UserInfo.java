package lipnus.com.realworld.retro.ResponseBody;

public class UserInfo {

    public String login;
    public String mobile_no;
    public String name;

    public UserInfo(String login, String mobile_no, String name) {
        this.login = login;
        this.mobile_no = mobile_no;
        this.name = name;
    }
}
