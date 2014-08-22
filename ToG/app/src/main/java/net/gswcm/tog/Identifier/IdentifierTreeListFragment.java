package net.gswcm.tog.Identifier;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.gswcm.tog.Common.TreeInfoFragment;
import net.gswcm.tog.R;


public class IdentifierTreeListFragment extends Fragment {

	private SQLiteDatabase db;
	private int low_tree_id;
	private int high_tree_id;

	private String capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.toLowerCase().substring(1);
	}

	public static IdentifierTreeListFragment getInstance(SQLiteDatabase db, int low_tree_id, int high_tree_id) {
		IdentifierTreeListFragment f = new IdentifierTreeListFragment();
		f.db = db;
		f.low_tree_id = low_tree_id;
		f.high_tree_id = high_tree_id;
		f.setRetainInstance(true);
		return f;
	}

	public IdentifierTreeListFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_identifier_tree_list, container, false);
		Cursor cur = db.rawQuery("select _id, tree_common as cName, tree_botanical as bName from tree_name where _id >= " + low_tree_id + " and _id <= " + high_tree_id, null);
		ListView myList = (ListView) view.findViewById(R.id.identifierTreeListView);
		if (cur != null && cur.moveToFirst()) {
			SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
				getActivity(),
				R.layout.identifier_tree_item,
				cur,
				new String[]{"cName", "bName"},
				new int[]{R.id.treeCommonName, R.id.treeBotanicalName},
				0
			);
			myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
				@Override
				public boolean setViewValue(View view, Cursor cursor, int i) {
					if (i == cursor.getColumnIndex("cName") || i == cursor.getColumnIndex("bName")) {
						((TextView) view).setText(capitalize(cursor.getString(i)));
						return true;
					}
					return false;
				}
			});
			myList.setAdapter(myCursorAdapter);
			myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
					getFragmentManager()
						.beginTransaction()
						.replace(android.R.id.content, TreeInfoFragment.getInstance((int) id, db), null)
						.addToBackStack(null)
						.commit();
				}
			});
		}
		return view;
	}


}
