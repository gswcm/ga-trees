package com.gsw.treesofgeorgia;


import java.io.IOException;
import java.io.InputStream;

import com.gsw.DB.Tree_Main;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;	
import android.support.v4.app.Fragment;
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
        
        
        try {
        	//InputStream is = null;
        	addImage[] add = new addImage[8];
        	int i = 0;
            String galleryDirectoryName = "organized.reduced/"+commonName.toLowerCase();
            String[] listImages = getActivity().getAssets().list(galleryDirectoryName);
            for (String imageName : listImages) {
            	add[i] = new addImage();
            	add[i].execute(imageName);
            	i++;
                          
        }
            } catch (IOException e) {
            Log.e("GalleryWithHorizontalScrollView", e.getMessage(), e);
        }   
              
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
	private class addImage extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... params) {
        
        try{	
        	String galleryDirectoryName = "organized.reduced/"+commonName.toLowerCase();
        	InputStream is = getActivity().getAssets().open(galleryDirectoryName + "/" + params[0]);
        	
            final Bitmap bitmap = BitmapFactory.decodeStream(is);
            
            is.close();
            return bitmap;
        } catch (IOException e) {
            Log.e("GalleryWithHorizontalScrollView", e.getMessage(), e);
        }
            return null;
        }

        protected void onPostExecute(final Bitmap bitmap) {
        	View view = getView();
        	LinearLayout myGallery = (LinearLayout) view.findViewById(R.id.myGallery);
        	ImageView imageView = new ImageView(MainActivity.con);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap small = Bitmap.createScaledBitmap(bitmap, getDp(750), getDp(750), false);
            imageView.setImageBitmap(small);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getDp(500), getDp(500));
            lp.setMargins(getDp(4), getDp(4), getDp(4), getDp(4));
            imageView.setLayoutParams(lp);
            
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	View view = getView();
                	ImageView displayImage = (ImageView) view.findViewById(R.id.displayImage);
                    displayImage.setImageBitmap(bitmap);
                }
            });
            
            myGallery.addView(imageView);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
