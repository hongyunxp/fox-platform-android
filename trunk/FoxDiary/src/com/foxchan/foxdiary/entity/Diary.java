package com.foxchan.foxdiary.entity;

import java.io.File;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import cn.com.lezhixing.foxdb.annotation.Column;
import cn.com.lezhixing.foxdb.annotation.GeneratedType;
import cn.com.lezhixing.foxdb.annotation.GeneratedValue;
import cn.com.lezhixing.foxdb.annotation.Id;
import cn.com.lezhixing.foxdb.annotation.Table;
import cn.com.lezhixing.foxdb.annotation.Transient;

import com.foxchan.foxutils.data.DateUtils;
import com.foxchan.foxutils.data.StringUtils;
import com.foxchan.foxutils.tool.BitmapUtils;

/**
 * 实体：日记
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-16
 */
@Table(name="tb_core_diary")
public class Diary {
	
	/** 日记的唯一标识，采用32位UUID */
	@Id @GeneratedValue(strategy=GeneratedType.UUID)
	private String id;
	/** 日记的标题，可以为空，最多15个字符 */
	@Column(nullable=true)
	private String title;
	/** 日记的一张图片的存储位置 */
	@Column
	private String imagePath;
	/** 日记的图片文件对象 */
	@Transient
	private Bitmap image;
	/** 日记的语音信息的存储位置 */
	@Column
	private String voicePath;
	/** 日记的录音 */
	@Transient
	private File voice;
	/** 日记的正文，不可为空，最多140字 */
	@Column(nullable=false)
	private String content;
	/** 日记的心情 */
	@Column
	private int emotion;
	/** 日记的生成时间 */
	@Column(nullable=false)
	private Date createDate;
	/** 时间线节点的样式类型 */
	@Column(nullable=false)
	private int timeLineNodeStyleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		if(StringUtils.isEmpty(title)){
			title = StringUtils.concat(new Object[]{
					"diary_", DateUtils.formatDate(createDate)
			});
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getEmotion() {
		return emotion;
	}

	public void setEmotion(int emotion) {
		this.emotion = emotion;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public int getTimeLineNodeStyleId() {
		return timeLineNodeStyleId;
	}

	public void setTimeLineNodeStyleId(int timeLineNodeStyleId) {
		this.timeLineNodeStyleId = timeLineNodeStyleId;
	}
	
	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public File getVoice() {
		return voice;
	}

	public void setVoice(File voice) {
		this.voice = voice;
	}

	/**
	 * 获得节点的图片
	 * @param context
	 * @return			返回节点的图片
	 */
	public Bitmap photo(Context context){
		if(StringUtils.isEmpty(imagePath)) return null;
		if(image != null) return image;
		image = BitmapUtils.loadBitmapFromSdCard(context, imagePath);
		if(image != null){
			image = BitmapUtils.getRoundedCornerBitmap(image, 190.0f);
		}
		return image;
	}
	
	/**
	 * 获得日记节点的样式
	 * @return	返回日记节点的样式
	 */
	public TimeLineNodeStyle getStyle(){
		return new TimeLineNodeStyle(timeLineNodeStyleId);
	}
	
}
