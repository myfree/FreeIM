package com.ykhuo.im.view;

import com.ykhuo.im.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class LineView extends LinearLayout{
	private Context mContext;
	private LinearLayout line;
	private TextView infoTitle,infoContext;
	
	public LineView(Context context) {
		super(context);
		mContext=context;
		initView();
	}

	public LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext=context;
		initView();
		
		TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.LineView);

		String titleValue=ta.getString(R.styleable.LineView_titleValue);
		String contextValue=ta.getString(R.styleable.LineView_contextValue);
		
		setinfoValue(titleValue,contextValue);
		ta.recycle();
	}
	
	private void initView(){
		LayoutInflater.from(mContext).inflate(R.layout.common_line, this);
		line=(LinearLayout)findViewById(R.id.line);
		infoTitle=(TextView) findViewById(R.id.infoTitle);
		infoContext=(TextView) findViewById(R.id.infoContext);
		
		
	}
	
	private void setinfoValue(String title,String content){
		infoTitle.setText(title);
		infoContext.setText(content);
	}
	
	public String getStringValue(){
		return infoContext.getText().toString().trim();
	}
	
	public void setContext(String str){
		infoContext.setText(str);
	}


	public void setLineOnClickListener(OnClickListener listener){
		line.setOnClickListener(listener);
	}

}








