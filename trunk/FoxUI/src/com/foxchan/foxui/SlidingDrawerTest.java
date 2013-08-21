package com.foxchan.foxui;

import com.foxchan.foxui.core.R;
import com.foxchan.foxui.widget.lang.SlidingDrawer;

import android.app.Activity;
import android.os.Bundle;

/**
 * 滑动的抽屉测试类
 * @create 2013年8月21日
 * @author foxchan@live.cn
 * @version 1.0.0
 */
public class SlidingDrawerTest extends Activity {
	
	private SlidingDrawer slidingDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fox_drawer_view);
		slidingDrawer = (SlidingDrawer)findViewById(R.id.fox_drawer_view);
		slidingDrawer.setSlidingDirection(SlidingDrawer.FROM_BOTH);
		slidingDrawer.setScrollType(SlidingDrawer.SCROLL_PUSH);
	}

}
