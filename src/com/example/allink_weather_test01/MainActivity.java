package com.example.allink_weather_test01;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.allink_weather_test01.WeatherLoader.WeatherLoaderResult;

public class MainActivity extends Activity implements WeatherLoaderResult {

	TextView mTextView;
	String mLog = "";
	WeatherLoader mWeatherLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Context c = getApplicationContext();
		mTextView = (TextView)findViewById(R.id.textview); 
		
		mWeatherLoader = new WeatherLoader(c, this);
		Button gpsBtn = (Button)findViewById(R.id.button1);
		gpsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWeatherLoader.startGPSTracking();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 결과값 도착
	@Override
	public void weatherLoaderResult(String[][] val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnLogVal(String val) {
		// TODO Auto-generated method stub
		mLog = mLog + val ;
		mTextView.setText(mLog);
		
	}

	// @Override
	// public void weaReVal(String[][] val) {
	// // TODO Auto-generated method stub
	// Toast.makeText(this, "결과 후 MainActivity 호출하였음.",
	// Toast.LENGTH_LONG).show();
	// }
}
