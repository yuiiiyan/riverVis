package com.infopublic.util;

import com.hunau.controller.HttpUtils;
import com.hunau.entity.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘家玮
 * 程序名称：WeatherApi.java
 * 程序描述：调用网上天气数据接口,查询当前天气数据、未来24小时天气数据、未来15天天气数据、历史天气数据
 * 修改时间：2017-10-18
 * 开发单位：湖南农业大学信息科学技术学院物联网工程系
 */
public class WeatherApi {
	/*
	 * 获取当前天气数据
	 */
	public String NowWeather(String lat,String lng)
	{
		String str1 = null;
		int rpm25;
		double rco;
		int rno2;
		int ro3;
		int ro38h;
		String rpm10;
		int rso2;
		String rwinddirection;
		String rweathercode;
		String rtemperaturetime;
		String rwindpower;
		String rsd;
		String rweatherpic;
		String rweather;
		String rtemperature;
		String datatojson = null;
		String aqijson=null;
		
		String host = "http://saweather.market.alicloudapi.com";
	    String path = "/gps-to-weather";
	    String method = "GET";
	    String appcode = "5e50e5dead574ca2b4fc17b3c06400a3";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("from", "5");
	    if(lat==null||lng==null)  
	    {
	      querys.put("lat", "28.303652");   //长沙县政府的经度值
		  querys.put("lng", "113.182124");  //长沙县政府的纬度值
	    }
	    else
	    {
	      querys.put("lat", lat);   
	      querys.put("lng", lng);  
	    }
	    querys.put("need3HourForcast", "0");
	    querys.put("needAlarm", "0");
	    querys.put("needHourData", "0");
	    querys.put("needIndex", "0");
	    querys.put("needMoreDay", "0");
	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
	    	//System.out.println(response.toString());
	    	//获取response的body
	    	str1= EntityUtils.toString(response.getEntity());
	    	System.out.println(str1);	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    

/**********************************************************/
	    try {  
//	          1.创建JSON解析对象(两条规则的提现:大括号用JSONObject,注意传入数据对象)  
	            JSONObject jObj = new JSONObject(str1);
//	          2.实例化Person对象获取对应的值--->这里是获得外面大括号{}的name值 思考?里面{}如何获得?  
	            Weather weather=new Weather();  
	            int showapi_res_code = jObj.getInt("showapi_res_code");  
	            weather.setShowapi_res_code(showapi_res_code);  
	            
	            showapi_res_body showapi_res_body=new showapi_res_body();  
	            JSONObject jObj1 = jObj.getJSONObject("showapi_res_body");  //进入嵌套的第二个json对象中了
	            
	            now now=new now();
	            JSONObject jObj2 = jObj1.getJSONObject("now");  //进入嵌套的第三个json对象中了
	            
	            aqiDetail aqiDetail=new aqiDetail();
	            JSONObject jObj3 = jObj2.getJSONObject("aqiDetail");  //进入嵌套的第四个json对象中了
	            
	            
	   
	            /*********************************************/
	            String weather_code=jObj2.getString("weather_code");
	            now.setWeather_code(weather_code);
	            
	            String wind_direction=jObj2.getString("wind_direction");
	            now.setWind_direction(wind_direction);
	            
	            String temperature_time=jObj2.getString("temperature_time");
	            now.setTemperature_time(temperature_time);
	            
	            String wind_power=jObj2.getString("wind_power");
	            now.setWind_power(wind_power);
	            
	            int aqi=jObj2.getInt("aqi");
	            now.setAqi(aqi);
	            
	            String sd=jObj2.getString("sd");
	            now.setSd(sd);
	            
	            String weather_pic=jObj2.getString("weather_pic");
	            now.setWeather_pic(weather_pic);
	            
	            String weather1=jObj2.getString("weather");
	            now.setWeather(weather1);
	            /*********************************************/
	            String temperature=jObj2.getString("temperature");
	            now.setTemperature(temperature);
	            
	            double co=jObj3.getDouble("co");
	            aqiDetail.setCo(co);
	            
	            int so2=jObj3.getInt("so2");
	            aqiDetail.setSo2(so2);
	            
	            String area=jObj3.getString("area");
	            aqiDetail.setArea(area);
	            
	            int o3=jObj3.getInt("o3");
	            aqiDetail.setO3(o3);
	            
	            int no2=jObj3.getInt("no2");
	            aqiDetail.setNo2(no2);
	            
	          /*String area_code=jObj3.getString("area_code");
	            aqiDetail.setArea_code(area_code);*/
	            
	            String quality=jObj3.getString("quality");
	            aqiDetail.setQuality(quality);
	            
	            int aqi1=jObj3.getInt("aqi");
	            aqiDetail.setAqi(aqi1);
	            
	            String pm10=jObj3.getString("pm10");
	            aqiDetail.setPm10(pm10);
	            
	            int pm2_5=jObj3.getInt("pm2_5");
	            aqiDetail.setPm2_5(pm2_5);
	            
	            int o3_8h=jObj3.getInt("o3_8h");
	            aqiDetail.setO3_8h(o3_8h);
	            
	            String primary_pollutant=jObj3.getString("primary_pollutant");
	            aqiDetail.setPrimary_pollutant(primary_pollutant);
	            /*********************************************/
	            
	            /********************************************/
	 /*           f1 f1=new f1();
	            JSONObject jObj2f1 = jObj1.getJSONObject("f1");  //进入嵌套的第三个json对象中了
	            
	            String day_weather=jObj2f1.getString("day_weather");
	            f1.setDay_weather(day_weather);
	            
	            String night_weather=jObj2f1.getString("night_weather");
	            f1.setNight_weather(night_weather);
	            
	            String night_weather_code=jObj2f1.getString("night_weather_code");
	            f1.setNight_weather_code(night_weather_code);
	            
	            String air_press=jObj2f1.getString("air_press");
	            f1.setAir_press(air_press);
	            
	            String jiangshui=jObj2f1.getString("jiangshui");
	            f1.setJiangshui(jiangshui);
	            
	            String night_wind_power=jObj2f1.getString("night_wind_power");
	            f1.setNight_wind_power(night_wind_power);
	            
	            String day_wind_power=jObj2f1.getString("day_wind_power");
	            f1.setDay_wind_power(day_wind_power);
	            
	            String day_weather_code=jObj2f1.getString("day_weather_code");
	            f1.setDay_weather_code(day_weather_code);
	            
	            String sun_begin_end=jObj2f1.getString("sun_begin_end");
	            f1.setSun_begin_end(sun_begin_end);
	            
	            String ziwaixian=jObj2f1.getString("ziwaixian");
	            f1.setZiwaixian(ziwaixian);
	            
	            String day_weather_pic=jObj2f1.getString("day_weather_pic");
	            f1.setDay_weather_pic(day_weather_pic);
	            
	            int weekday=jObj2f1.getInt("")*/
	            
	            /********************************************/
	            f2 f2=new f2();
	            JSONObject jObj2f2 = jObj1.getJSONObject("f2");  //进入嵌套的第三个json对象中了
	            
	            String day_weather2=jObj2f2.getString("day_weather");
	            f2.setDay_weather(day_weather2);
	            
	            String night_weather2=jObj2f2.getString("night_weather");
	            f2.setNight_weather(night_weather2);
	            
	            String night_weather_code2=jObj2f2.getString("night_weather_code");
	            f2.setNight_weather_code(night_weather_code2);
	            
	            String air_press2=jObj2f2.getString("air_press");
	            f2.setAir_press(air_press2);
	            
	            String jiangshui2=jObj2f2.getString("jiangshui");
	            f2.setJiangshui(jiangshui2);
	            
	            String night_wind_power2=jObj2f2.getString("night_wind_power");
	            f2.setNight_wind_power(night_wind_power2);
	            
	            String day_wind_power2=jObj2f2.getString("day_wind_power");
	            f2.setDay_wind_power(day_wind_power2);
	            
	            String day_weather_code2=jObj2f2.getString("day_weather_code");
	            f2.setDay_weather_code(day_weather_code2);
	            
	            String sun_begin_end2=jObj2f2.getString("sun_begin_end");
	            f2.setSun_begin_end(sun_begin_end2);
	            
	            String ziwaixian2=jObj2f2.getString("ziwaixian");
	            f2.setZiwaixian(ziwaixian2);
	            
	            String day_weather_pic2=jObj2f2.getString("day_weather_pic");
	            f2.setDay_weather_pic(day_weather_pic2);
	            
	            String day_air_temperature2=jObj2f2.getString("day_air_temperature");
	            f2.setDay_air_temperature(day_air_temperature2);
	            
	            String night_air_temperature2=jObj2f2.getString("night_air_temperature");
	            f2.setNight_air_temperature(night_air_temperature2);
	           // int weekday=jObj2f2.getInt("");
	            
	            weather.setShowapi_res_body(showapi_res_body);
	            showapi_res_body.setF2(f2);
	            System.out.println("f2**************"+weather.getShowapi_res_body().getF2().getDay_weather_code()+"*"+weather.getShowapi_res_body().getF2().getNight_air_temperature()+"*"+weather.getShowapi_res_body().getF2().getDay_air_temperature());
	            
	            /********************************************/
	            
	            
	           /****************************************/
	            f3 f3=new f3();
	            JSONObject jObj2f3 = jObj1.getJSONObject("f3");  //进入嵌套的第三个json对象中了
	            
	            String day_weather3=jObj2f3.getString("day_weather");
	            f3.setDay_weather(day_weather3);
	            
	            String night_weather3=jObj2f3.getString("night_weather");
	            f3.setNight_weather(night_weather3);
	            
	            String night_weather_code3=jObj2f3.getString("night_weather_code");
	            f3.setNight_weather_code(night_weather_code3);
	            
	            String air_press3=jObj2f3.getString("air_press");
	            f3.setAir_press(air_press3);
	            
	            String jiangshui3=jObj2f3.getString("jiangshui");
	            f3.setJiangshui(jiangshui3);
	            
	            String night_wind_power3=jObj2f3.getString("night_wind_power");
	            f3.setNight_wind_power(night_wind_power3);
	            
	            String day_wind_power3=jObj2f3.getString("day_wind_power");
	            f3.setDay_wind_power(day_wind_power3);
	            
	            String day_weather_code3=jObj2f3.getString("day_weather_code");
	            f3.setDay_weather_code(day_weather_code3);
	            
	            String sun_begin_end3=jObj2f3.getString("sun_begin_end");
	            f3.setSun_begin_end(sun_begin_end3);
	            
	            String ziwaixian3=jObj2f3.getString("ziwaixian");
	            f3.setZiwaixian(ziwaixian3);
	            
	            String day_weather_pic3=jObj2f3.getString("day_weather_pic");
	            f3.setDay_weather_pic(day_weather_pic3);
	            
	            String day_air_temperature3=jObj2f3.getString("day_air_temperature");
	            f3.setDay_air_temperature(day_air_temperature3);
	            
	            String night_air_temperature3=jObj2f3.getString("night_air_temperature");
	            f3.setNight_air_temperature(night_air_temperature3);
	           // int weekday=jObj2f3.getInt("");
	            
	            weather.setShowapi_res_body(showapi_res_body);
	            showapi_res_body.setF3(f3);
	            System.out.println("f3**************"+weather.getShowapi_res_body().getF3().getDay_weather_code()+"*"+weather.getShowapi_res_body().getF3().getNight_air_temperature()+"*"+weather.getShowapi_res_body().getF3().getDay_air_temperature());
	            /********************************************/
	            
	            /********************************************/
	            now.setAqiDetail(aqiDetail);
	            showapi_res_body.setNow(now);
	            weather.setShowapi_res_body(showapi_res_body);
	            weather.setShowapi_res_code(showapi_res_code);
	            rpm25=weather.getShowapi_res_body().getNow().getAqiDetail().getPm2_5();//获取PM2.5
	            rco=weather.getShowapi_res_body().getNow().getAqiDetail().getCo();
	            rno2=weather.getShowapi_res_body().getNow().getAqiDetail().getNo2();
	            ro3=weather.getShowapi_res_body().getNow().getAqiDetail().getO3();
	            ro38h=weather.getShowapi_res_body().getNow().getAqiDetail().getO3_8h();
	            rpm10=weather.getShowapi_res_body().getNow().getAqiDetail().getPm10();
	            rso2=weather.getShowapi_res_body().getNow().getAqiDetail().getSo2();
	            
	            
	            rweathercode=weather.getShowapi_res_body().getNow().getWeather_code();
	            rwinddirection=weather.getShowapi_res_body().getNow().getWind_direction();
	            rtemperaturetime=weather.getShowapi_res_body().getNow().getTemperature_time();
	            rwindpower=weather.getShowapi_res_body().getNow().getWind_power();
	            rsd=weather.getShowapi_res_body().getNow().getSd();
	            rweatherpic=weather.getShowapi_res_body().getNow().getWeather_pic();
	            rweather=weather.getShowapi_res_body().getNow().getWeather();
	            rtemperature=weather.getShowapi_res_body().getNow().getTemperature();
	            
	            
	            
	            
	            
	            
	            System.out.println(rpm25+"*a*"+rco+"*b*"+rno2+"*c*"+ro3+"*d*"+ro38h+"*e*"+rpm10+"*f*"+rtemperaturetime+"*i*"+rwindpower+"*j*"+rsd+"*k*"+rweatherpic+"*l*"+rweather+"*m*"+rtemperature+"*n*"+rweathercode+"*o"+rwinddirection);
	     
	            datatojson="{"+"\""+"pm2_5"+"\""+":"+"\""+rpm25+"\""+","+"\""+"temperature"+"\""+":"+"\""+rtemperature+"\""+","+"\""+"wind_direction"+"\""+":"+"\""+rwinddirection+"\""+","+"\""+"wind_power"+"\""+":"+"\""+rwindpower+"\""+","+"\""+"weather_pic"+"\""+":"+"\""+rweatherpic+"\"" +","+"\""+"weather"+"\""+":"+"\""+rweather+"\""+"}";
	            //aqijson="{"+"\""+"pm2_5"+"\""+":"+"\""+rpm25+"\""+","+"\""+"pm10"+"\""+":"+"\""+rpm10+"\""+","+"\""+"co"+"\""+":"+"\""+rco+"\""+","+"\""+"no2"+"\""+":"+"\""+rno2+"\""+","+"\""+"so2"+"\""+":"+"\""+rso2+"\"" +","+"\""+"o3"+"\""+":"+"\""+ro3+"\""+"}";
	            
	            aqijson="{"+"\""+"pm2_5"+"\""+":"+"\""+rpm25+"\""+","+"\""+"pm10"+"\""+":"+"\""+rpm10+"\""+","+"\""+"co"+"\""+":"+"\""+rco+"\""+","+"\""+"no2"+"\""+":"+"\""+rno2+"\""+","+"\""+"so2"+"\""+":"+"\""+rso2+"\"" +","+"\""+"o3"+"\""+":"+"\""+ro3+"\""+","+"\""+"weather_picf2"+"\""+":"+"\""+weather.getShowapi_res_body().getF2().getDay_weather_code()+"\""+","+"\""+"weather_picf3"+"\""+":"+"\""+weather.getShowapi_res_body().getF3().getDay_weather_code()+"\""+","+"\""+"mintempf2"+"\""+":"+"\""+weather.getShowapi_res_body().getF2().getNight_air_temperature()+"\""+","+"\""+"maxtempf2"+"\""+":"+"\""+weather.getShowapi_res_body().getF2().getDay_air_temperature()+"\""+","+"\""+"mintempf3"+"\""+":"+"\""+weather.getShowapi_res_body().getF3().getNight_air_temperature()+"\""+","+"\""+"maxtempf3"+"\""+":"+"\""+weather.getShowapi_res_body().getF3().getDay_air_temperature()+"\""+"}";                                
	            //String dataset=String.valueOf(rpm25)+" "+String.valueOf(rco)+" "+String.valueOf(str3)+" "+String.valueOf(str4)+" "+String.valueOf(str5)+" "+String.valueOf(str6)+" "+String.valueOf(str7);
	            //mv.addObject("message",String.valueOf(str2));
	               
	    }catch (JSONException e) {
	        e.printStackTrace();  
	    }
	    return  aqijson;
	}
	
	
    /*
     * 获取未来24小时的天气数据
     */
	public String HoursFutureWeather(String address) throws JSONException
	{       
		    String str2 = null;
		    
		    String host = "http://saweather.market.alicloudapi.com";
		    String path = "/hour24";
		    String method = "GET";
		    String appcode = "5e50e5dead574ca2b4fc17b3c06400a3";
		    Map<String, String> headers = new HashMap<String, String>();
		    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		    headers.put("Authorization", "APPCODE " + appcode);
		    Map<String, String> querys = new HashMap<String, String>();
		    if(address==null)  //默认情况下为查询长沙的数据
		    {
		    	querys.put("area", "长沙");
		    }
		    else
		    {
		    	querys.put("area", address);
		    }
		   

		    try {
		    	/**
		    	* 重要提示如下:
		    	* HttpUtils请从
		    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
		    	* 下载
		    	*
		    	* 相应的依赖请参照
		    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
		    	*/
		    	HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
		    	//System.out.println(response.toString());
		    	//获取response的body
		    	str2= EntityUtils.toString(response.getEntity());
		    	System.out.println(str2);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		    
//	          1.创建JSON解析对象(两条规则的提现:大括号用JSONObject,注意传入数据对象)  
	            JSONObject jObj = new JSONObject(str2);
//	          2.实例化Person对象获取对应的值--->这里是获得外面大括号{}的name值 思考?里面{}如何获得?  
	            Hours24Weather hours24weather=new Hours24Weather();  
	            String code = jObj.getString("showapi_res_code");  
	            hours24weather.setCode(code);
	            
	            String error=jObj.getString("showapi_res_error");
				hours24weather.setError(error);
				
				JSONObject jObj1 = jObj.getJSONObject("showapi_res_body");  //进入嵌套的第二个json对象中了
				
				Hours24WeatherBody hours24WeatherBody=new Hours24WeatherBody();
				
				String ret_code=jObj1.getString("ret_code");
				hours24WeatherBody.setRet_code(ret_code);
				
				String area=jObj1.getString("area");
				hours24WeatherBody.setArea(area);
				
				String areaid=jObj1.getString("areaid");
				hours24WeatherBody.setAreaid(areaid);
				
				JSONArray hourList=jObj1.getJSONArray("hourList");
				hours24WeatherBody.setHourList(hourList);
				
			    String rcode=hours24weather.getCode();
			    String rerror=hours24weather.getError();
			    
			   
			    String rret_code=hours24WeatherBody.getRet_code();
			    String rarea=hours24WeatherBody.getArea();
			    String rareaid=hours24WeatherBody.getAreaid();
			    JSONArray rhourList=hours24WeatherBody.getHourList();
			    
			    //System.out.println("*********************");
			    //System.out.println(rcode+"?"+rerror+"?"+rret_code+"?"+rarea+"?"+rareaid+"?"+rhourList);
			    
			    //将JSON数组转化成JSON对象
			    String aqihoursjson=rhourList.toString();
			    return aqihoursjson;
	}
	/*
	 * 获取未来15天的天气数据
	 */
//	public String DayFutureWeather()
//	{
//		
//	}
	/*
	 * 获取历史天气数据
	 */
//	public String HistoryWeather()
//	{
//		
//	}
}
