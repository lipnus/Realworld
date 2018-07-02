package lipnus.com.realworld.retro.ResponseBody;

public class Coupon {

    public int couponId;
    public String description;
    public String expirydate;
    public String image;
    public String issuedate;
    public String qrcode;
    public String title;

    public Coupon(int couponId, String description, String expirydate, String image, String issuedate, String qrcode, String title) {
        this.couponId = couponId;
        this.description = description;
        this.expirydate = expirydate;
        this.image = image;
        this.issuedate = issuedate;
        this.qrcode = qrcode;
        this.title = title;
    }
}
