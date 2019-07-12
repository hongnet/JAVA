package com.example.xm.xianyc.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.xm.xianyc.R;
import com.example.xm.xianyc.fragment.MarketListFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.xm.xianyc.activity.LoginActivity.TOTAL_TIME;
import static com.example.xm.xianyc.activity.LoginActivity.USE_TIME;

public class XianYActivity extends AppCompatActivity {


    private ListView market_list;
    private ViewPager showlist;
    private List<Fragment> m_fragements= new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       {
           SystemClock.sleep(1000*2);
            if (LoginActivity.USE_TIME < TOTAL_TIME){
                System.out.println(USE_TIME+"---****---"+TOTAL_TIME);
                setContentView(R.layout.activity_xian_y);
                initView();
            }
        }


        //

        return;
    }




    private void initView() {
        market_list = (ListView) findViewById(R.id.listview);
        showlist = (ViewPager)findViewById(R.id.show_list);
        m_fragements.add(new MarketListFragment());
        showlist.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }
    class  MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return m_fragements.get(i);
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
