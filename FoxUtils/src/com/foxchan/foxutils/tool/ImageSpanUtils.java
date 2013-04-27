package com.foxchan.foxutils.tool;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.TextView;

import com.foxchan.foxutils.data.StringUtils;

/**
 * 带图片的文字标签
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-11
 */
public class ImageSpanUtils {
	
	private Context context;
	private String[] imageTags;
	private String[] imageNames;
	private int imageWidth = -1;
	private int imageHeight = -1;
	
	public ImageSpanUtils(Context context, String[] imageTags, String[] imageNames){
		this(context, imageTags, imageNames, -1, -1);
	}
	
	public ImageSpanUtils(Context context, String[] imageTags,
			String[] imageNames, int imageWidth, int imageHeight) {
		this.context = context;
		this.imageTags = imageTags;
		this.imageNames = imageNames;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}
	
	private Html.ImageGetter imageGetter = new Html.ImageGetter() {
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			String sourceName = context.getPackageName() + ":drawable/"
					+ source;
			int id = context.getResources().getIdentifier(sourceName, null, null);
			if (id != 0) {
				drawable = context.getResources().getDrawable(id);
				if (drawable != null) {
					int _width = imageWidth > 0 ? imageWidth : drawable
							.getIntrinsicWidth();
					int _height = imageHeight > 0 ? imageHeight : drawable
							.getIntrinsicHeight();
					drawable.setBounds(0, 0, _width, _height);
				}
			}
			return drawable;
		}
	};
	
	/**
	 * return a {@link Spanned} with image
	 * @param text
	 * @return
	 */
	public Spanned getImageSpan(CharSequence text){
		String cs = text.toString();
		if (!StringUtils.isEmpty(imageTags) 
				&& !StringUtils.isEmpty(imageNames) 
				&& imageNames.length == imageTags.length) {
			for (int i = 0; i < imageTags.length; i++) {
				String tag = imageTags[i];
				String name = imageNames[i];
				if (cs.contains(tag)) {
					cs = cs.replace(tag, "<img src='" + name + "'>");
				}
			}
		}
		return Html.fromHtml(cs, imageGetter, null);
	}
	
	/**
	 * 将TextView中的表情显示出来
	 * @param textView	需要设置的TextView
	 * @param text		需要设置的文本
	 */
	public void setImgTextView(TextView textView, String text){
		if(StringUtils.isEmpty(text)) return;
		Spanned spanned = getImageSpan(text);
		textView.setText(spanned);
	}
	
	/**
	 * 将EditText中的表情显示出来
	 * @param editText	需要设置的EditText
	 * @param text		需要设置的文本
	 */
	public void setImgTextView(EditText editText, String text){
		if(StringUtils.isEmpty(text)) return;
		Spanned spanned = getImageSpan(text);
		editText.setText(spanned);
	}

}
