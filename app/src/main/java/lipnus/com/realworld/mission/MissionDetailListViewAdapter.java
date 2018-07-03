package lipnus.com.realworld.mission;

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

import lipnus.com.realworld.R;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class MissionDetailListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MissionDetailListViewItem> listViewItemMissionDetailList = new ArrayList<>() ;


    // ListViewAdapter의 생성자
    public MissionDetailListViewAdapter() {

    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemMissionDetailList.size() ;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // menulist.xml을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_detail, parent, false);
        }

            MissionDetailListViewItem item = listViewItemMissionDetailList.get(pos);

            TextView btnTv = convertView.findViewById(R.id.detail_btn_tv);
            btnTv.setText( item.label );



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
        return listViewItemMissionDetailList.get(position) ;
    }


    // 아이템 데이터 추가
    public void addItem(int questId, int type, String label, int locked) {

        MissionDetailListViewItem item = new MissionDetailListViewItem(questId, type, label, locked);
        listViewItemMissionDetailList.add(item);
    }
}