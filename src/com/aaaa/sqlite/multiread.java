
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.spi.CharsetProvider;
import java.util.ArrayList;

import org.apache.http.impl.io.ChunkedOutputStream;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

public class multiread extends Thread {
	SQLiteAdapter newSqLiteAdapter;
	int threadNum;

	public multiread(SQLiteAdapter mySQLiteAdapter, boolean flag_journal,
			boolean flag_sync, boolean flag_auto_vacuum1,
			boolean flag_auto_vacuum2, boolean flag_cache,
			boolean flag_index, int thrNum) {
		newSqLiteAdapter = mySQLiteAdapter;
		threadNum = thrNum;
	}

	public void run() {
		newSqLiteAdapter.openToRead();
		long seconds1 = 0;
		long seconds2 = 0;
		Cursor cur = null;
		Transactions transactions = new Transactions();
		ArrayList<Long> array = new ArrayList<Long>();
		for (int i = 0; i < 10; i++) {
			seconds1 = System.currentTimeMillis();
			//cur = newSqLiteAdapter.queueAll("dammyTable", false, false, false,
		//			false, false, false, true, threadNum);
			seconds2 = System.currentTimeMillis();
			array.add(seconds2 - seconds1);
		}
	//	newSqLiteAdapter.close();
		FileOutputStream fos = null;
		try {
			byte[] data = new String("\nTotal time: "+transactions.average_time(array) / 8
					+ "ms  Thread Id: " + currentThread().getId() + " I Have read "
					+ cur.getCount()).getBytes();
			
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/Aaa/", "threadsResults.txt");
			
			FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
			
			FileLock lock = channel.lock();
			
			try {
				lock = channel.tryLock();
			} catch (OverlappingFileLockException e) {
				
			}
			
			
			fos = new FileOutputStream(file,true);
			fos.write(data);
			
			fos.flush();
			
			lock.release();
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}