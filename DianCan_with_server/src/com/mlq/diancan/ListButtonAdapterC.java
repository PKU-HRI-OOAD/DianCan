package com.mlq.diancan;

import java.util.ArrayList;
import java.util.HashMap;

import com.mlq.diancan.R.drawable;
import com.mlq.diancan.R.id;
import com.mlq.diancan.R.layout;
import com.mlq.diancan.R.string;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
* 继承自BaseAdapter，<br>
* 利用数据源等信息对UI布局进行初始化，<br>
* 基本和ListButtonAdapter1相同，只是作为Item1_YiDianActivity中的数据适配器，item中的控件有些不同
* @see com.mlq.diancan.ListButtonAdapterA
* @see com.mlq.diancan.Item1_YiDianActivity
*/
public class ListButtonAdapterC extends BaseAdapter { 	
    private ArrayList < HashMap < String , Object > > mAppList; 
    private int layoutResource;
    private LayoutInflater mInflater; 
    private Context ctx; 
    private String table_name;
    private String [ ] keyString; 
    private int [ ] valueViewID; 
    private buttonViewHolder holder;
    private Cursor cursor;
    private SqliteDbHelper dbHelper;  
    private final String ex = new String(" X  ");
    
    private class buttonViewHolder { 
        TextView food_num; 
        TextView food_name;
        TextView food_cost;
        TextView food_count;
    } 
    
    public ListButtonAdapterC( Context c, String mtable_name, SqliteDbHelper mdbHelper, 
    		ArrayList < HashMap < String , Object > > appList,
    		int resource, String [ ] key , int [ ] ID) { 
        ctx = c; 
        table_name = mtable_name;
        mAppList = appList; 
        dbHelper = mdbHelper;
        layoutResource = resource;
        mInflater = ( LayoutInflater) ctx. getSystemService( Context . LAYOUT_INFLATER_SERVICE) ; 
        keyString = new String [ key . length ] ; 
        valueViewID = new int [ ID. length ] ; 
        System . arraycopy ( key , 0, keyString, 0, key.length ) ; 
        System . arraycopy ( ID, 0, valueViewID, 0, ID.length ) ; 
    } 
    
    @Override 
    public int getCount ( ) { 
        return mAppList.size ( ) ; 
    } 

    @Override 
    public Object getItem ( int position ) { 
        return mAppList.get ( position ) ; 
    } 

    @Override 
    public long getItemId( int position ) { 
        return position ; 
    } 

    public void removeItem ( int position ) { 
        mAppList.remove( position ) ; 
        this.notifyDataSetChanged( ) ; 
    } 
    
    @Override 
    public View getView ( int position , View convertView, ViewGroup parent ) { 
        if ( convertView != null ) { 
            holder = ( buttonViewHolder) convertView. getTag ( ) ; 
        } else { 
            convertView = mInflater. inflate ( layoutResource, null ) ; 
            holder = new buttonViewHolder( ) ; 
            holder.food_num = (TextView ) convertView.findViewById( valueViewID[ 0] ) ; 
            holder.food_name = (TextView) convertView.findViewById( valueViewID[ 1] ) ; 
            holder.food_cost = (TextView) convertView.findViewById( valueViewID[ 2] ) ; 
            holder.food_count = (Button) convertView.findViewById( valueViewID[ 3] ) ; 
            convertView. setTag( holder) ; 
        } 
        //HashMap	中存的数据类型是	String、Integer，对于	TextView	直接存值，对于	Button	等存	id
        HashMap< String , Object > map = mAppList.get( position ) ; 
        if ( map != null ) {  
        	String food_name = ( String ) map.get ( keyString[ 1] ) ; 
            String food_cost = ( String ) map.get ( keyString[ 2] ) ;
            String food_count = ( String ) map.get ( keyString[ 3] ) ;
            int mposition = position + 1; //position is indexed from zero
            holder.food_num.setText("" + mposition) ; 
            holder.food_name.setText(food_name) ;
            holder.food_cost.setText(food_cost + ctx.getResources().getString(R.string.money_unit)) ;
            holder.food_count.setText(ex + food_count);
        }         
        return convertView; 
    }
}