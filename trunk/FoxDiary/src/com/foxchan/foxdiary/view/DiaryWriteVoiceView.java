package com.foxchan.foxdiary.view;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxdiary.view.DiaryWriteView.MyHandler;
import com.foxchan.foxutils.data.DateUtils;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.FileUtils;

/**
 * 用声音记录日记的界面
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-4-30
 */
public class DiaryWriteVoiceView extends FakeActivity implements OnTouchListener {
	
	/** 状态：更新录音时长 */
	public static final int UPDATE_RECORD_LONG = 0;
	
	private DiaryWriteView diaryWriteView;
	private View layoutView;
	
	/** 播放录音的按钮 */
	private ImageView ivPlay;
	/** 停止播放的按钮 */
	private ImageView ivStop;
	/** 删除录音的按钮 */
	private ImageView ivDelete;
	
	/** 录音机的滚轮（左边） */
	private ImageView ivWheelLeft;
	/** 录音机的滚轮（右边） */
	private ImageView ivWheelRight;
	/** 录音机的滚轮的滚动动画（顺时针旋转） */
	private Animation aniWheel;
	/** 录音机的滚轮的滚动动画（逆时针旋转） */
	private Animation aniWheelReverse;
	/** 开始录音的按钮 */
	private ImageButton ibRecordingStart;
	/** 停止录音的按钮 */
	private ImageButton ibRecordingStop;
	/** 录音的时长 */
	private TextView tvRecordingLong;
	/** 录音的时长 */
	private long recordLong = 0;
	/** 录音的计时线程 */
	private RecordThread recordThread;
	
	//与录音相关的对象
	private MediaRecorder mediaRecorder;
	/** 音频文件的保存路径 */
	private String audioPath;
	/** 播放器对象 */
	private MediaPlayer mediaPlayer;
	/** 是否存在音频文件的标志 */
	private boolean isAudioFileExist = false;
	
	private MyHandler handler = new MyHandler(this);
	static class MyHandler extends Handler{
		WeakReference<DiaryWriteVoiceView> reference;
		
		public MyHandler(DiaryWriteVoiceView activity){
			this.reference = new WeakReference<DiaryWriteVoiceView>(activity);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			DiaryWriteVoiceView activity = reference.get();
			switch (msg.what) {
			case UPDATE_RECORD_LONG:
				activity.updateRecordLong();
				break;
			}
		}
	}

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
	public void onCreate(Bundle savedInstanceState) {
		//初始化相关的部件
		ivDelete = (ImageView)layoutView.findViewById(R.id.diary_write_voice_delete);
		ivPlay = (ImageView)layoutView.findViewById(R.id.diary_write_voice_play);
		ivStop = (ImageView)layoutView.findViewById(R.id.diary_write_voice_stop);
		ibRecordingStart = (ImageButton)layoutView.findViewById(R.id.diary_write_voice_start_record);
		ibRecordingStop = (ImageButton)layoutView.findViewById(R.id.diary_write_voice_stop_record);
		tvRecordingLong = (TextView)layoutView.findViewById(R.id.diary_write_voice_record_long);
		tvRecordingLong.setText(DateUtils.formatTimeLong(recordLong));
		
		recordThread = new RecordThread();
		recordThread.start();
		//绑定开始录音的按钮的事件
		ibRecordingStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recordLong = 0;
				recordThread.isRecording = true;
				ibRecordingStart.setVisibility(View.GONE);
				ibRecordingStop.setVisibility(View.VISIBLE);
				ivWheelLeft.startAnimation(aniWheelReverse);
				ivWheelRight.startAnimation(aniWheelReverse);
			}
		});
		//绑定结束录音的按钮的事件
		ibRecordingStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recordThread.isRecording = false;
				ibRecordingStart.setVisibility(View.VISIBLE);
				ibRecordingStop.setVisibility(View.GONE);
				ivWheelLeft.clearAnimation();
				ivWheelRight.clearAnimation();
			}
		});
		
		ivWheelLeft = (ImageView)layoutView.findViewById(R.id.diary_write_voice_record_wheel_left);
		ivWheelRight = (ImageView)layoutView.findViewById(R.id.diary_write_voice_record_wheel_right);
		aniWheel = AnimationUtils.loadAnimation(diaryWriteView, R.anim.record_wheel);
		aniWheelReverse = AnimationUtils.loadAnimation(diaryWriteView, R.anim.record_wheel_reverse);
		
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
				if(isAudioFileExist && mediaPlayer != null){
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
				if(isAudioFileExist && mediaPlayer != null){
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
	}
	
	/**
	 * 初始化录音相关的组件
	 */
	private void startRecording(){
		mediaRecorder = new MediaRecorder();
		//设置音频的来源
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		//设置声音的品质
		mediaRecorder.setAudioSamplingRate(Constants.THREE_GPP_AUDIO_QUENTITY_HIGH);
		//设置音频的输出格式
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		//设置音频的编码方式
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		//设置音频输出文件
		mediaRecorder.setOutputFile(audioPath);
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			e.printStackTrace();
		}
		try {
			mediaRecorder.start();
		} catch (RuntimeException e) {
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			FoxToast.showException(diaryWriteView, R.string.ex_writing_recorder, Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
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
	public boolean onTouch(View v, MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
//			if(v.getId() == R.id.diary_write_voice_circle && isAudioFileExist == false){
//				onRecording();
//			}
			break;
		case MotionEvent.ACTION_UP:
//			if(v.getId() == R.id.diary_write_voice_circle && isAudioFileExist == false){
//				onRecorded();
//			}
			break;
		}
		return false;
	}
	
	/**
	 * 正在录音中
	 */
	private void onRecording(){
		startRecording();
	}
	
	/**
	 * 完成录音
	 */
	private void onRecorded(){
		if(mediaRecorder != null){
			ivDelete.setVisibility(View.VISIBLE);
			ivPlay.setVisibility(View.VISIBLE);
			ivStop.setVisibility(View.VISIBLE);
		
			//停止并保存录音
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
			isAudioFileExist = true;
		} else {
		}
	}
	
	/**
	 * 回到开始录音之前
	 */
	private void beforeRecording(){
		ivDelete.setVisibility(View.INVISIBLE);
		ivPlay.setVisibility(View.INVISIBLE);
		ivStop.setVisibility(View.INVISIBLE);
	}

	public boolean isAudioFileExist() {
		return isAudioFileExist;
	}
	
	/**
	 * 更新录音的时间长度
	 */
	public void updateRecordLong(){
		recordLong++;
		tvRecordingLong.setText(DateUtils.formatTimeLong(recordLong));
	}
	
	/**
	 * 录音的线程类
	 * @author foxchan@live.cn
	 * @create 2013-7-10
	 */
	class RecordThread extends Thread{
		
		/** 是否进行录音 */
		private boolean isRecording = false;
		
		public void run(){
			while(true){
				if(isRecording){
					handler.sendEmptyMessage(UPDATE_RECORD_LONG);
				}
				try {
					Thread.sleep(1024);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
