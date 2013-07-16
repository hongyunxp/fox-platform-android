package com.foxchan.foxdiary.entity;

import android.util.SparseArray;

import com.foxchan.foxdiary.core.R;

/**
 * 心情的集合
 * @author foxchan@live.cn
 * @create 2013-7-16
 */
public class Emotions {
	
	/** 心情对象的集合-键值采用RadioButton的id号 */
	private static SparseArray<Emotion> emotionMap;
	/** 心情对象的集合-键值采用表情对象的id号 */
	private static SparseArray<Emotion> emotionMapById;
	
	static{
		emotionMap = new SparseArray<Emotion>();
		emotionMap.put(R.id.rb_emotion_xiao, new Emotion(0, R.drawable.emotion_xiao_normal));
		emotionMap.put(R.id.rb_emotion_nu, new Emotion(1, R.drawable.emotion_nu_normal));
		emotionMap.put(R.id.rb_emotion_ku, new Emotion(2, R.drawable.emotion_ku_normal));
		emotionMap.put(R.id.rb_emotion_ganga, new Emotion(3, R.drawable.emotion_ganga_normal));
		emotionMap.put(R.id.rb_emotion_ai, new Emotion(4, R.drawable.emotion_ai_normal));
		emotionMap.put(R.id.rb_emotion_gandong, new Emotion(5, R.drawable.emotion_gandong_normal));
		
		emotionMapById = new SparseArray<Emotion>();
		emotionMapById.put(0, new Emotion(0, R.drawable.emotion_xiao_normal));
		emotionMapById.put(1, new Emotion(1, R.drawable.emotion_nu_normal));
		emotionMapById.put(2, new Emotion(2, R.drawable.emotion_ku_normal));
		emotionMapById.put(3, new Emotion(3, R.drawable.emotion_ganga_normal));
		emotionMapById.put(4, new Emotion(4, R.drawable.emotion_ai_normal));
		emotionMapById.put(5, new Emotion(5, R.drawable.emotion_gandong_normal));
	}
	
	/**
	 * 根据心情的id号获得心情对象
	 * @param emotionId	心情的id号
	 * @return			返回心情对象
	 */
	public static Emotion getEmotionById(int emotionId){
		return emotionMapById.get(emotionId);
	}
	
	/**
	 * 根据单选按钮的id号获得心情对象
	 * @param radioButtonId	单选框的id号
	 * @return				返回心情对象
	 */
	public static Emotion getEmotionByRadioButton(int radioButtonId){
		return emotionMap.get(radioButtonId);
	}

}
