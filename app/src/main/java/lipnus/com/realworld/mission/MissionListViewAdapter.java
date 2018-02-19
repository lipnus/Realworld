package lipnus.com.realworld.mission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lipnus.com.realworld.R;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class MissionListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MissionListViewItem> listViewItemMissionList = new ArrayList<>() ;


    // ListViewAdapter의 생성자
    public MissionListViewAdapter() {

    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemMissionList.size() ;
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

        ImageView lockIv = convertView.findViewById(R.id.list_lock_iv);
        TextView titleTv = convertView.findViewById(R.id.list_title_tv);
        TextView dateTv = convertView.findViewById(R.id.list_date_tv);

        titleTv.setText(listViewItemMissionList.get(pos).getTitile());
        dateTv.setText(listViewItemMissionList.get(pos).getDate());


        int imgPath;

        if(listViewItemMissionList.get(pos).getState().equals("locked")){
            imgPath = R.drawable.lock_lock;
        }else if(listViewItemMissionList.get(pos).getState().equals("now")) {
            imgPath = R.drawable.lock_now;
        }else{
            imgPath = R.drawable.lock_unlock;
        }

        Glide.with(context)
                .load( imgPath )
                .into(lockIv);
        lockIv.setScaleType(ImageView.ScaleType.FIT_XY);

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
        return listViewItemMissionList.get(position) ;
    }


    // 아이템 데이터 추가
    public void addItem(String title, String date, String state) {

        MissionListViewItem item = new MissionListViewItem(title, date, state);
        listViewItemMissionList.add(item);
    }
}