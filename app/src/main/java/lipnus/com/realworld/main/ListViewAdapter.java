package lipnus.com.realworld.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<>() ;


    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // menulist.xml을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_main, parent, false);
        }

        final LinearLayout lR = convertView.findViewById(R.id.list_lr);

        ImageView lockIv = convertView.findViewById(R.id.list_lock_iv);
        TextView titleTv = convertView.findViewById(R.id.list_title_tv);
        TextView dateTv = convertView.findViewById(R.id.list_date_tv);
        ImageView rightBackgroundIv = convertView.findViewById(R.id.list_rightbackground_iv);

        titleTv.setText(listViewItemList.get(pos).title);

        String date;
        if(listViewItemList.get(pos).date!=null){
            date = GlobalApplication.dateTrans( listViewItemList.get(pos).date );
        }else{
            date = "";
        }
        dateTv.setText(date);


        int imgPath;

        //지금 진행중인거 찾기
        int size = listViewItemList.size();
        int lastPlay = 0;
        for(int i=0; i<size; i++){
            if(listViewItemList.get(i).accomplished==true){
                lastPlay = i+1;
            }
        }


        //진행중
        if(lastPlay==pos){
            Glide.with(context)
                    .load( R.drawable.ing )
                    .into(rightBackgroundIv);
            rightBackgroundIv.setScaleType(ImageView.ScaleType.FIT_XY);
            imgPath = R.drawable.lock_now;
            dateTv.setText("");
        }

        //깬거
        else if(listViewItemList.get(pos).accomplished==true){
            imgPath = R.drawable.lock_unlock;
        }

        //못갠거
        else{
            imgPath = R.drawable.lock_lock;
            titleTv.setText("LOCKED");
        }




        Glide.with(context)
                .load( imgPath )
                .into(lockIv);
        lockIv.setScaleType(ImageView.ScaleType.FIT_XY);

        //애니메이션
        lR.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.FadeInLeft)
                        .duration(pos*300+300)
                        .playOn(lR);
            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }


    // 아이템 데이터 추가
    public void addItem(String title, String date, Boolean accomplished) {

        ListViewItem item = new ListViewItem(title, date, accomplished);
        listViewItemList.add(item);
    }
}