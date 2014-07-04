package com.gsw.treesofgeorgia;

import java.util.ArrayList;

import com.gsw.DB.DatabaseAdapter;
import com.gsw.DB.Tree_Group;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class BrowseGenusFragment extends Fragment {
	
	private SQLiteDatabase database=null;
	ArrayList<Tree_Group> tree_group;
	
	public interface OnGenusSelectedListener {
        public void onGenusSelected(int GID);
    }
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnGenusSelectedListener");
        }
    }
	
	public static Fragment newInstance(){
		Fragment g = new BrowseGenusFragment();
		return g;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			ScrollView view=new ScrollView(MainActivity.con);
			LinearLayout linear=new LinearLayout(MainActivity.con);
			linear.setOrientation(LinearLayout.VERTICAL);
			
			DatabaseAdapter adapter=new DatabaseAdapter(MainActivity.con);
			adapter.open();
			database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);
			tree_group=getGroup();
	
			for (int i = 0; i < tree_group.size(); i++){
				
				Button btn=new Button(MainActivity.con);
				final Tree_Group temp=tree_group.get(i);
				btn.setText(temp.getcName()+"/"+temp.getbName());
				btn.setId(temp.getGroup_ID());
				btn.setOnClickListener(new FirstLis());
				linear.addView(btn);
				}


			view.addView(linear);
			return view;
			}
	
	public void onCreate(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
	
	}
	 
	 class FirstLis implements OnClickListener {	
		
		@Override
		public void onClick(View v) {
			FragmentTransaction trans = getFragmentManager()
					.beginTransaction();
			new BrowseSpeciesFragment();
			trans.replace(R.id.rootFrame, BrowseSpeciesFragment.newInstance(v.getId()));
			trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			trans.addToBackStack(null);

			trans.commit();
			
		}

	}
	 
	 protected ArrayList<Tree_Group> getGroup()
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