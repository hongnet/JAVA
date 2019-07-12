package com.example.xm.xianyc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xm.xianyc.R;
import com.example.xm.xianyc.utils.StringConst;

import static com.example.xm.xianyc.activity.LoginActivity.TOTAL_TIME;
import static com.example.xm.xianyc.activity.LoginActivity.USE_TIME;

public class ChatActivity extends AppCompatActivity {

    public static String STRINGURL = "url";
    public static String DESC_INFO="desc";
    private TextView m_textview;
    private Button m_open_url_btn;


    public static String url;
    //private WebView webView = (WebView) findViewById(R.id.webView01);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        String id =STRINGURL.substring(STRINGURL.indexOf("id=")+3);
        url= StringConst.phone_url+id;
        //new MainActivity().onCreate(savedInstanceState);




         initView();
        initListener();

    }
//    protected void onResume(String url){
//        super.onResume();
//        // url = "http://www.baidu.com";
//        webView.loadUrl(url);
//    };

    private void initListener() {
     {
            m_open_url_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (USE_TIME>TOTAL_TIME)
                        return;
                    //String id =STRINGURL.substring(STRINGURL.indexOf("id=")+3);
                    //String url_phone= StringConst.phone_url+id;
                    Uri uri = Uri.parse(url);
//                    Intent intent = new Intent(ChatActivity.this, com.example.xm.xianyc.activity.WebView.class);
//                    System.out.println("-*-*-*-> webview");
//                    startActivity(intent);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }

    }


    private void init() {
        STRINGURL = getIntent().getStringExtra(ChatActivity.STRINGURL);
        DESC_INFO = getIntent().getStringExtra(ChatActivity.DESC_INFO);

    }


    private void initView() {
        m_textview = (TextView) findViewById(R.id.url_market);
        m_open_url_btn = (Button) findViewById(R.id.open_url);
        m_textview.setText(DESC_INFO);
    }
}


