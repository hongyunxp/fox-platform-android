package com.foxchan.foxdiary.view;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.lezhixing.foxdb.core.FoxDB;
import cn.com.lezhixing.foxdb.core.Session;

import com.foxchan.foxdiary.adapter.DiaryLineAdapter;
import com.foxchan.foxdiary.adapter.DiaryLineAdapter.NodeListener;
import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.entity.Diary;
import com.foxchan.foxdiary.utils.Constants;

/**
 * 日记时间线界面
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
public class DiaryLineView extends Activity {
	
	/** 日记的数据集 */
	private List<Diary> diaries;
	/** 日记的列表 */
	private ListView lvDiarys;
	/** 日记列表的数据适配器 */
	private DiaryLineAdapter diaryLineAdapter;
	
	private FoxDB db;
	private TextView tvContent;
	private ImageView ivPhoto;
	private LinearLayout llBalloon;
	private ImageView ivNode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_line);
		//初始化组件和数据
		db = FoxDB.create(this, Constants.DIARY_DB_NAME, Constants.DIARY_DB_VERSION);
		initDatas();
		initWidgets();
	}

	/**
	 * 初始化组件
	 */
	private void initWidgets() {
		lvDiarys = (ListView)findViewById(R.id.diary_line_listview);
		lvDiarys.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				
			}
		});
		diaryLineAdapter = new DiaryLineAdapter(this, diaries);
		diaryLineAdapter.setNodeListener(new NodeListener() {
			
			@Override
			public void share(int position) {
				Diary diary = diaries.get(position);
				Toast.makeText(DiaryLineView.this, "分享的日记的标题是：" + diary.getTitle(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void edit(int position) {
				Diary diary = diaries.get(position);
				Toast.makeText(DiaryLineView.this, "编辑的日记的标题是：" + diary.getTitle(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void delete(int position) {
				Diary diary = diaries.get(position);
				Toast.makeText(DiaryLineView.this, "删除的日记的标题是：" + diary.getTitle(), Toast.LENGTH_SHORT).show();
			}
		});
		lvDiarys.setAdapter(diaryLineAdapter);
		
		//初始化添加日记的节点
		tvContent = (TextView)findViewById(R.id.diary_line_content);
		ivPhoto = (ImageView)findViewById(R.id.diary_line_photo);
		llBalloon = (LinearLayout)findViewById(R.id.diary_line_balloon);
		ivNode = (ImageView)findViewById(R.id.diary_line_node);
		
		ivPhoto.setVisibility(View.GONE);
		tvContent.setText(getString(R.string.diary_line_add));
		ivNode.setImageResource(R.drawable.icon_plus_small);
		llBalloon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toDiaryWriteView();
			}
		});
	}
	
	/**
	 * 跳转到写日记的界面
	 */
	private void toDiaryWriteView() {
		Intent intent = new Intent(this, DiaryWriteView.class);
		startActivity(intent);
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		Session session = db.openSession();
		diaries = session.list(Diary.class);
	}

}
