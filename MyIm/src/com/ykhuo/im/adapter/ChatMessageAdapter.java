package com.ykhuo.im.adapter;

import java.util.List;

import com.ykhuo.im.R;
import com.ykhuo.im.bean.ApplicationData;
import com.ykhuo.im.bean.ChatEntity;
import com.ykhuo.im.bean.User;
import com.ykhuo.im.util.FaceConversionUtil;
import com.ykhuo.im.util.PhotoUtils;
import com.ykhuo.im.util.ToolsUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {
	private List<ChatEntity> chatEntities;
	private LayoutInflater mInflater;
	private Context mContext0;
	
	private TextView recorder_time;
	private FrameLayout recorder_length;
	
	private ImageView firend_photo;
	private ImageView user_photo;
	
	private int mMinItemWidth;
	private int mMaxItemWidth;

	public ChatMessageAdapter(Context context, List<ChatEntity> vector) {
		this.chatEntities = vector;
		mInflater = LayoutInflater.from(context);
		mContext0 = context;
		
		WindowManager wm=(WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		
		mMaxItemWidth = (int) (outMetrics.widthPixels*0.5f);
		mMinItemWidth=(int)(outMetrics.widthPixels*0.05f);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LinearLayout leftLayout;
		LinearLayout rightLayout;
		TextView leftMessageView;
		TextView rightMessageView;
		TextView timeView;
		ImageView leftPhotoView;
		ImageView rightPhotoView;
		view = mInflater.inflate(R.layout.chat_message_item_, parent,false);
		ChatEntity chatEntity = chatEntities.get(position);
		leftLayout = (LinearLayout) view
				.findViewById(R.id.chat_friend_left_layout);
		rightLayout = (LinearLayout) view
				.findViewById(R.id.chat_user_right_layout);
		timeView = (TextView) view.findViewById(R.id.message_time);
		leftPhotoView = (ImageView) view
				.findViewById(R.id.message_friend_userphoto);
		rightPhotoView = (ImageView) view
				.findViewById(R.id.message_user_userphoto);
		leftMessageView = (TextView) view.findViewById(R.id.friend_message);
		rightMessageView = (TextView) view.findViewById(R.id.user_message);
		
		firend_photo=(ImageView) view.findViewById(R.id.firend_photo);
		user_photo=(ImageView) view.findViewById(R.id.user_photo);
		//========================================

		User user = ApplicationData.getInstance().getUserInfo();
		timeView.setText(chatEntity.getSendTime());
		
		switch(chatEntity.getMessageType()){
		 	case ChatEntity.SEND:
		 		rightLayout.setVisibility(View.VISIBLE);
				leftLayout.setVisibility(View.GONE);

				rightPhotoView.setImageBitmap(ApplicationData.getInstance()
						.getUserPhoto());
				rightMessageView.setText(getSpannable(chatEntity.getContent()));
				break;
		 	
		 	case ChatEntity.RECEIVE:
		 		leftLayout.setVisibility(View.VISIBLE);
				rightLayout.setVisibility(View.GONE);
				Bitmap photo = ApplicationData.getInstance().getFriendPhotoMap()
						.get(chatEntity.getSenderId());
				if (photo != null)
					leftPhotoView.setImageBitmap(photo);
				leftMessageView.setText(getSpannable(chatEntity.getContent()));
				break;
		 	
		 	case ChatEntity.SEND_VOICE:
		 		rightLayout.setVisibility(View.VISIBLE);
				leftLayout.setVisibility(View.GONE);
				rightPhotoView.setImageBitmap(ApplicationData.getInstance()
						.getUserPhoto());
				rightMessageView.setText("");
				
				ViewHolder holder=null;
				if(view==null){
					holder=new ViewHolder();
					holder.seconds=(TextView)view.findViewById(R.id.id_recorder_time);
					holder.length=view.findViewById(R.id.id_recorder_length);
					holder.length.setVisibility(View.VISIBLE);
					view.setTag(holder);
				}else{
					holder=(ViewHolder) view.getTag();
					recorder_time=(TextView) view.findViewById(R.id.id_recorder_time);
					recorder_length=(FrameLayout) view.findViewById(R.id.id_recorder_length);
				}
				
				ChatEntity chats=(ChatEntity)getItem(position);
				if(chats.getTime()==0){
					chats.setTime(Float.parseFloat(chatEntity.getContent()));
				}
				
				
				recorder_length.setVisibility(View.VISIBLE);
				recorder_time.setText(Math.round(chats.getTime())+"\"");

				ViewGroup.LayoutParams lp=recorder_length.getLayoutParams();
				lp.width=(int) (mMinItemWidth+(mMaxItemWidth/60f*chats.getTime()));
				break;
			 	
		 	case ChatEntity.RECEIVE_VOICE:
		 		leftLayout.setVisibility(View.VISIBLE);
				rightLayout.setVisibility(View.GONE);
				Bitmap photo1 = ApplicationData.getInstance().getFriendPhotoMap()
						.get(chatEntity.getSenderId());
				if (photo1 != null)
					leftPhotoView.setImageBitmap(photo1);
				
				recorder_time=(TextView) view.findViewById(R.id.id_recorder_time_friend);
				recorder_length=(FrameLayout) view.findViewById(R.id.id_recorder_length_firend);
				
				recorder_length.setVisibility(View.VISIBLE);
				if(chatEntity.getTime()==0){
					chatEntity.setTime(Float.parseFloat(chatEntity.getContent()));
				}
				
				recorder_time.setText(Math.round(chatEntity.getTime())+"\"");
		
				
				leftMessageView.setText("");
				
				ViewGroup.LayoutParams lp1=recorder_length.getLayoutParams();
				lp1.width=(int) (mMinItemWidth+(mMaxItemWidth/60f*chatEntity.getTime()));
		 		break;
			 
		 	case ChatEntity.SEND_PHOTO:
		 		rightLayout.setVisibility(View.VISIBLE);
				leftLayout.setVisibility(View.GONE);
				rightPhotoView.setImageBitmap(ApplicationData.getInstance()
						.getUserPhoto());
				//rightMessageView.setText("");
				rightMessageView.setVisibility(View.GONE);
				user_photo.setImageBitmap(PhotoUtils.getBitmap((byte [])chatEntity.getObject()));
				
				user_photo.setVisibility(View.VISIBLE);
				
				break;
			 
		 	case ChatEntity.RECEIVE_PHOTO:
		 		leftLayout.setVisibility(View.VISIBLE);
				rightLayout.setVisibility(View.GONE);
				Bitmap photo2 = ApplicationData.getInstance().getFriendPhotoMap()
						.get(chatEntity.getSenderId());
				if (photo2 != null)
					leftPhotoView.setImageBitmap(photo2);
				leftMessageView.setVisibility(View.GONE);
				firend_photo.setVisibility(View.VISIBLE);
				
				String cccpath=chatEntity.getFilePath();
				byte[] sssbyte=ToolsUtils.getBytes(cccpath);
				Bitmap tempBitmap=PhotoUtils.getBitmap(sssbyte);
				//Bitmap tempBitmap=PhotoUtils.getBitmap(ToolsUtils.getBytes(chatEntity.getFilePath()));
				firend_photo.setImageBitmap(tempBitmap);
				break;
			
		 	case ChatEntity.SEND_FILE:
		 		
			break;
			 
		 	case ChatEntity.RECEIVE_FILE:
		 		
			break;
		}
		
		return view;
	}
	
	//消息气泡上显示表情转换
	public SpannableString getSpannable(String str) {

		return FaceConversionUtil.getInstace().getExpressionString(mContext0, str);
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chatEntities.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return chatEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	private class ViewHolder{
		TextView seconds;
		View length;
	}

}
