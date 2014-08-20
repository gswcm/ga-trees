package net.gswcm.tog.Explorer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import net.gswcm.tog.R;

public class ExplorerListAdapter extends CursorTreeAdapter {


	private Context con;
	private LayoutInflater inflater;

	private String capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.toLowerCase().substring(1);
	}

	public ExplorerListAdapter(Cursor cursor, Context context) {
		super(cursor, context);
		this.con = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	protected Cursor getChildrenCursor(Cursor cursor) {
		int groupPos = cursor.getPosition();
		int groupId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
		SQLiteDatabase db = ((SQLiteCursor)cursor).getDatabase();

		Cursor treeCursor = db.rawQuery("select * from tree_name where tree_name.tree_group_id=" + groupId, null);
		return treeCursor;
	}

	@Override
	protected View newGroupView(Context context, Cursor cursor, boolean b, ViewGroup viewGroup) {
		final View view = inflater.inflate(R.layout.explorer_family_item, viewGroup, false);
		return view;
	}

	@Override
	protected void bindGroupView(View view, Context context, Cursor cursor, boolean b) {
		TextView cName = (TextView) view.findViewById(R.id.familyCommonName);
		TextView bName = (TextView) view.findViewById(R.id.familyBotanicalName);
		cName.setText(cursor.getString(cursor.getColumnIndex("group_common")));
		bName.setText(cursor.getString(cursor.getColumnIndex("group_botanical")));
		/*
		if (b) {
			Iconify.setIcon(IndicatorText, IconValue.icon_caret_up);
		} else {
			Iconify.setIcon(IndicatorText, IconValue.icon_caret_down);
		}*/
	}

	@Override
	protected View newChildView(Context context, Cursor cursor, boolean b, ViewGroup viewGroup) {
		final View view = inflater.inflate(R.layout.explorer_tree_item, viewGroup, false);
		return view;
	}

	@Override
	protected void bindChildView(View view, Context context, Cursor cursor, boolean b) {
		TextView cName = (TextView) view.findViewById(R.id.treeCommonName);
		TextView bName = (TextView) view.findViewById(R.id.treeBotanicalName);
		cName.setText(capitalize(cursor.getString(cursor.getColumnIndex("tree_common"))));
		bName.setText(cursor.getString(cursor.getColumnIndex("tree_botanical")));
	}
}
