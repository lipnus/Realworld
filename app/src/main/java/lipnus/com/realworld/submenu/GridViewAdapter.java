package lipnus.com.realworld.submenu;

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

public class GridViewAdapter extends BaseAdapter {

    private Context mContext; //쓸까?
    private ArrayList<GridViewItem> gridViewItems = new ArrayList<>() ;
    private int width;

    // Constructor
    public GridViewAdapter(int screenWidth){ width = screenWidth; }

    public GridViewAdapter(Context c) {
        mContext = c;
    }


    public int getCount() {
        return gridViewItems.size();
    }

    public Object getItem(int position) {
        return gridViewItems.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_coupon, parent, false);
        }

        //레이아웃
        ImageView gridIv = convertView.findViewById(R.id.grid_title_iv);
        TextView gridTv = convertView.findViewById(R.id.grid_title_tv);

        //데이터
        GridViewItem item = gridViewItems.get(pos);

        //적용
        gridTv.setText(item.title);


        ViewGroup.LayoutParams params = gridIv.getLayoutParams();
        params.width = width/3 -30;
        params.height = params.width;
        gridIv.setLayoutParams(params);


        Glide.with(context)
                .load( item.image )
                .centerCrop()
                .into( gridIv );
        gridIv.setScaleType(ImageView.ScaleType.FIT_XY);

        return convertView;
    }

    // 아이템 데이터 추가
    public void addItem(int couponId, String title, String description, String image) {
        GridViewItem item = new GridViewItem(couponId, title, description, image);
        gridViewItems.add(item);
    }

}
