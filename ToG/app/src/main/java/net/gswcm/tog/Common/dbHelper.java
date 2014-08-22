package net.gswcm.tog.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class dbHelper extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "ga-trees.db";
	private static final int DATABASE_VERSION = 1;
	private final SQLiteDatabase db;

	public dbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.db = getReadableDatabase();
	}

	public SQLiteDatabase getDb() {
		return db;
	}
}
