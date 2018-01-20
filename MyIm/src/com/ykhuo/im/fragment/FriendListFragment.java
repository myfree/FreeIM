package com.ykhuo.im.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.ykhuo.im.R;
import com.ykhuo.im.activity.ChatActivity;
import com.ykhuo.im.activity.SearchFriendActivity;
import com.ykhuo.im.adapter.FriendListAdapter;
import com.ykhuo.im.bean.ApplicationData;
import com.ykhuo.im.bean.User;
import com.ykhuo.im.view.TitleBarView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class FriendListFragment extends Fragment {
	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	private ListView mFriendListView;
	private SearchView mSearchView;
	private List<User> mFriendList;
	private List<User> mFriendList_temp; //查询时临时存储的
	private Handler handler;
	private FriendListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_friendlist, null);
		System.out.println("初始化riendListFragment");
		findView();
		init();
		return mBaseView;
	}

	private void findView() {
		mTitleBarView = (TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mFriendListView = (ListView)mBaseView.findViewById(R.id.friend_list_listview);
		mSearchView = (SearchView)mBaseView.findViewById(R.id.searchtxt);
		
	}

	private void init() {
		mFriendList = ApplicationData.getInstance().getFriendList();
		mFriendList_temp=new ArrayList<User>();
		adapter = new FriendListAdapter(mContext, mFriendList);
		mFriendListView.setAdapter(adapter);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					mFriendListView.setSelection(mFriendList.size());
					break;
				default:
					break;
				}
			}
		};
		ApplicationData.getInstance().setfriendListHandler(handler);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.VISIBLE);
		mTitleBarView.setTitleText("好友");
		mTitleBarView.setBtnLeft(R.string.control);
		mTitleBarView.setBtnRight(R.drawable.qq_constact);
		
		mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User friend = mFriendList.get(position);
				Intent intent = new Intent(mContext,ChatActivity.class);
				intent.putExtra("friendName", friend.getNickname());
				intent.putExtra("friendId", friend.getAccount());
				startActivity(intent);
			}
		});
		
		mSearchView.setBackgroundResource(R.drawable.searchview);
		//去掉搜索框下划线
		if (mSearchView != null) {


            try {        //--拿到字节码
                Class<?> argClass = mSearchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearchView);
                //--设置背景
                mView.setBackgroundResource(R.drawable.searchviews);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		//搜索
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			//当点击提交按钮时
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show();
				return false;
			}
			
			//当改变搜索框文本值时
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, newText, Toast.LENGTH_SHORT).show();
				mFriendList_temp.clear();
				
				if(mFriendList!=null){
					for(int i=0;i<mFriendList.size();i++){
						boolean querystr1=mFriendList.get(i).getNickname().contains(newText);
						boolean querystr2=mFriendList.get(i).getUsername().contains(newText);
						if(querystr1||querystr2){
							mFriendList_temp.add(mFriendList.get(i));
							
						}
					}
					//查询后，再重新把数组插入到list
					adapter = new FriendListAdapter(mContext, mFriendList_temp);
					mFriendListView.setAdapter(adapter);
				}
				return false;
			}
		});
		
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,SearchFriendActivity.class);
				startActivity(intent);
			}
		});
		
	}
}
