
 /* Copyright (C) 2013 Giannis Kitromilides 
 *
 * @version    : 1.0
 * @author     : Giannis Kitromilides (giannis.info) gkitromilides[at]gmail.com
 * @author     : Giannis Kitromilides                     gkitromilides[at]gmail.com
 *
 * Data Management Systems Laboratory (DMSL)
 * Department of Computer Science
 * University of Cyprus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * Õou should have received a copy of the GNU General Public License
 * along with this program. If not, see<http://www.gnu.org/licenses/> .
 *
 */
package com.sqlite;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import com.sqlite.MainActivity.table_;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.widget.Toast;

public class SQLiteAdapter {

	public static final String MYDATABASE_NAME = "MY_DATABASE";
	public static String MYDATABASE_TABLE;
	public static final int MYDATABASE_VERSION = 1;
	public static final String KEY_ID = "_id";
	public static final String KEY_CONTENT = "Content";

	// create table MY_DATABASE (ID integer primary key, Content text not null);
	// private static String SCRIPT_CREATE_DATABASE;
	private SQLiteHelper sqLiteHelper;
	public SQLiteDatabase sqLiteDatabase;

	private Context context;

	public SQLiteAdapter(Context c) {

		context = c;
	}

	public SQLiteAdapter openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();

		return this;
	}

	public SQLiteAdapter openToWrite(boolean flag_journal, boolean flag_sync,
			boolean flag_auto_vacuum1, boolean flag_auto_vacuum2,
			boolean flag_cache, boolean flag_index) {
		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		try {
			sqLiteDatabase = sqLiteHelper.getWritableDatabase();

			pragma_settings(flag_journal, flag_sync, flag_auto_vacuum1,
					flag_auto_vacuum2, flag_cache, flag_index);

		} catch (SQLException ex) {
			Log.d("Sql exeption", ex.getMessage());
		}
		return this;
	}

	public void close() {
		sqLiteHelper.close();
	}

	// /////////////////////////

	public void pragma_settings(boolean flag_journal, boolean flag_sync,
			boolean flag_auto_vacuum1, boolean flag_auto_vacuum2,
			boolean flag_cache, boolean flag_index) {
		Log.d("Pragma", flag_cache + "");
		try {

			if (flag_sync == true) {
				sqLiteDatabase.rawQuery("PRAGMA synchronous=OFF", null);
				
			}

			if (flag_auto_vacuum1 == true) {
				sqLiteDatabase.rawQuery("PRAGMA auto_vacuum=1", null);
			}

			if (flag_auto_vacuum2 == true) {
				sqLiteDatabase.rawQuery("PRAGMA auto_vacuum=2", null);
			}

			if (flag_journal == true)
				sqLiteDatabase.enableWriteAheadLogging();

			if (flag_cache == true) {
				sqLiteDatabase.rawQuery("PRAGMA page_size = 5120", null);

			}

		} catch (SQLException ex) {
			Log.d("Exeption Pragma", ex.getMessage());

		}

	}

	public String insert(String fileinsert,  boolean flag_journal,
			boolean flag_sync, boolean flag_auto_vacuum1,
			boolean flag_auto_vacuum2, boolean flag_cache, boolean flag_index,
			int ii) throws IOException {

		try {
			sqLiteDatabase.beginTransaction();
			int iterator = MainActivity.repeatTime;
			//for (int j = 0; j < iterator; j++) {

			File myFile = new File(fileinsert);
			FileInputStream fIn = new FileInputStream(myFile);
			String line;
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			line = myReader.readLine();
				//for (int i = 0; i < content.size(); i++) {
           Log.i("Line", line);
			while (line != null) {
				Log.i("Line", line);
				sqLiteDatabase.execSQL(line);
				line = myReader.readLine();
			}
					
					//Log.d("transactions" + ii, j + "");
				//}
		//	}

			sqLiteDatabase.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.d("" + ii, ":"+e.getMessage());
			return e.getMessage();
		} finally {
			sqLiteDatabase.endTransaction();
		}

		return null;
	}

	public void insert(String fileinsert) throws IOException {


		File myFile = new File(fileinsert);
		FileInputStream fIn = new FileInputStream(myFile);
		String line;
		BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
		line = myReader.readLine();
		Log.i("test", line);
		while (line != null) {
			Log.i("test", line);
			sqLiteDatabase.execSQL(line);
			
			line = myReader.readLine();

		}
		myReader.close();
		//
		// pragma_settings(flag_journal, flag_sync, flag_auto_vacuum1,
		// flag_auto_vacuum2, flag_cache1000, flag_cache500, flag_index);
		//sqLiteDatabase.execSQL(content);

	}

	public Cursor findTables() {
		Cursor cursor;
		cursor = sqLiteDatabase.rawQuery(" SELECT * FROM sqlite_master "
				+ " WHERE type='table'", null);

		return cursor;
	}

	public void create_table(table_ t, boolean flag_journal, boolean flag_sync,
			boolean flag_auto_vacuum1, boolean flag_auto_vacuum2,
			boolean flag_cache, boolean flag_index) {

		for (int i = 0; i < t.tableCounter; i++) {
			Log.d("Create table", "Start Create table");
			sqLiteDatabase
					.execSQL("DROP TABLE IF EXISTS " + t.tableName.get(i));
			sqLiteDatabase.execSQL(t.table.get(i));

			if (flag_index == true) {
				sqLiteDatabase
						.execSQL("CREATE INDEX mytable_id_idx ON dammyTable(id,test1)");

			} else
				sqLiteDatabase.execSQL("DROP INDEX IF EXISTS mytable_id_idx ");

			// pragma_settings(flag_journal, flag_sync, flag_auto_vacuum1,
			// flag_auto_vacuum2, flag_cache1000, flag_cache500,
			// flag_index);

		}
		Log.d("Create table", "End Create table");
	}

	
	public void drop_table() {
		try {
			File file = new File(sqLiteDatabase.getPath());
			
			if (file.exists())
				file.delete();
		} catch (Exception ex) {
			Log.d("Fuck you drop table" , ":"+ex.getMessage());
		}
	}

	public int deleteAll(String tableName) {

		return sqLiteDatabase.delete(tableName, null, null);

	}

	public Cursor queueAll(String filename,String tableName, boolean flag_journal,
			boolean flag_sync, boolean flag_auto_vacuum1,
			boolean flag_auto_vacuum2, boolean flag_cache, boolean flag_index,
			boolean flagLimit, int thrID) throws IOException {

		String a;
		Cursor cursor = null;
	

		if (flagLimit == false)
		{
			File myFile = new File(filename);
			FileInputStream fIn = new FileInputStream(myFile);
			String line;
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			line = myReader.readLine();
				//for (int i = 0; i < content.size(); i++) {
           Log.i("Line", line);
		
			cursor = sqLiteDatabase.rawQuery(line,
					null);
		}
		else {
			String limit;
			String offset;
			limit = MainActivity.repeatTime / MainActivity.threadNum + "";
			offset = thrID * Integer.parseInt(limit)+"";
			cursor = sqLiteDatabase.rawQuery("SELECT * FROM  " + tableName
					+ " LIMIT " + offset + ", " + limit, null);
		}
		//Log.d("Column count", cursor.getColumnCount() + "");
		//Log.d("Database size", (file.length() / 1024 / 1024) + "");
		if (cursor.moveToFirst()) {
			do {
				a = cursor.getString(0);
				 Log.d("ID", a);
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return cursor;

	}

	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			// sqLiteDatabase.rawQuery("PRAGMA journal_mode=MEMORY",null);

			// db.execSQL("DROP TABLE IF EXISTS " + MYDATABASE_TABLE);
			// db.execSQL(SCRIPT_CREATE_DATABASE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

}