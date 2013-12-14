package com.mlq.diancan;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.mlq.diancan.R;
/**
* 继承自Activity，<br>
* 用来进行商店信息（店名）、服务员信息（员工号）的编辑
*/
public class Item3Activity extends Activity {  
//	private Resources resources;
	/**
	* android中存储数据的组件,此处存储了商店信息（店名）、服务员信息（员工号）<br>
	*/
	protected SharedPreferences sp;
	/**
	* 操作数据库的对象
	*/
	protected SqliteDbHelper dbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item3);
		dbHelper = new SqliteDbHelper(Constant.DbPath);

		// 返回
        Button back_btn = (Button)findViewById(R.id.item3_back_btn);
        
        back_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        // 确定
        final EditText waiter_num_ev = (EditText)findViewById(R.id.item3_edit_waiter_no);
        final Button ok_btn = (Button)findViewById(R.id.item3_ok_btn);
        sp = Item3Activity.this.getSharedPreferences("DianCan", Context.MODE_PRIVATE);
        String waiter_num_str = sp.getString("WAITER_NUM", "0");
        waiter_num_ev.setText(waiter_num_str);
        
        ok_btn.setOnClickListener(new View.OnClickListener() {
			
        	@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
        		String waiter_num_str = waiter_num_ev.getText().toString();
        		Editor editor = sp.edit();
		        editor.putString("WAITER_NUM", waiter_num_str);
		        editor.commit();
			}
		});

        // 更新菜谱
        TextView caidan_time_tv = (TextView)findViewById(R.id.item3_text_caidan_time);
    	Date date = new Date(dbHelper.getVersion(Constant.table_version, Constant.table_caidan));
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	String modify_time = formatter.format(date);
    	caidan_time_tv.setText(modify_time);

        final Button update_btn = (Button) findViewById(R.id.update_btn);
        update_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//使用本地的XML文件进行数据库菜单table的创建
				update_btn.setText(getResources().getString(R.string.updating));
				SqliteDbHelper dbHelper = new SqliteDbHelper(Constant.DbPath);
				dbHelper.XML2DB(new File(Constant.CaiDan_GuiZe_XmlPath), 
						Constant.table_caidan, Constant.table_guize, Constant.table_jiezhang);
				dbHelper.close();
				update_btn.setText(getResources().getString(R.string.update_file));
			}
		}); 
	}

}


