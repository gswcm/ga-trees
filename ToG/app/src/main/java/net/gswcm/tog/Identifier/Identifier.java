package net.gswcm.tog.Identifier;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.gswcm.tog.Explorer.Explorer;
import net.gswcm.tog.R;
import net.gswcm.tog.ToG;


public class Identifier extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Tree Identifier");
		if (savedInstanceState == null) {
			getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, IdentifierQuestListFragment.getInstance(ToG.getDBHelper().getDb()), null)
				.commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.identifier, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_mode_explore:
				Intent intent = new Intent();
				intent.setClass(ToG.getAppContext(), Explorer.class);
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
