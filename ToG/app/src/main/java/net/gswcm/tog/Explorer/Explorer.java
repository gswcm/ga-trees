package net.gswcm.tog.Explorer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.gswcm.tog.Common.dbHelper;
import net.gswcm.tog.Identifier.Identifier;
import net.gswcm.tog.R;


public class Explorer extends Activity {

	public static Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		con = this.getApplicationContext();
		dbHelper dbh = new dbHelper();
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Tree Explorer");

		if (savedInstanceState == null) {
			getFragmentManager()
				.beginTransaction()
				.add(android.R.id.content, ExplorerListFragment.getInstance(dbh.getDb()))
				.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explorer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_mode_identify:
				Intent intent=new Intent();
				intent.setClass(this.getApplicationContext(), Identifier.class);
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
