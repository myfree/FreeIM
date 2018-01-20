package com.ykhuo.im.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ykhuo.im.R;
import com.ykhuo.im.action.UserAction;
import com.ykhuo.im.adapter.FriendSearchResultAdapter;
import com.ykhuo.im.bean.ApplicationData;
import com.ykhuo.im.bean.TranObject;
import com.ykhuo.im.bean.User;
import com.ykhuo.im.global.Result;
import com.ykhuo.im.view.TitleBarView;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

public class FriendSearchResultActivity extends Activity{


	private ListView mListviewOfResults;
	private TitleBarView mTitleBarView;
	private List<User> mFriendList;
	private User requestee;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend_search_result);
		initView();
		initEvent();
	}
	private void initView() {
		mListviewOfResults = (ListView)findViewById(R.id.friend_search_result_listview);
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE);
		mTitleBarView.setTitleText("���Һ��ѽ��");
	}
	private void initEvent() {
		
		
		mFriendList = ApplicationData.getInstance().getFriendSearched();
		System.out.println(mFriendList.size() + " friendSearch result");
		System.out.println("123 1");
		mListviewOfResults.setAdapter(new FriendSearchResultAdapter(FriendSearchResultActivity.this,mFriendList));
		System.out.println("123 2");
		mListviewOfResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User mySelf = ApplicationData.getInstance().getUserInfo();
				requestee = mFriendList.get(position);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						FriendSearchResultActivity.this);
				
				alertDialogBuilder.setTitle(null);
				//System.out.println(mySelf.getId() +" "+requestee.getId());
				if(mySelf.getAccount() == requestee.getAccount()) {
					alertDialogBuilder
					.setMessage("�㲻������Լ�Ϊ����")
					.setCancelable(true)
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
				}
				else if(!hasFriend(ApplicationData.getInstance().getFriendList(), requestee)){
					alertDialogBuilder
					.setMessage("ȷ����������")
					.setCancelable(true)
					.setPositiveButton("��",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
								UserAction.sendFriendRequest(Result.MAKE_FRIEND_REQUEST,requestee.getAccount());
						}
					  })
					.setNegativeButton("��",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
					}
					});
				} else {
					alertDialogBuilder
					.setMessage("�����Ѿ��Ǻ���")
					.setCancelable(true)
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
				}

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
		
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//mInstance = null;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	private boolean hasFriend(List<User> friendList,User person) {
		for(int i = 0;i < friendList.size();i++) {
			if(friendList.get(i).getAccount() == person.getAccount())
				return true;
		}
		return false;
	}
	
}
