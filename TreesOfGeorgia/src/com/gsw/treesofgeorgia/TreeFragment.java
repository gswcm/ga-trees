package com.gsw.treesofgeorgia;


import java.io.IOException;
import java.io.InputStream;

import com.gsw.treesofgeorgia.R;
import com.gsw.DB.Tree_Main;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	
	public static Fragment newInstance(int sid){
		Fragment t = new TreeFragment();
		id = sid;
		return t;
	}
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		View view = inflater.inflate(R.layout.tree_view, container, false);
		
		database=SQLiteDatabase.openOrCreateDatabase("data/data/com.gsw.treesofgeorgia/databases/trees.db", null);
		
        tree=getTree(id); 
        final String commonName = tree.getcName();
        final String altName = tree.getaName();
        final String botName = tree.getbName();
        final String keyCharacteristics = tree.getKey();
        final String wood = tree.getWood();
        final String dist = tree.getDist();
        final String uses = tree.getUses();
        final String desc = tree.getDesc();
        
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
        
        final ImageView displayImage = (ImageView) view.findViewById(R.id.displayImage);
        final LinearLayout myGallery = (LinearLayout) view.findViewById(R.id.mygallery);

        int margin = (int) ((4 * MainActivity.con.getResources().getDisplayMetrics().density)+.5);
        
        try {
            String galleryDirectoryName = "organized.reduced/"+commonName.toLowerCase();
            String[] listImages = getActivity().getAssets().list(galleryDirectoryName);
            for (String imageName : listImages) {
                InputStream is = getActivity().getAssets().open(galleryDirectoryName + "/" + imageName);
                final Bitmap bitmap = BitmapFactory.decodeStream(is);

                ImageView imageView = new ImageView(MainActivity.con);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(bitmap);
                
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getDp(500), getDp(500));
                lp.setMargins(margin, margin, margin, margin);
                imageView.setLayoutParams(lp);
                
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        displayImage.setImageBitmap(bitmap);
                    }
                });

                myGallery.addView(imageView);
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
			return tree;
		}
		else {
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
 
	
}
