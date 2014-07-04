package com.gsw.treesofgeorgia;


import java.io.IOException;
import java.util.ArrayList;

import com.gsw.treesofgeorgia.R;
import com.gsw.DB.Tree_Main;
import com.gsw.Image.ImageAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;	
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.TextView;



public class TreeFragment extends Fragment 
{
	Gallery g;
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
        
        
        String[] flLists;
		 ArrayList<String> fileList=new ArrayList<String>();
        try {
			flLists = getActivity().getAssets().list("organized.reduced/"+commonName.toLowerCase());

			 for (int i = 0; i < flLists.length; i++) {
				fileList.add("organized.reduced/"+commonName.toLowerCase()+"/"+flLists[i]);
				Log.v("Jeff", flLists[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		    g=(Gallery)view.findViewById(R.id.ImgView);
	        ImageAdapter adapter=new ImageAdapter(getActivity(), fileList);
	        g.setAdapter(adapter);
	        
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
	
 

}
