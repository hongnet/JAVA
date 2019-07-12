package com.example.xm.xianyc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xm.xianyc.R;
import com.example.xm.xianyc.utils.FileReFer;
import com.example.xm.xianyc.utils.StringConst;
import com.example.xm.xianyc.utils.ThreadUtils;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class LoginActivity extends AppCompatActivity {

    public static  int TOTAL_TIME =60*24*31 ;
    private TextView login_password;
    private TextView login_username;
    private Button login_btn;
    public static File file =new File(Environment.getExternalStorageDirectory()+"/Android/"+"tzmappings");
    public static  String temp_str=StringConst.temp;
    private TextView min_price;
    private boolean thread_stop=false;
    public static int USE_TIME=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();

       return;

    }

    private void initListener() {
        boolean is_valid=false;
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(login_username.getText().toString().equals("test") & login_password.getText().toString().equals("test")){
                    //使用30分钟
                    try {
                        StringConst.key_word=login_username.getText().toString();
                        StringConst.max_price=login_password.getText().toString();
                        StringConst.min_price=min_price.getText().toString();
                        useXianYCThirstyMin();

                    } catch (IOException e) {
                        ThreadUtils.runInUIThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(getApplicationContext(),"试用时间到,请搜索微信号XmXmXm_C购买",Toast.LENGTH_LONG);
                                showMyToast(toast,9*1000);
                                toast = Toast.makeText(getApplicationContext(),"试用时间到,请搜索微信号XmXmXm_C购买",Toast.LENGTH_LONG);
                                showMyToast(toast,9*1000);
                                toast = Toast.makeText(getApplicationContext(),"试用时间到,请搜索微信号XmXmXm_C购买",Toast.LENGTH_LONG);
                                showMyToast(toast,9*1000);
                                toast = Toast.makeText(getApplicationContext(),"试用时间到,请搜索微信号XmXmXm_C购买",Toast.LENGTH_LONG);
                                showMyToast(toast,9*1000);
                                return;
                            }
                        });

                    }

                }
//                if(TextUtils.isEmpty(login_username.getText().toString())){
//                    System.out.println(login_username.getText().toString());
//                    login_username.setError("用户名不能为空");
//                    return;
//                }
//                if(TextUtils.isEmpty(login_password.getText().toString())){
//                    login_password.setError("密码不能为空");
//                    return;


        });
        //System.exit(0);;
    }

    private void useXianYOneYear() throws IOException {
        if(FileReFer.WatchFile(file,temp_str)!=60*24*365) {
            Intent intent = new Intent(LoginActivity.this, XianYActivity.class);
            startActivity(intent);
            finish();
        }else
        {
            Toast.makeText(getApplicationContext(),"使用时间到，请搜索微信号XmXmXm_C购买",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void useXianYCThirstyMin() throws IOException {
        {
            if((USE_TIME=FileReFer.WatchFile(file,temp_str))<TOTAL_TIME)
     {
                //finish();//finish();
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                         {
                                Intent intent = new Intent(LoginActivity.this, XianYActivity.class);
                                startActivity(intent);
                            }


                    }
                });
            }else
            {
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(),"试用时间到,请搜索微信号XmXmXm_C购买",Toast.LENGTH_LONG);
                        showMyToast(toast,9*1000);
                        toast = Toast.makeText(getApplicationContext(),"试用时间到,请搜索微信号XmXmXm_C购买",Toast.LENGTH_LONG);
                        showMyToast(toast,9*1000);
                        toast = Toast.makeText(getApplicationContext(),"试用时间到,请搜索微信号XmXmXm_C购买",Toast.LENGTH_LONG);
                        showMyToast(toast,9*1000);
                        toast = Toast.makeText(getApplicationContext(),"试用时间到,请搜索微信号XmXmXm_C购买",Toast.LENGTH_LONG);
                        showMyToast(toast,9*1000);
                        return;
                    }
                });

            }
        }

    }

    public static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3500);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
    private void initView() {
        login_username = (TextView) findViewById(R.id.search_name);
        login_password = (TextView)findViewById(R.id.max_price);
        min_price = (TextView)findViewById(R.id.min_price);
        login_btn = (Button)findViewById(R.id.login_btn);

    }
}
