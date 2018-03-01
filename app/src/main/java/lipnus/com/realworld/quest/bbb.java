package lipnus.com.realworld.quest;

import android.app.Activity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import lipnus.com.realworld.R;
import lipnus.com.realworld.retro.RetroClient;

public class bbb extends SlidingActivity {


    ImageView dragIv;
    RetroClient retroClient;


    @Override
    public void init(Bundle savedInstanceState) {
        setContent(R.layout.activity_word);
        disableHeader();
        enableFullscreen();

        dragIv = findViewById(R.id.drag_iv);


        retroClient = RetroClient.getInstance(this).createBaseApi(); //레트로핏


        //상단이미지
        Glide.with(this)
                .load(R.drawable.dragdown)
                .into(dragIv);
        dragIv.setScaleType(ImageView.ScaleType.FIT_XY);

        dragIv.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.BounceInDown)
                        .duration(1500)
                        .playOn(dragIv);
            }
        });
    }


    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(5);
    }
}
