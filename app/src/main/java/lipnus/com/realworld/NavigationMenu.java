package lipnus.com.realworld;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by LIPNUS on 2018-02-19.
 */

public class NavigationMenu {

    Context context;
    View view;
    int position;


    ImageView[] menuIv = new ImageView[3];

    TextView[] menuTv = new TextView[3];

    LinearLayout[] menuLr = new LinearLayout[3];

    public NavigationMenu(Context context, View view, int position) {
        this.context = context;
        this.view = view;
        this.position = position;

        menuIv[0] = view.findViewById(R.id.navitaion_menu_iv1);
        menuIv[1] = view.findViewById(R.id.navitaion_menu_iv2);
        menuIv[2] = view.findViewById(R.id.navitaion_menu_iv3);

        menuTv[0] = view.findViewById(R.id.navitaion_menu_tv1);
        menuTv[1] = view.findViewById(R.id.navitaion_menu_tv2);
        menuTv[2] = view.findViewById(R.id.navitaion_menu_tv3);

        menuLr[0] = view.findViewById(R.id.navitaion_menu_lr1);
        menuLr[1] = view.findViewById(R.id.navitaion_menu_lr2);
        menuLr[2] = view.findViewById(R.id.navitaion_menu_lr3);


              
        menuTv[position].setTextColor(Color.parseColor("#FFFFFF"));

    }


}
