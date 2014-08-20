package net.gswcm.tog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class Identifier extends Activity {

	public static Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		con = this.getApplicationContext();
		dbHelper dbh = new dbHelper();
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Tree Identifier");
		if (savedInstanceState == null) {
			getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, IdentifierListFragment.getInstance(dbh.getDb(),17,19), null)
				.commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.identifier, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_mode_explore:
				Intent intent=new Intent();
				intent.setClass(this.getApplicationContext(), Explorer.class);
				startActivity(intent);
				return true;
			case R.id.action_settings:
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
