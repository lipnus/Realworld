package lipnus.com.realworld.quest.list_multi;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import lipnus.com.realworld.R;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class Multi_ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Multi_ListViewItem> items = new ArrayList<>() ;


    // ListViewAdapter의 생성자
    public Multi_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_multi, parent, false);
        }

        Multi_ListViewItem item = items.get(pos);

        TextView quizTv = convertView.findViewById(R.id.multi_quiz_tv);
        EditText answerEt = convertView.findViewById(R.id.multi_et);
        quizTv.setText( item.text );


        //힌트에 의해 조정된 값이 있을경우 적용하기 위해
        answerEt.setText(item.userAnswer);

        //텍스트에 변화가 있을 때
        answerEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String inputStr = answerEt.getText().toString();
                item.userAnswer = inputStr;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputStr = answerEt.getText().toString();
                item.userAnswer = inputStr;
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
        return items.get(position) ;
    }



    // 아이템 데이터 추가
    public void addItem(int order, String text, String answer) {

        Multi_ListViewItem item = new Multi_ListViewItem(order, text, answer);
        items.add(item);
    }

    public void setAnswer(int order, String answer){
        items.get(order).userAnswer = answer;

    }
}