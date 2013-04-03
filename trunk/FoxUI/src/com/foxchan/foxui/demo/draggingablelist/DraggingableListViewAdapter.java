package com.foxchan.foxui.demo.draggingablelist;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foxchan.foxui.core.R;
import com.foxchan.foxui.widget.lang.Draggingable;
import com.foxchan.foxui.widget.lang.Draggingable.OnItemClickListener;
import com.foxchan.foxui.widget.listener.OnItemDraggedListener;
import com.foxchan.foxui.widget.listener.OnItemDraggingListener;

public class DraggingableListViewAdapter extends BaseAdapter {
	
	private List<String> arrays;
	private Activity activity;
	private LayoutInflater inflater;
	private OnItemDraggedListener onItemDraggedListener;
	private OnItemClickListener onItemClickListener;
	
	static final class ViewHolder{
		public RelativeLayout rlItem;
		public LinearLayout llItem;
		public TextView tvTitle;
		public Draggingable draggingable;
	}
	
	public DraggingableListViewAdapter(Activity activity, List<String> arrays,
			OnItemDraggedListener onItemDraggedListener) {
		this.activity = activity;
		this.arrays = arrays;
		this.inflater = LayoutInflater.from(activity);
		this.onItemDraggedListener = onItemDraggedListener;
	}
	
	@Override
	public int getCount() {
		return arrays.size();
	}

	@Override
	public Object getItem(int position) {
		return arrays.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.string_list_child_demo, null);
			holder = new ViewHolder();
			holder.llItem = (LinearLayout)convertView.findViewById(R.id.string_list_child_demo);
			holder.rlItem = (RelativeLayout)convertView.findViewById(R.id.string_list_child_demo_parent);
			holder.tvTitle = (TextView)convertView.findViewById(R.id.string_list_child_demo_title);
			//设置列表项的拖动事件
			holder.draggingable = new Draggingable(activity, holder.llItem, position);
			holder.draggingable.setOnItemDraggingListener(new OnItemDraggingListener() {

				@Override
				public void onDraggingToLeft(int deltaX, int deltaY,
						int maxDeltaX, int maxDeltaY, LinearLayout layout) {
					((RelativeLayout)layout.getParent()).setBackgroundColor(Color.BLACK);
				}

				@Override
				public void onDraggingToRight(int deltaX, int deltaY,
						int maxDeltaX, int maxDeltaY, LinearLayout layout) {
					int result = deltaX / (maxDeltaX / 4);
					if(result <= 0){
						((RelativeLayout)layout.getParent()).setBackgroundColor(Color.GREEN);
					} else if(result <= 1){
						((RelativeLayout)layout.getParent()).setBackgroundColor(Color.BLUE);
					} else if(result <= 2){
						((RelativeLayout)layout.getParent()).setBackgroundColor(Color.GRAY);
					} else {
						((RelativeLayout)layout.getParent()).setBackgroundColor(Color.RED);
					}
				}
				
			});
			holder.draggingable.setOnItemDraggedListener(onItemDraggedListener);
			holder.draggingable.setOnItemClickListener(onItemClickListener);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		String title = arrays.get(position);
		holder.draggingable.setObj(position);
		holder.tvTitle.setText(title);
		return convertView;
	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	
}
