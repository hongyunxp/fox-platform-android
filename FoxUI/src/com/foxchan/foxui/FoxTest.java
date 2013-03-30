package com.foxchan.foxui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import com.foxchan.foxui.core.R;
import com.foxchan.foxui.widget.lang.Draggingable;
import com.foxchan.foxui.widget.listener.OnItemDragListener;

public class FoxTest extends Activity implements OnItemDragListener {
	
	private Draggingable draggingable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		draggingable = new Draggingable();
		draggingable.setOnItemDragListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fox_test, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		draggingable.onDraging(e, null);
		return super.onTouchEvent(e);
	}

	@Override
	public void onDraggingToLeft(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDraggingToRight(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDraggedToLeft(View view) {
		Log.d("cqm", "控件被拉到了左边。");
	}

	@Override
	public void onDraggedToRight(View view) {
		Log.d("cqm", "控件被拉到了右边。");
	}

}
