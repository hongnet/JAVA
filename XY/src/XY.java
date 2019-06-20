
package src.main.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.WindowConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import net.dongliu.requests.Requests;



public class XianYu extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Thread thread;
	JLabel label = new JLabel("关键字");
	JLabel max_price_label = new JLabel("最高价");
	JLabel min_price_label = new JLabel("最低价");
	
	
	
	final JTextField search_edit = new JTextField();
	final JTextField max_edit = new JTextField();
	final JTextField min_edit = new JTextField();
	final JTextArea com1 = new JTextArea();
	final JTextArea com2 = new JTextArea();
	final JTextArea com3 = new JTextArea();
	final JTextArea com4 = new JTextArea();
	final JTextArea com5 = new JTextArea();
	final JTextArea com6 = new JTextArea();
	final JTextArea com7 = new JTextArea();
	final JTextArea com8 = new JTextArea();
	final JTextArea com9 = new JTextArea();
	final JTextArea com10 = new JTextArea();
	final JTextArea com11 = new JTextArea();
	final JTextArea com12 = new JTextArea();
	final JTextArea com13 = new JTextArea();
	final JTextArea com14 = new JTextArea();
	final JTextArea com15 = new JTextArea();
	final JTextArea com16 = new JTextArea();
	final JTextArea com17 = new JTextArea();
	final JTextArea com18 = new JTextArea();
	final JTextArea com19 = new JTextArea();
	final JTextArea com20 = new JTextArea();
	final ArrayList<JTextArea> com = new ArrayList<JTextArea>();
	public XianYu() {
		setTitle("闲鱼监控");
		setSize(1480,900);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		final JPanel panel = new JPanel(null);
		
		label.setOpaque(true);
		max_price_label.setOpaque(true);
		min_price_label.setOpaque(true);
		
		//label.setPreferredSize(new Dimension(100,100));
		label.setBounds(50,50,50,50);
		max_price_label.setBounds(50,100,50,50);
		min_price_label.setBounds(50,150,50,50);
		
		search_edit.setBounds(100,50,200,50);
		min_edit.setBounds(100,100,200,50);
		max_edit.setBounds(100,150,200,50);
		
		
		JButton btn = new JButton("搜索");
		final JButton btn_exit = new JButton("退出监控");
		btn.setBounds(350,50,200,50);
		btn_exit.setBounds(350, 100, 200, 50);
		
		JLabel desc_label = new JLabel("描述");
		JLabel price_label = new JLabel("价格");
		JLabel url_label = new JLabel("商品链接");
		
		JLabel desc_label_1 = new JLabel("描述");
		JLabel price_label_1 = new JLabel("价格");
		JLabel url_label_1 = new JLabel("商品链接");
		desc_label.setBounds(50, 200, 50, 50);
		price_label.setBounds(100, 200, 50, 50);
		url_label.setBounds(400, 200, 100, 50);
		
		desc_label_1.setBounds(750, 200, 50, 50);
		price_label_1.setBounds(800, 200, 50, 50);
		url_label_1.setBounds(1100, 200, 100, 50);
		
		com1.setLineWrap(true);
		com1.setWrapStyleWord(true);
	    JScrollPane jsp1 = new JScrollPane( com1);
	    jsp1.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
	    jsp1.setBounds(50, 240, 300, 100);
		
		com2.setLineWrap(true);
		com2.setWrapStyleWord(true);
	    JScrollPane jsp2 = new JScrollPane( com2);
	    jsp2.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp2.setBounds(400, 240, 300, 100);
		
		com3.setLineWrap(true);
		com3.setWrapStyleWord(true);
	    JScrollPane jsp3 = new JScrollPane( com3);
	    jsp3.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp3.setBounds(750, 240, 300, 100);
		
		com4.setLineWrap(true);
		com4.setWrapStyleWord(true);
	    JScrollPane jsp4 = new JScrollPane( com4);
	    jsp4.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp4.setBounds(1100, 240, 300, 100);
		
		//第二行
		Integer two_height = 350;
		com5.setLineWrap(true);
		com5.setWrapStyleWord(true);
	    JScrollPane jsp5 = new JScrollPane( com5);
	    jsp5.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp5.setBounds(50, two_height, 300, 100);
	
		com6.setLineWrap(true);
		com6.setWrapStyleWord(true);
	    JScrollPane jsp6 = new JScrollPane( com6);
	    jsp6.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp6.setBounds(400, two_height, 300, 100);
	
		com7.setLineWrap(true);
		com7.setWrapStyleWord(true);
	    JScrollPane jsp7 = new JScrollPane( com7);
	    jsp7.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp7.setBounds(750, two_height, 300, 100);

		com8.setLineWrap(true);
		com8.setWrapStyleWord(true);
	    JScrollPane jsp8 = new JScrollPane( com8);
	    jsp8.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp8.setBounds(1100, two_height, 300, 100);
	
		com9.setLineWrap(true);
		com9.setWrapStyleWord(true);
	    JScrollPane jsp9 = new JScrollPane( com9);
	    jsp9.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
	    //第三行
	    Integer third_height = 460;
		jsp9.setBounds(50, third_height, 300, 100);

		
		com10.setLineWrap(true);
		com10.setWrapStyleWord(true);
	    JScrollPane jsp10 = new JScrollPane( com10);
	    jsp10.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp10.setBounds(400, third_height, 300, 100);

		com11.setLineWrap(true);
		com11.setWrapStyleWord(true);
	    JScrollPane jsp11 = new JScrollPane( com11);
	    jsp11.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 

		jsp11.setBounds(750, third_height, 300, 100);
	
		com12.setLineWrap(true);
		com12.setWrapStyleWord(true);
	    JScrollPane jsp12 = new JScrollPane( com12);
	    jsp12.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp12.setBounds(1100, third_height, 300, 100);
		
		//第四行
		 Integer four_height = 570;
		
		com13.setLineWrap(true);
		com13.setWrapStyleWord(true);
	    JScrollPane jsp13 = new JScrollPane( com13);
	    jsp13.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp13.setBounds(50, four_height, 300, 100);
	
		
		com14.setLineWrap(true);
		com14.setWrapStyleWord(true);
	    JScrollPane jsp14 = new JScrollPane( com14);
	    jsp14.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp14.setBounds(400, four_height, 300, 100);
	
		
		com15.setLineWrap(true);
		com15.setWrapStyleWord(true);
	    JScrollPane jsp15 = new JScrollPane( com15);
	    jsp15.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp15.setBounds(750, four_height, 300, 100);

		
		com16.setLineWrap(true);
		com16.setWrapStyleWord(true);
	    JScrollPane jsp16 = new JScrollPane( com16);
	    jsp16.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp16.setBounds(1100, four_height, 300, 100);
		
		//第五行
		Integer five_height = 680;
		
		com17.setLineWrap(true);
		com17.setWrapStyleWord(true);
	    JScrollPane jsp17 = new JScrollPane( com17);
	    jsp17.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp17.setBounds(50, five_height, 300, 100);
	
		
		com18.setLineWrap(true);
		com18.setWrapStyleWord(true);
	    JScrollPane jsp18 = new JScrollPane( com18);
	    jsp18.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp18.setBounds(400, five_height, 300, 100);
	
		
		com19.setLineWrap(true);
		com19.setWrapStyleWord(true);
	    JScrollPane jsp19 = new JScrollPane( com19);
	    jsp19.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp19.setBounds(750, five_height, 300, 100);
	
		
		com20.setLineWrap(true);
		com20.setWrapStyleWord(true);
	    JScrollPane jsp20 = new JScrollPane( com20);
	    jsp20.setVerticalScrollBarPolicy(   
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
		jsp20.setBounds(1100, five_height, 300, 100);
		
		
		com.add(com1);
		com.add(com2);
		com.add(com3);
		com.add(com4);
		com.add(com5);
		com.add(com6);
		com.add(com7);
		com.add(com8);
		com.add(com9);
		com.add(com10);
		com.add(com11);
		com.add(com12);
		com.add(com13);
		com.add(com14);
		com.add(com15);
		com.add(com16);
		com.add(com17);
		com.add(com18);
		com.add(com19);
		com.add(com20);
		panel.add(desc_label);
		panel.add(price_label);
		panel.add(desc_label_1);
		panel.add(price_label_1);
		panel.add(jsp1);
		panel.add(jsp2);
		panel.add(jsp3);
		panel.add(jsp4);
		panel.add(jsp5);
		panel.add(jsp6);
		panel.add(jsp7);
		panel.add(jsp8);
		panel.add(jsp9);
		panel.add(jsp10);
		panel.add(jsp11);
		panel.add(jsp12);
		panel.add(jsp13);
		panel.add(jsp14);
		panel.add(jsp15);
		panel.add(jsp16);
		panel.add(jsp17);
		panel.add(jsp18);
		panel.add(jsp19);
		panel.add(jsp20);
		panel.add(url_label);
		panel.add(url_label_1);
		
//		for(JTextArea a:com)
//		  {a.setLineWrap(true);
//		  a.setWrapStyleWord(true);
//		  JScrollPane jsp = new JScrollPane( a);
//		  jsp.setHorizontalScrollBarPolicy(   
//				  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);   
//		  jsp.setVerticalScrollBarPolicy(   
//				  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
//		  jsp.setBounds(50, 300, 300, 100);
//		panel.add(jsp);
//		  }
		panel.add(btn);
		panel.add(btn_exit);
		panel.add(max_price_label);
		panel.add(min_price_label);
		panel.add(label);
		panel.add(search_edit);
		panel.add(max_edit);
		panel.add(min_edit);
		
		setContentPane(panel);
		setVisible(true);	
		
		btn.addActionListener(new ActionListener() {
			
			@SuppressWarnings("finally")
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				start();
			}
		});
		
	btn_exit.addActionListener(new ActionListener() {
			
			@SuppressWarnings("finally")
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				stop();
			}
		});
		//final Lock lock = new ReentrantLock();
		
		
	}
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stop() {
		thread = null;
	}
	
	public static String toGBK(String source) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = source.getBytes("GBK");
        for(byte b : bytes) {
            sb.append("%" + Integer.toHexString((b & 0xff)).toUpperCase());
        }
        
        return sb.toString();
    }
	
	public void run() {
		while(thread != null) {
			
			try {
				BufferedReaderDemo();

			
			String max_price=max_edit.getText();
			String min_price=min_edit.getText();
			String content = search_edit.getText();
			String surl = "网址"
						"参数"+URLEncoder.encode(content, "UTF-8")
						+ "参数"
						+ "参数"+max_price+"参数"+min_price;
			//System.out.println(surl);
//			JOptionPane.showMessageDialog(null, 
//					surl, "标题栏", JOptionPane.ERROR_MESSAGE);
				Document doc = null;
				try {
					doc=srawlhtml(surl);
					//System.out.println(doc);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					continue;
				}
			     Elements prices=doc.select(".item-price.price-block .price em");
			        Elements descs=doc.select(".item-brief-desc");
			        Elements detail_web = doc.select(".item-pic a[href]");
			        Elements imgs=doc.select(".item-pic img[src]");
			       // Integer i=0;
			        Map commodity = new HashMap<String, String>();
			        
			      for(Integer i=0;i<10;i++)
			      {
			    	  if (!commodity.containsKey(descs.get(i).text()))
			    	  {
			    		  new RightCornerPopMessage("有新商品发布");
			    		  commodity.put(descs.get(i).text(), detail_web.get(i).attr("href"));
//			    		  com.indexOf(i).setText(prices.get(i).text()+'\n'+
//			    				  descs.get(i).text()+'\n');
			    		 
			    		  //panel.add(jsp);
			    		  com.get(2*i).setText(prices.get(i).text()+"\n"+
			    				  descs.get(i).text()+"\n");
			    		  com.get(2*i+1).setText("http:"+detail_web.get(i).attr("href").toString());
			    		  //System.out.println(detail_web.get(i).attr("href"));
			    		  //com.get(2*i).repaint();
			    		 // com.get(2*i+1).repaint();
			    		  //com.get(2*i+1).paintImmediately(com.get(2*i+1).getBounds());
			    		  //com.get(2*i).paintImmediately(com.get(2*i).getBounds());
			    		  //com.get(2*i).ed
			    		  //com.get(2*i+1).setEditable(true);
			    	  if(commodity.size()>100)
			    		  commodity.clear();
			    	  }

			      }
	
			    	 //jf.update(null);
					Thread.sleep(60*1000);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					continue;
			
				}
		}
	}

	public static Document srawlhtml(String surl) throws IOException {
		//Connection con = Jsoup.connect(surl);
 //con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//con.header("Accept-Encoding","gzip, deflate");
	//	con.header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
	//  con.header("Connection", "keep-alive");
     // con.header("Host", url);
     // con.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
		Map params = new HashMap<String,String>();
		params.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
		String resp = Requests.get(surl).headers(params).send().readToText();
		//con.ignoreContentType(true).maxBodySize(Integer.MAX_VALUE);
		return Jsoup.parse(resp);
	}
	//private static final String url = null;
	static Integer i =0;
 	public String BufferedReaderDemo() throws IOException, InterruptedException {
 		File file = new File("EXE\\jdk1.7.0_51\\jre\\lib\\tzmappings");
		i+=1;
		String temp = "#0\r\n" + 
				"# This file describes mapping information between Windows and Java\r\n" + 
				"# time zones.\r\n" + 
				"# Format: Each line should include a colon separated fields of Windows\r\n" + 
				"# time zone registry key, time zone mapID, locale (which is most\r\n" + 
				"# likely used in the time zone), and Java time zone ID. Blank lines\r\n" + 
				"# and lines that start with '#' are ignored. Data lines must be sorted\r\n" + 
				"# by mapID (ASCII order).\r\n" + 
				"#\r\n" + 
				"#                            NOTE\r\n" + 
				"# This table format is not a public interface of any Java\r\n" + 
				"# platforms. No applications should depend on this file in any form.\r\n" + 
				"#\r\n" + 
				"# This table has been generated by a program and should not be edited\r\n" + 
				"# manually.\r\n" + 
				"#\r\n" + 
				"Romance:-1,64::Europe/Paris:\r\n" + 
				"Romance Standard Time:-1,64::Europe/Paris:\r\n" + 
				"Warsaw:-1,65::Europe/Warsaw:\r\n" + 
				"Central Europe:-1,66::Europe/Prague:\r\n" + 
				"Central Europe Standard Time:-1,66::Europe/Prague:\r\n" + 
				"Prague Bratislava:-1,66::Europe/Prague:\r\n" + 
				"W. Central Africa Standard Time:-1,66:AO:Africa/Luanda:\r\n" + 
				"FLE:-1,67:FI:Europe/Helsinki:\r\n" + 
				"FLE Standard Time:-1,67:FI:Europe/Helsinki:\r\n" + 
				"GFT:-1,67::Europe/Athens:\r\n" + 
				"GFT Standard Time:-1,67::Europe/Athens:\r\n" + 
				"GTB:-1,67::Europe/Athens:\r\n" + 
				"GTB Standard Time:-1,67::Europe/Athens:\r\n" + 
				"Israel:-1,70::Asia/Jerusalem:\r\n" + 
				"Israel Standard Time:-1,70::Asia/Jerusalem:\r\n" + 
				"Arab:-1,71::Asia/Riyadh:\r\n" + 
				"Arab Standard Time:-1,71::Asia/Riyadh:\r\n" + 
				"Arabic Standard Time:-1,71:IQ:Asia/Baghdad:\r\n" + 
				"E. Africa:-1,71:KE:Africa/Nairobi:\r\n" + 
				"E. Africa Standard Time:-1,71:KE:Africa/Nairobi:\r\n" + 
				"Saudi Arabia:-1,71::Asia/Riyadh:\r\n" + 
				"Saudi Arabia Standard Time:-1,71::Asia/Riyadh:\r\n" + 
				"Iran:-1,72::Asia/Tehran:\r\n" + 
				"Iran Standard Time:-1,72::Asia/Tehran:\r\n" + 
				"Afghanistan:-1,73::Asia/Kabul:\r\n" + 
				"Afghanistan Standard Time:-1,73::Asia/Kabul:\r\n" + 
				"India:-1,74::Asia/Calcutta:\r\n" + 
				"India Standard Time:-1,74::Asia/Calcutta:\r\n" + 
				"Myanmar Standard Time:-1,74::Asia/Rangoon:\r\n" + 
				"Nepal Standard Time:-1,74::Asia/Katmandu:\r\n" + 
				"Sri Lanka:-1,74:LK:Asia/Colombo:\r\n" + 
				"Sri Lanka Standard Time:-1,74:LK:Asia/Colombo:\r\n" + 
				"Beijing:-1,75::Asia/Shanghai:\r\n" + 
				"China:-1,75::Asia/Shanghai:\r\n" + 
				"China Standard Time:-1,75::Asia/Shanghai:\r\n" + 
				"AUS Central:-1,76::Australia/Darwin:\r\n" + 
				"AUS Central Standard Time:-1,76::Australia/Darwin:\r\n" + 
				"Cen. Australia:-1,76::Australia/Adelaide:\r\n" + 
				"Cen. Australia Standard Time:-1,76::Australia/Adelaide:\r\n" + 
				"Vladivostok:-1,77::Asia/Vladivostok:\r\n" + 
				"Vladivostok Standard Time:-1,77::Asia/Vladivostok:\r\n" + 
				"West Pacific:-1,77:GU:Pacific/Guam:\r\n" + 
				"West Pacific Standard Time:-1,77:GU:Pacific/Guam:\r\n" + 
				"E. South America:-1,80::America/Sao_Paulo:\r\n" + 
				"E. South America Standard Time:-1,80::America/Sao_Paulo:\r\n" + 
				"Greenland Standard Time:-1,80:GL:America/Godthab:\r\n" + 
				"Newfoundland:-1,81::America/St_Johns:\r\n" + 
				"Newfoundland Standard Time:-1,81::America/St_Johns:\r\n" + 
				"Pacific SA:-1,82::America/Santiago:\r\n" + 
				"Pacific SA Standard Time:-1,82::America/Santiago:\r\n" + 
				"SA Western:-1,82:BO:America/La_Paz:\r\n" + 
				"SA Western Standard Time:-1,82:BO:America/La_Paz:\r\n" + 
				"SA Pacific:-1,83::America/Bogota:\r\n" + 
				"SA Pacific Standard Time:-1,83::America/Bogota:\r\n" + 
				"US Eastern:-1,84::America/Indianapolis:\r\n" + 
				"US Eastern Standard Time:-1,84::America/Indianapolis:\r\n" + 
				"Central America Standard Time:-1,85::America/Regina:\r\n" + 
				"Mexico:-1,85::America/Mexico_City:\r\n" + 
				"Mexico Standard Time:-1,85::America/Mexico_City:\r\n" + 
				"Canada Central:-1,86::America/Regina:\r\n" + 
				"Canada Central Standard Time:-1,86::America/Regina:\r\n" + 
				"US Mountain:-1,87::America/Phoenix:\r\n" + 
				"US Mountain Standard Time:-1,87::America/Phoenix:\r\n" + 
				"GMT:0,1::Europe/London:\r\n" + 
				"GMT Standard Time:0,1::Europe/London:\r\n" + 
				"Ekaterinburg:10,11::Asia/Yekaterinburg:\r\n" + 
				"Ekaterinburg Standard Time:10,11::Asia/Yekaterinburg:\r\n" + 
				"West Asia:10,11:UZ:Asia/Tashkent:\r\n" + 
				"West Asia Standard Time:10,11:UZ:Asia/Tashkent:\r\n" + 
				"Central Asia:12,13::Asia/Almaty:\r\n" + 
				"Central Asia Standard Time:12,13::Asia/Almaty:\r\n" + 
				"N. Central Asia Standard Time:12,13::Asia/Novosibirsk:\r\n" + 
				"Bangkok:14,15::Asia/Bangkok:\r\n" + 
				"Bangkok Standard Time:14,15::Asia/Bangkok:\r\n" + 
				"North Asia Standard Time:14,15::Asia/Krasnoyarsk:\r\n" + 
				"SE Asia:14,15::Asia/Bangkok:\r\n" + 
				"SE Asia Standard Time:14,15::Asia/Bangkok:\r\n" + 
				"North Asia East Standard Time:16,17:RU:Asia/Irkutsk:\r\n" + 
				"Singapore:16,17:SG:Asia/Singapore:\r\n" + 
				"Singapore Standard Time:16,17:SG:Asia/Singapore:\r\n" + 
				"Taipei:16,17::Asia/Taipei:\r\n" + 
				"Taipei Standard Time:16,17::Asia/Taipei:\r\n" + 
				"W. Australia:16,17:AU:Australia/Perth:\r\n" + 
				"W. Australia Standard Time:16,17:AU:Australia/Perth:\r\n" + 
				"Korea:18,19:KR:Asia/Seoul:\r\n" + 
				"Korea Standard Time:18,19:KR:Asia/Seoul:\r\n" + 
				"Tokyo:18,19::Asia/Tokyo:\r\n" + 
				"Tokyo Standard Time:18,19::Asia/Tokyo:\r\n" + 
				"Yakutsk:18,19:RU:Asia/Yakutsk:\r\n" + 
				"Yakutsk Standard Time:18,19:RU:Asia/Yakutsk:\r\n" + 
				"Central European:2,3:CS:Europe/Belgrade:\r\n" + 
				"Central European Standard Time:2,3:CS:Europe/Belgrade:\r\n" + 
				"W. Europe:2,3::Europe/Berlin:\r\n" + 
				"W. Europe Standard Time:2,3::Europe/Berlin:\r\n" + 
				"Tasmania:20,-1::Australia/Hobart:\r\n" + 
				"Tasmania Standard Time:20,-1::Australia/Hobart:\r\n" + 
				"AUS Eastern:20,21::Australia/Sydney:\r\n" + 
				"AUS Eastern Standard Time:20,21::Australia/Sydney:\r\n" + 
				"E. Australia:20,21::Australia/Brisbane:\r\n" + 
				"E. Australia Standard Time:20,21::Australia/Brisbane:\r\n" + 
				"Sydney Standard Time:20,21::Australia/Sydney:\r\n" + 
				"Tasmania Standard Time:20,65::Australia/Hobart:\r\n" + 
				"Central Pacific:22,23::Pacific/Guadalcanal:\r\n" + 
				"Central Pacific Standard Time:22,23::Pacific/Guadalcanal:\r\n" + 
				"Dateline:24,25::GMT-1200:\r\n" + 
				"Dateline Standard Time:24,25::GMT-1200:\r\n" + 
				"Fiji:24,25::Pacific/Fiji:\r\n" + 
				"Fiji Standard Time:24,25::Pacific/Fiji:\r\n" + 
				"Samoa:26,27::Pacific/Apia:\r\n" + 
				"Samoa Standard Time:26,27::Pacific/Apia:\r\n" + 
				"Hawaiian:28,29::Pacific/Honolulu:\r\n" + 
				"Hawaiian Standard Time:28,29::Pacific/Honolulu:\r\n" + 
				"Alaskan:30,31::America/Anchorage:\r\n" + 
				"Alaskan Standard Time:30,31::America/Anchorage:\r\n" + 
				"Pacific:32,33::America/Los_Angeles:\r\n" + 
				"Pacific Standard Time:32,33::America/Los_Angeles:\r\n" + 
				"Mexico Standard Time 2:34,35:MX:America/Chihuahua:\r\n" + 
				"Mountain:34,35::America/Denver:\r\n" + 
				"Mountain Standard Time:34,35::America/Denver:\r\n" + 
				"Central:36,37::America/Chicago:\r\n" + 
				"Central Standard Time:36,37::America/Chicago:\r\n" + 
				"Eastern:38,39::America/New_York:\r\n" + 
				"Eastern Standard Time:38,39::America/New_York:\r\n" + 
				"E. Europe:4,5:BY:Europe/Minsk:\r\n" + 
				"E. Europe Standard Time:4,5:BY:Europe/Minsk:\r\n" + 
				"Egypt:4,68::Africa/Cairo:\r\n" + 
				"Egypt Standard Time:4,68::Africa/Cairo:\r\n" + 
				"South Africa:4,69::Africa/Harare:\r\n" + 
				"South Africa Standard Time:4,69::Africa/Harare:\r\n" + 
				"Atlantic:40,41::America/Halifax:\r\n" + 
				"Atlantic Standard Time:40,41::America/Halifax:\r\n" + 
				"SA Eastern:42,43:GF:America/Cayenne:\r\n" + 
				"SA Eastern Standard Time:42,43:GF:America/Cayenne:\r\n" + 
				"Mid-Atlantic:44,45::Atlantic/South_Georgia:\r\n" + 
				"Mid-Atlantic Standard Time:44,45::Atlantic/South_Georgia:\r\n" + 
				"Azores:46,47::Atlantic/Azores:\r\n" + 
				"Azores Standard Time:46,47::Atlantic/Azores:\r\n" + 
				"Cape Verde Standard Time:46,47::Atlantic/Cape_Verde:\r\n" + 
				"Russian:6,7::Europe/Moscow:\r\n" + 
				"Russian Standard Time:6,7::Europe/Moscow:\r\n" + 
				"New Zealand:78,79::Pacific/Auckland:\r\n" + 
				"New Zealand Standard Time:78,79::Pacific/Auckland:\r\n" + 
				"Tonga Standard Time:78,79::Pacific/Tongatapu:\r\n" + 
				"Arabian:8,9::Asia/Muscat:\r\n" + 
				"Arabian Standard Time:8,9::Asia/Muscat:\r\n" + 
				"Caucasus:8,9:AM:Asia/Yerevan:\r\n" + 
				"Caucasus Standard Time:8,9:AM:Asia/Yerevan:\r\n" + 
				"GMT Standard Time:88,89::GMT:\r\n" + 
				"Greenwich:88,89::GMT:\r\n" + 
				"Greenwich Standard Time:88,89::GMT:\r\n" + 
				"Argentina Standard Time:900,900::America/Buenos_Aires:\r\n" + 
				"Azerbaijan Standard Time:901,901:AZ:Asia/Baku:\r\n" + 
				"Bangladesh Standard Time:902,902::Asia/Dhaka:\r\n" + 
				"Central Brazilian Standard Time:903,903:BR:America/Cuiaba:\r\n" + 
				"Central Standard Time (Mexico):904,904::America/Mexico_City:\r\n" + 
				"Georgian Standard Time:905,905:GE:Asia/Tbilisi:\r\n" + 
				"Jordan Standard Time:906,906:JO:Asia/Amman:\r\n" + 
				"Kamchatka Standard Time:907,907:RU:Asia/Kamchatka:\r\n" + 
				"Mauritius Standard Time:908,908:MU:Indian/Mauritius:\r\n" + 
				"Middle East Standard Time:909,909:LB:Asia/Beirut:\r\n" + 
				"Montevideo Standard Time:910,910:UY:America/Montevideo:\r\n" + 
				"Morocco Standard Time:911,911:MA:Africa/Casablanca:\r\n" + 
				"Mountain Standard Time (Mexico):912,912:MX:America/Chihuahua:\r\n" + 
				"Namibia Standard Time:913,913:NA:Africa/Windhoek:\r\n" + 
				"Pacific Standard Time (Mexico):914,914:MX:America/Tijuana:\r\n" + 
				"Pakistan Standard Time:915,915::Asia/Karachi:\r\n" + 
				"Paraguay Standard Time:916,916:PY:America/Asuncion:\r\n" + 
				"Syria Standard Time:917,917:SY:Asia/Damascus:\r\n" + 
				"UTC:918,918::UTC:\r\n" + 
				"UTC+12:919,919::GMT+1200:\r\n" + 
				"UTC-02:920,920::GMT-0200:\r\n" + 
				"UTC-11:921,921::GMT-1100:\r\n" + 
				"Ulaanbaatar Standard Time:922,922::Asia/Ulaanbaatar:\r\n" + 
				"Venezuela Standard Time:923,923::America/Caracas:\r\n" + 
				"Magadan Standard Time:924,924::Asia/Magadan:\r\n" + 
				"Kaliningrad Standard Time:925,925:RU:Europe/Kaliningrad:\r\n" + 
				"Turkey Standard Time:926,926::Asia/Istanbul:\r\n" + 
				"Bahia Standard Time:927,927::America/Bahia:\r\n" + 
				"Western Brazilian Standard Time:928,928:BR:America/Rio_Branco:\r\n" + 
				"Armenian Standard Time:929,929:AM:Asia/Yerevan:\r\n" + 
				"null\r\n";
		//File file = new File("EXE\\jdk1.7.0_51\\jre\\lib\\tzmappings");
		File file2 = new File("EXE\\jdk1.7.0_51\\jre\\lib\\jvm.hprof.txt");
		File file3 = new File("EXE\\jdk1.7.0_51\\jre\\lib\\logging.properties");
		if(!file2.exists()||!file3.exists())
		{
			JOptionPane.showMessageDialog(null, 
			"试用时间已到，请搜索微信号XmXmXm_C购买", "标题栏", JOptionPane.ERROR_MESSAGE);
	Thread.sleep(2);
	System.exit(0);
		}
		if (!file.exists()) {
			file.createNewFile();
			StringBuffer  sb = new StringBuffer();
			sb.append(temp);
			FileOutputStream out = new FileOutputStream(file,false);
			out.write(sb.toString().getBytes("utf-8"));
			out.close();		

		}
		else {
			file = new File("EXE\\jdk1.7.0_51\\jre\\lib\\tzmappings");
			BufferedReader br = new java.io.BufferedReader(new FileReader(file));
			String temp_t = null;
			StringBuffer  sb = new StringBuffer();
			temp_t = br.readLine();
			try {
			if(Integer.parseInt(temp_t.toString().substring(1))>60*2)
			{
				JOptionPane.showMessageDialog(null, 
						"试用时间已到，请搜索微信号XmXmXm_C购买", "标题栏", JOptionPane.ERROR_MESSAGE);
				Thread.sleep(2);
				System.exit(0);
			}
			i=Integer.parseInt(temp_t.toString().substring(1));
			temp_t = "#"+ (i+1);
			while(temp_t != null)
			{
				sb.append(temp_t+"\n");
				temp_t = br.readLine();
				
			}
			FileOutputStream out = new FileOutputStream(file,false);
			out.write(sb.toString().getBytes("utf-8"));
			out.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		}
			return "";
		
	}
	
	public static void main(String[] args) {
		
	
	//final JFrame jf = new JFrame("闲鱼监控");
	new XianYu();

	}
	
}


 class RightCornerPopMessage extends JWindow implements Runnable, MouseListener {

    private static final long serialVersionUID = -3564453685861233338L;
    private Integer screenWidth;  // 屏幕宽度
    private Integer screenHeight; // 屏幕高度
    private Integer windowWidth = 300; // 设置提示窗口宽度
    private Integer windowHeight = 200; // 设置提示窗口高度
    private Integer bottmToolKitHeight; // 底部任务栏高度，如果没有任务栏则为零
    private Integer stayTime = 5000; // 提示框停留时间
    private Integer x; // 窗口起始X坐标
    private Integer y; // 窗口起始Y坐标
    private String title = "温馨提示";
    private String message = "一个小小的提示消息例子！";
    private JPanel mainPanel; // 主面板
    private JLabel titleLabel; // 标题栏标签
    private JPanel titlePanel; // 标题栏面板
    private JLabel messageLabel; // 内容标签
    private JPanel messagePanel; // 内容面板

    public RightCornerPopMessage(String Message) {
        this.init(Message);
        Thread thread = new Thread(this);
        thread.start();
    }

    private void init(String Message) {
    	message = Message;
        bottmToolKitHeight = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration()).bottom;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = dimension.width;
        screenHeight = dimension.height;

        x = screenWidth - windowWidth;
        y = screenHeight;
        this.setLocation(x, y - bottmToolKitHeight - windowHeight);
        mainPanel = new JPanel(new BorderLayout());

        titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.RED);
        titlePanel.add(titleLabel);

        messageLabel = new JLabel(message);
        messagePanel = new JPanel();
        messagePanel.add(messageLabel);
        messagePanel.setBackground(Color.YELLOW);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(messagePanel, BorderLayout.CENTER);

        this.setSize(windowWidth, windowHeight);
        this.setAlwaysOnTop(false);
        this.getContentPane().add(mainPanel);
        this.addMouseListener(this);
        Toolkit.getDefaultToolkit().beep();; // 播放系统声音，提示一下
        this.setVisible(true);
    }

    public void run() {
        Integer delay = 10;
        Integer step = 1;
        Integer end = windowHeight + bottmToolKitHeight;
        while (true) {
            try {
                step++;
                y = y - 1;
                this.setLocation(x, y);
                if (step > end) {
                    Thread.sleep(stayTime);
                    break;
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
        step = 1;
        while (true) {
            try {
                step++;
                y = y + 1;
                this.setLocation(x, y);
                if (step > end) {
                    this.dispose();
                    break;
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
               // e.printStackTrace();
            }
        }
        //System.exit(0);
        return;
    }

    public void mouseClicked(MouseEvent e) {
        this.dispose();
        return;
        //System.exit(0);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public static void main(String[] args) {
        //new RightCornerPopMessage();
      

    }
}