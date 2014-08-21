package net.gswcm.tog.Identifier;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.gswcm.tog.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class QuestItem {
	private String questText;
	private Bitmap yesBitmap;
	private Bitmap noBitmap;
	private String yesHelperText;
	private String noHelperText;
	private Cursor cursor;

	QuestItem(Cursor cursor, String questText, Bitmap yesBitmap, Bitmap noBitmap, String yesHelperText, String noHelperText) {
		this.cursor = cursor;
		this.questText = questText;
		this.yesBitmap = yesBitmap;
		this.noBitmap = noBitmap;
		this.yesHelperText = yesHelperText;
		this.noHelperText = noHelperText;
	}

	public String getQuestText() {
		return questText;
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

	public Cursor getCursor() {
		return cursor;
	}
}

class IdentifierListViewAdapter extends ArrayAdapter<QuestItem> {
	private IdentifierQuestListFragment f;

	public IdentifierListViewAdapter(IdentifierQuestListFragment f, Context context, int resource, List<QuestItem> objects) {
		super(context, resource, objects);
		this.f = f;
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
		if (s != null) {
			v.setText(s);
			v.setVisibility(View.VISIBLE);
		} else {
			v.setVisibility(View.GONE);
		}
	}

	private void setImageView(ImageView v, Bitmap b) {
		if (b != null) {
			v.setImageBitmap(b);
			v.setVisibility(View.VISIBLE);
		} else {
			v.setVisibility(View.GONE);
		}
	}

	private class QuestAnswerActionHandler implements View.OnClickListener {
		private int position;

		public QuestAnswerActionHandler(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			int N = getCount();
			for (int i = N - 1; i > position; i--) {
				remove(getItem(i));
			}
			notifyDataSetChanged();
		}
	}

	private class YesNoActionHandler implements View.OnClickListener {
		private Cursor cursor;
		private boolean yesAction;

		public YesNoActionHandler(Cursor cursor, boolean yesAction) {
			this.cursor = cursor;
			this.yesAction = yesAction;
		}

		@Override
		public void onClick(View view) {
			TextView answerField = (TextView) ((RelativeLayout) view.getParent()).findViewById(R.id.answerTextView);
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			SQLiteDatabase db = ((SQLiteCursor) cursor).getDatabase();
			//Toast.makeText(getContext(), String.format("ID: %d, yes/no: %s", id, yesAction), Toast.LENGTH_LONG).show();
			Cursor curNav = db.rawQuery("select * from quest_navigation where _id = " + id, null);
			String questColName = yesAction ? "yes_quest_id" : "no_quest_id";
			String rangeColName = yesAction ? "yes_range_id" : "no_range_id";
			String labelColName = yesAction ? "yes_label" : "no_label";
			String labelText = cursor.getString(cursor.getColumnIndex(labelColName));
			if (labelText == null)
				labelText = yesAction ? "yes" : "no";
			answerField.setText(labelText);
			if (curNav != null && curNav.moveToFirst()) {
				if (curNav.getString(curNav.getColumnIndex(questColName)) != null) {
					//-- next question
					int nextQuestId = curNav.getInt(curNav.getColumnIndex(questColName));
					String questText, yesText, noText;
					Bitmap yesBitmap, noBitmap;
					Cursor curData = db.rawQuery("select * from quest_data where _id = " + nextQuestId, null);
					if (curData != null && curData.moveToFirst()) {
						questText = curData.getString(curData.getColumnIndex("quest_text"));
						yesText = curData.getString(curData.getColumnIndex("yes_label"));
						if (yesText == null)
							yesText = "Yes";
						noText = curData.getString(curData.getColumnIndex("no_label"));
						if (noText == null)
							noText = "No";

						yesBitmap = f.loadImage(curData.getString(curData.getColumnIndex("yes_pic")));
						noBitmap = f.loadImage(curData.getString(curData.getColumnIndex("no_pic")));
						add(new QuestItem(curData, questText, yesBitmap, noBitmap, yesText, noText));
						notifyDataSetChanged();
					}
				} else {
					//-- tree range
					int nextRangeId = curNav.getInt(curNav.getColumnIndex(rangeColName));
					Cursor curData = db.rawQuery("select * from tree_range where _id = " + nextRangeId, null);
					if (curData != null && curData.moveToFirst()) {
						int lowTreeId = curData.getInt(curData.getColumnIndex("low_tree_name_id"));
						int highTreeId = curData.getInt(curData.getColumnIndex("high_tree_name_id"));
						f.getFragmentManager()
							.beginTransaction()
							.replace(android.R.id.content, IdentifierTreeListFragment.getInstance(db, lowTreeId, highTreeId), null)
							.addToBackStack(null)
							.commit();
					}
				}
			}
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		QuestItem rowItem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) Identifier.con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.identifier_quest_item, null);
			holder = new ViewHolder();
			holder.questTextView = (TextView) convertView.findViewById(R.id.questTextView);
			holder.answerTextView = (TextView) convertView.findViewById(R.id.answerTextView);
			holder.yesTextView = (TextView) convertView.findViewById(R.id.yesTextView);
			holder.noTextView = (TextView) convertView.findViewById(R.id.noTextView);
			holder.yesImageView = (ImageView) convertView.findViewById(R.id.yesImageView);
			holder.noImageView = (ImageView) convertView.findViewById(R.id.noImageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setTextView(holder.questTextView, rowItem.getQuestText());
		setTextView(holder.yesTextView, rowItem.getYesHelperText());
		setTextView(holder.noTextView, rowItem.getNoHelperText());
		setImageView(holder.yesImageView, rowItem.getYesBitmap());
		setImageView(holder.noImageView, rowItem.getNoBitmap());

		if (position < getCount() - 1) {
			holder.yesImageView.setVisibility(View.GONE);
			holder.noImageView.setVisibility(View.GONE);
			holder.yesTextView.setVisibility(View.GONE);
			holder.noTextView.setVisibility(View.GONE);
		}
		//-- Instantiation of yes/no handlers
		YesNoActionHandler yesHandler = new YesNoActionHandler(rowItem.getCursor(), true);
		YesNoActionHandler noHandler = new YesNoActionHandler(rowItem.getCursor(), false);
		QuestAnswerActionHandler qaHandler = new QuestAnswerActionHandler(position);
		//-- Assigning handlers to corresponding views
		holder.yesImageView.setOnClickListener(yesHandler);
		holder.noImageView.setOnClickListener(noHandler);
		holder.yesTextView.setOnClickListener(yesHandler);
		holder.noTextView.setOnClickListener(noHandler);
		holder.questTextView.setOnClickListener(qaHandler);
		holder.answerTextView.setOnClickListener(qaHandler);

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

	public Bitmap loadImage(String imageName) {
		try {
			Bitmap b = BitmapFactory.decodeStream(getActivity().getAssets().open("images/identityInfo/" + imageName));
			return b;
		} catch (IOException e) {
			Log.i("IdentifierQuestListFragment.onCreateView: ", e.getMessage());
		}
		return null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_identifier_quest_list, container, false);
		Cursor cur;
		ArrayList<QuestItem> questItems = new ArrayList<QuestItem>();
		QuestItem rawItem;
		ListView myList = (ListView) view.findViewById(R.id.identifierQuestListView);
		String questText, yesText, noText;
		Bitmap yesBitmap, noBitmap;

		cur = db.rawQuery("select * from quest_data where _id = 10000", null);
		if (cur != null && cur.moveToFirst()) {
			questText = cur.getString(cur.getColumnIndex("quest_text"));
			yesText = cur.getString(cur.getColumnIndex("yes_label"));
			if (yesText == null)
				yesText = "Yes";
			noText = cur.getString(cur.getColumnIndex("no_label"));
			if (noText == null)
				noText = "No";
			//-- Reading Bitmap object from the imageMap
			yesBitmap = loadImage(cur.getString(cur.getColumnIndex("yes_pic")));
			noBitmap = loadImage(cur.getString(cur.getColumnIndex("no_pic")));
			//-- Creating an instance of QuestItem to be passed to the adapter
			rawItem = new QuestItem(cur, questText, yesBitmap, noBitmap, yesText, noText);
			//-- Adding fresh QuestItem instance to List
			questItems.add(rawItem);
		}
		IdentifierListViewAdapter adapter = new IdentifierListViewAdapter(this, Identifier.con, R.layout.identifier_quest_item, questItems);
		myList.setAdapter(adapter);

		return view;
	}
}
