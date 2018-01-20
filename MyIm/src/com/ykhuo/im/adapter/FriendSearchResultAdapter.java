package com.ykhuo.im.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ykhuo.im.R;
import com.ykhuo.im.bean.User;
import com.ykhuo.im.util.PhotoUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendSearchResultAdapter extends BaseAdapter {
	private List<User> mVector;
	private LayoutInflater mInflater;
	private Context mContext0;

	public FriendSearchResultAdapter(Context context, List<User> vector) {
		this.mVector = vector;
		mInflater = LayoutInflater.from(context);
		mContext0 = context;
	}

	public View getView(int position, View convertView, ViewGroup root) {
		ImageView avatarView;
		TextView nameView;
		TextView ageView;
		TextView sexView;

		User user = mVector.get(position);
		Bitmap photo = PhotoUtils.getBitmap(user.getPhoto());
		String name = user.getNickname();
		int age = new Date().getYear() - user.getBirthday().getYear();
		int gender = user.getSex();

		convertView = mInflater.inflate(R.layout.friend_search_result_item,
				null);
		avatarView = (ImageView) convertView
				.findViewById(R.id.cc0_friend_search_result_item_avatar);
		nameView = (TextView) convertView
				.findViewById(R.id.cc0_friend_search_result_item_name);
		ageView = (TextView) convertView
				.findViewById(R.id.cc0_friend_search_result_item_age);
		sexView = (TextView) convertView
				.findViewById(R.id.cc0_friend_search_result_item_gender);

		nameView.setText(name);

		if (photo != null) {
			avatarView.setImageBitmap(photo);
		}
		ageView.setText(age + "Ëê");
		if (gender == 0) {
			sexView.setText("Å®");
		} else {
			sexView.setText("ÄÐ");
		}

		return convertView;
	}

	public int getCount() {
		return mVector.size();
	}

	public Object getItem(int position) {
		return mVector.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
