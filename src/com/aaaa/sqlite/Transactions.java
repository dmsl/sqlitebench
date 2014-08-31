
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
 * **PEDOMETER CODE was found freely available at a public domain at 
 * http://stackoverflow.com/questions/9895402/wp7-sdk-pedometer
 * 
 * ***The overall code about sensor usage is based heavily on WP8 dev samples.
 */
package com.aaaa.sqlite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.aaaa.sqlite.MainActivity.table_;
import android.util.Log;

public class Transactions {

	public Transactions() {

	}

	long average_time(ArrayList<Long> array) {
		long min = 0;
		long max = 0;
		long num = array.get(0);
		long sum = 0;
		for (int i = 0; i < array.size(); i++) {

			if (array.get(i) < num) {
				min = i;
				num = array.get(i);
			}
		}
		// Log.d("min", min + "");
		num = array.get(0);
		for (int i = 0; i < array.size(); i++) {

			if (array.get(i) > num) {
				max = i;
				num = array.get(i);
			}
		}
		// Log.d("max", max + "");
		for (int i = 0; i < array.size(); i++) {
			if ((i != min) && (i != max)) {
				// Log.d("Sum", "" + i);
				sum += array.get(i);
			}
		}

		/*
		 * for(int i=0;i<10;i++) { Log.d("Array List "+i, array.get(i)+""); }
		 */

		return sum;
	}

	void logcat(String text, long i) {
		Log.d(text, i + "");
	}

	// create table from file

	void createTable(table_ t, String filecreate) throws IOException {
		
		t.tableName.clear();
		t.table.clear();
		t.tableCounter=0;
		
		File myFile = new File(filecreate);
		FileInputStream fIn = new FileInputStream(myFile);
		String line;
		BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
		line = myReader.readLine();
		while (line != null) {
			t.tableName.add(line);
			line = myReader.readLine();
			t.table.add(line);
			line = myReader.readLine();
			t.tableCounter++;
		}
		myReader.close();
		Log.d("Create size file",t.table.size()+""+"|||"+t.tableCounter);
	}

	// create insert script

	void inserttable(table_ t, String fileinsert) throws IOException {
				
		t.sqlinsert.clear();
		
		File myFile = new File(fileinsert);
		FileInputStream fIn = new FileInputStream(myFile);
		String line;
		BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
		line = myReader.readLine();
		
		while (line != null) {
			t.sqlinsert.add(line);
			line = myReader.readLine();

		}
		myReader.close();
		Log.d("Insert size file",t.sqlinsert.size()+"");
	}

	void deletettable(table_ t, String filedelete) throws IOException {
		
		t.sqldelete.clear();
		
		File myFile = new File(filedelete);
		FileInputStream fIn = new FileInputStream(myFile);
		String line;
		BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
		line = myReader.readLine();

		while (line != null) {
			t.sqldelete.add(line);
			line = myReader.readLine();

		}
		myReader.close();
		Log.d("Delete size file",t.sqldelete.size()+"");
	}
}
