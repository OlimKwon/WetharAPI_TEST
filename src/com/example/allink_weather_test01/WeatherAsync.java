package com.example.allink_weather_test01;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class WeatherAsync extends AsyncTask<Void, Void, Void> {

	final String yahooPlaceApisBase = "http://query.yahooapis.com/v1/public/yql?q=select%20woeid%20from%20geo.places%20where%20text=";

	final String yahooapisFormat = "&format=xml";

	String yahooPlaceAPIsQuery;

	String gpsVal = "";

	
	int mWOEID;

	WeatherAsyncResult mWaResult;

	String[][] mWeatherResult = new String[6][4];

	public WeatherAsync(String gpsResult, WeatherAsyncResult weaAsynRe) {
		// TODO Auto-generated constructor stub
		Log.i("testLog1", "WeatherAsync()");
		mWaResult = weaAsynRe;
		gpsVal = gpsResult;
	}
	
	
	
	public interface WeatherAsyncResult {

		public void OnWeatherReturnVal(String[][] val);
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		// background 작업 시작
		Log.i("testLog1", "WeatherAsync doInBackground");
		String str = QueryYahooPlaceAPIs();
		Log.i("testLog1", "WeatherAsync doInBackground woeid : " + str);

		if (!str.equals("")) {
			new MyQueryYahooWeatherTask(str);
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// background 작업 시작 전 후 발생.

		Log.i("testLog1", "onPostExecute");

		super.onPostExecute(result);

		mWaResult.OnWeatherReturnVal(mWeatherResult);
		Log.i("testLog1", "callWeaReVal ");

		Log.i("testLog1", "onPostExecute End");
	}

	// ########WOEID 가져오기 #### START ##################

	private String QueryYahooPlaceAPIs() {
		String uriPlace = Uri.encode(gpsVal);

		Log.i("testLog1", "uriPlace : " + uriPlace);

		yahooPlaceAPIsQuery = yahooPlaceApisBase + "%22" + uriPlace + "%22"
				+ yahooapisFormat;

		Log.i("testLog1", "yahooPlaceAPIsQuery : " + yahooPlaceAPIsQuery);

		String woeidString = QueryYahooWeather(yahooPlaceAPIsQuery);
		Document woeidDoc = convertStringToDocument(woeidString);
		return parseWOEID(woeidDoc);
	}

	private String parseWOEID(Document srcDoc) {
		String woeid = "";

		NodeList nodeListDescription = null;
		if (srcDoc != null) {
			nodeListDescription = srcDoc.getElementsByTagName("woeid");

			if (nodeListDescription.getLength() >= 0) {
				woeid = nodeListDescription.item(0).getTextContent();
			}
		}

		return woeid;
	}

	private Document convertStringToDocument(String src) {
		Document dest = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dest;
	}

	private String QueryYahooWeather(String queryString) {
		String qResult = "";

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(queryString);

		try {
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();

				String stringReadLine = null;

				while ((stringReadLine = bufferedreader.readLine()) != null) {
					stringBuilder.append(stringReadLine + "\n");
				}

				qResult = stringBuilder.toString();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return qResult;
	}

	// ######## WOEID 가져오기 #### END ##################

	// ######## 날씨 가져오기 #### START ##################
	private class MyQueryYahooWeatherTask {

		String woeid;

		String weatherString;

		MyQueryYahooWeatherTask(String w) {

			Log.i("testLog1", "MyQueryYahooWeatherTask");

			woeid = w;

			weatherString = QueryYahooWeather();
			Document weatherDoc = convertStringToDocument(weatherString);

			if (weatherDoc != null) {
				mWeatherResult = parseWeather(weatherDoc);
			} else {
				mWeatherResult = null;
			}
		}

		private String QueryYahooWeather() {

			Log.i("testLog1", "QueryYahooWeather");

			String qResult = "";
			String queryString = "http://weather.yahooapis.com/forecastrss?w="
					+ woeid;

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(queryString);

			try {
				HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

				if (httpEntity != null) {
					InputStream inputStream = httpEntity.getContent();
					Reader in = new InputStreamReader(inputStream);
					BufferedReader bufferedreader = new BufferedReader(in);
					StringBuilder stringBuilder = new StringBuilder();

					String stringReadLine = null;

					while ((stringReadLine = bufferedreader.readLine()) != null) {
						stringBuilder.append(stringReadLine + "\n");
					}

					qResult = stringBuilder.toString();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return qResult;
		}

		private Document convertStringToDocument(String src) {
			Document dest = null;

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder parser;

			try {
				parser = dbFactory.newDocumentBuilder();
				dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return dest;
		}

		private String[][] parseWeather(Document srcDoc) {

			String[][] MyWeatherArr = new String[6][4];

			int[] resDrawableIdArr = { R.drawable.icon_weather01,
					R.drawable.icon_weather01, R.drawable.icon_weather02,
					R.drawable.icon_weather03, R.drawable.icon_weather04,
					R.drawable.icon_weather05, R.drawable.icon_weather06,
					R.drawable.icon_weather07, R.drawable.icon_weather08,
					R.drawable.icon_weather09, R.drawable.icon_weather10,
					R.drawable.icon_weather11, R.drawable.icon_weather12 };

			// // <description>Yahoo! Weather for New York, NY</description>
			// NodeList descNodelist =
			// srcDoc.getElementsByTagName("description");
			// if (descNodelist != null && descNodelist.getLength() > 0) {
			// myWeather.description = descNodelist.item(0).getTextContent();
			// } else {
			// myWeather.description = "EMPTY";
			// }

			// <yweather:location city="New York" region="NY"
			// country="United States"/>
			NodeList locationNodeList = srcDoc
					.getElementsByTagName("yweather:location");
			if (locationNodeList != null && locationNodeList.getLength() > 0) {
				Node locationNode = locationNodeList.item(0);
				NamedNodeMap locNamedNodeMap = locationNode.getAttributes();

				MyWeatherArr[0][0] = locNamedNodeMap.getNamedItem("city")
						.getNodeValue().toString();
			} else {
				for (int i = 0; i < 4; i++) {
					MyWeatherArr[0][i] = "EMPTY";
				}
			}

			// yweather:forecast
			NodeList forecastNodeList = srcDoc
					.getElementsByTagName("yweather:forecast");
			if (forecastNodeList != null && forecastNodeList.getLength() > 0) {

				for (int i = 0; i < forecastNodeList.getLength(); i++) {
					Node forecastNode = forecastNodeList.item(i);

					NamedNodeMap forecastNamedNodeMap = forecastNode
							.getAttributes();

					String yCode = forecastNamedNodeMap.getNamedItem("code")
							.getNodeValue().toString();
					String aCode = changeCode(yCode);

					// 날씨코드
					MyWeatherArr[i + 1][0] = aCode;

					// 이미지 drawable명
					MyWeatherArr[i + 1][1] = getImageResource(resDrawableIdArr[Integer
							.parseInt(aCode)]);

					// 최고온도
					MyWeatherArr[i + 1][2] = changeCelsius(forecastNamedNodeMap
							.getNamedItem("high").getNodeValue().toString());
					// 최저온도
					MyWeatherArr[i + 1][3] = changeCelsius(forecastNamedNodeMap
							.getNamedItem("low").getNodeValue().toString());

				}

			} else {
				// myWeather.forecasttext = "EMPTY";
				for (int i = 1; i < 4; i++) {
					MyWeatherArr[i][0] = "EMPTY";
					MyWeatherArr[i][1] = "EMPTY";
					MyWeatherArr[i][2] = "EMPTY";
				}
			}

			return MyWeatherArr;
		}

		// 화씨를 섭씨로 바꾸는 방법
		private String changeCelsius(String str) {

			int v = Integer.parseInt(str);

			double i = 0;
			i = (v + 40) / 1.8 - 40;
			i = Math.round(i);

			return String.valueOf(i);
		}

	}

	// ######## 날씨 가져오기 #### END ##################

	private String changeCode(String yCode) {

		int key = Integer.parseInt(yCode);

		// 정보없음
		int resultKey = 12;

		// 0,1,2,9,10
		if ((0 <= key && key <= 2) || 9 == key || key == 10) {
			resultKey = 1;
		}
		// 3,4,37,38,39,45,47
		else if (3 == key || 4 == key || (37 <= key && key <= 39) || key == 45
				|| key == 47) {
			resultKey = 2;
		}
		// 5,6,7,8,18,35
		else if ((5 <= key && key <= 8) || key == 18 || key == 35) {
			resultKey = 3;
		}
		// 11,12,40
		else if (key == 11 || key == 12 || key == 40) {
			resultKey = 4;
		}
		// 13,14,15,16,17,41,42,43,46
		else if ((13 <= key && key <= 17) || (41 <= key && key <= 43)
				|| key == 46) {
			resultKey = 5;
		}
		// 19,
		else if (key == 19) {
			resultKey = 6;
		}
		// 23,24
		else if ((23 <= key && key <= 24)) {
			resultKey = 7;
		}
		// 25,29,30,36,44
		else if (key == 25 || key == 29 || key == 30 || key == 36 || key == 44) {
			resultKey = 8;
		}
		// 26,27,28
		else if ((26 <= key && key <= 28)) {
			resultKey = 9;
		}
		// 31,32,33,34
		else if ((31 <= key && key <= 34)) {
			resultKey = 10;
		}
		// 20,21,22
		else if ((20 <= key && key <= 22)) {
			resultKey = 11;
		}
		// 3200
		else {
			resultKey = 12;
		}

		return String.valueOf(resultKey);
	}

	public String getImageResource(int v) {
		// TODO Auto-generated method stub
		return String.valueOf(v);
	}
}
