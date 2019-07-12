package com.example.xm.xianyc.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xm.xianyc.ContentProvider.MyContentProvider;
import com.example.xm.xianyc.R;
import com.example.xm.xianyc.activity.ChatActivity;
import com.example.xm.xianyc.activity.LoginActivity;
import com.example.xm.xianyc.activity.XianYActivity;
import com.example.xm.xianyc.dbhelper.ContactOpenHelper;
import com.example.xm.xianyc.fragment.MarketListFragment;
import com.example.xm.xianyc.utils.FileReFer;
import com.example.xm.xianyc.utils.StringConst;
import com.example.xm.xianyc.utils.ThreadUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static com.example.xm.xianyc.activity.LoginActivity.TOTAL_TIME;
import static com.example.xm.xianyc.activity.LoginActivity.file;
import static com.example.xm.xianyc.activity.LoginActivity.showMyToast;
import static com.example.xm.xianyc.activity.LoginActivity.temp_str;
import static com.example.xm.xianyc.fragment.MarketListFragment.srawlhtml;

public class IMService extends Service {
    private  Handler handler1;
    private int notificationCount;
    private Map<String,String> unique;
    private Message msg = new Message(); // 在安卓中，不要在线程中直接现实方法，这样app容易崩溃，有什么要搞，扔到消息处理器中实现。
    @Override
    public IBinder onBind(Intent intent) {
        return new IMyAidlInterface.Stub(){
        };
    }
    public void loop1(){
        try {
            if (FileReFer.WatchFile(file, StringConst.temp) < 50) {

                String max_price = StringConst.max_price;
                String min_price = StringConst.min_price;
                String content = StringConst.key_word;
                final String surl = "https://s.2.taobao.com/list/?"
                        + "st_edtime=1&_input_charset=utf8&search_type=item&"
                        + "q=" + URLEncoder.encode(content, "UTF-8")
                        + "&page=1"
                        + "&start=" + max_price + "&end=" + min_price;


                try {
                    MarketListFragment.doc = srawlhtml(surl);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Document doc = MarketListFragment.doc;
                if (doc == null)
                    return;
                final Elements info = doc.select(".item-info .item-pic a");
                final Elements prices = doc.select(".item-price.price-block .price em");
                final Elements descs = doc.select(".item-brief-desc");
                final Elements regions = doc.select(".item-location");


                ThreadUtils.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        if (300 < getContentResolver().query(MyContentProvider.URI_CONTACT, null, null, null, null).getCount())
                            getContentResolver().delete(MyContentProvider.URI_CONTACT, null, null);
                        for (Element e : info) {
                            //                                System.out.println(e.toString());
                            //                                int f=e.toString().indexOf("href=\"");
                            //                                int af=e.toString().indexOf("\" target=");
                            //                                String url = "http:"+e.toString().substring(e.toString().indexOf("href=\"")+6,e.toString().indexOf("\" target="));
                            //                                System.out.println("http:"+e.toString().substring(e.toString().indexOf("href=\"")+6,e.toString().indexOf("\" target=")));
                            ContentValues values = new ContentValues();
                            if (0 == getContentResolver().query(MyContentProvider.URI_CONTACT, null,
                                    ContactOpenHelper.ContactTable.desc + "=\"" +
                                            descs.get(i).toString().substring(descs.get(i).toString().indexOf(">") + 1, descs.get(i).toString().indexOf("</div>")) + "\"", null, null).getCount()) {
                                values.put(ContactOpenHelper.ContactTable.title,
                                        e.toString().substring(e.toString().indexOf("title=\"") + 7, e.toString().indexOf("\"><img src=")));
                                values.put(ContactOpenHelper.ContactTable.price, prices.get(i).toString().substring(4, prices.get(i).toString().lastIndexOf("</em>")));
                                values.put(ContactOpenHelper.ContactTable.desc, descs.get(i).toString().substring(descs.get(i).toString().indexOf(">") + 1, descs.get(i).toString().indexOf("</div>")));
                                values.put(ContactOpenHelper.ContactTable.region, regions.get(i).toString());
                                values.put(ContactOpenHelper.ContactTable.url,
                                        "http:" + e.toString().substring(e.toString().indexOf("href=\"") + 6, e.toString().indexOf("\" target=")));
                                values.put(ContactOpenHelper.ContactTable.img_url,
                                        "http:" + e.toString().substring(e.toString().indexOf("data-ks-lazyload-custom=\"") + 25, e.toString().lastIndexOf("\" title=")));
                                getContentResolver().insert(MyContentProvider.URI_CONTACT, values);
                                //getActivity().getContentResolver().update(MyContentProvider.URI_CONTACT,values,ContactOpenHelper.ContactTable.title+"=?",new String[]{"闲鱼小米9"});
                                i++;
                                Message msg = new Message(); // 在安卓中，不要在线程中直接现实方法，这样app容易崩溃，有什么要搞，扔到消息处理器中实现。
                                Bundle b = new Bundle();
                                b.putString("price",prices.get(i).toString().substring(4, prices.get(i).toString().lastIndexOf("</em>")));
                                b.putString("desc",descs.get(i).toString().substring(descs.get(i).toString().indexOf(">") + 1, descs.get(i).toString().indexOf("</div>")));
                                msg.setData(b);
                                handler1.sendMessage(msg);
                            }

                        }
                        final Cursor c = getContentResolver().query(MyContentProvider.URI_CONTACT, null, null, null, "_id DESC");
                        ThreadUtils.runInUIThread(new Runnable() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void run() {
                                MarketListFragment.adapter = new CursorAdapter(getApplicationContext(), c) {
                                    // if
                                    @Override
                                    public View newView(Context context, Cursor cursor, ViewGroup parent) {
                                        //TextView tv = new TextView(context);
                                        View view = View.inflate(context, R.layout.item_contact, null);
                                        return view;
                                    }

                                    @Override
                                    public void bindView(View view, Context context, Cursor cursor) {
                                        //TextView tv = (TextView) view;
                                        ImageView ivHead = view.findViewById(R.id.head);
                                        TextView price_view = view.findViewById(R.id.price_item);
                                        TextView title_view = view.findViewById(R.id.title_item);
                                        String price = cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.price));
                                        String img = cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.img_url));
                                        String title = cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.title));
                                        price_view.setText(price);
                                        title_view.setText(title);
                                        //ivHead.setImageIcon(img_url);
                                    }
                                };
                                //显示通知

                                //NotifyInBar();
                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                r.play();
                                ThreadUtils.runInUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast = Toast.makeText(getApplicationContext(), "有新商品发布", Toast.LENGTH_LONG);
                                        showMyToast(toast, 9 * 1000);
                                        toast = Toast.makeText(getApplicationContext(), "有新商品发布", Toast.LENGTH_LONG);
                                        showMyToast(toast, 9 * 1000);
                                        toast = Toast.makeText(getApplicationContext(), "有新商品发布", Toast.LENGTH_LONG);
                                        showMyToast(toast, 9 * 1000);
                                    }
                                });
                                MarketListFragment.list_view.setAdapter(MarketListFragment.adapter);
                            }
                        });


                    }
                });
                SystemClock.sleep(1000 * 60);
            } else {
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //NotifyInBar();
                        Toast toast = Toast.makeText(getApplicationContext(), "使用时间到，请搜索微信号XmXmXm_C购买", Toast.LENGTH_LONG);
                        showMyToast(toast, 9 * 1000);
                        toast = Toast.makeText(getApplicationContext(), "使用时间到，请搜索微信号XmXmXm_C购买", Toast.LENGTH_LONG);
                        showMyToast(toast, 9 * 1000);
                        toast = Toast.makeText(getApplicationContext(), "使用时间到，请搜索微信号XmXmXm_C购买", Toast.LENGTH_LONG);
                        showMyToast(toast, 9 * 1000);
                        toast = Toast.makeText(getApplicationContext(), "使用时间到，请搜索微信号XmXmXm_C购买", Toast.LENGTH_LONG);
                        showMyToast(toast, 9 * 1000);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loop(){
        while (true) {
            System.out.println("---->in");
            try {
                if ((LoginActivity.USE_TIME=FileReFer.WatchFile(file,temp_str)) < TOTAL_TIME) {

                    String max_price = StringConst.max_price;
                    String min_price = StringConst.min_price;
                    String content = StringConst.key_word;
                    final String surl = "https://s.2.taobao.com/list/?"
                            + "st_edtime=1&_input_charset=utf8&search_type=item&"
                            + "q=" + URLEncoder.encode(content, "UTF-8")
                            + "&page=1"
                            + "&start=" + max_price + "&end=" + min_price;


                    try {
                        if (((LoginActivity.USE_TIME)) < LoginActivity.TOTAL_TIME){
                            MarketListFragment.doc = srawlhtml(surl);
                        }else{
                            return;
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(10*1000);
                            continue;
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                            continue;
                        }
                    }
                    Document doc = MarketListFragment.doc;
                    if (doc == null)
                        continue;
                    final Elements info = doc.select(".item-info .item-pic a");
                    final Elements prices = doc.select(".item-price.price-block .price em");
                    final Elements descs = doc.select(".item-brief-desc");
                    final Elements regions = doc.select(".item-location");


                    ThreadUtils.runInThread(new Runnable() {
                        @Override
                        public void run() {

                            int i = 0;
                            if (300 < getContentResolver().query(MyContentProvider.URI_CONTACT, null, null, null, null).getCount())
                                getContentResolver().delete(MyContentProvider.URI_CONTACT, null, null);
                            for (Element e : info) {
                                //                                System.out.println(e.toString());
                                //                                int f=e.toString().indexOf("href=\"");
                                //                                int af=e.toString().indexOf("\" target=");
                                //                                String url = "http:"+e.toString().substring(e.toString().indexOf("href=\"")+6,e.toString().indexOf("\" target="));
                                //                                System.out.println("http:"+e.toString().substring(e.toString().indexOf("href=\"")+6,e.toString().indexOf("\" target=")));
                                ContentValues values = new ContentValues();
                               {
                                   try {
                                       Thread.sleep(70);
                                   } catch (InterruptedException m) {
                                       m.printStackTrace();
                                   }
                                    if (0 == getContentResolver().query(MyContentProvider.URI_CONTACT, null,
                                            ContactOpenHelper.ContactTable.url + "=\"" +
                                                    "http:" + e.toString().substring(e.toString().indexOf("href=\"") + 6, e.toString().indexOf("\" target="))+"\"" , null, null)
                                            .getCount())
                                    {
                                        values.put(ContactOpenHelper.ContactTable.title,
                                                e.toString().substring(e.toString().indexOf("title=\"") + 7, e.toString().indexOf("\"><img src=")));
                                        values.put(ContactOpenHelper.ContactTable.price, prices.get(i).toString().substring(4, prices.get(i).toString().lastIndexOf("</em>")));
                                        values.put(ContactOpenHelper.ContactTable.desc, descs.get(i).toString().substring(descs.get(i).toString().indexOf(">") + 1, descs.get(i).toString().indexOf("</div>")));
                                        values.put(ContactOpenHelper.ContactTable.region, regions.get(i).toString());
                                        values.put(ContactOpenHelper.ContactTable.url,
                                                "http:" + e.toString().substring(e.toString().indexOf("href=\"") + 6, e.toString().indexOf("\" target=")));
                                        values.put(ContactOpenHelper.ContactTable.img_url,
                                                "http:" + e.toString().substring(e.toString().indexOf("data-ks-lazyload-custom=\"") + 25, e.toString().lastIndexOf("\" title=")));
                                        getContentResolver().insert(MyContentProvider.URI_CONTACT, values);
                                        //getActivity().getContentResolver().update(MyContentProvider.URI_CONTACT,values,ContactOpenHelper.ContactTable.title+"=?",new String[]{"闲鱼小米9"});


                                        Bundle b = new Bundle();
                                        b.putString("price",prices.get(i).toString().substring(4, prices.get(i).toString().lastIndexOf("</em>")));
                                        b.putString("desc",descs.get(i).toString().substring(descs.get(i).toString().indexOf(">") + 1, descs.get(i).toString().indexOf("</div>")));
                                        b.putString("url", "http:" + e.toString().substring(e.toString().indexOf("href=\"") + 6, e.toString().indexOf("\" target=")));
                                        msg.setData(b);
                                        //handler1.sendMessage(msg);
                                        ThreadUtils.runInUIThread(new Runnable() {
                                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                            @Override
                                            public void run() {
                                                notificationCount++;
                                                if (Build.VERSION.SDK_INT>=26){
                                                    id=id+Integer.toString(notificationCount);
                                                    name=name+Integer.toString(notificationCount);
                                                    NotificationChannel channel = new NotificationChannel
                                                            (id,name,NotificationManager.IMPORTANCE_HIGH);
                                                    //if (manager == null) {
                                                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    // }
                                                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                                    intent.putExtra(ChatActivity.STRINGURL,msg.getData().getString("url"));
                                                    PendingIntent pendingIntent =
                                                            PendingIntent.getActivity(getApplicationContext(),notificationCount,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                                    manager.createNotificationChannel(channel);
                                                    notification = new Notification.Builder(getApplicationContext(), id)
                                                            .setContentTitle(msg.getData().getString("price"))
                                                            .setContentText(msg.getData().getString("desc"))
                                                            .setSmallIcon(android.R.drawable.stat_notify_more)
                                                            //.setContentIntent(pendingIntent)
                                                            .setAutoCancel(true)
                                                            .build();
                                                    //startForeground(1,notification );
                                                    startForeground(1,notification );
                                                    manager.notify(notificationCount,notification);
                                                    if (notificationCount==40)
                                                        notificationCount=0;
                                                }else{
                                                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                                    intent.putExtra(ChatActivity.STRINGURL,msg.getData().getString("url"));
                                                    PendingIntent pendingIntent =
                                                            PendingIntent.getActivity(getApplicationContext(),notificationCount,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                                    notification = new Notification.Builder(getApplicationContext()).setTicker("123").
                                                            setSmallIcon(R.mipmap.ic_launcher).setLargeIcon( BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                                                            .setContentText(msg.getData().getString("desc"))
                                                            .setContentTitle( msg.getData().getString("price") )
                                                           // .setContentIntent(pendingIntent)
                                                            .setAutoCancel(true)
                                                            .build();
                                                    manager.notify(notificationCount,notification);
                                                    if (notificationCount==40)
                                                        notificationCount=0;
                                                }
                                                bindService(new Intent(getApplicationContext(),GuardService.class),
                                                        mServiceConnection, Context.BIND_IMPORTANT);

//                Notification notification = new Notification();
//
//                notification.flags = Notification.FLAG_AUTO_CANCEL;
//                notification.tickerText="hello";
//                notification.when = Notification.DEFAULT_ALL;
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                PendingIntent pendingIntent =
//                        PendingIntent.getActivity(getApplicationContext(),0,intent,0);
//                Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                        /**设置通知左边的大图标**/
//                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
//                        /**设置通知右边的小图标**/
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        /**通知首次出现在通知栏，带上升动画效果的**/
//                        .setTicker("通知来了")
//                        /**设置通知的标题**/
//                        .setContentTitle("这是一个通知的标题")
//                        /**设置通知的内容**/
//                        .setContentText("这是一个通知的内容这是一个通知的内容")
//                        /**通知产生的时间，会在通知信息里显示**/
//                        .setWhen(System.currentTimeMillis())
//                        /**设置该通知优先级**/
//                        .setPriority(Notification.PRIORITY_HIGH)
//                        /**设置这个标志当用户单击面板就可以让通知将自动取消**/
//                        .setAutoCancel(true)
//                        /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
//                        .setOngoing(false)
//                        /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
//                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
//                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 1, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
//                        .build();
//                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
//                notification.icon = R.drawable.ic_launcher_foreground;
//                /**发起通知**/
//                notificationManager.notify(notificationCount, notification);


                                                // 安卓显示当前时间的方法
                                                Toast toast = Toast.makeText(getApplicationContext(), "有新商品发布", Toast.LENGTH_LONG);
                                                showMyToast(toast,9*1000);
                                            }
                                        });
                                        i++;
                                    }
                                }


                            }
                            final Cursor c = getContentResolver().query(MyContentProvider.URI_CONTACT, null, null, null, "_id DESC");
                            ThreadUtils.runInUIThread(new Runnable() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void run() {

                                    MarketListFragment.adapter = new CursorAdapter(getApplicationContext(), c) {
                                        // if
                                        @Override
                                        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                                            //TextView tv = new TextView(context);
                                            View view = View.inflate(context, R.layout.item_contact, null);
                                            return view;
                                        }

                                        @Override
                                        public void bindView(View view, Context context, Cursor cursor) {
                                            //TextView tv = (TextView) view;
                                            ImageView ivHead = view.findViewById(R.id.head);
                                            TextView price_view = view.findViewById(R.id.price_item);
                                            TextView title_view = view.findViewById(R.id.title_item);
                                            String price = cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.price));
                                            String img = cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.img_url));
                                            String title = cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.title));
                                            price_view.setText(price);
                                            title_view.setText(title);
                                            //ivHead.setImageIcon(img_url);
                                        }
                                    };
                                    MarketListFragment.list_view.setAdapter(MarketListFragment.adapter);
                                }
                            });


                        }
                    });
                    try {

                        Thread.sleep(1000*60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    ThreadUtils.runInUIThread(new Runnable() {
                        @Override
                        public void run() {
                            //NotifyInBar();
                            Toast toast = Toast.makeText(getApplicationContext(), "使用时间到，请搜索微信号XmXmXm_C购买", Toast.LENGTH_LONG);
                            showMyToast(toast, 9 * 1000);
                            toast = Toast.makeText(getApplicationContext(), "使用时间到，请搜索微信号XmXmXm_C购买", Toast.LENGTH_LONG);
                            showMyToast(toast, 9 * 1000);
                            toast = Toast.makeText(getApplicationContext(), "使用时间到，请搜索微信号XmXmXm_C购买", Toast.LENGTH_LONG);
                            showMyToast(toast, 9 * 1000);
                            toast = Toast.makeText(getApplicationContext(), "使用时间到，请搜索微信号XmXmXm_C购买", Toast.LENGTH_LONG);
                            showMyToast(toast, 9 * 1000);
                            SystemClock.sleep(1000*3);
                        }
                    });
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public  void NotifyInBar() {
        //第一步：实例化通知栏构造器Notification.Builder：
        XianYActivity activity=(XianYActivity) getApplicationContext();
        Notification.Builder builder =new Notification.Builder(activity);//实例化通知栏构造器Notification.Builder，参数必填（Context类型），为创建实例的上下文
//第二步：获取状态通知栏管理：
        NotificationManager mNotifyMgr = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);//获取状态栏通知的管理类（负责发通知、清除通知等操作）
//第三步：设置通知栏PendingIntent（点击动作事件等都包含在这里）:
        Intent push =new Intent(activity,MarketListFragment.class);//新建一个显式意图，第一个参数 Context 的解释是用于获得 package name，以便找到第二个参数 Class 的位置
//PendingIntent可以看做是对Intent的包装，通过名称可以看出PendingIntent用于处理即将发生的意图，而Intent用来用来处理马上发生的意图
//本程序用来响应点击通知的打开应用，第二个参数非常重要，点击notification 进入到activity, 使用到pendingIntent类方法，PengdingIntent.getActivity()的第二个参数，即请求参数，实际上是通过该参数来区别不同的Intent的，如果id相同，就会覆盖掉之前的Intent了
        PendingIntent contentIntent = PendingIntent.getActivity(activity,0,push,FLAG_CANCEL_CURRENT);
//第四步：对Builder进行配置：
        builder
                .setContentTitle("My notification")//标题
                .setContentText("Hello World!")// 详细内容
                .setContentIntent(contentIntent)//设置点击意图
                .setTicker("New message")//第一次推送，角标旁边显示的内容
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL);//打开呼吸灯，声音，震动，触发系统默认行为
/*Notification.DEFAULT_VIBRATE    //添加默认震动提醒  需要VIBRATE permission
Notification.DEFAULT_SOUND    //添加默认声音提醒
Notification.DEFAULT_LIGHTS//添加默认三色灯提醒
Notification.DEFAULT_ALL//添加默认以上3种全部提醒*/
//.setLights(Color.YELLOW, 300, 0)//单独设置呼吸灯，一般三种颜色：红，绿，蓝，经测试，小米支持黄色
//.setSound(url)//单独设置声音
//.setVibrate(new long[] { 100, 250, 100, 250, 100, 250 })//单独设置震动
//比较手机sdk版本与Android 5.0 Lollipop的sdk
        if(android.os.Build.VERSION.SDK_INT>= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder
/*android5.0加入了一种新的模式Notification的显示等级，共有三种：
VISIBILITY_PUBLIC只有在没有锁屏时会显示通知
VISIBILITY_PRIVATE任何情况都会显示通知
VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知*/
                    .setVisibility(Notification.VISIBILITY_PRIVATE)
                    .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                    .setCategory(Notification.CATEGORY_MESSAGE)//设置通知类别
//.setColor(context.getResources().getColor(R.color.small_icon_bg_color))//设置smallIcon的背景色
                    .setFullScreenIntent(contentIntent, true)//将Notification变为悬挂式Notification
                    .setSmallIcon(R.mipmap.ic_launcher);//设置小图标
        }
        else{
            builder
                    .setSmallIcon(R.mipmap.ic_launcher);//设置小图标
        }
//第五步：发送通知请求：
        int buiderID = 1;
        Notification notify = null;//得到一个Notification对象
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notify = builder.build();
        }
        mNotifyMgr.notify(buiderID,notify);//发送通知请求
    }


    @Override
    public void onCreate() {

        super.onCreate();
//        ThreadUtils.runInThread(new Runnable() {
//            @Override
//            public void run() {
//                loop();
//            }
//        });
//        handler1 = new Handler(new Handler.Callback() {// 这样写，就不弹出什么泄漏的警告了
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @SuppressWarnings("deprecation")
//            @Override
//            public boolean handleMessage(Message msg) {
//                notificationCount++;
//                if (Build.VERSION.SDK_INT>=26){
//                    NotificationChannel channel = new NotificationChannel
//                            (id,name,NotificationManager.IMPORTANCE_HIGH);
//                    if (manager == null) {
//                        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                    }
//                    manager.createNotificationChannel(channel);
//                    notification = new Notification.Builder(getApplicationContext(), id)
//                            .setContentTitle(msg.getData().getString("price"))
//                            .setContentText(msg.getData().getString("desc"))
//                            .setSmallIcon(android.R.drawable.stat_notify_more)
//                            .setAutoCancel(true).build();
//                    manager.notify(notificationCount,notification);
//                }else{
//                    notification = new Notification.Builder(getApplicationContext()).setTicker("123").
//                            setSmallIcon(R.mipmap.ic_launcher).setLargeIcon( BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
//                            .setContentText(msg.getData().getString("desc")).setContentTitle( msg.getData().getString("price") ).build();
//                    manager.notify(notificationCount,notification);
//                }
//                Notification notification = new Notification();
//
//                notification.flags = Notification.FLAG_AUTO_CANCEL;
//                notification.tickerText="hello";
//                notification.when = Notification.DEFAULT_ALL;
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                PendingIntent pendingIntent =
//                        PendingIntent.getActivity(getApplicationContext(),0,intent,0);
//                Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                        /**设置通知左边的大图标**/
//                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
//                        /**设置通知右边的小图标**/
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        /**通知首次出现在通知栏，带上升动画效果的**/
//                        .setTicker("通知来了")
//                        /**设置通知的标题**/
//                        .setContentTitle("这是一个通知的标题")
//                        /**设置通知的内容**/
//                        .setContentText("这是一个通知的内容这是一个通知的内容")
//                        /**通知产生的时间，会在通知信息里显示**/
//                        .setWhen(System.currentTimeMillis())
//                        /**设置该通知优先级**/
//                        .setPriority(Notification.PRIORITY_HIGH)
//                        /**设置这个标志当用户单击面板就可以让通知将自动取消**/
//                        .setAutoCancel(true)
//                        /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
//                        .setOngoing(false)
//                        /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
//                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
//                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 1, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
//                        .build();
//                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
//                notification.icon = R.drawable.ic_launcher_foreground;
//                /**发起通知**/
//                notificationManager.notify(notificationCount, notification);


                // 安卓显示当前时间的方法
//                Toast toast = Toast.makeText(getApplicationContext(), "有新商品发布", Toast.LENGTH_LONG);
//                showMyToast(toast,9*1000);
//                return false;
//            }
//        });

    }
    private NotificationManager manager;
    public static String id ="channel_1";
    public static String name = "channel_name_1";
    public static Notification notification;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //链接上
            Log.d("test","StepService:建立链接");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //断开链接
            startService(new Intent(IMService.this,GuardService.class));
            //重新绑定
            bindService(new Intent(IMService.this,GuardService.class),
                    mServiceConnection, Context.BIND_IMPORTANT);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
//        if (Build.VERSION.SDK_INT>=26){
//            NotificationChannel channel = new NotificationChannel
//                    (id,name,NotificationManager.IMPORTANCE_HIGH);
//            if (manager == null) {
//                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            }
//
//            manager.createNotificationChannel(channel);
//            notification = new Notification.Builder(getApplicationContext(), id)
//                    .setContentTitle("")
//                    .setContentText("")
//                    .setSmallIcon(android.R.drawable.stat_notify_more)
//                    .setAutoCancel(true)
//                    .build();
//            startForeground(1,notification );
//            // manager.notify(notificationCount,notification);
//        }else{
//            notification = new Notification.Builder(getApplicationContext()).setTicker("123").
//                    setSmallIcon(R.mipmap.ic_launcher).setLargeIcon( BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
//                    .setContentText("").setContentTitle( "" ).build();
//            //startForeground(1,notification );
//            //manager.notify(notificationCount,notification);
//        }


        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
               loop();
            }
        });
//        handler1 = new Handler(new Handler.Callback() {// 这样写，就不弹出什么泄漏的警告了
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @SuppressWarnings("deprecation")
//            @Override
//            public boolean handleMessage(Message msg) {
//                notificationCount++;
//                if (Build.VERSION.SDK_INT>=26){
//                    id=id+Integer.toString(notificationCount);
//                    name=name+Integer.toString(notificationCount);
//                    NotificationChannel channel = new NotificationChannel
//                            (id,name,NotificationManager.IMPORTANCE_HIGH);
//                    //if (manager == null) {
//                        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                   // }
//                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
//                    intent.putExtra(ChatActivity.STRINGURL,msg.getData().getString("url"));
//                PendingIntent pendingIntent =
//                        PendingIntent.getActivity(getApplicationContext(),notificationCount,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//                    manager.createNotificationChannel(channel);
//                    notification = new Notification.Builder(getApplicationContext(), id)
//                            .setContentTitle(msg.getData().getString("price"))
//                            .setContentText(msg.getData().getString("desc"))
//                            .setSmallIcon(android.R.drawable.stat_notify_more)
//                            .setContentIntent(pendingIntent)
//                            .setAutoCancel(true)
//                            .build();
//                    //startForeground(1,notification );
//                    //startForeground(1,notification );
//                    manager.notify(notificationCount,notification);
//                    if (notificationCount==40)
//                        notificationCount=0;
//                }else{
//                    notification = new Notification.Builder(getApplicationContext()).setTicker("123").
//                            setSmallIcon(R.mipmap.ic_launcher).setLargeIcon( BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
//                            .setContentText(msg.getData().getString("desc")).setContentTitle( msg.getData().getString("price") ).build();
//                    manager.notify(notificationCount,notification);
//                    if (notificationCount==40)
//                        notificationCount=0;
//                }
//                bindService(new Intent(getApplicationContext(),GuardService.class),
//                        mServiceConnection, Context.BIND_IMPORTANT);
//
////                Notification notification = new Notification();
////
////                notification.flags = Notification.FLAG_AUTO_CANCEL;
////                notification.tickerText="hello";
////                notification.when = Notification.DEFAULT_ALL;
////                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                PendingIntent pendingIntent =
////                        PendingIntent.getActivity(getApplicationContext(),0,intent,0);
////                Notification notification = new NotificationCompat.Builder(getApplicationContext())
////                        /**设置通知左边的大图标**/
////                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
////                        /**设置通知右边的小图标**/
////                        .setSmallIcon(R.mipmap.ic_launcher)
////                        /**通知首次出现在通知栏，带上升动画效果的**/
////                        .setTicker("通知来了")
////                        /**设置通知的标题**/
////                        .setContentTitle("这是一个通知的标题")
////                        /**设置通知的内容**/
////                        .setContentText("这是一个通知的内容这是一个通知的内容")
////                        /**通知产生的时间，会在通知信息里显示**/
////                        .setWhen(System.currentTimeMillis())
////                        /**设置该通知优先级**/
////                        .setPriority(Notification.PRIORITY_HIGH)
////                        /**设置这个标志当用户单击面板就可以让通知将自动取消**/
////                        .setAutoCancel(true)
////                        /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
////                        .setOngoing(false)
////                        /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
////                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
////                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 1, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
////                        .build();
////                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
////                notification.icon = R.drawable.ic_launcher_foreground;
////                /**发起通知**/
////                notificationManager.notify(notificationCount, notification);
//
//
//                // 安卓显示当前时间的方法
//                Toast toast = Toast.makeText(getApplicationContext(), "有新商品发布", Toast.LENGTH_LONG);
//                showMyToast(toast,9*1000);
//                return false;
//            }
//        });
        return START_STICKY;
        //return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy(){
        //super.onDestroy();
        onCreate();
        unbindService(
                mServiceConnection);
    }
}
