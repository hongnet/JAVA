package com.example.xm.xianyc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.xm.xianyc.R;

import static com.example.xm.xianyc.activity.LoginActivity.TOTAL_TIME;
import static com.example.xm.xianyc.activity.LoginActivity.USE_TIME;

public class WebView extends AppCompatActivity {

    private android.webkit.WebView webView;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
         {
            if (USE_TIME >= TOTAL_TIME)
                return;
        }
        initWebView();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (webView.canGoBack()) {
//                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//                webView.goBack();   //返回上一页面
//                return true;
//            } else {
//                System.exit(0);     //退出程序
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void initWebView() {
        webView = (android.webkit.WebView) findViewById(R.id.webView01);
        //WebView加载本地资源
//        webView.loadUrl("file:///android_asset/example.html");
        //WebView加载web资源
        Uri uri = Uri.parse(ChatActivity.url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        //webView.loadUrl();
        //覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebView中打开
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
//                //返回值是true的时候是控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器打开
//                if( url.startsWith("http:") || url.startsWith("https:") ) {
//                    webView.loadUrl(url);
//                    return true;
//                }
//                try{
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity( intent );
//                }catch(Exception e){}
//                return true;
//            }
//            //WebViewClient帮助WebView去处理一些页面控制和请求通知
//        });
        //启用支持Javascript
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        //WebView加载页面优先使用缓存加载
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        //页面加载
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
//                //newProgress   1-100之间的整数
//                if (newProgress == 100) {
//                    //页面加载完成，关闭ProgressDialog
//                    closeDialog();
//                } else {
//                    //网页正在加载，打开ProgressDialog
//                    openDialog(newProgress);
//                }
//            }
//
//            private void openDialog(int newProgress) {
//                if (dialog == null) {
//                    dialog = new ProgressDialog(WebView.this);
//                    dialog.setTitle("正在加载");
//                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    dialog.setProgress(newProgress);
//                    dialog.setCancelable(true);
//                    dialog.show();
//                } else {
//                    dialog.setProgress(newProgress);
//                }
//            }
//
//            private void closeDialog() {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                    dialog = null;
//                }
//            }
//        });
    }
}


