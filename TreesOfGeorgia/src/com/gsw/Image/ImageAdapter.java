package com.gsw.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{

	 private Context content;  
	    private ArrayList<String> imageAssetPathList;  
	  
	    public ImageAdapter(Context content, ArrayList<String> imageAssetPathList) {  
	        this.content = content;  
	        this.imageAssetPathList = imageAssetPathList;  
	    }  
	  
	    @Override  
	    public int getCount() {  
	        if (this.imageAssetPathList != null) {  
	            return this.imageAssetPathList.size();  
	        } else {  
	            return 0;  
	        }  
	    }  
	  
	    @Override  
	    public Object getItem(int position) {  
	        return null;  
	    }  
	  
	    @Override  
	    public long getItemId(int position) {  
	        return 0;  
	    }  
	  
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        try {  
	            ImageView imageView;  
	            imageView = new ImageView(this.content);  
	            imageView.setAdjustViewBounds(true);  
	            imageView.setScaleType(ImageView.ScaleType.MATRIX);  
	            imageView.setPadding(25, 25, 25, 25);  
	            InputStream inputStream = this.content.getAssets().open(this.imageAssetPathList.get(position));  
	            imageView.setImageDrawable(Drawable.createFromStream(inputStream, "" + position)); 
	  
	            return imageView;  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	            return null;  
	        }  
	    }
}
