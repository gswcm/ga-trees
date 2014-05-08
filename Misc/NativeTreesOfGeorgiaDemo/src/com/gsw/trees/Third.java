package com.gsw.trees;


import java.io.IOException;
import java.util.ArrayList;

import com.example.nativetreesofgeorgiademo.R;
import com.gsw.DB.Tree_Main;
import com.gsw.Image.ImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;	
import android.util.Log;
import android.widget.Gallery;
import android.widget.TextView;



public class Third extends Activity
{
	Gallery g;
	Bundle b;
	Intent intent;
	Tree_Main tree;
	private SQLiteDatabase database=null;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.treemain);
		database=SQLiteDatabase.openOrCreateDatabase("data/data/com.example.nativetreesofgeorgiademo/databases/trees.db", null);
        intent=this.getIntent();
        b=intent.getExtras();
        int TID=b.getInt("TID");
        tree=getTree(TID); 
        TextView cnameView=(TextView)findViewById(R.id.cnameView);
        cnameView.setText(tree.getcName());
        TextView anameView=(TextView)findViewById(R.id.anameView);
        anameView.setText(tree.getaName());
        TextView bnameView=(TextView)findViewById(R.id.bnameView);
        bnameView.setText(tree.getbName());
        TextView keyView=(TextView)findViewById(R.id.KeyView);
        keyView.setText(tree.getKey());
        TextView woodView=(TextView)findViewById(R.id.woodView);
        woodView.setText(tree.getWood());
        TextView distView=(TextView)findViewById(R.id.DistView);
        distView.setText(tree.getDist());
        TextView usesView=(TextView)findViewById(R.id.UsesView);
        usesView.setText(tree.getUses());
        TextView desView=(TextView)findViewById(R.id.desView);
        desView.setText(tree.getDesc());
        
        
        String[] flLists;
		 ArrayList<String> fileList=new ArrayList<String>();
        try {
			flLists = this.getAssets().list("organized.reduced/"+tree.getcName().toLowerCase());

			 for (int i = 0; i < flLists.length; i++) {
				fileList.add("organized.reduced/"+tree.getcName().toLowerCase()+"/"+flLists[i]);
				Log.v("Jeff", flLists[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		    g=(Gallery)findViewById(R.id.ImgView);
	        ImageAdapter adapter=new ImageAdapter(this,fileList);  
	        g.setAdapter(adapter); 
	       
	      
		    
        
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
			tree.setDesc(cur.getString(cur.getColumnIndex("desc")));
			return tree;
		}
		else {
			return null;
		}
		
		
	}
	
 

}
