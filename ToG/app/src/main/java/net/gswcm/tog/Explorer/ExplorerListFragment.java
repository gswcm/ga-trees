package net.gswcm.tog.Explorer;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import net.gswcm.tog.Common.TreeInfoFragment;
import net.gswcm.tog.R;


public class ExplorerListFragment extends Fragment {

	private SQLiteDatabase db;

	public static ExplorerListFragment getInstance(SQLiteDatabase db) {
		ExplorerListFragment f = new ExplorerListFragment();
		f.db = db;
		f.setRetainInstance(true);
		return f;
	}

	public ExplorerListFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_explorer_list, container, false);
		Cursor cur = db.rawQuery("select * from tree_group", null);
		if (cur != null && cur.moveToFirst()) {
			ExplorerListAdapter myCursorAdapter = new ExplorerListAdapter(cur, getActivity());
			ExpandableListView myList = (ExpandableListView) view.findViewById(R.id.explorerListView);
			myList.setAdapter(myCursorAdapter);

			myList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
					getFragmentManager()
						.beginTransaction()
						.replace(android.R.id.content, TreeInfoFragment.getInstance((int) id, db), null)
						.addToBackStack(null)
						.commit();
					return true;
				}
			});
		}
		return view;
	}

}
