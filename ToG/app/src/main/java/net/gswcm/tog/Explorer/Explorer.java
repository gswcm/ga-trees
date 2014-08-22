package net.gswcm.tog.Explorer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.gswcm.tog.Identifier.Identifier;
import net.gswcm.tog.R;
import net.gswcm.tog.ToG;


public class Explorer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Tree Explorer");
		if (savedInstanceState == null) {
			getFragmentManager()
				.beginTransaction()
				.add(android.R.id.content, ExplorerListFragment.getInstance(ToG.getDBHelper().getDb()))
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
				Intent intent = new Intent();
				intent.setClass(ToG.getAppContext(), Identifier.class);
				startActivity(intent);
				return true;
			/*
			case R.id.action_settings:
				return true;
			*/
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
