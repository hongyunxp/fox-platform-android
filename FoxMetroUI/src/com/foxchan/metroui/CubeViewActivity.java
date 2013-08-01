package com.foxchan.metroui;

import com.foxchan.metroui.widget.CubeView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class CubeViewActivity extends Activity {
	
	private CubeView cubeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cube_view);
		cubeView = (CubeView)findViewById(R.id.email_cube_view);
		cubeView.setOnCubeStartListener(new CubeView.OnCubeStartListener() {
			
			@Override
			public void onCubeStart() {
				Toast.makeText(CubeViewActivity.this, "进入电子邮件的应用", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
