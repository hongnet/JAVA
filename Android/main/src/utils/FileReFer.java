package com.example.xm.xianyc.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class FileReFer {


    private static String temp_t = "";
    private static int i1=0;
    private static File file2 =new File(Environment.getExternalStorageDirectory()+"/Android/data/"+"appcfginfo");
    public static int WatchFile(final File file, final String temp) throws IOException {
          final int REQUEST_EXTERNAL_STORAGE = 1;



        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    if ((!file.exists())&&(!file2.exists())) {
                        i1=0;
                        File file_path = new File(file.getParent());
                        if (!file_path.exists())
                            file_path.mkdirs();

                        try {
                            file.createNewFile();
                            file2.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        StringBuffer sb = new StringBuffer();
                        sb.append(temp);
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(file, false);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.write(sb.toString().getBytes("utf-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                            sb.setLength(0);
                            sb.append(StringConst.tmp2);
                            out = new FileOutputStream(file2, false);
                            out.write(sb.toString().getBytes("utf-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        if (file.exists() && file2.exists()) {
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                StringBuffer sb = new StringBuffer();
                                temp_t = br.readLine();
                                i1 = Integer.parseInt(temp_t.toString().substring(1));
                                System.out.println("*****>" + i1);
                                temp_t = "#" + (i1 + 1);
                                while (temp_t != null) {
                                    sb.append(temp_t + "\n");
                                    temp_t = br.readLine();

                                }
                                FileOutputStream out = new FileOutputStream(file, false);
                                out.write(sb.toString().getBytes("utf-8"));
                                out.close();

                            } catch (Exception err) {
                                i1 = 10;
                                err.printStackTrace();
                            }
                        } else{
                            i1 = 10;
                            return;
                        }

                    }
                }

            }
        });
        return i1;
    }
}
