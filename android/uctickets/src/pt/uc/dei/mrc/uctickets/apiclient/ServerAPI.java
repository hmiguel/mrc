package pt.uc.dei.mrc.uctickets.apiclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServerAPI {

	private static String path = "http://mrcserver.herokuapp.com/api/";
	
	//private static String path = "http://0.0.0.0:5000/api/";
	
	private static JSONObject toJSONObject(String jsonString)
	{
		JSONObject jsonObj = null;
		try
		{
			jsonObj = new JSONObject(jsonString);
			
		}catch (JSONException e)
		{
			jsonObj = new JSONObject();
		}
		
		return jsonObj;
	}
	
	//GET - funcao generica para GETS
	
	public static JSONObject get(String call) throws JSONException
	{
		//SharedPreferences prefs = UCFrontDesk.getAppContext().getSharedPreferences("frontdesk", Context.MODE_PRIVATE);
		//String token = prefs.getString("frontdesk.token", "");
		
		
		String token = "abc";
		
		String url = path + call;

		HttpResponse response = null;

		StringBuilder json = new StringBuilder();
	
		int timeout = 10000;  // = 10 seconds

		HttpGet get = new HttpGet(url);

		get.setHeader("Authorization", "UCFD " + token);
		
		
		get.setHeader("content-type", "application/json;charset=UTF-8");
		get.setHeader("Accept", "application/json;charset=UTF-8");
		
		
		try
		{
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
			HttpConnectionParams.setSoTimeout(httpParams, timeout);

			HttpClient httpClient = new DefaultHttpClient(httpParams);
			
			response = httpClient.execute(get);
			

			if (response.getStatusLine().getStatusCode() > 400)
			{
				String msg = response.getStatusLine().getReasonPhrase();
				
				
					return toJSONObject("");
					
			}else{
					
					HttpEntity entity = response.getEntity();

					InputStream is = entity.getContent();
					BufferedReader rd = new BufferedReader(new InputStreamReader(is));

					String line;
				
					while ((line = rd.readLine()) != null){
							json.append(line);
					}

					rd.close();
				}
		}
		catch (ClientProtocolException e)
		{
			Log.w("UCFRONTDESK", "HTTP protocol error " + e);
			//throw new NoInternetConnection();
		}catch (IOException e){
			Log.w("UCFRONTDESK", "Communication error "+ e);
		}
			
		Log.w("UCFRONTDESK", "Json String Response: " + json.toString());
				
		return new JSONObject(json.toString());
		
	}
	
	public static JSONObject post(String call, String data)
	{
		Log.w("UCFRONTDESK", "DATA: " + data);
		
		String url = path + call;

		HttpPost post = new HttpPost(url);
		
		post.setHeader("content-type", "application/json;charset=UTF-8");
		post.setHeader("Accept", "application/json;charset=UTF-8");

		try
		{
			StringEntity entity = new StringEntity(data);
			entity.setContentType("application/json");
			post.setEntity(entity);
			
		}catch(UnsupportedEncodingException e)
		{
			Log.w("UCFRONTDESK", e.toString());
		}

		StringBuilder json = new StringBuilder();

		int timeout = 10000;  // = 10 seconds

		try
		{
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
			HttpConnectionParams.setSoTimeout(httpParams, timeout);

			HttpClient client = new DefaultHttpClient(httpParams);
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() > 400)
			{
				String msg = response.getStatusLine().getReasonPhrase();
				
				
				return toJSONObject("");
				
			}else
			{
				HttpEntity receive = response.getEntity();

				InputStream is = receive.getContent();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));

				String line;

				while ((line = rd.readLine()) != null)
				{
					json.append(line);
				}
				
				rd.close();
			}
		}catch (ClientProtocolException e)
		{
			Log.w("UCFRONTDESK", "HTTP protocol error " + e);
		}catch (IOException e)
		{
			Log.w("UCFRONTDESK", "Communication error "+ e);
		
		}
		
		Log.w("UCFRONTDESK", "JSON: " + json.toString());
		
		return toJSONObject(json.toString());
	}
	
}
