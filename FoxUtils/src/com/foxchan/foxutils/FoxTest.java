package com.foxchan.foxutils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.foxchan.foxutils.tool.FileUtils;
import com.foxchan.foxutils.tool.SdCardUtils;

public class FoxTest extends Activity {
	
	public static final String TAG = "FoxUtils";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
//		String fileName = "demo.txt";
//		String ext = FileUtils.getExt(fileName);
//		Log.d(TAG, "文件的拓展名是：" + ext);
//		
//		String[] filePaths = new String[]{
//				"study", "books", "notes"
//		};
//		String filePath = FileUtils.buildFileName(filePaths, fileName);
//		Log.d(TAG, "文件的保存路径是：" + filePath);
		
		testDeleteDir();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fox_test, menu);
		return true;
	}
	
	/**
	 * 删除文件夹下的所有文件的测试用例
	 */
	private void testDeleteDir(){
		String dirPath = FileUtils.buildFilePath(new String[]{
			SdCardUtils.getSdCardPath(), "FoxDiary"	
		});
		Log.d(TAG, "要删除的文件夹的路径是：" + dirPath);
		FileUtils.deleteDir(dirPath);
	}

}
