package com.gsw.treesofgeorgia;

import com.gsw.DB.DatabaseAdapter;
import com.gsw.DB.Quest_Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IdTreeFragment extends Fragment {
	
	protected SQLiteDatabase database=null;
	static boolean yesTreeEnd;
	static boolean noTreeEnd;
	Quest_Main idQuest=new Quest_Main();
	static int questionId;
	static int answerWidth;
	static int[] questionNum = new int[25];
	
	public static Fragment newInstance(){
		Fragment idTreeFrag = new IdTreeFragment();
		return idTreeFrag;
	}
	

	
	 public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	 
	 }
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		DatabaseAdapter adapter=new DatabaseAdapter(MainActivity.con);
		adapter.open();
		database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);
		
		
		yesTreeEnd = false;	
		noTreeEnd = false;
		questionId=1;
		
		questionNum[questionId] = 10000;
				
		getQuestionData(questionNum[questionId]);
		
		View view = inflater.inflate(R.layout.fragment_id, container, false);
		
		RelativeLayout relative= (RelativeLayout) view.findViewById(R.id.id_relative);
		

		answerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());
		
		Button yes=new Button(MainActivity.con);
		yes.setOnClickListener(new yesLis());
		yes.setTextColor(Color.BLACK);
		yes.setText(idQuest.getYesText());
		yes.setPadding(4, 4, 4, 4);
		yes.setId(101);
		
		
		TextView start = new TextView(MainActivity.con);
		start.setOnClickListener(new textLis());
		start.setText(idQuest.getqText());
		start.setTextColor(Color.BLACK);
		start.setId(questionId);
		
		
		Button no=new Button(MainActivity.con);
		no.setOnClickListener(new noLis());
		no.setTextColor(Color.BLACK);
		no.setText(idQuest.getNoText());
		no.setPadding(4, 4, 4, 4);
		no.setId(1001);
		
		RelativeLayout.LayoutParams yesLp = new RelativeLayout.LayoutParams(
				answerWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
		yesLp.addRule(RelativeLayout.BELOW, start.getId());
		yesLp.addRule(RelativeLayout.ALIGN_LEFT, start.getId());
		
		
		RelativeLayout.LayoutParams startLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		
		RelativeLayout.LayoutParams noLp = new RelativeLayout.LayoutParams(
				answerWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
		noLp.addRule(RelativeLayout.ALIGN_TOP, yes.getId());
		noLp.addRule(RelativeLayout.ALIGN_BOTTOM, yes.getId());
		noLp.addRule(RelativeLayout.RIGHT_OF, yes.getId());
		
		/**RelativeLayout.LayoutParams yesLp = new RelativeLayout.LayoutParams(
				answerWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
		yesLp.addRule(RelativeLayout.BELOW, start.getId());
		yesLp.addRule(RelativeLayout.ALIGN_LEFT, start.getId());
		
		RelativeLayout.LayoutParams startLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		startLp.addRule(RelativeLayout.CENTER_HORIZONTAL, -1);
		
		
		RelativeLayout.LayoutParams noLp = new RelativeLayout.LayoutParams(
				answerWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
		noLp.addRule(RelativeLayout.BELOW, start.getId());
		noLp.addRule(RelativeLayout.ALIGN_RIGHT, start.getId());
		noLp.addRule(RelativeLayout.ALIGN_TOP, yes.getId());
		noLp.addRule(RelativeLayout.ALIGN_BOTTOM, yes.getId());
		noLp.addRule(RelativeLayout.RIGHT_OF, yes.getId());**/
				

		relative.addView(start, startLp);
		relative.addView(yes, yesLp);
		relative.addView(no, noLp);
		return view;
		
	}
	
	
	class yesLis implements OnClickListener {	
			
		@Override
		public void onClick(View v) {
		
			int id = idQuest.getYesNav();
						
			if(yesTreeEnd){
				
				Cursor cur=database.rawQuery("select * from tree_id_range where range_id="+id, null);
				cur.moveToFirst();
								
				int low = cur.getInt(cur.getColumnIndex("low"));
				int high = cur.getInt(cur.getColumnIndex("high"));
				
				if (low == high){
					
					Cursor cur1=database.rawQuery("select group_id from tree_main where tree_id="+low, null);
					cur1.moveToFirst();
					int groupId = cur1.getInt(cur1.getColumnIndex("group_id"));
					
					FragmentTransaction trans = getFragmentManager()
							.beginTransaction();
					new BrowseSpeciesFragment();
					trans.replace(R.id.rootFrame, BrowseSpeciesFragment.newInstance(groupId));
					trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					trans.addToBackStack(null);
					
					//trans.commit();
					
					new TreeFragment();
					trans.replace(R.id.rootFrame, TreeFragment.newInstance(low));
					trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					trans.addToBackStack(null);

					trans.commit();
					
					
				}
				else{
					
					Cursor cur1=database.rawQuery("select group_id from tree_main where tree_id="+low, null);
					cur1.moveToFirst();
					int groupId = cur1.getInt(cur1.getColumnIndex("group_id"));
					
					FragmentTransaction trans = getFragmentManager()
							.beginTransaction();
					new BrowseSpeciesFragment();
					trans.replace(R.id.rootFrame, BrowseSpeciesFragment.newInstance(groupId));
					trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					trans.addToBackStack(null);

					trans.commit();
				
				
				
								
				}
				
				yesTreeEnd = false;
				
				MainActivity.actionBar.selectTab(MainActivity.actionBar.getTabAt(0));
			}
			

			else{
				
				/**View view = getView();
				
				TextView text = (TextView) view.findViewById(questionNum[questionId]);
				
				text.setText(text.getText() + " " + idQuest.getYesText());**/
				
				questionId++;
				
				questionAnswered(id);
			
			}

		}
	 }
	
	class noLis implements OnClickListener {	
			
			@Override
			public void onClick(View v) {
			
				int id = idQuest.getNoNav();
				
				
				if(noTreeEnd){
					
					Cursor cur=database.rawQuery("select * from tree_id_range where range_id="+id, null);
					cur.moveToFirst();
					
					int low = cur.getInt(cur.getColumnIndex("low"));
					int high = cur.getInt(cur.getColumnIndex("high"));
					
					if (low == high){
						Cursor cur1=database.rawQuery("select group_id from tree_main where tree_id="+low, null);
						cur1.moveToFirst();
						int groupId = cur1.getInt(cur1.getColumnIndex("group_id"));
						FragmentTransaction trans = getFragmentManager()
								.beginTransaction();
						new BrowseSpeciesFragment();
						trans.replace(R.id.rootFrame, BrowseSpeciesFragment.newInstance(groupId));
						trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						trans.addToBackStack(null);
						
						//trans.commit();
						
						new TreeFragment();
						trans.replace(R.id.rootFrame, TreeFragment.newInstance(low));
						trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						trans.addToBackStack(null);

						trans.commit();
						
						noTreeEnd = false;
					}
					else{
						
						Cursor cur1=database.rawQuery("select group_id from tree_main where tree_id="+low, null);
						cur1.moveToFirst();
						int groupId = cur.getInt(cur1.getColumnIndex("group_id"));
						
						FragmentTransaction trans = getFragmentManager()
								.beginTransaction();
						new BrowseSpeciesFragment();
						trans.replace(R.id.rootFrame, BrowseSpeciesFragment.newInstance(groupId));
						trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						trans.addToBackStack(null);

					trans.commit();
					}
					
					
					
					MainActivity.actionBar.selectTab(MainActivity.actionBar.getTabAt(0));
					
					
				}
				
				
				else{
					
					/**View view = getView();
					
				    TextView text = (TextView) view.findViewById(questionNum[questionId]);
					
					text.setText(text.getText() + " " + idQuest.getNoText());**/
					
					questionId++;
					
					questionAnswered(id);
				
				}

			}
	 	
	}
	
	class textLis implements OnClickListener{

		@Override
		public void onClick(View args0) {
			
			int id = args0.getId();
			View view =  getView();	
			RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.id_relative);
			
			relative.removeView(view.findViewById(101));
			relative.removeView(view.findViewById(1001));
			
			/**int j = 0;
			
			for (int i = 0 ; i <questionId ; i++){
				if (questionNum[i] == id){
					j = i;
					break;
				}
			}
			**/
			for (int i = id + 1; i <= questionId ; i++){
				relative.removeView(relative.findViewById(i));
			}
			
			questionId = id;
			
			Log.v(" questionId = " + questionId, " getting data for id " + questionNum[questionId]);
			getQuestionData(questionNum[questionId]);
			
			
			Button yes=new Button(MainActivity.con);
			yes.setOnClickListener(new yesLis());
			yes.setTextColor(Color.BLACK);
			yes.setText(idQuest.getYesText());
			yes.setPadding(4, 4, 4, 4);
			yes.setId(101);
			
			
			Button no=new Button(MainActivity.con);
			no.setOnClickListener(new noLis());
			no.setTextColor(Color.BLACK);
			no.setText(idQuest.getNoText());
			no.setPadding(4, 4, 4, 4);
			no.setId(1001);
			
			RelativeLayout.LayoutParams yesLp = new RelativeLayout.LayoutParams(
					answerWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
			yesLp.addRule(RelativeLayout.BELOW, questionId);
			yesLp.addRule(RelativeLayout.ALIGN_LEFT, questionId);
			
			RelativeLayout.LayoutParams noLp = new RelativeLayout.LayoutParams(
					answerWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
			noLp.addRule(RelativeLayout.BELOW, questionId);
			noLp.addRule(RelativeLayout.ALIGN_RIGHT, questionId);
			noLp.addRule(RelativeLayout.ALIGN_BOTTOM, yes.getId());
			noLp.addRule(RelativeLayout.RIGHT_OF, yes.getId());
					
			relative.addView(yes, yesLp);
			relative.addView(no, noLp);
			
		}
		
	}
	
	protected View questionAnswered(int answer){
		
		Log.v("questionId =" + questionId,""+answer);
		
		questionNum[questionId] = answer;
		
		getQuestionData(answer);
		
		View view =  getView();		
		
		RelativeLayout relative= (RelativeLayout) view.findViewById(R.id.id_relative);
		
		relative.removeView(view.findViewById(101));
		relative.removeView(view.findViewById(1001));
			
		Button yes = new Button(MainActivity.con);
		yes.setOnClickListener(new yesLis());
		yes.setTextColor(Color.BLACK);
		yes.setText(idQuest.getYesText());
		yes.setPadding(4, 4, 4, 4);
		yes.setId(101);
		
		
		TextView question = new TextView(MainActivity.con);
		question.setOnClickListener(new textLis());
		question.setBackgroundColor(Color.TRANSPARENT);
		question.setText(idQuest.getqText());
		question.setTextColor(Color.BLACK);
		question.setId(questionId);
		
		
		Button no = new Button(MainActivity.con);
		no.setOnClickListener(new noLis());
		no.setTextColor(Color.BLACK);
		no.setText(idQuest.getNoText());
		no.setPadding(4, 4, 4, 4);
		no.setId(1001);
		
		
		RelativeLayout.LayoutParams yesLp = new RelativeLayout.LayoutParams(
				answerWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
		yesLp.addRule(RelativeLayout.BELOW, question.getId());
		yesLp.addRule(RelativeLayout.ALIGN_LEFT, question.getId());
		
		
		RelativeLayout.LayoutParams startLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		startLp.addRule(RelativeLayout.BELOW, (questionId - 1));
		
		
		RelativeLayout.LayoutParams noLp = new RelativeLayout.LayoutParams(
				answerWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
		noLp.addRule(RelativeLayout.ALIGN_TOP, yes.getId());
		noLp.addRule(RelativeLayout.ALIGN_BOTTOM, yes.getId());
		noLp.addRule(RelativeLayout.RIGHT_OF, yes.getId());
				
		relative.addView(question, startLp);
		
		relative.addView(yes, yesLp);

		relative.addView(no, noLp);
				
		return null;
		
	}
	
	protected Quest_Main getQuestionData(int ID)
	{
		Cursor cur=database.rawQuery("select * from quest_navigation where quest_id="+ID, null);
		

		if (cur != null)
		{
			cur.moveToFirst();
			
			idQuest.setQuest_id(ID);
			
			if (!(cur.isNull(cur.getColumnIndexOrThrow("y")))){
				idQuest.setYesNav((cur.getInt(cur.getColumnIndexOrThrow("y"))));
				yesTreeEnd = false;
			}
			else{
				idQuest.setYesNav((cur.getInt(cur.getColumnIndexOrThrow("y_range_id"))));
				yesTreeEnd = true;
			}
			
			if (!(cur.isNull(cur.getColumnIndexOrThrow("n")))){
				idQuest.setNoNav((cur.getInt(cur.getColumnIndexOrThrow("n"))));
				noTreeEnd = false;
			}
			else{
				idQuest.setNoNav((cur.getInt(cur.getColumnIndexOrThrow("n_range_id"))));
				noTreeEnd = true;
			}
			
			
			idQuest.setQuest_id(cur.getColumnIndexOrThrow("quest_id"));
			
		}
		else {
			return null;
		}
		
		Cursor cur1=database.rawQuery("select * from quest_data where quest_id="+ID, null);
		
		if(cur1 != null){
			cur1.moveToFirst();
			
			if (!(cur1.isNull(cur1.getColumnIndexOrThrow("yes_text")))){
				idQuest.setYesText((cur1.getString(cur1.getColumnIndexOrThrow("yes_text"))));
			}
			else{
				idQuest.setYesText("Yes");
			}
			
			if (!(cur1.isNull(cur1.getColumnIndexOrThrow("no_text")))){
				idQuest.setNoText((cur1.getString(cur1.getColumnIndexOrThrow("no_text"))));
			}
			else{
				idQuest.setNoText("No");
			}
			
			idQuest.setqText((cur1.getString(cur1.getColumnIndexOrThrow("quest_text"))));
		}
		
		return idQuest;
	}
}
