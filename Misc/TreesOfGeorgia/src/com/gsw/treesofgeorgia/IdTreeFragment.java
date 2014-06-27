package com.gsw.treesofgeorgia;

import java.util.ArrayList;

import com.gsw.DB.DatabaseAdapter;
import com.gsw.DB.Quest_Main;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class IdTreeFragment extends Fragment {
	protected SQLiteDatabase database=null;
	ArrayList<Quest_Main> trees;
	public static boolean yTree = false;
	public static boolean nTree = false;
	public static Fragment newInstance(){
		Fragment idTreeFrag = new BrowseSpeciesFragment();
		return idTreeFrag;
	}
	
	 public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	 
	 }
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		
		View view = inflater.inflate(R.layout.fragment_id, container, false);
		
		LinearLayout linear= (LinearLayout) view.findViewById(R.id.id_list);
		linear.setOrientation(LinearLayout.VERTICAL);
		
		DatabaseAdapter adapter=new DatabaseAdapter(MainActivity.con);
		adapter.open();
		database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);
		
		TextView start = new TextView(MainActivity.con);
		linear.setBackgroundColor(Color.TRANSPARENT);
		start.setText("Is it summer or winter?");
		start.setTextColor(Color.BLACK);
		linear.addView(start);
		
		Button yes=new Button(MainActivity.con);
		yes.setOnClickListener(new yesLis());
		yes.setText("Summer");
		yes.setId(1);
		linear.addView(yes);
		
		Button no=new Button(MainActivity.con);
		no.setOnClickListener(new noLis());
		no.setText("Winter");
		no.setId(0);
		linear.addView(no);
		
		return view;
		
	}
	
	 class yesLis implements OnClickListener {	
			
		@Override
		public void onClick(View v) {
		
			
		}

	}
	 
	 class noLis implements OnClickListener {	
			
		@Override
		public void onClick(View v) {
			
			
		}

	}
	
	
	
	protected Quest_Main getQuestionNav(int ID)
	{
		Cursor cur=database.rawQuery("select * from quest_navigation where quest_id="+ID, null);
		Quest_Main idQuest=new Quest_Main();
		if (cur!=null)
		{
			cur.moveToFirst();
			
			idQuest.setQuest_id(cur.getColumnIndex("quest_id"));
			
			if (!((cur.getInt(cur.getColumnIndex("y"))) == 0)){
				idQuest.setYesNav((cur.getInt(cur.getColumnIndex("y"))));
				yTree = false;
			}
			else{
				idQuest.setYesNav((cur.getInt(cur.getColumnIndex("y_range_id"))));
				yTree = true;
			}
			
			if (!((cur.getInt(cur.getColumnIndex("n"))) == 0)){
				idQuest.setNoNav((cur.getInt(cur.getColumnIndex("n"))));
				nTree = false;
			}
			else{
				idQuest.setYesNav((cur.getInt(cur.getColumnIndex("n_range_id"))));
				nTree = true;
			}
			
			return idQuest;
		}
		else {
			return null;
		}
	}
		
	protected Quest_Main getQuestionData(int ID)
	{
		Cursor cur=database.rawQuery("select quest_text from quest_data where quest_id="+ID, null);
		Quest_Main idQuest=new Quest_Main();
		if (cur!=null)
		{
			cur.moveToFirst();
			idQuest.setqText((cur.getString(cur.getColumnIndex("quest_text"))));
			
			
			return idQuest;
		}
		else {
			return null;
		}
	}
	
	protected View questionAnswered(int answer){
		
		ScrollView view = (ScrollView) getView();
		LinearLayout linear= (LinearLayout) view.findViewById(R.id.id_list);
		
		
		
		TextView start = new TextView(MainActivity.con);
		linear.setBackgroundColor(Color.TRANSPARENT);
		start.setText("Is it summer or winter?");
		linear.addView(start);
		
		Button yes=new Button(MainActivity.con);
		yes.setOnClickListener(new yesLis());
		linear.addView(yes);
		Button no=new Button(MainActivity.con);
		no.setOnClickListener(new noLis());
		linear.addView(no);
		
		
		return null;
		
	}
	
	
}