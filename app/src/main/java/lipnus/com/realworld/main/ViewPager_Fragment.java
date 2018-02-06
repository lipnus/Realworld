package lipnus.com.realworld.main;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import lipnus.com.realworld.R;


/**
 * Created by Sunpil on 2017-01-24.
 */

//프래그먼트
public class ViewPager_Fragment {

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            int viewNum = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            Context context;

            Log.d("SSFF", "지금: " + viewNum);

            try{
                switch(viewNum){
                    case 1:
                        rootView = setFragment1(container, inflater);
                        break;
                    case 2:
                        rootView = setFragment2(container, inflater);
                        break;

                    default:
                        rootView = setFragment3(container, inflater);
                        break;
                }

            }catch (Exception e){

                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                context = rootView.getContext();
            }

            Log.d("FFF", viewNum + "번째뷰생성");
            return rootView;
        }//onCreateView




        //각 프래그먼트들의 소스코드가 들어있다
        public View setFragment1(ViewGroup container, LayoutInflater inflater){

            View rootView;
            final Context context;

            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            context = rootView.getContext();


//            //리스트뷰와 어댑터
            ListView listview = (ListView)rootView.findViewById(R.id.main_fragment_listview);
            ListViewAdapter adapter = new ListViewAdapter();
            listview.setAdapter(adapter);

            adapter.addItem("제목", "20180312", true);
            adapter.addItem("제목2", "20180312", true);
            adapter.addItem("제목3", "20180312", true);
            adapter.notifyDataSetChanged();

            return rootView;
        }

        public View setFragment2(ViewGroup container, LayoutInflater inflater){

            View rootView;
            final Context context;

            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            context = rootView.getContext();


            return rootView;
        }

        public View setFragment3(ViewGroup container, LayoutInflater inflater){

            View rootView;
            final Context context;

            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            context = rootView.getContext();



            return rootView;
        }

    }
}
