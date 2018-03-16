package lipnus.com.realworld.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import lipnus.com.realworld.GlobalApplication;
import lipnus.com.realworld.R;
import lipnus.com.realworld.mission.MissionActivity;
import lipnus.com.realworld.retro.ResponseBody.Scenario;


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

            //리스트뷰와 어댑터
            ListView listview = (ListView)rootView.findViewById(R.id.main_fragment_listview);
            ListViewAdapter adapter = new ListViewAdapter();
            listview.setAdapter(adapter);


            int listSize = GlobalApplication.scenarioList.size();

            for(int i=0; i<listSize; i++){
                Scenario scenario = GlobalApplication.scenarioList.get(i);
                adapter.addItem(scenario.name, scenario.lastPlayed, scenario.accomplished);
            }

            adapter.notifyDataSetChanged();

            //리스트뷰의 클릭리스너
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListViewItem scenario = (ListViewItem)parent.getAdapter().getItem(position);

                    Log.e("CCC", "위치: " + position + " / 내용: " + scenario.date );

                    if(scenario.date==null && position!=0){
                        Toast.makeText(getContext(), "LOCKED", Toast.LENGTH_LONG).show();
                    }else{
                        Intent iT = new Intent(context, MissionActivity.class);
                        iT.putExtra("scenarioId", position+1);
                        startActivity(iT);
                    }
                }
            });

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
