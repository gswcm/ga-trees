package com.gsw.treesofgeorgia;

import java.io.IOException;
import java.io.InputStream;

import com.gsw.DB.DatabaseAdapter;
import com.gsw.DB.Quest_Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class IdTreeFragment extends Fragment {
	
	protected SQLiteDatabase database=null;
	static boolean yesTreeEnd = false;
	static boolean noTreeEnd = false;
	Quest_Main idQuest=new Quest_Main();
	static int questionId = 1;
	static int answerWidth;
	static int[] questionNum = new int[25];
	Bitmap yesPic;
	Bitmap noPic;
	String picPath = "key.images/";
	DatabaseAdapter adapter;
	
	public static Fragment newInstance(){
		Fragment idTreeFrag = new IdTreeFragment();
		return idTreeFrag;
	}
		
	public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	 
	 }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		adapter=new DatabaseAdapter(MainActivity.con);
		adapter.open();
		database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);
				
		questionNum[questionId] = 10000;
				
		getQuestionData(questionNum[questionId]);
		
		
		
		View view = inflater.inflate(R.layout.fragment_id, container, false);
		
		RelativeLayout relative= (RelativeLayout) view.findViewById(R.id.id_relative);
		

		answerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());

		
		Button yes=new Button(MainActivity.con);
		yes.setOnClickListener(new btnLis());
		yes.setText(idQuest.getYesText());
		yes.setPadding(4, 4, 4, 4);
		yes.setId(101);
		
		TextView start = new TextView(MainActivity.con);
		start.setOnClickListener(new textLis());
		start.setText(idQuest.getqText());
		start.setTextColor(Color.BLACK);
		start.setPadding(50, 25, 50, 25);
		start.setId(questionId);
		start.setTypeface(null, Typeface.BOLD);
		
		Button no=new Button(MainActivity.con);
		no.setOnClickListener(new btnLis());
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
				

		relative.addView(start, startLp);
		relative.addView(yes, yesLp);
		relative.addView(no, noLp);
		database.close();
		adapter.close();
		return view;
	
		
	}
	
	class btnLis implements OnClickListener {	
			
		@Override
		public void onClick(View v) {
		
			
			adapter.open();
			int id = 0;
			switch (v.getId()){
			
			case 101 :  id = idQuest.getYesNav();
						database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);			
						if(yesTreeEnd ){
				
							Cursor cur=database.rawQuery("select * from tree_id_range where range_id="+id, null);
							cur.moveToFirst();
								
							int low = cur.getInt(cur.getColumnIndex("low"));
							int high = cur.getInt(cur.getColumnIndex("high"));
							cur.close();
							if (low == high){
					
								Cursor cur1=database.rawQuery("select group_id from tree_main where tree_id="+low, null);
								cur1.moveToFirst();
								int groupId = cur1.getInt(cur1.getColumnIndex("group_id"));
								cur1.close();
								database.close();
								FragmentTransaction trans = getFragmentManager()
										.beginTransaction();
								FragmentManager manage = getFragmentManager();
								if (manage.getBackStackEntryCount() >= 1){
									for (int i = 0 ; i < manage.getBackStackEntryCount() ; i++){
							
										int backStackId = manage.getBackStackEntryAt(i).getId();

										manage.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
									}
								}
				
								trans.replace(R.id.rootFrame, BrowseSpeciesFragment.newInstance(groupId), "speciesFragment");
								trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
								trans.addToBackStack(null);
								trans.commit();
					
								FragmentTransaction trans1 = getFragmentManager()
										.beginTransaction();
								trans1.replace(R.id.rootFrame, TreeFragment.newInstance(low), "treeFragment");
								trans1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
								trans1.addToBackStack(null);

								trans1.commit();
					
					
							}
							else{
					
								Cursor cur1=database.rawQuery("select group_id from tree_main where tree_id="+low, null);
								cur1.moveToFirst();
								int groupId = cur1.getInt(cur1.getColumnIndex("group_id"));
								cur1.close();
								database.close();
								FragmentTransaction trans = getFragmentManager()
							.beginTransaction();
								FragmentManager manage = getFragmentManager();
								if (manage.getBackStackEntryCount() >= 1){
									for (int i = 0 ; i < manage.getBackStackEntryCount() ; i++){
							
										int backStackId = manage.getBackStackEntryAt(i).getId();

										manage.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
									}
								}
								trans.replace(R.id.rootFrame, BrowseSpeciesFragment.newInstance(groupId), "speciesFragment");
								trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
								trans.addToBackStack(null);

								trans.commit();
				
				
				
								
							}
				
							adapter.close();
							MainActivity.actionBar.selectTab(MainActivity.actionBar.getTabAt(0));
							break;
						}
			

						else{
				
							View view = getView();
							TextView text = (TextView) view.findViewById(questionId);				
							text.setText(Html.fromHtml(text.getText() + " <strong>" + idQuest.getYesText() + "</strong>"));
				
							text.setTextColor(Color.GRAY);
				
							text.setTypeface(null, Typeface.NORMAL);
				
							questionId++;
							database.close();
							questionAnswered(id);
			
						}
						adapter.close();
						break;
						
			case 1001 : id = idQuest.getNoNav();
						database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);			
						if(noTreeEnd ){
				
							Cursor cur=database.rawQuery("select * from tree_id_range where range_id="+id, null);
							cur.moveToFirst();
					
							int low = cur.getInt(cur.getColumnIndex("low"));
							int high = cur.getInt(cur.getColumnIndex("high"));
							cur.close();
							if (low == high){
		
								Cursor cur1=database.rawQuery("select group_id from tree_main where tree_id="+low, null);
								cur1.moveToFirst();
								int groupId = cur1.getInt(cur1.getColumnIndex("group_id"));
								cur1.close();
								database.close();
								FragmentTransaction trans = getFragmentManager()
										.beginTransaction();
								FragmentManager manage = getFragmentManager();
								if (manage.getBackStackEntryCount() >= 1){
									for (int i = 0 ; i < manage.getBackStackEntryCount() ; i++){
				
										int backStackId = manage.getBackStackEntryAt(i).getId();

										manage.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
									}
								}
	
								trans.replace(R.id.rootFrame, BrowseSpeciesFragment.newInstance(groupId), "speciesFragment");
								trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
								trans.addToBackStack(null);
								trans.commit();
		
								FragmentTransaction trans1 = getFragmentManager()
										.beginTransaction();
								//new TreeFragment();
								trans1.replace(R.id.rootFrame, TreeFragment.newInstance(low), "treeFragment");
								trans1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
								trans1.addToBackStack(null);

								trans1.commit();
		
		
							}
							else{
		
								Cursor cur1=database.rawQuery("select group_id from tree_main where tree_id="+low, null);
								cur1.moveToFirst();
								int groupId = cur1.getInt(cur1.getColumnIndex("group_id"));
								cur1.close();
								database.close();
								FragmentTransaction trans = getFragmentManager()
										.beginTransaction();
								FragmentManager manage = getFragmentManager();
								if (manage.getBackStackEntryCount() >= 1){
									for (int i = 0 ; i < manage.getBackStackEntryCount() ; i++){
				
										int backStackId = manage.getBackStackEntryAt(i).getId();

										manage.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
									}
								}
								trans.replace(R.id.rootFrame, BrowseSpeciesFragment.newInstance(groupId), "speciesFragment");
								trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
								trans.addToBackStack(null);

								trans.commit();
					
							}
	
							adapter.close();
							MainActivity.actionBar.selectTab(MainActivity.actionBar.getTabAt(0));
							break;
						}


						else{
	
							View view = getView();
							
							TextView text = (TextView) view.findViewById(questionId);				
							text.setText(Html.fromHtml(text.getText() + " <strong>" + idQuest.getNoText() + "</strong>"));
	
							text.setTextColor(Color.GRAY);
	
							text.setTypeface(null, Typeface.NORMAL);
	
							questionId++;
							database.close();
							questionAnswered(id);

						}
						adapter.close();
						break;
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
			for (int i = id; i <= questionId ; i++){
				relative.removeView(relative.findViewById(i));
			}
			
			questionId = id;
			questionAnswered(questionNum[questionId]);
			getQuestionData(questionNum[questionId]);
		}
		
	}
	
	protected View questionAnswered(int answer){
		
			
		Log.v("questionId =" + questionId,""+answer);
				
		
		questionNum[questionId] = answer;
		
		getQuestionData(answer);
		
		final View view =  getView();		
		
		final ScrollView scroll = (ScrollView) view.findViewById(R.id.id_scroll);
		RelativeLayout relative= (RelativeLayout) view.findViewById(R.id.id_relative);
		
		relative.removeView(view.findViewById(101));
		relative.removeView(view.findViewById(1001));
		relative.removeView(view.findViewById(900));
		relative.removeView(view.findViewById(901));
			
		if(answer == 10000){
			Button yes=new Button(MainActivity.con);
			yes.setOnClickListener(new btnLis());
			yes.setText(idQuest.getYesText());
			yes.setPadding(4, 4, 4, 4);
			yes.setId(101);
			
			TextView start = new TextView(MainActivity.con);
			start.setOnClickListener(new textLis());
			start.setText(idQuest.getqText());
			start.setTextColor(Color.BLACK);
			start.setPadding(50, 25, 50, 25);
			start.setId(questionId);
			start.setTypeface(null, Typeface.BOLD);
			
			Button no=new Button(MainActivity.con);
			no.setOnClickListener(new btnLis());
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
					

			relative.addView(start, startLp);
			relative.addView(yes, yesLp);
			relative.addView(no, noLp);
		}
		
		else{
			try {
				InputStream yesS = getActivity().getAssets().open(picPath + idQuest.getYesPic());
				yesPic = BitmapFactory.decodeStream(yesS);
				yesS.close();
				InputStream noS = getActivity().getAssets().open(picPath + idQuest.getNoPic());
				noPic = BitmapFactory.decodeStream(noS);
				noS.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         		
			ImageView yes = new ImageView(MainActivity.con);
			yes.setOnClickListener(new btnLis());
			yes.setImageBitmap(yesPic);
			yes.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			yes.setBackgroundColor(Color.TRANSPARENT);
			yes.setId(101);
				
			TextView question = new TextView(MainActivity.con);
			question.setOnClickListener(new textLis());
			question.setBackgroundColor(Color.TRANSPARENT);
			question.setText(idQuest.getqText());
			question.setTextColor(Color.BLACK);
			question.setPadding(50, 25, 50, 25);
			question.setTypeface(null, Typeface.BOLD);
			question.setId(questionId);
		
			ImageView no = new ImageView(MainActivity.con);
			no.setOnClickListener(new btnLis());
			no.setImageBitmap(noPic);
			no.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			no.setBackgroundColor(Color.TRANSPARENT);
			no.setId(1001);
					
			TextView yesText = new TextView(MainActivity.con);
			yesText.setText(idQuest.getYesText());
			yesText.setTextColor(Color.BLACK);
			yesText.setPadding(75, 10, 75, 10);
			yesText.setId(900);
			
			TextView noText = new TextView(MainActivity.con);
			noText.setText(idQuest.getNoText());
			noText.setTextColor(Color.BLACK);
			noText.setPadding(75, 10, 150, 10);
			noText.setId(901);
		
			RelativeLayout.LayoutParams startLp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			startLp.addRule(RelativeLayout.BELOW, (questionId - 1));
			
			
			RelativeLayout.LayoutParams yesPicLp = new RelativeLayout.LayoutParams(
					answerWidth, getDp(1200));
			yesPicLp.addRule(RelativeLayout.BELOW, question.getId());
			yesPicLp.addRule(RelativeLayout.ALIGN_LEFT, question.getId());
		
				
			RelativeLayout.LayoutParams noPicLp = new RelativeLayout.LayoutParams(
					answerWidth, getDp(1200));
			noPicLp.addRule(RelativeLayout.BELOW, question.getId());
			noPicLp.addRule(RelativeLayout.ALIGN_RIGHT, question.getId());
			//noPicLp.addRule(RelativeLayout.ALIGN_TOP, yes.getId());
			//noPicLp.addRule(RelativeLayout.ALIGN_BOTTOM, yes.getId());
			noPicLp.addRule(RelativeLayout.RIGHT_OF, yes.getId());
			
			RelativeLayout.LayoutParams yesTextLp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			yesTextLp.addRule(RelativeLayout.BELOW, yes.getId());
			yesTextLp.addRule(RelativeLayout.ALIGN_LEFT, yes.getId());
			yesTextLp.addRule(RelativeLayout.ALIGN_RIGHT, yes.getId());
			
			RelativeLayout.LayoutParams noTextLp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			noTextLp.addRule(RelativeLayout.BELOW, no.getId());
			noTextLp.addRule(RelativeLayout.RIGHT_OF, yesText.getId());
			noTextLp.addRule(RelativeLayout.ALIGN_RIGHT, no.getId());
			noTextLp.addRule(RelativeLayout.ALIGN_LEFT, no.getId());
				
			relative.addView(question, startLp);
			
			relative.addView(yes, yesPicLp);

			relative.addView(no, noPicLp);
			
			relative.addView(yesText, yesTextLp);
			
			relative.addView(noText, noTextLp);
		
		
			scroll.post(new Runnable() {
				@Override
				public void run () {
					scroll.smoothScrollTo(0, scroll.getBottom());
				}
			});
		
		}
		return null;
		
	}
	
	protected Quest_Main getQuestionData(int ID){
		
		adapter.open();
		database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);
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
			cur.close();
			
		}
		else {
			
			return null;
		}
		
		Cursor cur1=database.rawQuery("select * from quest_data where quest_id="+ID, null);
		
		if(cur1 != null){
			cur1.moveToFirst();
			
			if (!((cur1.isNull(cur1.getColumnIndexOrThrow("yes_text"))) || (cur1.getString(cur1.getColumnIndexOrThrow("yes_text")).isEmpty()))){
				idQuest.setYesText((cur1.getString(cur1.getColumnIndexOrThrow("yes_text"))));
			}
			else{
				idQuest.setYesText("Yes");
			}
			
			if (!(cur1.isNull(cur1.getColumnIndexOrThrow("no_text")) || (cur1.getString(cur1.getColumnIndexOrThrow("no_text")).isEmpty()))){
				idQuest.setNoText((cur1.getString(cur1.getColumnIndexOrThrow("no_text"))));
			}
			else{
				idQuest.setNoText("No");
			}
			
			if (!(cur1.isNull(cur1.getColumnIndexOrThrow("pic_y")))){
				idQuest.setYesPic((cur1.getString(cur1.getColumnIndexOrThrow("pic_y"))));
			}
			if (!(cur1.isNull(cur1.getColumnIndexOrThrow("pic_n")))){
				idQuest.setNoPic((cur1.getString(cur1.getColumnIndexOrThrow("pic_n"))));
			}
			
			idQuest.setqText((cur1.getString(cur1.getColumnIndexOrThrow("quest_text"))));
			cur1.close();
		}
		else{
			return null;
		}
		database.close();
		adapter.close();
		return idQuest;
	}
	
	public int getDp(int i){
		
		return ((int) ((i / Resources.getSystem().getDisplayMetrics().density)+0.5));
		
		}
	
}
