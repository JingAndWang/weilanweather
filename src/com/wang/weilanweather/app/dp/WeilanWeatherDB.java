package com.wang.weilanweather.app.dp;

import java.util.ArrayList;
import java.util.List;

import com.wang.weilanweather.app.model.City;
import com.wang.weilanweather.app.model.County;
import com.wang.weilanweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/***
 * ����ʵ����
 * @author JingWang
 * @date 2016/4/2
 */
public class WeilanWeatherDB {
	/***
	 * ���ݿ���
	 */
	public static final String DB_NAME="weilan_weather";
	/***
	 * ���ݿ�汾
	 */
	public static final int VERSION=1;
	private static WeilanWeatherDB weilanWeatherDB;
	private SQLiteDatabase db;
	/***
	 * ���ݿ⹹�췽��˽�з�
	 */
	private WeilanWeatherDB(Context context){
		WeilanWheatherOpenHelper dbHelper=new WeilanWheatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}
	/***
	 * ��ȡWeilanWheatherDB��ʵ��
	 */
	public synchronized static WeilanWeatherDB getInstance(Context context){
		if(weilanWeatherDB==null){
			weilanWeatherDB=new WeilanWeatherDB(context);
		}
		return weilanWeatherDB;
	}
	/**
	 * ��Provinceʵ�д洢�����ݿ�
	 */
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	/**
	 * �����ݿ��ȡȫ�����е�ʡ����Ϣ
	 */
	public List<Province> loadProvinces(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	/**
	 * ��Cityʵ�д������ݿ�
	 */
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	/**
	 * �����ݿ��ȡʡ�����г��е���Ϣ
	 */
	public List<City> loadCities(int provinceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, "province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	/**
	 * ��County���뵽���ݿ�
	 */
	public void saveCounty(County county){
		try{
			if(county!=null){
				ContentValues values=new ContentValues();
				values.put("county_name", county.getCountyName());
				values.put("county_code", county.getCountyCode());
				values.put("city_id", county.getCityId());
				db.insert("County", null, values);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			Log.e("TAG","error");
		}
	}
	/**
	 * �����ݿ��ȡ���������е��ص���Ϣ
	 */
	public List<County> loadCounties(int cityId){
		//try{
			List<County> list=new ArrayList<County>();
			Cursor cursor=db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, 
					null, null, null);
			if(cursor.moveToFirst()){
				do{
					County county=new County();
					county.setId(cursor.getInt(cursor.getColumnIndex("id")));
					county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
					county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
					county.setCityId(cityId);
					list.add(county);
				}while(cursor.moveToNext());
			}
			if(cursor!=null){
				cursor.close();
			}
			
		//}catch(SQLException ex){
			//ex.printStackTrace();
			//Log.e("TAG","error");
		//}
		return list;
	}
}