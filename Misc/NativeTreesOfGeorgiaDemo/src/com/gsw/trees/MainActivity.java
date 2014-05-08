package com.gsw.trees;


import java.util.ArrayList;

import com.gsw.DB.DatabaseAdapter;
import com.gsw.DB.Tree_Group;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends Activity {

	private SQLiteDatabase database=null;
	ArrayList<Tree_Group> tree_group;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView view=new ScrollView(this);
		setContentView(view);
		DatabaseAdapter adapter=new DatabaseAdapter(this);
		adapter.open();
		database=SQLiteDatabase.openOrCreateDatabase("data/data/com.example.nativetreesofgeorgiademo/databases/trees.db", null);
		tree_group=getGroup();
		
		LinearLayout linear=new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);

		for (int i = 0; i < tree_group.size(); i++)
		{
			Button btn=new Button(this);
			final Tree_Group temp=tree_group.get(i);
			btn.setText(temp.getcName()+"/"+temp.getbName());
			btn.setId(temp.getGroup_ID());
			btn.setOnClickListener(new FirstLis());
			linear.addView(btn);
	
		}
		

		
		view.addView(linear);
	
	}
	 
	class FirstLis implements OnClickListener {	
		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, Second.class);
			Bundle b=new Bundle();
			b.putInt("GID", v.getId());
			intent.putExtras(b);
			startActivity(intent);	

		}

	}
	
	
	private ArrayList<Tree_Group> getGroup()
	{
		Cursor cur=database.rawQuery("select * from tree_group", null);
		if (cur!=null) 
		{
			ArrayList<Tree_Group> tree_groupTemp=new ArrayList<Tree_Group>();
			if (cur.moveToFirst()) {
				do {
					int g_ID=cur.getInt(cur.getColumnIndex("group_id"));
					String cName=cur.getString(cur.getColumnIndex("cName"));
					String bName=cur.getString(cur.getColumnIndex("bName"));
					int t_ID=cur.getInt(cur.getColumnIndex("type_id"));
					Tree_Group group=new Tree_Group();
					group.setGroup_ID(g_ID);
					group.setType_ID(t_ID);
					group.setcName(cName);
					group.setbName(bName);
					tree_groupTemp.add(group);
				} while (cur.moveToNext());
				
			}
			return tree_groupTemp;
		}
		
		else {
			return null;
		}
		
	}



}
