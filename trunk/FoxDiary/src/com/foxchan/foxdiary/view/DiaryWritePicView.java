package com.foxchan.foxdiary.view;

import java.io.File;

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
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.BitmapUtils;
import com.foxchan.foxutils.tool.FileUtils;

/**
 * 图片（从相册）记录日记的界面
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-4-30
 */
public class DiaryWritePicView extends FakeActivity {
	
	private DiaryWriteView diaryWriteView;
	private View layoutView;
	
	/** 裁减后的图片 */
	private ImageView ivPhoto;
	/** 从相册中选择图片的按钮 */
	private ImageView ivAlbum;
	/** 从相机中拍摄照片的按钮 */
	private ImageView ivCamara;
	/** 删除按钮 */
	private ImageView ivDelete;

	public DiaryWritePicView(DiaryWriteView diaryWriteView) {
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
	public void onCreate(Bundle savedInstanceState) {
		//初始化组件
		ivDelete = (ImageView)layoutView.findViewById(R.id.diary_write_pic_delete);
		ivCamara = (ImageView)layoutView.findViewById(R.id.diary_write_pic_from_camara);
		ivAlbum = (ImageView)layoutView.findViewById(R.id.diary_write_pic_from_album);
		ivPhoto = (ImageView)layoutView.findViewById(R.id.diary_write_pic_main);
		
		//绑定从相册中选择图片的按钮的事件
		ivAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				diaryWriteView.startActivityForResult(intent, DiaryWriteView.ACTIVITY_CODE_IMAGE_FROM_ALBUM);
			}
		});
		
		//绑定从相机中拍摄照片的按钮的事件
		ivCamara.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//设置临时的照片文件
				String tempPhotoPath = Constants.buildDiaryTempImagePath();
				File tempPhoto = new File(tempPhotoPath);
				Uri uri = Uri.fromFile(tempPhoto);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, false);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				diaryWriteView.startActivityForResult(intent, DiaryWriteView.ACTIVITY_CODE_IMAGE_FROM_CAMARA);
			}
		});
		
		//绑定删除当前图片的按钮的事件
		ivDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(diaryWriteView.getImage() != null){
					diaryWriteView.setImage(null);
					//将预览图片设置为默认样式
					ivPhoto.setImageResource(R.drawable.demo_pic2);
				}
				if(!StringUtils.isEmpty(diaryWriteView.getImageName())){
					diaryWriteView.setImageName(null);
				}
				if(!StringUtils.isEmpty(diaryWriteView.getImagePath())){
					diaryWriteView.setImagePath(null);
				}
			}
		});
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
		intentCarema.putExtra("noFaceDetection", true);//不需要人脸识别功能
//		intentCarema.putExtra("circleCrop", "");// 设定此方法选定区域会是圆形区域
		// aspectX aspectY是宽高比例
		intentCarema.putExtra("aspectX", 1);
		intentCarema.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片的宽高
		intentCarema.putExtra("outputX", 380);
		intentCarema.putExtra("outputY", 380);
		intentCarema.putExtra("return-data", true);
		diaryWriteView.startActivityForResult(intentCarema,
				DiaryWriteView.ACTIVITY_CODE_IMAGE_CUT);
	}
	
	/**
	 * 保存裁剪后的图片
	 * @param picIntent
	 */
	public void setPicToView(Intent picIntent){
		Bundle bundle = picIntent.getExtras();
		if(bundle != null){
			Bitmap picTemp = bundle.getParcelable("data");
			Bitmap picTemp4Show = BitmapUtils.getRoundedCornerBitmap(picTemp, 190.0f);
			String imagePath = FileUtils.buildFilePath(new String[]{
					Constants.APP_RESOURCE, Constants.IMAGES
			});
			String imageName = StringUtils.getUUID();
			diaryWriteView.setImagePath(imagePath);
			diaryWriteView.setImage(picTemp);
			diaryWriteView.setImageName(imageName);
			//显示图片
			Drawable drawable = new BitmapDrawable(diaryWriteView.getResources(), picTemp4Show);
			ivPhoto.setBackgroundDrawable(drawable);
		}
	}

}
