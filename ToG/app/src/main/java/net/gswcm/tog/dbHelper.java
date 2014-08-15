package net.gswcm.tog;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class dbHelper extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "myDB.db";
	private static final int DATABASE_VERSION = 1;
	private final SQLiteDatabase db;

	public dbHelper() {
		super(Explorer.con, DATABASE_NAME, null, DATABASE_VERSION);
		this.db = getReadableDatabase();
	}

	public SQLiteDatabase getDb() {
		return db;
	}

}
