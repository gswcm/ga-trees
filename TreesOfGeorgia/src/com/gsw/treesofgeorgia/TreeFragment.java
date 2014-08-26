package com.gsw.treesofgeorgia;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.gsw.DB.Tree_Main;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;	
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class TreeFragment extends Fragment implements OnClickListener
{
	
	//Gallery g;
	Bundle b;
	Intent intent;
	Tree_Main tree;
	private SQLiteDatabase database=null;
	static int id;
	
	private static DisplayMetrics metrics;
	private static int imageViewPixelSize;
	
	private static final int IMAGEVIEW_RELATIVE_HEIGHT = 15; // % of screen height in portrait orientation
	String commonName;
    String altName;
    String botName;
    String keyCharacteristics;
    String wood;
    String dist;
    String uses;
    String desc;
    
	public static Fragment newInstance(int sid){
		Fragment t = new TreeFragment();
		id = sid;
		metrics = Resources.getSystem().getDisplayMetrics();
		imageViewPixelSize = (int)(metrics.heightPixels*IMAGEVIEW_RELATIVE_HEIGHT/100.0);
		return t;
	}
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		final View view = inflater.inflate(R.layout.tree_view, container, false);
		
		
		
        tree=getTree(id); 
        commonName = tree.getcName();
        altName = tree.getaName();
        botName = tree.getbName();
        keyCharacteristics = tree.getKey();
        wood = tree.getWood();
        dist = tree.getDist();
        uses = tree.getUses();
        desc = tree.getDesc();
        
        TextView cnameView=(TextView) view.findViewById(R.id.cnameView);
        TextView cnameTitle = (TextView) view.findViewById(R.id.cnameTitle);
        
        cnameTitle.setText(commonName);
        cnameView.setText(commonName);
        
        TextView anameView=(TextView)view.findViewById(R.id.anameView);
        if (!(altName == null)){
        anameView.setText(altName);
        }
        else{
        	anameView.setText("There is no known alternative name");
        }
        
        TextView bnameView=(TextView)view.findViewById(R.id.bnameView);
        bnameView.setText(botName);
        
        TextView keyView=(TextView)view.findViewById(R.id.KeyView);
        keyView.setText(keyCharacteristics);
        
        TextView woodView=(TextView)view.findViewById(R.id.woodView);
        woodView.setText(wood);
        
        TextView distView=(TextView)view.findViewById(R.id.DistView);
        distView.setText(dist);
                
        TextView usesView=(TextView)view.findViewById(R.id.UsesView);
        usesView.setText(uses);
        
        TextView desView=(TextView)view.findViewById(R.id.desView);
        desView.setText(desc);
        
		String galleryDirectoryName = "organized.reduced/"+commonName.toLowerCase();
		addImage add = new addImage();
		add.execute(galleryDirectoryName);   
              
	    return view;    
	}
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        
	}
	

	private Tree_Main getTree(int ID)
	{
		database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);
		Cursor cur=database.rawQuery("select * from tree_main where tree_id="+ID, null);
		
		Tree_Main tree=new Tree_Main();
		if (cur!=null)
		{
			cur.moveToFirst();
			tree.setWood(cur.getString(cur.getColumnIndex("wood")));
			tree.setaName(cur.getString(cur.getColumnIndex("aName")));
			tree.setbName(cur.getString(cur.getColumnIndex("bName")));
			tree.setcName(cur.getString(cur.getColumnIndex("cName")));
			tree.setDist(cur.getString(cur.getColumnIndex("dist")));
			tree.setUses(cur.getString(cur.getColumnIndex("uses")));
			tree.setKey(cur.getString(cur.getColumnIndex("KEY")));
			Cursor cur1=database.rawQuery("select * from tree_desc where desc_id="+cur.getInt(cur.getColumnIndex("desc_id")), null);
			cur1.moveToFirst();
			tree.setDesc(cur1.getString(cur1.getColumnIndex("full")));
			cur.close();
			cur1.close();
			database.close();
			return tree;
		}
		else {
			database.close();
			return null;
		}
		
		
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

 
	public int getDp(int i){
		
	return ((int) ((i / Resources.getSystem().getDisplayMetrics().density)+0.5));
	
	}
	
	
	private class addImage extends AsyncTask<String, Void, ArrayList<ImageView>> {

        protected ArrayList<ImageView> doInBackground(String... strings) {
        
        	ArrayList<Drawable> listOfDrawables = new ArrayList<Drawable>();
			ArrayList<ImageView> result = new ArrayList<ImageView>();
			InputStream is = null;
			Drawable d;
			Bitmap large, small;
			try {
				String[] listImages = getActivity().getAssets().list(strings[0]);
				for (String imageName : listImages) {
					is = getActivity().getAssets().open(strings[0] + "/" + imageName);
					large = BitmapFactory.decodeStream(is);
					small = Bitmap.createScaledBitmap(large, imageViewPixelSize, imageViewPixelSize, false);
					d = new BitmapDrawable(getResources(),large);
					listOfDrawables.add(d);
					ImageView imageView = new ImageView(MainActivity.con);
					imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
					imageView.setImageBitmap(small);
					imageView.setOnClickListener(new onClickListener(listOfDrawables));
					result.add(imageView);
					is.close();
				}
				
			}
			catch (IOException e) {
				Log.e("TreeFragment.addImage ",e.getMessage());
			}

			return result;
        }

        protected void onPostExecute(ArrayList<ImageView> list) {
			View view = getView();
			LinearLayout treeInfoGallery = (LinearLayout) view.findViewById(R.id.myGallery);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageViewPixelSize,imageViewPixelSize);
			int margin = imageViewPixelSize/20;
			lp.setMargins(margin,margin,margin,margin);
			for (ImageView imageView : list) {
				imageView.setLayoutParams(lp);
				treeInfoGallery.addView(imageView);
			}
		}

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	private class onClickListener implements View.OnClickListener {

		private final ArrayList<Drawable> listOfDrawables;

		public onClickListener(ArrayList<Drawable> list) {
			super();
			this.listOfDrawables = list;
		}
		@Override
		public void onClick(View clickedImage) {
			int indexOfImage = ((LinearLayout)clickedImage.getParent()).indexOfChild(clickedImage);
			getFragmentManager()
				.beginTransaction()
				.replace(R.id.rootFrame, TreeImageFragment.getInstance(indexOfImage, listOfDrawables), null)
				.addToBackStack(null)
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
		}
	}
}
