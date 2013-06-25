package com.foxchan.foxui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foxchan.foxui.core.R;
import com.foxchan.foxui.widget.lang.RefreshListView;

/**
 * 手势驱动事件的列表的测试用例
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-24
 */
public class RefreshListViewTest extends Activity {
	
	private RefreshListView lv;
	private String[] arrays;
	private ListViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refresh_listview);
		
		arrays = new String[20];
		for(int i = 0; i < 20; i++){
			arrays[i] = "第" + i + "项";
		}
		lv = (RefreshListView)findViewById(R.id.refresh_listview);
		adapter = new ListViewAdapter();
		lv.setAdapter(adapter);
	}
	
	class ListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return arrays.length;
		}

		@Override
		public Object getItem(int pos) {
			return (String)arrays[pos];
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			TextView title = null;
			if(convertView == null){
				convertView = LayoutInflater.from(RefreshListViewTest.this)
						.inflate(R.layout.string_list_child_demo, null);
				title = (TextView)convertView.findViewById(R.id.string_list_child_demo_title);
				convertView.setTag(title);
			} else {
				title = (TextView)convertView.getTag();
			}
			title.setText(arrays[pos]);
			return convertView;
		}
		
	}

}
