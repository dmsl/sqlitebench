
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import com.sqlite.R;

public class MainActivity extends Activity {
	private SQLiteAdapter mySQLiteAdapter;

	private Button btn_insert;
	private Button btn_selectall;
	private Button btn_transactioninsert;
	private Button btn_droptable;
	private Button btn_createtable;
	private Button btn_findtables;
	private Button btn_deleterowfromtable;
	private Button btn_testinsert;
	private Button btn_multiread;
	private CheckBox checkBoxjournal;
	private CheckBox checkBoxsync;
	private CheckBox checkBoxVacuum1;
	private CheckBox checkBoxVacuum2;
	private CheckBox cache1000;
	private CheckBox cache500;
	private CheckBox index;
	private TextView read;
	private TextView delete;
	private TextView insert;
	private TextView transinsert;
	private TextView tableNum;
	private TextView deleterowtime;
	private EditText input;
	public static long seconds1 = System.currentTimeMillis();
	public static long seconds2 = System.currentTimeMillis();

	public static boolean flag_journal = false;
	public static boolean flag_sync = false;
	public static boolean flag_auto_vacuum1 = false;
	public static boolean flag_auto_vacuum2 = false;
	public static boolean flag_cache = false;

	public static boolean flag_Fcreate = false;
	public static boolean flag_Finsert = false;
	public static boolean flag_Fdelete = false;
	public static boolean flag_index = false;
	public static String filecreate = null;
	public static String fileinsert = null;
	public static String filedelete = null;
	public static int repeatTime = 0;
	public static int threadNum = 0;
	private AlertDialog.Builder Insert_alert;

	// //////////////////

	File newFolder;
	File file1;
	FileOutputStream fOut1;
	OutputStreamWriter myOutWriter1;

	File file2;
	FileOutputStream fOut2;
	OutputStreamWriter myOutWriter2;

	private Transactions transactions;
	table_ t = new table_();
int count=0;
	class table_ {
		ArrayList<String> table = new ArrayList<String>();
		ArrayList<String> tableName = new ArrayList<String>();
		ArrayList<String> sqlinsert = new ArrayList<String>();
		int tableCounter = 0;
		ArrayList<String> sqldelete = new ArrayList<String>();
	}

	void customToast(String text) {
		Toast ImageToast = Toast.makeText(getApplicationContext(), "",
				Toast.LENGTH_LONG);
		LinearLayout toastLayout = new LinearLayout(getBaseContext());
		toastLayout.setOrientation(LinearLayout.HORIZONTAL);
		ImageView image = new ImageView(getBaseContext());
		TextView mTV = new TextView(getBaseContext());

		mTV.setText(text);
		mTV.setTextSize(19);
		image.setImageResource(R.drawable.error);

		toastLayout.addView(image);
		toastLayout.addView(mTV);
		toastLayout.setBackgroundColor(Color.WHITE);

		ImageToast.setView(toastLayout);
		ImageToast.setDuration(Toast.LENGTH_LONG);

		ImageToast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		transactions = new Transactions();

		// ///////////////////
		mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());

		// ////////////////////
		btn_transactioninsert = (Button) findViewById(R.id.button1);
		btn_insert = (Button) findViewById(R.id.button2);
		btn_selectall = (Button) findViewById(R.id.button3);
		btn_droptable = (Button) findViewById(R.id.button4);
		btn_findtables = (Button) findViewById(R.id.button5);
		btn_createtable = (Button) findViewById(R.id.button6);
		btn_deleterowfromtable = (Button) findViewById(R.id.button7);
		btn_testinsert = (Button) findViewById(R.id.button8);
		btn_multiread = (Button) findViewById(R.id.button9);
		// ////////////////////////////////////////////////////////
		btn_insert.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if ((fileinsert != null) && (filecreate != null)) {
					try {
						try {
							newFolder = new File(Environment
									.getExternalStorageDirectory(), "Aaa");
							if (!newFolder.exists()) {
								newFolder.mkdir();
							}
							try {

								file1 = new File(newFolder, "ExecutionTime"
										+ ".txt");
								file1.createNewFile();
								fOut1 = new FileOutputStream(file1);
								myOutWriter1 = new OutputStreamWriter(fOut1);

								file2 = new File(newFolder, "CpuUsage" + ".txt");
								file2.createNewFile();
								fOut2 = new FileOutputStream(file2);
								myOutWriter2 = new OutputStreamWriter(fOut2);

								// fOut.close();
							} catch (Exception ex) {
								System.out.println("ex: " + ex);
							}
						} catch (Exception e) {
							System.out.println("e: " + e);
						}
						// read insert query from file
						try {
							transactions.createTable(t, filecreate);
							//transactions.inserttable(t, fileinsert);
						} catch (Exception e) {

							customToast("Error reading the files "
									+ e.getMessage());
						}

						ArrayList<Long> array = new ArrayList<Long>();
						// delete all elements and execute the insert's queries
						// timer from the beginning of the query as the end
						for (int ii = 0; ii < repeatTime; ii++) {
							mySQLiteAdapter.openToWrite(flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);

							mySQLiteAdapter.drop_table();

							mySQLiteAdapter.close();

							mySQLiteAdapter.openToWrite(flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);

							mySQLiteAdapter.create_table(t, flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);

							mySQLiteAdapter.close();

							mySQLiteAdapter.openToWrite(flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);

							seconds1 = System.currentTimeMillis();
							
							// for (int j = 0; j < iterator; j++) {
						//	for (int i = 0; i < t.sqlinsert.size(); i++) {

								mySQLiteAdapter.insert(fileinsert);

							//}
							
							// }
							mySQLiteAdapter.close();
							seconds2 = System.currentTimeMillis();

							myOutWriter1.write("End=" + (seconds2 - seconds1)
									/ 1000 + " sec \n");
							mySQLiteAdapter.close();
						}
						
						myOutWriter1.close();
						fOut1.close();

						myOutWriter2.close();
						fOut2.close();
					} catch (Exception ex) {
						customToast(ex.getMessage());
						// mySQLiteAdapter.close();
					}
					// mySQLiteAdapter.close();
				} else
					customToast("Please select the file that contains the values that will be executed with the INSERT and CREATE table query");

			}
		});
		btn_transactioninsert.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if ((fileinsert != null) && (filecreate != null)) {
					try {
						try {
							newFolder = new File(Environment
									.getExternalStorageDirectory(), "Aaa");
							if (!newFolder.exists()) {
								newFolder.mkdir();
							}
							try {

								file1 = new File(newFolder, "ExecutionTime"
										+ ".txt");
								file1.createNewFile();
								fOut1 = new FileOutputStream(file1);
								myOutWriter1 = new OutputStreamWriter(fOut1);

								file2 = new File(newFolder, "CpuUsage" + ".txt");
								file2.createNewFile();
								fOut2 = new FileOutputStream(file2);
								myOutWriter2 = new OutputStreamWriter(fOut2);

								// fOut.close();
							} catch (Exception ex) {
								System.out.println("ex: " + ex);
							}
						} catch (Exception e) {
							System.out.println("e: " + e);
						}
						try {
							transactions.createTable(t, filecreate);
							// transactions.inserttable(t, fileinsert);
						} catch (Exception e) {
							customToast("Error reading the files "
									+ e.getMessage());
						}

						ArrayList<Long> array = new ArrayList<Long>();

						for (int i = 0; i < repeatTime; i++) {

							
							

							mySQLiteAdapter.openToWrite(flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);

							mySQLiteAdapter.drop_table();

							mySQLiteAdapter.close();

							mySQLiteAdapter.openToWrite(flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);

							mySQLiteAdapter.create_table(t, flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);
							mySQLiteAdapter.close();

							mySQLiteAdapter.openToWrite(flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);

							seconds1 = System.currentTimeMillis();
							mySQLiteAdapter.insert(fileinsert, flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index,
									i);
							seconds2 = System.currentTimeMillis();

							// execution time
							myOutWriter1.write("End=" + (seconds2 - seconds1)
									/ 1000 + " sec \n");

							// customToast("End"+(seconds2 - seconds1));
							// Log.d("End insert", "" + (seconds2 - seconds1));

							mySQLiteAdapter.close();
						}
						myOutWriter1.close();
						fOut1.close();

						myOutWriter2.close();
						fOut2.close();

					} catch (Exception ex) {
						customToast(ex.getMessage());
						Log.d("Exeption------------------------------",
								ex.getMessage());
						// mySQLiteAdapter.close();
					}
					// mySQLiteAdapter.close();
				} else
					customToast("Please select the file that contains the values that will be executed with the INSERT and CREATE table query");

			}
		});
		btn_selectall.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (filecreate != null) {

					try {

						ArrayList<Long> array = new ArrayList<Long>();
						// transactions.createTable(t, filecreate);

						Cursor cur = null;
						for (int i = 0; i < 10; i++) {
							cur = null;

							seconds1 = System.currentTimeMillis();
							mySQLiteAdapter.openToWrite(flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);
						//	cur = mySQLiteAdapter.queueAll(t.tableName.get(0),
						//			flag_journal, flag_sync, flag_auto_vacuum1,
						//			flag_auto_vacuum2, flag_cache, flag_index,
						//			false, 0);

							seconds2 = System.currentTimeMillis();
							array.add(seconds2 - seconds1);
							Log.d("View" + i, " " + (seconds2 - seconds1));

							mySQLiteAdapter.close();
						}
						read = (TextView) findViewById(R.id.readtime);
						read.setText(cur.getCount() + " read in "
								+ transactions.average_time(array) / 8 + "ms");
					} catch (Exception ex) {
						Toast.makeText(getApplicationContext(),
								ex.getMessage(), Toast.LENGTH_LONG).show();

					}
					// mySQLiteAdapter.close();
				} else
					customToast("Please select the file that contains the values that will be used to select value from the tables");
			}
		});
		btn_droptable.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {

					mySQLiteAdapter.openToWrite(flag_journal, flag_sync,
							flag_auto_vacuum1, flag_auto_vacuum2, flag_cache,
							flag_index);

					seconds1 = System.currentTimeMillis();

					mySQLiteAdapter.drop_table();
					seconds2 = System.currentTimeMillis();
					delete = (TextView) findViewById(R.id.deletetime);

					mySQLiteAdapter.close();
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.getMessage(),
							Toast.LENGTH_LONG).show();
				}

			}
		});
		btn_createtable.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (filecreate != null) {

					// //////////////////////////
					try {
						mySQLiteAdapter.openToWrite(flag_journal, flag_sync,
								flag_auto_vacuum1, flag_auto_vacuum2,
								flag_cache, flag_index);
						transactions.createTable(t, filecreate);

						mySQLiteAdapter.create_table(t, flag_journal,
								flag_sync, flag_auto_vacuum1,
								flag_auto_vacuum2, flag_cache, flag_index);
						Log.d("test", t.table + "||" + t.tableName);
					} catch (Exception e) {

						Log.d("Create Exeption", e.getMessage());
						mySQLiteAdapter.close();
					}

					mySQLiteAdapter.close();
				} else {

					customToast("Please select the file that contains the values that will be used to create new tables");
				}

			}
		});
		btn_findtables.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				mySQLiteAdapter.openToWrite(flag_journal, flag_sync,
						flag_auto_vacuum1, flag_auto_vacuum2, flag_cache,
						flag_index);
				// TODO Auto-generated method stub
				Cursor cur = null;

				cur = mySQLiteAdapter.findTables();
				int i = 0;
				if (cur.getCount() != 0) {
					for (i = 0; i < cur.getCount(); i++) {
						cur.moveToNext();
						Log.d("Tables", cur.getString(1));
					}
				}
				tableNum = (TextView) findViewById(R.id.table_num);
				tableNum.setText("" + i);
				mySQLiteAdapter.close();
			}
		});
		btn_deleterowfromtable.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				count++;
				if ((fileinsert != null) && (filecreate != null)) {
					try {
						try {
							newFolder = new File(Environment
									.getExternalStorageDirectory(), "Aaa");
							if (!newFolder.exists()) {
								newFolder.mkdir();
							}
							try {

								file1 = new File(newFolder, "ExecutionTime"
										+ ".txt");
								file1.createNewFile();
								fOut1 = new FileOutputStream(file1);
								myOutWriter1 = new OutputStreamWriter(fOut1);

								file2 = new File(newFolder, "CpuUsage" + ".txt");
								file2.createNewFile();
								fOut2 = new FileOutputStream(file2);
								myOutWriter2 = new OutputStreamWriter(fOut2);

								// fOut.close();
							} catch (Exception ex) {
								System.out.println("ex: " + ex);
							}
						} catch (Exception e) {
							System.out.println("e: " + e);
						}
						try {
							transactions.createTable(t, filecreate);
							// transactions.inserttable(t, fileinsert);
						} catch (Exception e) {
							customToast("Error reading the files "
									+ e.getMessage());
						}

						ArrayList<Long> array = new ArrayList<Long>();

						for (int i = 0; i < repeatTime; i++) {				
							

							mySQLiteAdapter.openToWrite(flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);


							seconds1 = System.currentTimeMillis();
							mySQLiteAdapter.insert(fileinsert, flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index,
									i);
							seconds2 = System.currentTimeMillis();

							// execution time
							myOutWriter1.write("End=" + (seconds2 - seconds1)
									/ 1000 + " sec \n");

							// customToast("End"+(seconds2 - seconds1));
							// Log.d("End insert", "" + (seconds2 - seconds1));

							mySQLiteAdapter.close();
						}
						myOutWriter1.close();
						fOut1.close();

						myOutWriter2.close();
						fOut2.close();

					} catch (Exception ex) {
						customToast(ex.getMessage());
						Log.d("Exeption------------------------------",
								ex.getMessage());
						// mySQLiteAdapter.close();
					}
					// mySQLiteAdapter.close();
				} else
					customToast("Please select the file that contains the values that will be executed with the INSERT and CREATE table query");

			}
		});
		btn_testinsert.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if ((fileinsert != null) && (filecreate != null)) {
					try {
						try {
							newFolder = new File(Environment
									.getExternalStorageDirectory(), "Aaa");
							if (!newFolder.exists()) {
								newFolder.mkdir();
							}
							try {

								file1 = new File(newFolder, "ExecutionTime"
										+ ".txt");
								file1.createNewFile();
								fOut1 = new FileOutputStream(file1);
								myOutWriter1 = new OutputStreamWriter(fOut1);

								file2 = new File(newFolder, "CpuUsage" + ".txt");
								file2.createNewFile();
								fOut2 = new FileOutputStream(file2);
								myOutWriter2 = new OutputStreamWriter(fOut2);

								// fOut.close();
							} catch (Exception ex) {
								System.out.println("ex: " + ex);
							}
						} catch (Exception e) {
							System.out.println("e: " + e);
						}
						// read insert query from file
						try {
							transactions.createTable(t, filecreate);
							//transactions.inserttable(t, fileinsert);
						} catch (Exception e) {

							customToast("Error reading the files "
									+ e.getMessage());
						}

						ArrayList<Long> array = new ArrayList<Long>();
						// delete all elements and execute the insert's queries
						// timer from the beginning of the query as the end
						for (int ii = 0; ii < repeatTime; ii++) {
							
							mySQLiteAdapter.openToWrite(flag_journal,
									flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index);

							seconds1 = System.currentTimeMillis();
							
							// for (int j = 0; j < iterator; j++) {
						//	for (int i = 0; i < t.sqlinsert.size(); i++) {

								//mySQLiteAdapter.insert(fileinsert);
							 mySQLiteAdapter.queueAll(fileinsert,t.tableName.get(0),
									flag_journal, flag_sync, flag_auto_vacuum1,
									flag_auto_vacuum2, flag_cache, flag_index,
									false, 0);
							//}
							
							// }
							mySQLiteAdapter.close();
							seconds2 = System.currentTimeMillis();

							myOutWriter1.write("End=" + (seconds2 - seconds1)
									/ 1000 + " sec \n");
							mySQLiteAdapter.close();
						}
						
						myOutWriter1.close();
						fOut1.close();

						myOutWriter2.close();
						fOut2.close();
					} catch (Exception ex) {
						customToast(ex.getMessage());
						// mySQLiteAdapter.close();
					}
					// mySQLiteAdapter.close();
				} else
					customToast("Please select the file that contains the values that will be executed with the INSERT and CREATE table query");

			}
		});
		btn_multiread.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				File file = new File(Environment.getExternalStorageDirectory()
						+ "/Aaa/", "threadsResults.txt");
				if (file.exists())
					file.delete();
				for (int i = 0; i < threadNum; i++) {
					try {
						new multiread(mySQLiteAdapter, flag_journal, flag_sync,
								flag_auto_vacuum1, flag_auto_vacuum2,
								flag_index, flag_cache, i).start();
					} catch (Exception ex) {
						Log.d("Exeption", ex.getMessage());
					}
				}
			}
		});
	}

	// ////////////////////////////////////////////
	// ////////////////////////////////////////////
	// ////////////////////////////////////////////

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void dialog(String message, String title, int flag, final int item) {
		Insert_alert = new AlertDialog.Builder(this);
		Insert_alert.setTitle(title);
		Insert_alert.setMessage(message);
		if (flag == 1) {
			input = new EditText(this);
			Insert_alert.setView(input);
		}
		Insert_alert.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (item == 1) {

							String text = input.getText().toString();
							repeatTime = Integer.parseInt(text);
							Toast.makeText(getApplicationContext(),
									repeatTime + " Repeat time",
									Toast.LENGTH_LONG).show();
						} else if (item == 0) {
							String text = input.getText().toString();
							threadNum = Integer.parseInt(text);
							Toast.makeText(getApplicationContext(),
									threadNum + " Threads", Toast.LENGTH_LONG)
									.show();

						}
					}
				});

		Insert_alert.setNegativeButton("Clear",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
		Insert_alert.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.selectall:
			dialog("Give the repeat number", "Thread", 1, 0);

			break;
		case R.id.Fcreatetable:
			flag_Fcreate = true;
			flag_Fdelete = false;
			flag_Finsert = false;
			Intent intent = new Intent(getApplicationContext(),
					AndroidExplorer.class);
			startActivity(intent);
			break;
		case R.id.repeat_Insert:
			dialog("Give the repeat number", "Repeat", 1, 1);

			break;
		case R.id.Fdeletetable:
			flag_Fcreate = false;
			flag_Fdelete = true;
			flag_Finsert = false;
			Intent intent1 = new Intent(getApplicationContext(),
					AndroidExplorer.class);
			startActivity(intent1);
			break;
		case R.id.Finsertable:
			flag_Fcreate = false;
			flag_Fdelete = false;
			flag_Finsert = true;
			Intent intent11 = new Intent(getApplicationContext(),
					AndroidExplorer.class);
			startActivity(intent11);
			break;
		case R.id.pragma_settings:
			View checkBoxView = View.inflate(this, R.layout.checkbox, null);

			checkBoxjournal = (CheckBox) checkBoxView
					.findViewById(R.id.checkboxjournal);
			checkBoxsync = (CheckBox) checkBoxView
					.findViewById(R.id.checkboxsync);
			checkBoxVacuum1 = (CheckBox) checkBoxView
					.findViewById(R.id.checkboxvacuum1);
			checkBoxVacuum2 = (CheckBox) checkBoxView
					.findViewById(R.id.checkboxvacuum2);
			cache1000 = (CheckBox) checkBoxView.findViewById(R.id.cache);

			index = (CheckBox) checkBoxView.findViewById(R.id.index);

			if (flag_journal == true)
				checkBoxjournal.setChecked(true);
			else
				checkBoxjournal.setChecked(false);
			if (flag_sync == true)
				checkBoxsync.setChecked(true);
			else
				checkBoxsync.setChecked(false);
			if (flag_auto_vacuum1 == true)
				checkBoxVacuum1.setChecked(true);
			else
				checkBoxVacuum1.setChecked(false);
			if (flag_auto_vacuum2 == true)
				checkBoxVacuum2.setChecked(true);
			else
				checkBoxVacuum2.setChecked(false);

			if (flag_cache == true)
				cache1000.setChecked(true);
			else
				cache1000.setChecked(false);

			if (flag_index == true)
				index.setChecked(true);
			else
				index.setChecked(false);

			checkBoxjournal
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {

							// Save to shared preferences
						}
					});

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(" Pragma Settings ")
					.setView(checkBoxView)
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									if (checkBoxjournal.isChecked())
										flag_journal = true;
									else
										flag_journal = false;
									if (checkBoxsync.isChecked())
										flag_sync = true;
									else
										flag_sync = false;
									if (checkBoxVacuum1.isChecked()) {

										flag_auto_vacuum1 = true;
									} else
										flag_auto_vacuum1 = false;
									if (checkBoxVacuum2.isChecked()) {

										flag_auto_vacuum2 = true;
									} else
										flag_auto_vacuum2 = false;
									// /////////
									if (cache1000.isChecked()) {

										flag_cache = true;
									} else
										flag_cache = false;

									if (index.isChecked()) {

										flag_index = true;
									} else
										flag_index = false;
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).show();
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Insert_alert = new AlertDialog.Builder(this);

			Insert_alert.setTitle("Quit");
			Insert_alert.setMessage("Are you sure to quit?");

			Insert_alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							System.exit(0);
						}
					});

			Insert_alert.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});
			Insert_alert.show();

		}
		return super.onKeyDown(keyCode, event);
	}
}
// //////////////////////////////////
// /////////////////////////////////
