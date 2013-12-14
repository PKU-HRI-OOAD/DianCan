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
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

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
import android.database.sqlite.SQLiteException;
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
public class Item2Activity extends Activity {  
//	private Resources resources;
	private final String table_name = Constant.table_jiezhang;
	/**
	* android中存储数据的组件,此处存储了商店信息（店名）、服务员信息（员工号）<br>
	*/
	protected SharedPreferences sp;
	/**
	* 操作数据库的对象
	*/
	protected SqliteDbHelper dbHelper;
	/**
	* 列表视图组件，用于显示已点菜单信息
	*/
	protected ListView list;
	protected TextView sum_cost;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item2);
//        resources = getResources();
        Button back_btn = (Button)findViewById(R.id.item2_back_btn);
        list = (ListView)findViewById(R.id.food_list_yidian);
        sum_cost = (TextView)findViewById(R.id.yidian_sum_text);
        sp = Item2Activity.this.getSharedPreferences("DianCan", Context.MODE_PRIVATE);
        String waiter_num_str = sp.getString("WAITER_NUM", "0");
		dbHelper = new SqliteDbHelper(Constant.DbPath);
        
        //设置返回按钮
        back_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        // 提交按钮
        Button submit_btn = (Button)findViewById(R.id.submit_btn_jiezhang);
  		submit_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dbHelper.XML2DB(new File(Constant.JieZhang_XmlPath), 
						Constant.table_caidan, Constant.table_guize, Constant.table_jiezhang);
				try {
					ArrayList <HashMap <String, Object>> mAppList = dbHelper.selectAll(table_name);
					//更新ListView组件信息
					inflateList(mAppList, list);
				}catch(SQLiteException  se){
					System.out.println("no such table");
					//执行DDL创建数据表
					dbHelper.creatDbTable(table_name);
				}
			}
		});
        
	}
	/**
	* 更新ListView组件,以ListButtonAdapter3类为适配器<br>
	* @param mAppList 数据源
	* @param list 显示数据的ListView组件
	*/
    protected void inflateList(ArrayList <HashMap <String, Object>> mAppList, ListView list)
	{
		// 生成适配器的Item和动态数组对应的元素  
		ListButtonAdapterC adapter = new ListButtonAdapterC( 
				this , 
				Constant.table_yidian,
				dbHelper,
				mAppList, //数据源  
				R.layout.item2_list_item, 
				new String[]{"", "food_name", "food_cost", "food_count"} , 
				new int[]{R.id.food_num2, R.id.food_name2, R.id.food_cost2, R.id.food_count2} 
        );
		list.setAdapter(adapter);
		// 计算总的价格，并进行显示更新
		int sum = 0;
		for(HashMap <String, Object> map:mAppList){
            String food_cost = ( String ) map.get ("food_cost");
            String food_count = ( String ) map.get ("food_count");
            sum += Integer.valueOf(food_cost) * Integer.valueOf(food_count);
		}
		sum_cost.setText(String.valueOf(sum) + this.getResources().getString(R.string.money_unit));
	}
    /**
	* 退出页面时关闭数据库对象
	*/
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//退出程序时关闭SQLiteDatabase
		dbHelper.close();
		System.out.println("onPause yidian");
	}
}


