package com.foxchan.foxui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.foxchan.foxui.core.R;
import com.foxchan.foxui.widget.lang.AutoChangeLineViewGroup;

public class FoxTest extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_change_line);
		AutoChangeLineViewGroup viewGroup = (AutoChangeLineViewGroup)findViewById(R.id.box_container);
		viewGroup.setChildViewMargin(10);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fox_test, menu);
		return true;
	}

}
