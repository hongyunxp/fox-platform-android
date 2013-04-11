package com.foxchan.foxui.demo.foxdrawerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxchan.foxui.core.R;
import com.foxchan.foxui.widget.lang.FoxDrawerActivity;

/**
 * 支持左右滑动的界面
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-3
 */
public class FoxDrawerViewDemo extends FoxDrawerActivity {
	
	/** 右边的菜单列表 */
	private ListView lvRightMenus;
	/** 右边菜单列表的菜单项 */
	private String[] rightMenus;
	/** 右边菜单的数据适配器 */
	private RightAdapter rightAdapter;
	
	/** 左边的菜单列表 */
	private ListView lvLeftMenus;
	/** 左边菜单列表的菜单项 */
	private String[] leftMenus;
	/** 左边菜单的数据适配器 */
	private LeftAdapter leftAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fox_drawer_view);
		
		//初始化FoxDrawer
		initFoxDrawer();
		
		//初始化数据
		rightMenus = new String[30];
		leftMenus = new String[30];
		for(int i = 0; i < 30; i++){
			rightMenus[i] = "右边的菜单" + i;
			leftMenus[i] = "左边的菜单" + i;
		}
		//初始化组件
		lvRightMenus = (ListView)findViewById(R.id.fox_drawer_view_right_listview);
		lvRightMenus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Toast.makeText(v.getContext(), rightMenus[position], Toast.LENGTH_SHORT).show();
			}
		});
		lvLeftMenus = (ListView)findViewById(R.id.fox_drawer_view_left_listview);
		lvLeftMenus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Toast.makeText(v.getContext(), leftMenus[position], Toast.LENGTH_SHORT).show();
			}
		});
		//渲染界面
		rightAdapter = new RightAdapter();
		lvRightMenus.setAdapter(rightAdapter);
		leftAdapter = new LeftAdapter();
		lvLeftMenus.setAdapter(leftAdapter);
	}
	
	/**
	 * 右边的菜单列表的数据适配器
	 * @author gulangxiangjie@gmail.com
	 * @create 2013-4-3
	 */
	private class LeftAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return leftMenus.length;
		}

		@Override
		public Object getItem(int position) {
			return leftMenus[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tvTitle = null;
			if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fox_drawer_view_child, null);
				tvTitle = (TextView)convertView.findViewById(R.id.title);
				convertView.setTag(tvTitle);
			} else {
				tvTitle = (TextView)convertView.getTag();
			}
			tvTitle.setText(leftMenus[position]);
			return convertView;
		}
		
	}
	
	/**
	 * 右边的菜单列表的数据适配器
	 * @author gulangxiangjie@gmail.com
	 * @create 2013-4-3
	 */
	private class RightAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return rightMenus.length;
		}

		@Override
		public Object getItem(int position) {
			return rightMenus[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tvTitle = null;
			if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fox_drawer_view_child, null);
				tvTitle = (TextView)convertView.findViewById(R.id.title);
				convertView.setTag(tvTitle);
			} else {
				tvTitle = (TextView)convertView.getTag();
			}
			tvTitle.setText(rightMenus[position]);
			return convertView;
		}
		
	}

	@Override
	protected int getScrollMode() {
		return TO_BOTH;
	}

	@Override
	protected int getLeftLayoutResource() {
		return R.id.fox_drawer_view_left;
	}

	@Override
	protected int getMiddleLayoutResource() {
		return R.id.fox_drawer_view_middle;
	}

	@Override
	protected int getRightLayoutResource() {
		return R.id.fox_drawer_view_right;
	}

}
