
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.sqlite.R;

public class AndroidExplorer extends ListActivity {

	private List<String> item = null;

	private List<String> path = null;

	private String root = "/sdcard";

	private TextView myPath;
	File file;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		myPath = (TextView) findViewById(R.id.path);

		getDir(root);

	}

	private void getDir(String dirPath)

	{

		myPath.setText("Location: " + dirPath);

		item = new ArrayList<String>();

		path = new ArrayList<String>();

		File f = new File(dirPath);

		File[] files = f.listFiles();

		if (!dirPath.equals(root))

		{

			item.add(root);

			path.add(root);

			item.add("../");

			path.add(f.getParent());

		}

		for (int i = 0; i < files.length; i++)

		{

			File file = files[i];

			path.add(file.getPath());

			if (file.isDirectory())

				item.add(file.getName() + "/");

			else

				item.add(file.getName());

		}

		ArrayAdapter<String> fileList =

		new ArrayAdapter<String>(this, R.layout.row, item);

		setListAdapter(fileList);

	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position,
			long id) {

		file = new File(path.get(position));

		if (file.isDirectory())

		{

			if (file.canRead())

				getDir(path.get(position));

			else

			{

				new AlertDialog.Builder(this)

				.setTitle("[" + file.getName() + "] folder can't be read!")

				.setPositiveButton("OK",

				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						// TODO Auto-generated method stub
						// Transactions.file = file.getAbsolutePath();
					}

				}).show();

			}

		}

		else

		{

			new AlertDialog.Builder(this)

			.setTitle("[" + file.getAbsolutePath() + "]")

			.setPositiveButton("OK",

			new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					// TODO Auto-generated method stub
					if (MainActivity.flag_Fcreate == true)
						MainActivity.filecreate = file.getAbsolutePath();
					else if (MainActivity.flag_Fdelete == true)
						MainActivity.filedelete = file.getAbsolutePath();
					else if (MainActivity.flag_Finsert == true)
						MainActivity.fileinsert = file.getAbsolutePath();

					finish();
				}

			}).show();

		}

	}

}