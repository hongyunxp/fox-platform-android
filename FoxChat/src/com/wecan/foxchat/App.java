package com.wecan.foxchat;

import com.wecan.foxchat.ui.SmsView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class App extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        toMainActivity();
    }

    /**
     * 跳转到程序的主界面
     */
    private void toMainActivity() {
		Intent intent = new Intent(this, SmsView.class);
		startActivity(intent);
		finish();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_app, menu);
        return true;
    }
}
