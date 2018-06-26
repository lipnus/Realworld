package lipnus.com.realworld.quest.list_hint;

import android.content.Context;
import android.util.Log;
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
import lipnus.com.realworld.mission.MissionDetailListViewItem;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class Hint_ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Hint_ListViewItem> listViewItemList = new ArrayList<>() ;


    // ListViewAdapter의 생성자
    public Hint_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_hint, parent, false);
        }

        Hint_ListViewItem item = listViewItemList.get(pos);

        //힌트입력
        TextView hintTv = convertView.findViewById(R.id.hintlist_tv);
        hintTv.setText( item.hint );
        Log.e("HHTT", "어댑터에서 힌트: " + listViewItemList.get(pos).hint);

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
    public void addItem(String hint, int id) {

        Hint_ListViewItem item = new Hint_ListViewItem(hint, id);
        listViewItemList.add(item);

    }
}