package com.ykhuo.im.activity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ykhuo.im.BaseActivity;
import com.ykhuo.im.R;
import com.ykhuo.im.R.id;
import com.ykhuo.im.R.layout;
import com.ykhuo.im.R.menu;
import com.ykhuo.im.action.UserAction;
import com.ykhuo.im.bean.ApplicationData;
import com.ykhuo.im.bean.User;
import com.ykhuo.im.global.Result;
import com.ykhuo.im.network.NetService;
import com.ykhuo.im.util.PhotoUtils;
import com.ykhuo.im.util.ToolsUtils;
import com.ykhuo.im.view.LineView;
import com.ykhuo.im.view.TitleBarView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class InfoActivity extends BaseActivity{
	
	private final static int CHANGE_STRING = 11;
	private TitleBarView mTitleBarView;
	private ImageView myphoto;
	private Bitmap mUserPhoto;
	private Handler handler;
	private LineView myusername,mynickname,mysex,mybirthday;
	private String photoPath=null;
	private NetService mNetService = NetService.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ���Դ���bar�����ܷ�setContentView���棬��Ϊҳ���Ѿ���Ⱦ���˲����ٴθ���
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info);
		
		initViews();
		initEvents();
		
	}
	
	@Override
	protected void initViews() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		
		mTitleBarView.setCommonTitle(View.VISIBLE , View.VISIBLE, View.VISIBLE);
		mTitleBarView.setTitleText("�ҵ���Ϣ");
		mTitleBarView.setBtnLeft(R.drawable.bar_back,R.string.back_str);
		mTitleBarView.setBtnRight(R.drawable.save_data);
		
		//��ȡ�ؼ�
		myphoto=(ImageView) findViewById(R.id.myphoto);
		myusername=(LineView) findViewById(R.id.myusername);
		mynickname=(LineView) findViewById(R.id.mynickname);
		mysex=(LineView) findViewById(R.id.mysex);
		mybirthday=(LineView) findViewById(R.id.mybirthday);
		//��ʼ�������ֵ
		User user=ApplicationData.getInstance().getUserInfo();
		mUserPhoto=PhotoUtils.getBitmap(user.getPhoto());
		myphoto.setImageBitmap(PhotoUtils.getBitmap(user.getPhoto()));
		myusername.setContext(user.getUsername()+" (ID:"+user.getAccount()+")");
		mynickname.setContext(user.getNickname());
		mysex.setContext(user.getSex()==1?"��":"Ů");
		mybirthday.setContext(ToolsUtils.dateToString(user.getBirthday()));
	}

	@Override
	protected void initEvents() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					showShortToast("�޸ĳɹ�");
					break;
				case 2:
					showShortToast("�޸�ʧ��");
				default:
					break;
				}
			}
		};
		ApplicationData.getInstance().setInfoHandler(handler);
		
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�����ϸ�ҳ��
				ToolsUtils.onBack();
			}
		});
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InfoActivity.this,SearchFriendActivity.class);
				User myinfo=ApplicationData.getInstance().getUserInfo();
				myinfo.setPhoto(PhotoUtils.getBytes(mUserPhoto));
				myinfo.setNickname(mynickname.getStringValue());
				myinfo.setSex(mysex.getStringValue().equals("��")?1:0);
				
				Date birdate = ToolsUtils.getDate(mybirthday.getStringValue());
				myinfo.setBirthday(birdate);

				tryChange(myinfo); 
			}
		});
		
		myphoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);  
			       //builder.setIcon(R.drawable.ic_launcher);  
			       builder.setTitle("ѡ���ȡ��Ƭ��ʽ");  
			       //    ָ�������б����ʾ����  
			       final String[] cities = {"�ֻ����", "�ֻ�����"};  
			       //    ����һ���������б�ѡ����  
			       builder.setItems(cities, new DialogInterface.OnClickListener()  
			       {  
			           @Override  
			           public void onClick(DialogInterface dialog, int which)  
			           {  
			               if(which==0){
			            	   PhotoUtils.selectPhoto(InfoActivity.this);
			               }  
			               if(which==1){
			            	   photoPath=PhotoUtils.takePicture(InfoActivity.this);
			            	   System.out.println(photoPath);
			            	   Toast.makeText(InfoActivity.this, "12", Toast.LENGTH_SHORT).show();
			               }
			               
			           }  
			       });  
			       builder.show();
			       
			      
			       
			       
				
			}
		});
		
		//Ϊ�������õ���¼�
		mynickname.setLineOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();  
	            intent.setClass(InfoActivity.this, InfoChangeActivity.class);  
	            intent.putExtra("type", "nick"); 
	            intent.putExtra("value", mynickname.getStringValue());  
	            //startActivity(intent);  
	            startActivityForResult(intent, CHANGE_STRING);  
			}
		});
		mysex.setLineOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();  
	            intent.setClass(InfoActivity.this, InfoChangeActivity.class);  
	            intent.putExtra("type", "sex"); 
	            intent.putExtra("value", mysex.getStringValue());  
	            startActivityForResult(intent, CHANGE_STRING);
			}
		});
		mybirthday.setLineOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();  
	            intent.setClass(InfoActivity.this, InfoChangeActivity.class);  
	            intent.putExtra("type", "birthday"); 
	            intent.putExtra("value", mybirthday.getStringValue());  
	            startActivityForResult(intent, CHANGE_STRING);
			}
		});
		
	}
	
	
	private void tryChange(final User myuser) {
		
		try {
			UserAction.UserChange(myuser);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//ȥ�����־���
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
						if (PhotoUtils.bitmapIsLarge(bitmap)) {
							PhotoUtils.cropPhoto(this, this, path);
						} else {
							mUserPhoto=PhotoUtils.compressImage(bitmap);
						}
					}
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
			if (resultCode == RESULT_OK) {
				String path = photoPath;
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				if (PhotoUtils.bitmapIsLarge(bitmap)) {
					PhotoUtils.cropPhoto(this, this, path);
				} else {
					//mStepPhoto.setUserPhoto(bitmap);
					mUserPhoto=PhotoUtils.compressImage(bitmap);
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CROP:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (bitmap != null) {
						//mStepPhoto.setUserPhoto(bitmap);
						mUserPhoto=PhotoUtils.compressImage(bitmap);
					}
				}
			}
			break;
		case CHANGE_STRING:
			Bundle bundle = data.getExtras();  
            String mtype = bundle.getString("type");
            String str = bundle.getString("back");
            if(mtype.equals("nick")){
            	mynickname.setContext(str);
            }
            if(mtype.equals("sex")){
            	mysex.setContext(str);
            }
            if(mtype.equals("birthday")){
            	mybirthday.setContext(str);
            }
			break;
		}
			
		
		myphoto.setImageBitmap(mUserPhoto);
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
