package com.foxchan.foxui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
	private List<String> arrays;
	private ListViewAdapter adapter;
	private int index = 0;
	private int i = 0;
	private boolean isRefresh = true;
	
	private MyHandler handler = new MyHandler(this);
	static class MyHandler extends Handler{
		WeakReference<RefreshListViewTest> reference;
		
		public MyHandler(RefreshListViewTest activity){
			this.reference = new WeakReference<RefreshListViewTest>(activity);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			RefreshListViewTest activity = reference.get();
			switch (msg.what) {
			case 0:
				activity.lv.refreshingDataComplete();
				Toast.makeText(activity, "数据刷新成功！！", Toast.LENGTH_SHORT).show();
				activity.isRefresh = false;
				activity.i = 0;
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refresh_listview);
		
		arrays = new ArrayList<String>();
		for(int i = 0; i < 20; i++){
			arrays.add("第" + i + "项");
		}
		lv = (RefreshListView)findViewById(R.id.refresh_listview);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int postion,
					long id) {
				Toast.makeText(RefreshListViewTest.this, "哈哈，这是第" + postion + "个选项", Toast.LENGTH_SHORT).show();
			}
		});
		lv.setOnTaskDoingListener(new RefreshListView.OnTaskDoingListener() {

			@Override
			public void refreshingData(RefreshListView view) {
				isRefresh = true;
				new Thread(){
					public void run(){
						while(isRefresh){
							if(i < 1){
								i++;
							} else {
								handler.sendEmptyMessage(0);
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}.start();
			}

			@Override
			public void loadMoreData(RefreshListView view) {
				if(index < 3){
					List<String> moreData = new ArrayList<String>();
					for(int i = 0; i < 10; i++){
						moreData.add("新标签" + i);
					}
					arrays.addAll(moreData);
					adapter.notifyDataSetChanged();
					if(index == 2){
						view.loadMoreDataComplete();
					}
					index++;
					Log.d("cqm", "数据集的数量是" + arrays.size());
				}
			}
			
		});
		
		adapter = new ListViewAdapter();
		lv.setAdapter(adapter);
	}
	
	class ListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return arrays.size();
		}

		@Override
		public Object getItem(int pos) {
			return (String)arrays.get(pos);
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
			title.setText(arrays.get(pos));
			return convertView;
		}
		
	}

}
