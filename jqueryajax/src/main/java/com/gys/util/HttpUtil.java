package com.gys.util;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;


public class HttpUtil {

    public static String sendHttpGetRequestWithString(String url) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200) {

                InputStream inputStream = response.getEntity().getContent();
                String result = IOUtils.toString(inputStream,"UTF-8");
                httpClient.close();

                return result;
            } else {
                throw new RuntimeException("请求" + url + "异常" + response.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            throw new RuntimeException("请求" + url + "异常",e);
        }

    }

}
