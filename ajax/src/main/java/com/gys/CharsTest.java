package com.gys;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class CharsTest {

    public static void main(String[] args) throws IOException {

        //创建了一个可以发出请求的客户端（浏览器）
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建一个get请求方式
        HttpGet httpGet = new HttpGet("http://www.kaishengit.com");
        //发出请求并接受服务端响应
        HttpResponse response = httpClient.execute(httpGet);
        //获取http响应的状态码
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 200) {

            //获取响应输入流
            InputStream inputStream = response.getEntity().getContent();
            //将输入流转换为字符串
            String result = IOUtils.toString(inputStream,"UTF-8");

            inputStream.close();

            System.out.println(result);

        } else {
            System.out.println("服务器异常：" + statusCode);
        }

        httpClient.close();

    }
}
