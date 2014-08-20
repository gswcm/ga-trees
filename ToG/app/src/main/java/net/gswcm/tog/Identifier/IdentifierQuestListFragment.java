package net.gswcm.tog.Identifier;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.gswcm.tog.R;


public class IdentifierQuestListFragment extends Fragment {

	private SQLiteDatabase db;

	private String capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.toLowerCase().substring(1);
	}

	public static IdentifierQuestListFragment getInstance(SQLiteDatabase db) {
		IdentifierQuestListFragment f = new IdentifierQuestListFragment();
		f.db = db;
		return f;
	}

	public IdentifierQuestListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_identifier_quest_list, container, false);
	}


}
