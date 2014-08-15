package com.example.android.exlistexample;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.exlistexample.provider.CfpContract;
import com.example.android.exlistexample.provider.CfpContract.Groups;
import com.joanzapata.android.iconify.Iconify;
import com.joanzapata.android.iconify.Iconify.IconValue;

public class MyListAdapter extends CursorTreeAdapter {

	public HashMap<String, View> childView = new HashMap<String, View>();

	/**
	 * The columns we are interested in from the database
	 */
	protected static final String[] GROUPS_PROJECTION = new String[] {
			BaseColumns._ID, CfpContract.Groups.GROUP_ID,
			CfpContract.Groups.GROUP_NAME, CfpContract.Groups.GROUP_IMAGE, };

	protected static final String[] CLASSES_PROJECTION = new String[] {
			BaseColumns._ID, CfpContract.Classes.CLASS_ID,
			CfpContract.Classes.CLASS_NAME, CfpContract.Classes.CLASS_CAT, };

	protected static final String[] TEACHERS_PROJECTION = new String[] {
			BaseColumns._ID, CfpContract.Teachers.TEACHER_ID,
			CfpContract.Teachers.TEACHER_NAME,
			CfpContract.Teachers.TEACHER_SHORT,
			CfpContract.Teachers.TEACHER_OPLEIDINGEN, };

	private final String DEBUG_TAG = getClass().getSimpleName().toString();

	protected final HashMap<Integer, Integer> mGroupMap;

	private MainActivity mActivity;
	private LayoutInflater mInflater;

	public MyListAdapter(Cursor cursor, Context context) {

		super(cursor, context);
		mActivity = (MainActivity) context;
		mInflater = LayoutInflater.from(context);
		mGroupMap = new HashMap<Integer, Integer>();
	}

	@Override
	public View newGroupView(Context context, Cursor cursor,
			boolean isExpanded, ViewGroup parent) {

		final View view = mInflater.inflate(R.layout.list_group, parent, false);
		return view;
	}

	@Override
	public void bindGroupView(View view, Context context, Cursor cursor,
			boolean isExpanded) {

		TextView lblListHeader = (TextView) view
				.findViewById(R.id.lblListHeader);

		if (lblListHeader != null) {
			lblListHeader.setText(cursor.getString(cursor
					.getColumnIndex(Groups.GROUP_NAME)));

			ImageView groupIcon = (ImageView) view
					.findViewById(R.id.lblListHeaderIcon);
			groupIcon.setImageResource(cursor.getInt(cursor
					.getColumnIndex(Groups.GROUP_IMAGE)));
		}

		TextView IndicatorText = (TextView) view
				.findViewById(R.id.lblListHeaderIndicator);

		if (IndicatorText != null) {
			if (isExpanded) {
				Iconify.setIcon(IndicatorText, IconValue.icon_caret_up);
			} else {
				Iconify.setIcon(IndicatorText, IconValue.icon_caret_down);
			}
		}
	}

	@Override
	public View newChildView(Context context, Cursor cursor,
			boolean isLastChild, ViewGroup parent) {

		final View view = mInflater.inflate(R.layout.list_item, parent, false);

		return view;
	}

	@Override
	public void bindChildView(View view, Context context, Cursor cursor,
			boolean isLastChild) {

		TextView txtListChild = (TextView) view.findViewById(R.id.lblListItem);

		txtListChild.setText(cursor.getString(2));

	}

	protected Cursor getChildrenCursor(Cursor groupCursor) {
		// Given the group, we return a cursor for all the children within that
		// group
		int groupPos = groupCursor.getPosition();
		int groupId = groupCursor.getInt(groupCursor
				.getColumnIndex(BaseColumns._ID));

		Log.d(DEBUG_TAG, "getChildrenCursor() for groupPos " + groupPos);
		Log.d(DEBUG_TAG, "getChildrenCursor() for groupId " + groupId);

		mGroupMap.put(groupId, groupPos);

		Loader loader = mActivity.getSupportLoaderManager().getLoader(groupId);
		if (loader != null && !loader.isReset()) {
			mActivity.getSupportLoaderManager().restartLoader(groupId, null,
					mActivity.mSpeakersLoaderCallback);
		} else {
			mActivity.getSupportLoaderManager().initLoader(groupId, null,
					mActivity.mSpeakersLoaderCallback);
		}

		return null;
	}

	// Access method
	public HashMap<Integer, Integer> getGroupMap() {
		return mGroupMap;
	}

	public void filterData(String query) {
		// TODO Filter the data here
	}
}
