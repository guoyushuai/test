package com.gys;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/save");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name","tom"));
        params.add(new BasicNameValuePair("address","usa"));

        httpPost.setEntity(new UrlEncodedFormEntity(params));

        for(int i = 0;i < 5;i++) {

            httpClient.execute(httpPost);

        }

    }

}
