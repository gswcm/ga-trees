package com.gsw.treesofgeorgia;



import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager()
        	.beginTransaction()
        	.replace(android.R.id.content, new AboutFragment())
        	.commit();
    }


    public static class AboutFragment extends Fragment {

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	
    		View view = inflater.inflate(R.layout.about, container, false);
    		TextView text = (TextView)view.findViewById(R.id.aboutText);
    		text.setText(Html.fromHtml("<p style=\"text-align: center\";><br /><h2>Trees of Georgia</h2> "
    								+ "<h3><br /> Created by:</h3> "
    								+ "<h4> Yuija Wang "
    								+ "<br /> Tatiana Baeva "
    								+ "<br /> James Lamb "
    								+ "<br /> and Dr. Simon Baev "
    								+ "<br /> with assistance from Chuck Bargeron </h4> "
    								+ "<br /> "
    								+ "<br /> <h3> For the University of Georgia </h3></p>"));
    		
				return view;
    	
    }
   }


}