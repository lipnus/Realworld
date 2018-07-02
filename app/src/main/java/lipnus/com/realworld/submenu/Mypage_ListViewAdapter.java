package lipnus.com.realworld.submenu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import lipnus.com.realworld.R;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class Mypage_ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Mypage_ListViewItem> items = new ArrayList<>() ;


    // ListViewAdapter의 생성자
    public Mypage_ListViewAdapter() {

    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return items.size() ;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // menulist.xml을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_mypage, parent, false);
        }

        TextView scenarioTv = convertView.findViewById(R.id.mypage_list_scenario_tv);
        TextView scoreTv = convertView.findViewById(R.id.mypage_list_score_tv);
        TextView maxScoreTv = convertView.findViewById(R.id.mypage_list_max_score_tv);
        ProgressBar progressBar = convertView.findViewById(R.id.mypage_list_progressBar);




//        double score = Double.parseDouble(items.get(position).score);
//        double maxScore = Double.parseDouble(items.get(position).maxScore);

        scenarioTv.setText( items.get(position).scenarioTitle );
        maxScoreTv.setText( "" + items.get(position).maxScore );
        scoreTv.setText( "" +items.get(position).score );

        progressBar.setMax( items.get(position).maxScore );
        progressBar.setProgress(  items.get(position).score );
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
        return items.get(position) ;
    }


    // 아이템 데이터 추가
    public void addItem(String scenario, int score, int maxScore) {

        Mypage_ListViewItem item = new Mypage_ListViewItem(scenario, score, maxScore);
        items.add(item);
    }
}