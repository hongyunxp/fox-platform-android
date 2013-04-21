package com.foxchan.foxdiary.core;

import com.foxchan.foxdiary.entity.TimeLineNodeStyle;
import com.foxchan.foxdiary.utils.BitmapUtils;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppStart extends Activity {
	
	private TextView time;
	private ImageView node;
	private LinearLayout balloon;
	private ImageView ivPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_line_item);
		
		time = (TextView)findViewById(R.id.diary_line_item_time);
		node = (ImageView)findViewById(R.id.diary_line_node);
		balloon = (LinearLayout)findViewById(R.id.diary_line_balloon);
		ivPhoto = (ImageView)findViewById(R.id.diary_line_photo);
		
		TimeLineNodeStyle style = new TimeLineNodeStyle(TimeLineNodeStyle.STYLE_RED);
		time.setTextColor(getResources().getColor(style.getTimeColor()));
		node.setBackgroundResource(style.getNodeBg());
		balloon.setBackgroundResource(style.getBalloonBg());
		Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.demo_pic1);
		photo = BitmapUtils.getRoundedCornerBitmap(photo, 57.5f);
		ivPhoto.setImageBitmap(photo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_start, menu);
		return true;
	}

}
