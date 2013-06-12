package com.foxchan.foxdiary.view;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.FileUtils;

/**
 * 用声音记录日记的界面
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-4-30
 */
public class DiaryWriteVoiceView extends FakeActivity implements OnTouchListener {
	
	private DiaryWriteView diaryWriteView;
	private View layoutView;
	
	/** 录音的圆形按钮 */
	private LinearLayout llRecord;
	/** 录音标志 */
	private ImageView ivMic;
	/** 录音的说明文字 */
	private TextView tvRecordStatus;
	/** 播放录音的按钮 */
	private ImageView ivPlay;
	/** 停止播放的按钮 */
	private ImageView ivStop;
	/** 删除录音的按钮 */
	private ImageView ivDelete;
	
	//与录音相关的对象
	private MediaRecorder mediaRecorder;
	/** 音频文件的保存路径 */
	private String audioPath;
	/** 播放器对象 */
	private MediaPlayer mediaPlayer;
	/** 是否存在音频文件的标志 */
	private boolean isAudioFileExist = false;

	public DiaryWriteVoiceView(DiaryWriteView diaryWriteView) {
		this.diaryWriteView = diaryWriteView;
	}

	@Override
	public View layoutView() {
		if(layoutView == null){
			layoutView = diaryWriteView.getLayoutInflater().inflate(R.layout.diary_write_voice, null);
		}
		return layoutView;
	}

	@Override
	public void onCreate() {
		//初始化相关的部件
		ivDelete = (ImageView)layoutView.findViewById(R.id.diary_write_voice_delete);
		ivPlay = (ImageView)layoutView.findViewById(R.id.diary_write_voice_play);
		ivStop = (ImageView)layoutView.findViewById(R.id.diary_write_voice_stop);
		tvRecordStatus = (TextView)layoutView.findViewById(R.id.diary_write_voice_status);
		llRecord = (LinearLayout)layoutView.findViewById(R.id.diary_write_voice_circle);
		ivMic = (ImageView)layoutView.findViewById(R.id.diary_write_voice_mic);
		
		//绑定删除录音的按钮的事件
		ivDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isAudioFileExist = false;
				beforeRecording();
			}
		});
		//绑定播放录音的按钮的事件
		ivPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isAudioFileExist){
					initMediaPlayer();
					try {
						mediaPlayer.prepare();
						mediaPlayer.start();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		//绑定停止播放按钮的事件
		ivStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isAudioFileExist){
					mediaPlayer.stop();
				}
			}
		});
		//初始化音频录制相关的组件和播放录音相关的组件
		//设置音频文件的保存路径和名称
		File audioFile = new File(Constants.buildDiaryAudioPath());
		if(!audioFile.exists()){
			audioFile.mkdirs();
		}
		audioPath = FileUtils.buildFileName(new String[]{Constants.buildDiaryAudioPath()}, 
				StringUtils.getUUID() + ".amr");
		diaryWriteView.setVoicePath(audioPath);
		initMediaPlayer();
		
		llRecord.setOnTouchListener(this);
	}
	
	/**
	 * 初始化录音相关的组件
	 */
	private void startRecording(){
		mediaRecorder = new MediaRecorder();
		//设置音频的来源
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		//设置音频的输出格式
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		//设置音频的编码方式
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		//设置音频输出文件
		mediaRecorder.setOutputFile(audioPath);
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaRecorder.start();
	}
	
	/**
	 * 初始化媒体播放器
	 */
	private void initMediaPlayer(){
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(audioPath);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.seekTo(0);
				mp.stop();
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
		if(mediaRecorder != null){
			mediaRecorder.release();
			mediaRecorder = null;
		}
		if(mediaPlayer == null){
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(v.getId() == R.id.diary_write_voice_circle && isAudioFileExist == false){
				onRecording();
			}
			break;
		case MotionEvent.ACTION_UP:
			if(v.getId() == R.id.diary_write_voice_circle && isAudioFileExist == false){
				onRecorded();
			}
			break;
		}
		return false;
	}
	
	/**
	 * 正在录音中
	 */
	private void onRecording(){
		tvRecordStatus.setText(R.string.diary_write_recording);
		startRecording();
	}
	
	/**
	 * 完成录音
	 */
	private void onRecorded(){
		tvRecordStatus.setText(R.string.diary_write_press_to_record);
		tvRecordStatus.setVisibility(View.GONE);
		ivMic.setImageResource(R.drawable.icon_voice_wave_229_normal);
		ivDelete.setVisibility(View.VISIBLE);
		ivPlay.setVisibility(View.VISIBLE);
		ivStop.setVisibility(View.VISIBLE);
		
		//停止并保存录音
		mediaRecorder.stop();
		mediaRecorder.release();
		mediaRecorder = null;
		isAudioFileExist = true;
	}
	
	/**
	 * 回到开始录音之前
	 */
	private void beforeRecording(){
		tvRecordStatus.setText(R.string.diary_write_press_to_record);
		tvRecordStatus.setVisibility(View.VISIBLE);
		ivMic.setImageResource(R.drawable.icon_white_mic_229_normal);
		ivDelete.setVisibility(View.INVISIBLE);
		ivPlay.setVisibility(View.INVISIBLE);
		ivStop.setVisibility(View.INVISIBLE);
	}

}
