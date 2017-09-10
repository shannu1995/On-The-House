package com.onthehouse.connection;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class APIConnection
{

	private final String USER_AGENT = "Mozilla/5.0";
	private static final String TAG = "APIConnection";

	// HTTP GET request
	public String sendGet(String urlCall) throws Exception {

		StringBuffer response = new StringBuffer();
		String url = "http://ma2.on-the-house.org" + urlCall;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		try {
			// optional default is GET
			con.setRequestMethod("GET");
			//con.setDoOutput(true);
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			Log.d(TAG, "sendGet: sending http request: "+obj);
			//int responseCode = con.getResponseCode();
			//Log.d(TAG, "sendGet: response code is: "+responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			Log.d(TAG, "sendGet: buffer reader is reading: "+in);
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				System.out.println("readline data for output is: "+inputLine);
				response.append(inputLine);
			}
			in.close();

			System.out.println(response);

		} catch (MalformedURLException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
		finally {
			Log.d(TAG, "sendGet: in a finally block");
			con.disconnect();
		}
		System.out.println("\nGetting  : " + response);
		return response.toString();
	}

	// HTTP POST request
	public String sendPost(String urlCall, ArrayList<String> parameterList) throws Exception
    {
        StringBuffer response = new StringBuffer();

		try
		{
            // open a connection to the site
            URL url = new URL("http://ma2.on-the-house.org"+urlCall);
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());

            for(int i=0; i<parameterList.size(); i++)
            {
                ps.print(parameterList.get(i));
            }
            // send your parameters to your site
            //ps.print("&email=abubakar128@hotmail.com");
            //ps.print("&password=qwerty54321");

            // we have to get the input stream in order to actually send the request
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    		String inputLine;

    		while ((inputLine = in.readLine()) != null)
    		{
    			response.append(inputLine);
    		}
    		in.close();

    		System.out.println(response);
            // close the print stream
            ps.close();
        }
		catch (MalformedURLException e1)
		{
                e1.printStackTrace();
        }
		catch (IOException e2)
		{
                e2.printStackTrace();
        }

        return response.toString();
	}

}