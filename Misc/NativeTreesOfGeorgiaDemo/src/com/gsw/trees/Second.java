package com.gsw.trees;

import java.util.ArrayList;
import com.gsw.DB.Tree_Main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class Second extends Activity 
{
	Bundle b;
	Intent intent;
	private SQLiteDatabase database=null;
	ArrayList<Tree_Main> trees;
	 protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        ScrollView view=new ScrollView(this);
	        setContentView(view);
	        database=SQLiteDatabase.openOrCreateDatabase("data/data/com.example.nativetreesofgeorgiademo/databases/trees.db", null);
	        intent=this.getIntent();
	        b=intent.getExtras();
	        int GID=b.getInt("GID");
	        trees=getTrees(GID);
	        LinearLayout linear=new LinearLayout(this);
			linear.setOrientation(LinearLayout.VERTICAL);

			for (int i = 0; i < trees.size(); i++)
			{
				Button btn=new Button(this);
				Tree_Main temp=trees.get(i);
				btn.setText(temp.getcName()+"/"+temp.getbName());
				btn.setId(temp.getTree_id());
				btn.setOnClickListener(new SecondLis());
					
					
				linear.addView(btn);
				
			}

			view.addView(linear);
			
				
	 
	 }
	        
	 class SecondLis implements OnClickListener {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(Second.this, Third.class);
				Bundle b=new Bundle();
				b.putInt("TID", v.getId());
				intent.putExtras(b);
				startActivity(intent);	

			}

		}
		
	          
	    
	 
	 
		private ArrayList<Tree_Main> getTrees(int ID)
		{
			Cursor cur=database.rawQuery("select cName,bName,tree_id from tree_main where group_id="+ID,null);
			if (cur!=null) 
			{
				ArrayList<Tree_Main> tree_Temp=new ArrayList<Tree_Main>();
				if (cur.moveToFirst()) {
					do {
						int t_ID=cur.getInt(cur.getColumnIndex("tree_id"));
						String cName=cur.getString(cur.getColumnIndex("cName"));
						String bName=cur.getString(cur.getColumnIndex("bName"));
						Tree_Main temp=new Tree_Main();
						temp.setcName(cName);
						temp.setbName(bName);
						temp.setTree_id(t_ID);
						tree_Temp.add(temp);
						
					} while (cur.moveToNext());
					
				}
				return tree_Temp;
			}
			
			else {
				return null;
			}
			
		}

}
