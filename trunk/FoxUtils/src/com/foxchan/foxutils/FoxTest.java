package com.foxchan.foxutils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FoxTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fox_test, menu);
		return true;
	}

}
