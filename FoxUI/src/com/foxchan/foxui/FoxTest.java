package com.foxchan.foxui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.foxchan.foxui.core.R;
import com.foxchan.foxui.widget.lang.AutoChangeLineViewGroup;
import com.foxchan.foxui.widget.lang.CardsSwitcher;

public class FoxTest extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		testCardsSwitcher();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fox_test, menu);
		return true;
	}
	
	/**
	 * 测试自动换行的控件
	 */
	protected void testAutoChangeLineViewGroup(){
		setContentView(R.layout.auto_change_line);
		AutoChangeLineViewGroup viewGroup = (AutoChangeLineViewGroup)findViewById(R.id.box_container);
		viewGroup.setChildViewMargin(10);
	}
	
	/**
	 * 测试卡片切换控件
	 */
	protected void testCardsSwitcher(){
		setContentView(R.layout.cards_switcher_test);
		CardsSwitcher cardsSwitcher = (CardsSwitcher)findViewById(R.id.cards_switcher);
		
	}

}
