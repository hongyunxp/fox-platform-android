package com.foxchan.foxui.demo.draggingablelist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.foxchan.foxui.core.R;
import com.foxchan.foxui.widget.lang.Draggingable;
import com.foxchan.foxui.widget.listener.OnItemDraggedListener;

public class DraggingableListViewDemo extends Activity implements OnItemDraggedListener {
	
	private ListView listView;
	private DraggingableListViewAdapter adapter;
	private List<String> strings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.string_list);
		//初始化数据
		strings = new ArrayList<String>();
		for(int i = 0; i < 55; i++){
			strings.add("选项" + i);
		}
		//初始化组件
		listView = (ListView)findViewById(R.id.string_list_listview);
		adapter = new DraggingableListViewAdapter(this, strings, this);
		adapter.setOnItemClickListener(new Draggingable.OnItemClickListener() {
			
			@Override
			public void onItemClick(Object obj) {
				
			}
		});
		//渲染界面
		listView.setAdapter(adapter);
	}

	@Override
	public void onDraggedToLeft(int deltaX, int deltaY, int maxDeltaX,
			int maxDeltaY, Object obj) {
		Toast.makeText(this, "onDraggedToLeft，当前控件被拖动的位置是：" + deltaX, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDraggedToRight(int deltaX, int deltaY, int maxDeltaX,
			int maxDeltaY, Object obj) {
		int position = (Integer)obj;
		int result = deltaX / (maxDeltaX / 4);
		if(result <= 0){
			Toast.makeText(this, strings.get(position) + "已经标志为已读。", Toast.LENGTH_SHORT).show();
		} else if(result <= 1){
			Toast.makeText(this, strings.get(position) + "已经标志为事件。", Toast.LENGTH_SHORT).show();
		} else if(result <= 2){
			Toast.makeText(this, strings.get(position) + "已经标志为待处理。", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, strings.get(position) + "已经被删除。", Toast.LENGTH_SHORT).show();
			strings.remove(position);
			adapter.notifyDataSetChanged();
		}
	}

}
