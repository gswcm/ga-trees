package net.gswcm.tog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TreeInfoFragment extends Fragment{

	private int id;
	private SQLiteDatabase db;
	private String capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.toLowerCase().substring(1);
	}
	private HashMap<String,String> mapBuilder(String K,String V) {
		HashMap<String, String> M = new HashMap<String, String>();
		M.put(K, V);
		return M;
	}
	public static TreeInfoFragment getInstance(int id, SQLiteDatabase db) {
		TreeInfoFragment f = new TreeInfoFragment();
		f.db = db;
		f.id = id;
		return f;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tree_info, container, false);
		//-- Query to DB to extract tree information
		String selectQuery =
			"select " +
			"tree_name.tree_common as cName, " +
			"tree_name.tree_botanical as bName, " +
			"tree_name.tree_alternative as aName, " +
			"tree_info.characteristics as key, " +
			"tree_info.wood as wood, " +
			"tree_info.uses as uses, " +
			"tree_info.distribution as dist, " +
			"tree_info.description as desc " +
			"from tree_name inner join tree_info on tree_name.tree_info_id = tree_info._id where tree_name._id = " + id;
		Cursor cur = db.rawQuery(selectQuery, null);
		cur.moveToFirst();
		//-- Auxiliary map that links column names with group titles
		Map<String,String> columnToHeaderMap = new HashMap<String,String>();
		columnToHeaderMap.put("cName","Common Name");
		columnToHeaderMap.put("bName","Botanical Name");
		columnToHeaderMap.put("aName","Alternative Name");
		columnToHeaderMap.put("desc","Description");
		columnToHeaderMap.put("key","Key Characteristics");
		columnToHeaderMap.put("wood","Wood");
		columnToHeaderMap.put("uses","Uses");
		columnToHeaderMap.put("dist","Distribution");
		//-- Order of header lines
		String columnOrder[] = {"bName","aName","desc","key","wood","uses","dist"};
		//-- Fill up Group and Child data
		ArrayList<Map<String,String>> groupData = new ArrayList<Map<String,String>>();
		ArrayList<ArrayList<Map<String,String>>> childData = new ArrayList<ArrayList<Map<String,String>>>();
		for(int i=0; i<columnOrder.length; i++) {
			String childEntry = cur.getString(cur.getColumnIndex(columnOrder[i]));
			if( childEntry != null && !childEntry.equals("")) {
				//-- Prepare groupData
				groupData.add(mapBuilder("headerLine", columnToHeaderMap.get(columnOrder[i])));
				//-- Prepare childData
				if(columnOrder[i].equals("cName") || columnOrder[i].equals("aName") || columnOrder[i].equals("bName")) {
					childEntry = capitalize(childEntry);
				}
				ArrayList<Map<String, String>> temp = new ArrayList<Map<String, String>>();
				temp.add(mapBuilder("textLine", childEntry));
				childData.add(temp);
			}
		}
		//-- Instantiation of SimpleExpandableListAdapter
		SimpleExpandableListAdapter myListAdapter = new SimpleExpandableListAdapter(
			Explorer.con,
			groupData,
			R.layout.tree_info_header_item,
			new String[] {"headerLine"},
			new int[] {R.id.treeInfoHeader},
			childData,
			R.layout.tree_info_text_item,
			new String[] {"textLine"},
			new int[] {R.id.treeInfoText}
		);
		ExpandableListView myList = (ExpandableListView) view.findViewById(R.id.treeInfoListView);
		myList.setAdapter(myListAdapter);
		//myList.expandGroup(2);
		myList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				parent.collapseGroup(groupPosition);
				return true;
			};
		});
		//-- Set "common name" title
		((TextView)view.findViewById(R.id.commonName)).setText(capitalize(cur.getString(cur.getColumnIndex("cName"))));
		//-- Display image gallery
		LinearLayout treeInfoGallery = (LinearLayout) view.findViewById(R.id.treeInfoGallery);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200,200);
		lp.setMargins(10,10,10,10);
		try {
			String galleryDirectoryName = "images/treeInfo/" + cur.getString(cur.getColumnIndex("cName"));
			String[] listImages = getActivity().getAssets().list(galleryDirectoryName);
			ArrayList<Drawable> listOfDrawables = new ArrayList<Drawable>();
			for (String imageName : listImages) {
				InputStream is = getActivity().getAssets().open(galleryDirectoryName + "/" + imageName);
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				ImageView imageView = new ImageView(Explorer.con);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setImageBitmap(bitmap);
				listOfDrawables.add(imageView.getDrawable());
				imageView.setLayoutParams(lp);
				treeInfoGallery.addView(imageView);
				imageView.setOnClickListener(new onClickListener(listOfDrawables));
			}
		}
		catch (java.io.IOException e) {
			Log.e("TreeInfoFragment: ",e.getMessage());
		}
		return view;
	}
	private class onClickListener implements View.OnClickListener {

		private final ArrayList<Drawable> listOfDrawables;

		public onClickListener(ArrayList<Drawable> list) {
			super();
			this.listOfDrawables = list;
		}
		@Override
		public void onClick(View clickedImage) {
			Drawable drawable = ((ImageView)clickedImage).getDrawable();
			getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, TreeImageFragment.getInstance(drawable,listOfDrawables),null)
				.addToBackStack(null)
				.commit();
		}
	}
}
