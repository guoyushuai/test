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

        //创建一个可以发出请求的客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建一个GET请求方式
        HttpGet httpGet = new HttpGet(url);

        try {

            //发出请求并接受服务器响应
            HttpResponse response = httpClient.execute(httpGet);

            //判断http响应的状态码
            if(response.getStatusLine().getStatusCode() == 200) {

                //获取响应输入流
                InputStream inputStream = response.getEntity().getContent();
                //将输入流转换为字符串
                String result = IOUtils.toString(inputStream,"UTF-8");

                //关闭创建的客户端
                httpClient.close();

                return result;
            } else {
                //http响应状态码非200的情况抛出运行时异常，并将状态码发送出去
                throw new RuntimeException("请求" + url + "异常" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            //服务器异常，并发送异常信息
            throw new RuntimeException("请求" + url + "异常",e);
        }
    }
}
