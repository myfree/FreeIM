package com.ykhuo.im.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ykhuo.im.R;
import com.ykhuo.im.activity.InfoActivity;
import com.ykhuo.im.bean.ApplicationData;
import com.ykhuo.im.util.PhotoUtils;
import com.ykhuo.im.util.ToolsUtils;
import com.ykhuo.im.view.TitleBarView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoFragment extends Fragment{
	private Context mContext;
	private View mBaseView;
	private ListView mlistView;
	private SimpleAdapter adapter;
	private ImageView me_img;
	private TextView me_name;
	private TitleBarView mTitleBarView;
	
	private int[] imageId;
	private String[] title;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_userinfo, null);
		findView();
		init();
		return mBaseView;
	}
	private void findView(){
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mlistView=(ListView)mBaseView.findViewById(R.id.mesetting);
		me_img=(ImageView)mBaseView.findViewById(R.id.header_img);
		me_name=(TextView)mBaseView.findViewById(R.id.me_uname);
	}
	
	private void init(){
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE);
		mTitleBarView.setTitleText("������Ϣ");
		
		//================���ù��ܲ˵�=============================
		imageId=new int []{R.drawable.ziliao,R.drawable.dongtai,R.drawable.setting};//���岢��ʼ������ͼƬid������  
        //���岢��ʼ�������б������ֵ�����  
		title=new String[]{"�ҵ�����","���Ѷ�̬","����"};  
        List<Map<String,Object>> listitems=new ArrayList<Map<String,Object>>();  
        for (int i = 0; i <imageId.length; i++) {  
            Map<String,Object> map=new HashMap<String,Object>();//ʵ����map����  
            map.put("image", imageId[i]);  
            map.put("title",title[i]);  
            listitems.add(map);//��map����װ��List������  
        }  
          
        adapter=new SimpleAdapter(mContext,listitems,R.layout.list_items,new String[]{"image","title"},new int[]{R.id.me_setting_image,R.id.me_setting_title});  
        mlistView.setAdapter(adapter);  
		
		
		
		
		
		
		
		ApplicationData data=ApplicationData.getInstance();
		
		//����ͷ����ǳ�
		me_img.setImageBitmap(PhotoUtils.getBitmap(data.getUserInfo().getPhoto()));
		//��ȡ��Ļ������
		DisplayMetrics dm =getResources().getDisplayMetrics();  
        int w_screen = Math.round(dm.widthPixels/5-5);  
        
        Toast.makeText(mContext, String.valueOf(w_screen), Toast.LENGTH_SHORT).show();
		
		LayoutParams params = me_img.getLayoutParams();    
	    params.height=w_screen;    
	    params.width =w_screen;   
		me_img.setLayoutParams(params);
		me_name.setText(data.getUserInfo().getNickname());
		
		
		
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String,String> map=(HashMap<String, String>) adapter.getItem(position);
				switch(Stringkey(title,map.get("title"))){
					case 0: 
						Toast.makeText(mContext, "���ڿ�����", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(mContext,InfoActivity.class);
						startActivity(intent);
						break;
					case 1: 
						Toast.makeText(mContext, "���ڿ�����", Toast.LENGTH_SHORT).show();
						
						break;
					case 2: 
						Toast.makeText(mContext, "���ڿ�����", Toast.LENGTH_SHORT).show();
						
						break;
				}
				
			}
		});
	}
	
	//�����ַ����������е��±�
	private int Stringkey(String[] str,String value){
		for(int i=0;i<str.length;i++){
			if(str[i].equals(value)) return i;
		}
		return -1;
	}
}

