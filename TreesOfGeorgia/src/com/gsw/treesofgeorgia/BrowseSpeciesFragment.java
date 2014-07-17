package com.gsw.treesofgeorgia;

import java.util.ArrayList;

import com.gsw.DB.Tree_Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class BrowseSpeciesFragment extends Fragment {

	static int id;
	private SQLiteDatabase database=null;
	ArrayList<Tree_Main> trees;

	public static Fragment newInstance(int gid){
		Fragment s = new BrowseSpeciesFragment();
		id = gid;
		return s;
	}
	
	 public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	 
	 }
	 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ScrollView view=new ScrollView(MainActivity.con);
	        database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);
	        trees=getTrees(id);
	        LinearLayout linear=new LinearLayout(MainActivity.con);
			linear.setOrientation(LinearLayout.VERTICAL);

			for (int i = 0; i < trees.size(); i++)
			{
				Button btn=new Button(MainActivity.con);
				Tree_Main temp=trees.get(i);
				btn.setText(Html.fromHtml(temp.getcName()+"<br\\>     <small>"+temp.getbName()+"</small>"));
				btn.setId(temp.getTree_id());
				btn.setOnClickListener(new SecondLis());
					
					
				linear.addView(btn);
				
			}
			database.close();
			view.addView(linear);
			return view;
		}
	        
	 class SecondLis implements OnClickListener {	
			@Override
			public void onClick(View v) {
				FragmentTransaction trans = getFragmentManager()
						.beginTransaction();
				trans.replace(R.id.rootFrame, TreeFragment.newInstance(v.getId()), "treeFragment");
				trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				trans.addToBackStack(null);

				trans.commit();
			}

		}
	 
	 private ArrayList<Tree_Main> getTrees(int ID)
		{
			database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);
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
				database.close();
				return tree_Temp;
			}
			
			else {
				database.close();
				return null;
			}
			
		}

}
