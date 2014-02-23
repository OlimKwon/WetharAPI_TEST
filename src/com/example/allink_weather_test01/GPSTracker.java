package com.example.allink_weather_test01;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class GPSTracker {

	String result = "";
	GPSResult mGpsResult;
	Context mC;

	int millis = 1;
	int distance = 1;

	Location location;

	boolean isNetworkEnabled = false;
	boolean isGPSEnabled = false;

	LocationManager locationManager;

	LocationListener myLocationListener;

	boolean isLocationChanged = false;

	private Handler mHandler;

	Runnable mRunnable = new Runnable() {
		public void run() {
//			if (!isLocationChanged) {
//				locationManager.removeUpdates(myLocationListener);
//				mGpsResult.sendGPSLog("Runnable _ removeUpdates ");
//				Toast.makeText(mC, "��ġ�� ã�� �� �����ϴ�.",
//						Toast.LENGTH_LONG).show();
//				Log.i("testLog1", "20 second GPS ���� .. ");
//			}
			if (location != null) {
				mGpsResult.sendGPSLog("onHandler" + "\n" );
				result = location.getLatitude() + " " + location.getLongitude();
				locationManager.removeUpdates(myLocationListener);
				
				mGpsResult.OnGPSReturnVal(result);
			}else{
				mGpsResult.sendGPSLog("Failed" + "\n" );
				Toast.makeText(mC, "���� ��ġ�� ã�� �� �����ϴ�.", Toast.LENGTH_LONG).show();
				locationManager.removeUpdates(myLocationListener);
			}
		}
	};
	
//	TimerTask myTask = new TimerTask() {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			mHandler.post(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//
//					if (!isLocationChanged) {
//						locationManager.removeUpdates(myLocationListener);
//						mGpsResult.sendGPSLog("removeUpdates ");
//						Toast.makeText(mC, "��ġ�� ã�� �� �����ϴ�.",
//								Toast.LENGTH_LONG).show();
//						Log.i("testLog1", "20 second GPS ���� .. ");
//					}
//				}
//			});
//		}
//	};
	
	public interface GPSResult {
		public void OnGPSReturnVal(String val);
		public void sendGPSLog(String val);
	}

	public GPSTracker(Context context, GPSResult gpsResult) {
		// TODO Auto-generated constructor stub

		Log.i("testLog1", "GPSTracker()");
		
		mGpsResult = gpsResult;
		mC = context;
		mHandler = new Handler();
		locationManager = (LocationManager) mC
				.getSystemService(Context.LOCATION_SERVICE);
		myLocationListener = new MyLocationListener();

		getGPSLocation();
//		locationManager.
	}

	public void getGPSLocation()
	{

		// ������ ���� ��Ų �׸���� �����Ѵ�.
		locationManager.removeUpdates(myLocationListener);
		mHandler.removeCallbacks(mRunnable);
		
		// 20�� ���� onlocation�� ���� �� ��ġ�� ���� ���ϸ� �ش� �ڵ鷯���� ������ ������ִ� ��ġ�� ���� 
		mHandler.postDelayed(mRunnable, 20000);
		
		
		
		mGpsResult.sendGPSLog("\n startGPSTracking !!!!!!");
		isGPSEnabled = locationManager.isProviderEnabled("gps");
		isNetworkEnabled = locationManager.isProviderEnabled("network");

		mGpsResult.sendGPSLog("BestProvider : " + locationManager.getBestProvider(new Criteria(), true) + "\n" );
		
		 if (isNetworkEnabled) {
		
			 Log.i("testLog1", "GPSTracker() isNetworkEnabled true");

				mGpsResult.sendGPSLog("NetworkEnabled" + "\n" );
				
			 locationManager.requestLocationUpdates("network", millis, distance,
			 myLocationListener);
				mGpsResult.sendGPSLog("NETWORK_ RequestLocation" + "\n" );
			
			 if (locationManager != null) {
				
				 location = locationManager.getLastKnownLocation("network");
					mGpsResult.sendGPSLog("NETWORK_ getLastKnownLocation" + "\n" );
				 
				 Log.i("testLog1", "GPSTracker() locationManager.getLastKnownLocation(network)");
				 
				 if( location != null){
					 Log.i("testLog1", location.getLatitude() + " " + location.getLongitude());

						mGpsResult.sendGPSLog("NETWORK : " + location.getLatitude() + " " + location.getLongitude() + "\n" );
				 }
			 }
		 }

		if (isGPSEnabled ) {
			Log.i("testLog1", "GPSTracker() isGPSEnabled");

			mGpsResult.sendGPSLog("GPSEnabled" + "\n" );
			
			locationManager.requestLocationUpdates("gps", millis, distance,
					myLocationListener);
			mGpsResult.sendGPSLog("GPS_ RequestLocation" + "\n" );
			if (locationManager != null) {

				location = locationManager.getLastKnownLocation("gps");
				mGpsResult.sendGPSLog("GPS_ getLastKnownLocation" + "\n" );
				
				Log.i("testLog1", "GPSTracker() locationManager.getLastKnownLocation(gps)");
				if( location != null){
					Log.i("testLog1", location.getLatitude() + " " + location.getLongitude());
					mGpsResult.sendGPSLog("GPS_ : " + location.getLatitude() + " " + location.getLongitude() + "\n" );
				}else{
					mGpsResult.sendGPSLog("GPS_ location: NULL " + "\n" );
				}
			}
		}

		// ī��Ʈ ����.. ���� 20�� ���� ������ ������ GPS ���� Ȯ�� �޼��� START

		// END

		
		
//		if (location != null) {
//			result = location.getLatitude() + " " + location.getLongitude();
//			mGpsResult.OnGPSReturnVal(result);
//		} else {
//			Toast.makeText(mC, "GPS������ ������Ʈ �� �Դϴ�.", Toast.LENGTH_SHORT).show();
//		}

		Log.i("testLog1", "GPSTracker() result : " + result);
	}
	
	
	
	
	// ��ġ ������ �����ϴ� ������
	public class MyLocationListener implements LocationListener {

		// ��ġ ������ ����Ǿ��� �� ȣ��ȴ�.
		@Override
		public void onLocationChanged(Location loc) {
			String text = loc.getLatitude() + " " + loc.getLongitude();
			
			Log.i("testLog1", "onLocationChanged() :" + text);

			mGpsResult.sendGPSLog("onLocationChangedBY : " + loc.getProvider() + "\n" );
			mGpsResult.sendGPSLog(" : " + loc.getLatitude() + " " + loc.getLongitude() + "\n" );
			
			//��ġ���� off �� �ڵ鷯 ���. 
			locationManager.removeUpdates(myLocationListener);
			mHandler.removeCallbacks(mRunnable);
			
			//��ġ ���� ���� 
			mGpsResult.OnGPSReturnVal(text);

//			locationManager.removeUpdates(myLocationListener);
		}

		// ��ġ �����ڰ� ����Ǿ��� �� ȣ��ȴ�.
		@Override
		public void onProviderDisabled(String provider) {
			mGpsResult.sendGPSLog("onProviderDisabled" + "\n" );
			Toast.makeText(mC.getApplicationContext(), "GPS �̿� �Ұ�",
					Toast.LENGTH_SHORT).show();
		}

		// ��ġ �����ڰ� Ȱ��ȭ�Ǿ��� �� ȣ��ȴ�.
		@Override
		public void onProviderEnabled(String provider) {
			mGpsResult.sendGPSLog("onProviderEnabled" + "\n" );
			Toast.makeText(mC.getApplicationContext(), "GPS �̿� ����",
					Toast.LENGTH_SHORT).show();
		}

		// ��ġ �������� ���°� ����Ǿ��� �� ȣ��ȴ�.
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			mGpsResult.sendGPSLog("onStatusChanged" + "\n" );

		}
	}

}
