package lipnus.com.realworld.mission;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.realworld.R;

public class MissionActivity extends AppCompatActivity {

    @BindView(R.id.mission_title_iv)
    ImageView titleIv;

    @BindView(R.id.mission_back_iv)
    ImageView backIv;

    @BindView(R.id.mission_title_tv)
    TextView titleTv;

    @BindView(R.id.mission_subtitle_tv)
    TextView subtitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        ButterKnife.bind(this);

        titleTv.setText("그레이트 선필쓰의 대모험");
        subtitleTv.setText("서브타이틀 - 미션목록ㄴ");
    }
}
