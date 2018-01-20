package com.ykhuo.im.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


import com.ykhuo.im.bean.ChatEntity;
import com.ykhuo.im.bean.Recorder;
import com.ykhuo.im.BaseActivity;
import com.ykhuo.im.R;
import com.ykhuo.im.action.UserAction;
import com.ykhuo.im.adapter.ChatMessageAdapter;
import com.ykhuo.im.bean.ApplicationData;
import com.ykhuo.im.databse.ImDB;
import com.ykhuo.im.util.MediaManager;
import com.ykhuo.im.util.PhotoUtils;
import com.ykhuo.im.util.ToolsUtils;
import com.ykhuo.im.view.AudioRecorderButton;
import com.ykhuo.im.view.AudioRecorderButton.AudioFinishRecorderListener;
import com.ykhuo.im.view.TitleBarView;

public class ChatActivity extends BaseActivity {
	private TitleBarView mTitleBarView;
	private int friendId;
	private String friendName;
	private ListView chatMeessageListView;
	private ChatMessageAdapter chatMessageAdapter;
	private Button sendButton;
	private EditText inputEdit;
	private List<ChatEntity> chatList;
	private Handler handler;
	
	private AudioRecorderButton mAudioRecorderButton;
	
	private View mAnimView_left;
	private View mAnimView_right;
	
	private ImageButton zhaoxiang,xiangce,sendFile;
	private String photoPath=null; //相册路径
	private Bitmap mSendPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		Intent intent = getIntent();
		friendName = intent.getStringExtra("friendName");
		friendId = intent.getIntExtra("friendId", 0);
		initViews();
		initEvents();
	}
	

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE);
		mTitleBarView.setTitleText("与" + friendName + "对话");
		mTitleBarView.setBtnLeft(R.drawable.bar_back,R.string.back_str);
		
		chatMeessageListView = (ListView) findViewById(R.id.chat_Listview);
		sendButton = (Button) findViewById(R.id.chat_btn_send);
		inputEdit = (EditText) findViewById(R.id.chat_edit_input);
		
		mAudioRecorderButton=(AudioRecorderButton) findViewById(R.id.id_recorder_button);
		
		zhaoxiang=(ImageButton) findViewById(R.id.chat_btn_zhaoxiang);
		xiangce=(ImageButton) findViewById(R.id.chat_btn_img);
		sendFile=(ImageButton) findViewById(R.id.chat_btn_file);
		//设置chatMeessageListView显示在最末
		chatMeessageListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		chatMeessageListView.setStackFromBottom(true);
		
	}

	@Override
	protected void initEvents() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					chatMessageAdapter.notifyDataSetChanged();
					chatMeessageListView.setSelection(chatList.size());
					break;
				default:
					break;
				}
			}
		};
		ApplicationData.getInstance().setChatHandler(handler);
		chatList = ApplicationData.getInstance().getChatMessagesMap()
				.get(friendId);
		if(chatList == null){
			chatList = ImDB.getInstance(ChatActivity.this).getChatMessage(friendId);
			ApplicationData.getInstance().getChatMessagesMap().put(friendId, chatList);
		}
		
		chatMessageAdapter = new ChatMessageAdapter(ChatActivity.this,chatList);
		chatMeessageListView.setAdapter(chatMessageAdapter);
		mAudioRecorderButton.setAudioFinishRecorderListener(new AudioFinishRecorderListener() {
			
			@Override
			public void onFinish(float seconds, String filePath) {
				System.out.println(filePath);
				ChatEntity ChatVoice=new ChatEntity();
				ChatVoice.Recorder(seconds, filePath);
				ChatVoice.setSenderId(ApplicationData.getInstance()
						.getUserInfo().getAccount());
				ChatVoice.setReceiverId(friendId);
				ChatVoice.setMessageType(ChatEntity.SEND_VOICE);
				chatList.add(ChatVoice);
				chatMessageAdapter.notifyDataSetChanged();
				chatMeessageListView.setSelection(chatList.size()-1);
				
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
				String sendTime = sdf.format(date);
				ChatVoice.setSendTime(sendTime);
				
				
				ChatVoice.setObject(ToolsUtils.getBytes(ChatVoice.getFilePath()));
				UserAction.sendMessage(ChatVoice);
				ImDB.getInstance(ChatActivity.this)
						.saveChatMessage(ChatVoice);
				
			}
		});
		
		
		
		sendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String content = inputEdit.getText().toString();
				//如果为内容为空则不发送
				if(content.equals("")) return;
				//不为空，则清空原editText的值，进行发送
				inputEdit.setText("");
				ChatEntity chatMessage = new ChatEntity();
				chatMessage.setContent(content);
				chatMessage.setSenderId(ApplicationData.getInstance()
						.getUserInfo().getAccount());
				chatMessage.setReceiverId(friendId);
				chatMessage.setMessageType(ChatEntity.SEND);
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
				String sendTime = sdf.format(date);
				chatMessage.setSendTime(sendTime);
				chatList.add(chatMessage);
				chatMessageAdapter.notifyDataSetChanged();
				chatMeessageListView.setSelection(chatList.size());
				UserAction.sendMessage(chatMessage);
				ImDB.getInstance(ChatActivity.this)
						.saveChatMessage(chatMessage);
			}
		});
		
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//返回上个页面
				ToolsUtils.onBack();
			}
		});
		
		//为功能菜单设置功能
		zhaoxiang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				photoPath=PhotoUtils.takePicture(ChatActivity.this);
			}
		});
		
		xiangce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoUtils.selectPhoto(ChatActivity.this);
			}
		});
		
		sendFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ToolsUtils.selectFile(ChatActivity.this);
			}
		});
		
		
		chatMeessageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				ChatMessageAdapter cadapter=(ChatMessageAdapter) parent.getAdapter();
				ChatEntity Chatobj=(ChatEntity) cadapter.getItem(position);
				final int msgType=Chatobj.getMessageType();
				
				if(msgType!=ChatEntity.SEND_VOICE&&msgType!=ChatEntity.RECEIVE_VOICE){
					return;
				}
				
				if(msgType==ChatEntity.SEND_VOICE&&mAnimView_right!=null){
					mAnimView_right.setBackgroundResource(R.drawable.adj);
					mAnimView_right=null;
				}
				if(msgType==ChatEntity.RECEIVE_VOICE&&mAnimView_left!=null){
					mAnimView_left.setBackgroundResource(R.drawable.adj_friend);
					mAnimView_left=null;
				}

				
				

				if(msgType==ChatEntity.SEND_VOICE){
					mAnimView_right=view.findViewById(R.id.id_recorder_anim);
					//播放动画
					mAnimView_right.setBackgroundResource(R.drawable.play_anim);
					AnimationDrawable anim=(AnimationDrawable) mAnimView_right.getBackground();
					anim.start();
				}else{
					mAnimView_left=view.findViewById(R.id.id_recorder_anim_firend);
					//播放动画
					mAnimView_left.setBackgroundResource(R.drawable.play_anim_friend);
					AnimationDrawable anim=(AnimationDrawable) mAnimView_left.getBackground();
					anim.start();
				}
				
				
				
				//播放音频
				MediaManager.playSound(chatList.get(position).getFilePath(), new MediaPlayer.OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						if(msgType==ChatEntity.SEND_VOICE){
							mAnimView_right.setBackgroundResource(R.drawable.adj);
						}else{
							mAnimView_left.setBackgroundResource(R.drawable.adj_friend);
						}
						
						
					}
				});
				
			}
			
		});
		
	}
	
	//去除部分警告
	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				Uri uri = data.getData();
				String[] proj = { MediaColumns.DATA };
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaColumns.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						String path = cursor.getString(column_index);
						Bitmap bitmap = BitmapFactory.decodeFile(path);
						
						mSendPhoto=PhotoUtils.compressImage(bitmap);
						photoPath=path;
						SendPhoto(mSendPhoto);
					}
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
			if (resultCode == RESULT_OK) {
				String path = photoPath;
				Bitmap bitmap = BitmapFactory.decodeFile(path);
//				if (PhotoUtils.bitmapIsLarge(bitmap)) {
//					PhotoUtils.cropPhoto(this, this, path);
//				} else {
					//mStepPhoto.setUserPhoto(bitmap);
					mSendPhoto=PhotoUtils.compressImage(bitmap);
	
					SendPhoto(mSendPhoto);
//				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CROP:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (bitmap != null) {
						//mStepPhoto.setUserPhoto(bitmap);
						mSendPhoto=PhotoUtils.compressImage(bitmap);
						photoPath=path;
						SendPhoto(mSendPhoto);
					}
				}
			}
			break;
		case ToolsUtils.INTENT_REQUEST_FILE:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String path=uri.getPath().toString();
				System.out.println(path);
				showShortToast(path);
			}
			break;
		}
			
		
	}
	
	public void SendPhoto(Bitmap SendPhoto){
		showShortToast(photoPath);
		System.out.println(photoPath);
		ChatEntity ChatPhoto=new ChatEntity();
		ChatPhoto.setSenderId(ApplicationData.getInstance()
				.getUserInfo().getAccount());
		ChatPhoto.setReceiverId(friendId);
		ChatPhoto.setFilePath(photoPath);
		ChatPhoto.setMessageType(ChatEntity.SEND_PHOTO);
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
		String sendTime = sdf.format(date);
		ChatPhoto.setSendTime(sendTime);
		
		chatList.add(ChatPhoto);
		chatMessageAdapter.notifyDataSetChanged();
		chatMeessageListView.setSelection(chatList.size()-1);
		
		ChatPhoto.setObject(PhotoUtils.getBytes(SendPhoto));
		UserAction.sendMessage(ChatPhoto);
		ImDB.getInstance(ChatActivity.this)
				.saveChatMessage(ChatPhoto);
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		MediaManager.pause();
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		MediaManager.resume();
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		MediaManager.release();
	}

}
