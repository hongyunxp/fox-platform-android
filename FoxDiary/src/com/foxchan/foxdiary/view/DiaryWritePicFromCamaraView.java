package com.foxchan.foxdiary.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;

/**
 * 图片记录日记的界面
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-4-30
 */
public class DiaryWritePicFromCamaraView extends FakeActivity {
	
	private DiaryWriteView diaryWriteView;
	private View layoutView;
	
	/** 裁减后的图片 */
	private ImageView ivPhoto;
	/** 编辑按钮 */
	private ImageView ivEdit;
	/** 删除按钮 */
	private ImageView ivDelete;

	public DiaryWritePicFromCamaraView(DiaryWriteView diaryWriteView) {
		this.diaryWriteView = diaryWriteView;
	}

	@Override
	public View layoutView() {
		if(layoutView == null){
			layoutView = diaryWriteView.getLayoutInflater().inflate(R.layout.diary_write_pic, null);
		}
		return layoutView;
	}

	@Override
	public void onCreate() {
		//初始化组件
		ivDelete = (ImageView)layoutView.findViewById(R.id.diary_write_pic_delete);
		ivEdit = (ImageView)layoutView.findViewById(R.id.diary_write_pic_edit);
		ivPhoto = (ImageView)layoutView.findViewById(R.id.diary_write_pic_main);
		
		//绑定编辑按钮的事件
		ivEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				diaryWriteView.startActivityForResult(intent, DiaryWriteView.ACTIVITY_CODE_IMAGE_FROM_ALBUM);
			}
		});
	}

	@Override
	public void onRestart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}
	
	/**
	 * 裁剪图片的方法
	 * @param uri
	 */
	public void startPicCut(Uri uri) {
		Intent intentCarema = new Intent("com.android.camera.action.CROP");
		intentCarema.setDataAndType(uri, "image/*");
		intentCarema.putExtra("crop", true);
		// intentCarema.putExtra("scale", false);
		// intentCarema.putExtra("noFaceDetection", true);//不需要人脸识别功能
		// intentCarema.putExtra("circleCrop", "");//设定此方法选定区域会是圆形区域
		// aspectX aspectY是宽高比例
		intentCarema.putExtra("aspectX", 1);
		intentCarema.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片的宽高
		intentCarema.putExtra("outputX", 150);
		intentCarema.putExtra("outputY", 150);
		intentCarema.putExtra("return-data", true);
		diaryWriteView.startActivityForResult(intentCarema, DiaryWriteView.ACTIVITY_CODE_IMAGE_CUT);
	}
	
	/**
	 * 保存裁剪后的图片
	 * @param picIntent
	 */
	public void setPicToView(Intent picIntent){
		Bundle bundle = picIntent.getExtras();
		if(bundle != null){
			Bitmap picTemp = bundle.getParcelable("data");
			Drawable drawable = new BitmapDrawable(diaryWriteView.getResources(), picTemp);
			ivPhoto.setBackgroundDrawable(drawable);
		}
	}

}
