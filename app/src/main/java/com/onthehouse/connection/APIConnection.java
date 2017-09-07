package com.onthehouse.connection;

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
	private static final String TAG = "APIConnection";
	private final String USER_AGENT = "Mozilla/5.0";

	// HTTP GET request
	public String sendGet(String urlCall) throws Exception
    {

		StringBuffer response = new StringBuffer();
		try
        {
			String url = "http://ma2.on-the-house.org" + urlCall;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
			con.setDoOutput(true);

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			//int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode);

			PrintStream ps = new PrintStream(con.getOutputStream());

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println(response);
			// close the print stream
			ps.close();
		} catch (MalformedURLException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}

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