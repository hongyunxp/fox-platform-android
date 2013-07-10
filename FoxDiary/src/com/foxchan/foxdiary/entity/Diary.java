package com.foxchan.foxdiary.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.foxchan.foxdb.annotation.CascadeType;
import com.foxchan.foxdb.annotation.Column;
import com.foxchan.foxdb.annotation.GeneratedType;
import com.foxchan.foxdb.annotation.GeneratedValue;
import com.foxchan.foxdb.annotation.Id;
import com.foxchan.foxdb.annotation.OneToOne;
import com.foxchan.foxdb.annotation.Table;
import com.foxchan.foxdb.annotation.Transient;
import com.foxchan.foxdb.core.Session;
import com.foxchan.foxdiary.core.R;
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
	/** 日记的语音信息 */
	@OneToOne(cascade = CascadeType.ALL)
	private Record record;
	/** 日记的录音 */
	@Transient
	private File voice;
	/** 是否有录音的标志 */
	private Boolean hasVoice = null;
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
	/** 天气情况 */
	@Column(nullable=false)
	private int weatherId;

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

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
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

	public int getWeatherId() {
		return weatherId;
	}

	public void setWeatherId(int weatherId) {
		this.weatherId = weatherId;
	}

	/**
	 * 获得节点的图片
	 * @param context
	 * @return			返回节点的图片
	 */
	public Bitmap photo(Context context){
		if(image != null) return image;
		if(StringUtils.isEmpty(imagePath)) {
			image = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_not_found_image);
		} else {
			try {
				image = BitmapUtils.loadBitmapFromSdCard(context, imagePath);
			} catch (FileNotFoundException e) {
				image = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_not_found_image);
			}
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
	
	/**
	 * 判断日记是否有文字内容
	 * @return	如果日记有文字内容则返回true，否则返回false
	 */
	public boolean hasWords(){
		boolean flag = false;
		flag = (StringUtils.isEmpty(content) ? false : true);
		return flag;
	}
	
	/**
	 * 判断日记是否有照片内容
	 * @return	如果日记有照片内容则返回true，否则返回false
	 */
	public boolean hasPhoto(){
		boolean flag = false;
		flag = (StringUtils.isEmpty(imagePath) ? false : true);
		return flag;
	}
	
	/**
	 * 判断日记是否有录音内容
	 * @param session	数据会话
	 * @return			如果日记有录音内容则返回true，否则返回false
	 */
	public boolean hasVoice(Session session){
		checkVoice(session);
		return hasVoice;
	}
	
	/**
	 * 获得日记的创建日期的日期字符串
	 * @return	返回日记的创建日期的日期字符串
	 */
	public String getCreateDateStr(){
		return DateUtils.formatDate(createDate, "yyyy年MM月dd日");
	}
	
	/**
	 * 获得日记的创建时间的时间字符串
	 * @return	返回日记的创建时间的时间字符串
	 */
	public String getCreateDatetimeStr(){
		return DateUtils.formatDate(createDate, "MM月dd日  HH:mm");
	}
	
	/**
	 * 检查日记是否有录音文件
	 * @param session	数据库会话
	 */
	public void checkVoice(Session session){
		if(hasVoice == null){
			record = session.findObjectFrom(this, "record", Record.class);
			hasVoice = (record != null);
		}
	}
	
}
