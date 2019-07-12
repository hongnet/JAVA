package com.example.xm.xianyc.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.xm.xianyc.R;
import com.example.xm.xianyc.activity.ChatActivity;
import com.example.xm.xianyc.dbhelper.ContactOpenHelper;
import com.example.xm.xianyc.service.IMService;
import com.example.xm.xianyc.utils.ThreadUtils;

import net.dongliu.requests.Requests;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.xm.xianyc.activity.LoginActivity.TOTAL_TIME;
import static com.example.xm.xianyc.activity.LoginActivity.USE_TIME;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketListFragment extends Fragment {

    public static ListView list_view;
    public static  CursorAdapter adapter;
    static public Document doc = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);
    }

    private void init() {
    }


    public MarketListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_market_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        list_view=(ListView) view.findViewById(R.id.listview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData();
        initListener();
        super.onActivityCreated(savedInstanceState);
    }
    public void initData(){

        //list_view.setAdapter(adapter);
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                main_loop();
            }
        });

    }
    public static Document srawlhtml(String surl) throws IOException, InterruptedException {
        //Connection con = Jsoup.connect(surl);
        //con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        //con.header("Accept-Encoding","gzip, deflate");
        //	con.header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        //  con.header("Connection", "keep-alive");
        // con.header("Host", url);
        // con.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
        Map params = new HashMap<String,String>();
        params.put("Accept","text/html, application/xhtml+xml, application/xml; q=0.9, */*; q=0.8");
        params.put("Accept-Language","zh-CN");
        params.put("Host","s.2.taobao.com");
        params.put("Accept-Encoding", "gzip, deflate, br");
        params.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
        String resp="";
        try{
             resp = Requests.get(surl).headers(params).send().readToText();
        }catch (Exception e)
        {
            Thread.sleep(1000*10);
            resp = Requests.get(surl).headers(params).send().readToText();
        }

        //con.ignoreContentType(true).maxBodySize(Integer.MAX_VALUE);
        return Jsoup.parse(resp);
    }
    public void main_loop(){
        Intent intent = new Intent(getActivity(), IMService.class);
        if(USE_TIME>TOTAL_TIME)
        {
            getActivity().stopService(intent);
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            getActivity().startForegroundService(intent);
        } else
        {
            getActivity().startService(intent);
        }



                   // return;
                }



    public  void initListener(){
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(USE_TIME>TOTAL_TIME)
                {
                    return;
                }
                Cursor c = adapter.getCursor();
                c.moveToPosition(position);
                String desc_info = c.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.desc));
                String url_market=c.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.url));
                //view.findViewById("url_market");
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(ChatActivity.STRINGURL,url_market);
                intent.putExtra(ChatActivity.DESC_INFO,desc_info);
                startActivity(intent);
            }
        });

    }
}
