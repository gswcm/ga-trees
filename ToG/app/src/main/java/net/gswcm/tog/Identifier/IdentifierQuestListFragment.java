package net.gswcm.tog.Identifier;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.gswcm.tog.Explorer.Explorer;
import net.gswcm.tog.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


class QuestItem {
	private String questText;
	private String answerText;
	private Bitmap yesBitmap;
	private Bitmap noBitmap;
	private String yesHelperText;
	private String noHelperText;
	QuestItem(String questText, String answerText, Bitmap yesBitmap, Bitmap noBitmap, String yesHelperText, String noHelperText) {
		this.questText = questText;
		this.answerText = answerText;
		this.yesBitmap = yesBitmap;
		this.noBitmap = noBitmap;
		this.yesHelperText = yesHelperText;
		this.noHelperText = noHelperText;
	}
	public String getQuestText() {
		return questText;
	}
	public String getAnswerText() {
		return answerText;
	}
	public Bitmap getYesBitmap() {
		return yesBitmap;
	}
	public Bitmap getNoBitmap() {
		return noBitmap;
	}
	public String getYesHelperText() {
		return yesHelperText;
	}
	public String getNoHelperText() {
		return noHelperText;
	}
}

class IdentifierListViewAdapter extends ArrayAdapter<QuestItem> {
	public IdentifierListViewAdapter(Context context, int resource, List<QuestItem> objects) {
		super(context, resource, objects);
	}
	private class ViewHolder {
		ImageView yesImageView;
		ImageView noImageView;
		TextView questTextView;
		TextView answerTextView;
		TextView yesTextView;
		TextView noTextView;
	}
	private void setTextView(TextView v, String s) {
		if(s != null) {
			v.setText(s);
			v.setVisibility(View.VISIBLE);
		}
		else {
			v.setText("");
			v.setVisibility(View.GONE);
		}
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		QuestItem rowItem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) Identifier.con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.identifier_quest_item, null);
			holder = new ViewHolder();
			holder.questTextView = (TextView)convertView.findViewById(R.id.questTextView);
			holder.answerTextView = (TextView)convertView.findViewById(R.id.answerTextView);
			holder.yesTextView = (TextView)convertView.findViewById(R.id.yesTextView);
			holder.noTextView = (TextView)convertView.findViewById(R.id.noTextView);
			holder.yesImageView = (ImageView)convertView.findViewById(R.id.yesImageView);
			holder.noImageView = (ImageView)convertView.findViewById(R.id.noImageView);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		setTextView(holder.questTextView,rowItem.getQuestText());
		setTextView(holder.answerTextView,rowItem.getAnswerText());
		setTextView(holder.yesTextView,rowItem.getYesHelperText());
		setTextView(holder.noTextView,rowItem.getNoHelperText());
		holder.yesImageView.setImageBitmap(rowItem.getYesBitmap());
		holder.noImageView.setImageBitmap(rowItem.getNoBitmap());
		return convertView;
	}
}
public class IdentifierQuestListFragment extends Fragment {
	private SQLiteDatabase db;
	private DisplayMetrics metrics;
	private String capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.toLowerCase().substring(1);
	}
	public static IdentifierQuestListFragment getInstance(SQLiteDatabase db) {
		IdentifierQuestListFragment f = new IdentifierQuestListFragment();
		f.db = db;
		f.metrics = Resources.getSystem().getDisplayMetrics();
		return f;
	}
	public IdentifierQuestListFragment() {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_identifier_quest_list, container, false);
		Cursor cur;
		int currentQuestId = 94;
		ArrayList<QuestItem> questItems = new ArrayList<QuestItem>();
		QuestItem rawItem;
		ListView myList = (ListView) view.findViewById(R.id.identifierQuestListView);
		String questText, answerText, yesText, noText;
		String imageDirName = "images/treeIdentification/";
		InputStream is = null;
		Bitmap yesBitmap, noBitmap ;

		for(;;) {
			cur = db.rawQuery("select * from quest_data where _id = " + currentQuestId, null);
			if (cur != null && cur.moveToFirst()) {
				questText = cur.getString(cur.getColumnIndex("quest_text"));
				answerText = "No";
				yesText = cur.getString(cur.getColumnIndex("yes_label"));
				noText = cur.getString(cur.getColumnIndex("no_label"));
				yesBitmap = null;
				noBitmap = null;
				try {
					yesBitmap = BitmapFactory.decodeStream(getActivity().getAssets()
						.open(imageDirName + cur.getString(cur.getColumnIndex("yes_pic"))));
					noBitmap = BitmapFactory.decodeStream(getActivity().getAssets()
						.open(imageDirName + cur.getString(cur.getColumnIndex("no_pic"))));
				}
				catch (IOException e) {
					Log.e("IdentifierQuestListFragment.onCreateView: ", e.getMessage());
				}
				rawItem = new QuestItem(questText,answerText,yesBitmap,noBitmap,yesText,noText);
				questItems.add(rawItem);
			}
			break;
		}
		IdentifierListViewAdapter adapter = new IdentifierListViewAdapter(Identifier.con,R.layout.identifier_quest_item, questItems);
		myList.setAdapter(adapter);

		return view;
	}
}
