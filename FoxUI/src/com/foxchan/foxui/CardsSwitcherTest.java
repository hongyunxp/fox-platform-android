package com.foxchan.foxui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foxchan.foxui.core.R;
import com.foxchan.foxui.widget.lang.CardsSwitcher;

/**
 * CardsSwitcher的测试类
 * @create 2013年8月8日
 * @author foxchan@live.cn
 * @version 1.0.0
 */
public class CardsSwitcherTest extends Activity {
	
	public static final String TAG = "CardsSwitcherTest";
	
	private CardsSwitcher cardsSwitcher;
	private DataAdapter adapter;
	List<Card> cards = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cards_switcher_test);
		cardsSwitcher = (CardsSwitcher)findViewById(R.id.cards_switcher);
		cards = new ArrayList<CardsSwitcherTest.Card>();
		for(int i = 0; i < 20; i++){
			cards.add(new Card("卡片：" + i, "卡片内容：这是第" + i + "张卡片的内容。"));
		}
		adapter = new DataAdapter();
		cardsSwitcher.setAdapter(adapter);
	}
	
	class DataAdapter extends BaseAdapter{
		
		class ContentHolder{
			public TextView tvTitle;
			public TextView tvContent;
		}

		@Override
		public int getCount() {
			return cards.size();
		}

		@Override
		public Object getItem(int position) {
			return cards.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContentHolder contentHolder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(CardsSwitcherTest.this).inflate(R.layout.card_item, null);
				contentHolder = new ContentHolder();
				contentHolder.tvContent = (TextView)convertView.findViewById(R.id.card_item_content);
				contentHolder.tvTitle = (TextView)convertView.findViewById(R.id.card_item_title);
				convertView.setTag(contentHolder);
			} else {
				contentHolder = (ContentHolder)convertView.getTag();
			}
			Card card = cards.get(position);
			contentHolder.tvContent.setText(card.content);
			contentHolder.tvTitle.setText(card.title);
			return convertView;
		}
		
	}
	
	class Card {
		
		private String title;
		private String content;
		
		public Card(String title, String content) {
			this.title = title;
			this.content = content;
		}
		
		public String toString(){
			return "card: [title = " + title + ", content = " + content + "]";
		}
		
	}

}
