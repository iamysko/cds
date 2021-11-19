package com.misterveiga.cds.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class VirusTotal {
	/** The virus total token. */
	@Value("${virus.total.token}")
	public static String virusTotalToken;
	
    private static String virusTotalTokenStatic;

    @Value("${virus.total.token}")
    public void setNameStatic(String token){
        VirusTotal.virusTotalTokenStatic = token;
    }

	public static void scanUrl(String url) {
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create(mediaType, "apikey=" + virusTotalTokenStatic + "&url=" + url);
		Request request = new Request.Builder().url("https://www.virustotal.com/vtapi/v2/url/scan").post(body)
				.addHeader("Accept", "application/json").addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();
		try {
			Response response = client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ObjectNode getScannedUrlData(String url) {
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url("https://www.virustotal.com/vtapi/v2/url/report?apikey="
					+ virusTotalTokenStatic + "&resource=" + url + "&allinfo=false&scan=0").get()
					.addHeader("Accept", "application/json").build();
		
			Response response = client.newCall(request).execute();
			return new ObjectMapper().readValue(response.body().string(), ObjectNode.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
