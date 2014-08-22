package net.gswcm.tog;

import android.app.Application;
import android.content.Context;

import net.gswcm.tog.Common.dbHelper;

public class ToG extends Application {
	private static Context context;
	private static dbHelper dbh;

	public void onCreate() {
		super.onCreate();
		ToG.context = getApplicationContext();
		ToG.dbh = new dbHelper(ToG.context);
	}

	public static Context getAppContext() {
		return ToG.context;
	}

	public static dbHelper getDBHelper() {
		return ToG.dbh;
	}
}
