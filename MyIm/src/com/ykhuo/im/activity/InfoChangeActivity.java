package com.ykhuo.im.activity;

import com.ykhuo.im.BaseActivity;
import com.ykhuo.im.R;
import com.ykhuo.im.R.drawable;
import com.ykhuo.im.R.id;
import com.ykhuo.im.R.layout;
import com.ykhuo.im.R.menu;
import com.ykhuo.im.R.string;
import com.ykhuo.im.util.ToolsUtils;
import com.ykhuo.im.view.TitleBarView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ToggleButton;

public class InfoChangeActivity extends BaseActivity {

	private final static int CHANGE_STRING = 11;  
	private TitleBarView mTitleBarView;
	private EditText ChangeEdit;
	private ToggleButton mysex;
	private DatePicker birthdaydate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉自带的bar，不能放setContentView下面，因为页面已经渲染好了不能再次更改
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info_change);
		
		initViews();
		initEvents();
		
	}
	
	@Override
	protected void initViews() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		
		mTitleBarView.setCommonTitle(View.VISIBLE , View.VISIBLE, View.VISIBLE);
		mTitleBarView.setTitleText("我的信息");
		mTitleBarView.setBtnLeft(R.drawable.bar_back,R.string.back_str);
		mTitleBarView.setBtnRight(R.drawable.save_data);
		
		
		ChangeEdit=(EditText) findViewById(R.id.ChangeEdit);
		mysex=(ToggleButton) findViewById(R.id.mysex);
		birthdaydate=(DatePicker) findViewById(R.id.birthdaydate);
	}




	@Override
	protected void initEvents() {
		// 初始化
		Intent intent=getIntent();
		final String mtype=intent.getStringExtra("type");
		String value=intent.getStringExtra("value");
		
		if(mtype.equals("nick")){
			mTitleBarView.setTitleText("修改昵称");
			ChangeEdit.setVisibility(View.VISIBLE);
			mysex.setVisibility(View.GONE);
			birthdaydate.setVisibility(View.GONE);
			
			ChangeEdit.setText(value);
			
		}else if(mtype.equals("sex")){
			mTitleBarView.setTitleText("修改性别");
			ChangeEdit.setVisibility(View.GONE);
			mysex.setVisibility(View.VISIBLE);
			birthdaydate.setVisibility(View.GONE);
			
			mysex.setChecked(value.equals("男")?true:false);
			
		}else if(mtype.equals("birthday")){
			mTitleBarView.setTitleText("修改生日");
			ChangeEdit.setVisibility(View.GONE);
			mysex.setVisibility(View.GONE);
			birthdaydate.setVisibility(View.VISIBLE);
			
			String[] birlist=value.split("-");
			birthdaydate.init(Integer.valueOf(birlist[0].toString()),Integer.valueOf(birlist[1].toString())-1, Integer.valueOf(birlist[2].toString())
					, new OnDateChangedListener() {
	            
	            @Override
	            public void onDateChanged(DatePicker view, int year, int monthOfYear,
	                    int dayOfMonth) {
	                // TODO Auto-generated method stub
	                setTitle(year+"-"+monthOfYear+"-"+dayOfMonth);
	            }
	        });
		}
		
		
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//返回上个页面
				ToolsUtils.onBack();
			}
		});
		
		
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("保存");
				if(mtype.equals("nick")){
					String strValue=ChangeEdit.getText().toString().trim();
					if(!strValue.equals("")){
						Intent intent = new Intent();  
						intent.putExtra("type", "nick");  
			            intent.putExtra("back", strValue);  
			            setResult(CHANGE_STRING, intent);  
			            finish(); 
					}else{
						showShortToast("不能为空");
					}
				}
				if(mtype.equals("sex")){
					Intent intent = new Intent();  
					intent.putExtra("type", "sex");  
		            intent.putExtra("back", mysex.isChecked()?"男":"女");  
		            setResult(CHANGE_STRING, intent);  
		            finish();
				}
				if(mtype.equals("birthday")){
					int year=birthdaydate.getYear();
					int month=birthdaydate.getMonth()+1;
					int day=birthdaydate.getDayOfMonth();
					
					
					String mydate=year+"-"+(month<10?'0'+String.valueOf(month):month)+"-"+(day<10?'0'+String.valueOf(day):day);
					//返回
					Intent intent = new Intent();  
					intent.putExtra("type", "birthday");  
		            intent.putExtra("back", mydate);  
		            setResult(CHANGE_STRING, intent);  
		            finish();
				}
			}
		});
		
	}
	
	//监听返回键,不能直接返回
//	@Override    
//    public void onBackPressed() {    
//        //super.onBackPressed(); //自定调用finish()方法,关闭当前Actitivy
//		Intent intent = new Intent();  
//		intent.putExtra("type", "none");  
//        intent.putExtra("back", "");  
//        setResult(CHANGE_STRING, intent);  
//        finish();      
//    }
	/**  
    * 监听Back键按下事件
    * 注意:  
    * 返回值表示:是否能完全处理该事件  
    * 在此处返回false,所以会继续传播该事件.  
    * 在具体项目中此处的返回值视情况而定.  
    */    
    @Override    
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {    
        	 Intent intent = new Intent();  
    		 intent.putExtra("type", "none");  
             intent.putExtra("back", "");  
             setResult(CHANGE_STRING, intent);  
             System.out.println("按了返回键");
             finish();  
             return false;    
        }else {    
            return super.onKeyDown(keyCode, event);    
        }    
            
    } 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info_change, menu);
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
