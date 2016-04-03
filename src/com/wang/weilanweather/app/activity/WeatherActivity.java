/***
 * JSON���ݴ�����������ϸҳ�沼��
 * @author JingWang
 * @date 2016/4/2
 */
package com.wang.weilanweather.app.activity;

import org.json.JSONException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.weilanweather.app.util.HttpCallbackListener;
import com.wang.weilanweather.app.util.HttpUtil;
import com.wang.weilanweather.app.util.Utility;
import com.weilanweather.app.R;

public class WeatherActivity extends Activity{
	private LinearLayout weatherInfoLayout;
	/**
	 * use show city name
	 */
	private TextView cityNameText;
	/**
	 * use show now time
	 */
	private TextView publishText;
	/**
	 * use show wheather Desp.. infomation
	 */
	private TextView weatherDespText;
	/**
	 * use show weather temperature one
	 */
	private TextView temp1Text;
	/**
	 * use show weather temperature two
	 */
	private TextView temp2Text;
	/**
	 * use show current Date
	 */
	private TextView currentDateText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//Initialization control
		weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
		cityNameText=(TextView)findViewById(R.id.city_name);
		publishText=(TextView)findViewById(R.id.publish_text);
		weatherDespText=(TextView)findViewById(R.id.weather_desp);
		temp1Text=(TextView)findViewById(R.id.temp1);
		temp2Text=(TextView)findViewById(R.id.temp2);
		currentDateText=(TextView)findViewById(R.id.current_data);
		String countyCode=getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//�ؼ����ž�ȥ��ѯ����
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE); //�����ܼ���Ϊ��������
			cityNameText.setVisibility(View.INVISIBLE);
			//��ѯ
			queryWeatherCode(countyCode);
		}else{
			//û���ؼ�����ʱ���ֱ����ʾ��������
			showWeather();
		}
	}
	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 */
	private void queryWeatherCode(String countyCode){
		String address="http://www.weather.com.cn/data/list3/city" +
				countyCode + ".xml";
		//��Ҫд��Ĵ���
		queryFromServer(address, "countyCode");
		
	}
	/**
	 * ��ѯ������������Ӧ������
	 */
	private void queryWeatherInfo(String weatherCode){
		String address="http://www.weather.com.cn/data/cityinfo/" +
				weatherCode + ".html";
		//��Ҫд��Ĵ���
		queryFromServer(address, "weatherCode");
	}
	/**
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż�������Ϣ
	 */
	private void queryFromServer(final String address,final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//�ӷ��������ص������н���������������
						String[] array=response.split("\\|");
						if(array!=null&&array.length==2){
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					//�������������ػ�����������Ϣ
					//try {
						Utility.handleWeatherResponse(WeatherActivity.this, response);
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//���붫��
								showWeather();
							}
						});
					//} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					//}
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}
	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ,����ʾ��������
	 */
	private void showWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����"+prefs.getString("publish_time", "")+"����");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
}