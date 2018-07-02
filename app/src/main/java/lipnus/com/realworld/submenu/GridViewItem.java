package lipnus.com.realworld.submenu;

import android.graphics.Bitmap;

public class GridViewItem {
    public int couponId;
    public String title;
    public String description;
    public String image;

    

    public GridViewItem(int couponId, String title, String description, String image) {
        this.couponId = couponId;
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
