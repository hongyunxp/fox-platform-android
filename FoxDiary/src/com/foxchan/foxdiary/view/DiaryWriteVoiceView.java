package com.foxchan.foxdiary.view;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foxchan.foxdiary.core.R;
import com.foxchan.foxdiary.core.widgets.FakeActivity;
import com.foxchan.foxdiary.core.widgets.FoxToast;
import com.foxchan.foxdiary.utils.Constants;
import com.foxchan.foxutils.data.DateUtils;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.FileUtils;

/**
 * 用声音记录日记的界面
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-4-30
 */
public class DiaryWriteVoiceView extends FakeActivity {
	
	/** 状态：更新录音时长 */
	public static final int UPDATE_RECORD_LONG = 0;
	
	private DiaryWriteView diaryWriteView;
	private View layoutView;
	
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
	/** 录音记录的界面 */
	private RelativeLayout rlRecord;
	/** 录音记录的时长 */
	private TextView tvRecordLong;
	/** 删除当前的录音记录 */
	private ImageButton ibDeleteRecord;
	/** 播放当前的录音记录 */
	private ImageButton ibPlayRecord;
	/** 停止播放当前的录音记录 */
	private ImageButton ibShutdownRecord;
	
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
		ibRecordingStart = (ImageButton)layoutView.findViewById(R.id.diary_write_voice_start_record);
		ibRecordingStop = (ImageButton)layoutView.findViewById(R.id.diary_write_voice_stop_record);
		tvRecordingLong = (TextView)layoutView.findViewById(R.id.diary_write_voice_record_long);
		tvRecordingLong.setText(DateUtils.formatTimeLong(recordLong));
		
		ivWheelLeft = (ImageView)layoutView.findViewById(R.id.diary_write_voice_record_wheel_left);
		ivWheelRight = (ImageView)layoutView.findViewById(R.id.diary_write_voice_record_wheel_right);
		aniWheel = AnimationUtils.loadAnimation(diaryWriteView, R.anim.record_wheel);
		aniWheelReverse = AnimationUtils.loadAnimation(diaryWriteView, R.anim.record_wheel_reverse);
		recordThread = new RecordThread();
		recordThread.start();
		//绑定开始录音的按钮的事件
		ibRecordingStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recordLong = 0;
				//开始录音
				try {
					startRecording();
					recordThread.isRecording = true;
					ibRecordingStart.setVisibility(View.GONE);
					ibRecordingStop.setVisibility(View.VISIBLE);
					ivWheelLeft.startAnimation(aniWheelReverse);
					ivWheelRight.startAnimation(aniWheelReverse);
					//隐藏录音文件标志
					rlRecord.setVisibility(View.INVISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
				recordLong = -1;
				tvRecordLong.setText(tvRecordingLong.getText());
				tvRecordingLong.setText(R.string.time_temp_zero);
				rlRecord.setVisibility(View.VISIBLE);
				//停止录音
				stopRecording();
			}
		});
		
		rlRecord = (RelativeLayout)layoutView.findViewById(R.id.diary_write_voice_record);
		tvRecordLong = (TextView)layoutView.findViewById(R.id.diary_write_voice_record_long_text);
		ibDeleteRecord = (ImageButton)layoutView.findViewById(R.id.diary_write_voice_delete);
		ibPlayRecord = (ImageButton)layoutView.findViewById(R.id.diary_write_voice_play);
		ibShutdownRecord = (ImageButton)layoutView.findViewById(R.id.diary_write_voice_shutdown);
		
		//绑定删除录音的按钮的事件
		ibDeleteRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isAudioFileExist = false;
				rlRecord.setVisibility(View.INVISIBLE);
			}
		});
		//绑定播放录音的按钮的事件
		ibPlayRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isAudioFileExist && mediaPlayer != null){
					initMediaPlayer();
					try {
						mediaPlayer.prepare();
						mediaPlayer.start();
						//启动转轮的动画
						ivWheelLeft.startAnimation(aniWheel);
						ivWheelRight.startAnimation(aniWheel);
						recordLong = 0;
						//显示停止播放的按钮
						ibPlayRecord.setVisibility(View.GONE);
						ibShutdownRecord.setVisibility(View.VISIBLE);
						recordThread.isRecording = true;
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		//绑定停止播放按钮的事件
		ibShutdownRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isAudioFileExist && mediaPlayer != null){
					mediaPlayer.stop();
					//启动转轮的动画
					ivWheelLeft.clearAnimation();
					ivWheelRight.clearAnimation();
					//显示停止播放的按钮
					ibPlayRecord.setVisibility(View.VISIBLE);
					ibShutdownRecord.setVisibility(View.GONE);
					recordLong = -1;
					recordThread.isRecording = false;
					tvRecordingLong.setText(R.string.time_temp_zero);
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
	private void startRecording() throws Exception{
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
			mediaRecorder.start();
		} catch (Exception e) {
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			FoxToast.showException(diaryWriteView,
					R.string.ex_writing_recorder, Toast.LENGTH_SHORT);
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 完成录音
	 */
	private void stopRecording(){
		if(mediaRecorder != null){
			//停止并保存录音
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
			isAudioFileExist = true;
		} else {
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
				//更新滚轮的动画状态
				ivWheelLeft.clearAnimation();
				ivWheelRight.clearAnimation();
				recordLong = -1;
				recordThread.isRecording = false;
				tvRecordingLong.setText(R.string.time_temp_zero);
				ibPlayRecord.setVisibility(View.VISIBLE);
				ibShutdownRecord.setVisibility(View.GONE);
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
