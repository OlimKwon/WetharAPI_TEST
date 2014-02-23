package com.example.allink_weather_test01;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.allink_weather_test01.GPSTracker.GPSResult;
import com.example.allink_weather_test01.WeatherAsync.WeatherAsyncResult;

public class WeatherLoader implements GPSResult, WeatherAsyncResult {

	Context mContext;
	WeatherLoaderResult mWeatherLoaderResult;
	GPSTracker mGPSTracker;
	boolean isWeatherLoading = false;
	
	public interface WeatherLoaderResult {
		public void weatherLoaderResult(String[][] val);
		public void returnLogVal(String val);
	}

	public WeatherLoader(Context context, WeatherLoaderResult wlr) {
		// TODO Auto-generated constructor stub

		Log.i("testLog1", "WeatherLoader");
		mContext = context;
		mWeatherLoaderResult = wlr;

		// 인터넷에 연결돼 있나 확인
		// ConnectivityManager connect = (ConnectivityManager) context
		// .getSystemService(context.CONNECTIVITY_SERVICE);
		// if
		// (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
		// == NetworkInfo.State.CONNECTED
		// || connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
		// .getState() == NetworkInfo.State.CONNECTED) {
		// // 연결 O
		// new GPSTracker(context, this);
		// } else {
		// // 연결 X
		// Toast.makeText(mContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_LONG)
		// .show();
		// }

		mGPSTracker = new GPSTracker(context, this);

	}

	public void startGPSTracking()
	{
		mGPSTracker.getGPSLocation();
	}
	@Override
	public void OnGPSReturnVal(String gpsResult) {
		// TODO Auto-generated method stub
		Log.i("testLog1", "WeatherLoader _ gpsVal");
		sendGPSLog("------------------------------------------" + "\n");
		
		if(!isWeatherLoading){
			isWeatherLoading = true;
			WeatherAsync aSync = new WeatherAsync(gpsResult, this);
			aSync.execute();
			
		}
	}


	@Override
	public void OnWeatherReturnVal(String[][] val) {
		// TODO Auto-generated method stub
		Log.i("testLog1", "weaReVal : " + val);
		
		isWeatherLoading = false;
		
		if (val != null && val[0][0] != null) {
			for (int i = 0; i < 6; i++) {
				sendGPSLog("\n");
				for (int j = 0; j < 4; j++) {
					Log.i("testLog1", "result : " + val[i][j]);
					sendGPSLog(val[i][j] + " | ");
				}
			}
		} else {
			Toast.makeText(mContext, "인터넷 연결상태를 확인해 주세요.", Toast.LENGTH_LONG)
					.show();
		}

		sendGPSLog("\n");
		mWeatherLoaderResult.weatherLoaderResult(val);
	}

	@Override
	public void sendGPSLog(String val) {
		mWeatherLoaderResult.returnLogVal(val);
		// TODO Auto-generated method stub
		
	}

}