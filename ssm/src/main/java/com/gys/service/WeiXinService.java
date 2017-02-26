package com.gys.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.gys.dto.wx.TextMessage;
import com.gys.dto.wx.User;
import com.gys.exception.ServiceException;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class WeiXinService {

    private static Logger logger = LoggerFactory.getLogger(WeiXinService.class);

    //导入配置文件config.properties中的键值对
    //应用中心-回调模式
    @Value("${wx.token}")
    private String token;
    @Value("${wx.aeskey}")
    private String aesKey;
    //设置-账号信息
    @Value("${wx.corpid}")
    private String corpid;

    //AccessToken需要用CorpID和Secret来换取
    //设置-权限管理-普通管理组
    @Value("${wx.secret}")
    private String secret;

    private static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={0}&corpsecret={1}";//占位符0,1
    private static final String CREATE_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token={0}";
    private static final String EDIT_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token={0}";
    private static final String SEND_TEXT_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={0}";


    /**
     * 微信企业号初始化方法 验证url
     * @return
     */
    public String init(String msg_signature, String timestamp, String nonce, String echostr) {

        try {
            //创建解密对象
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(token,aesKey,corpid);
            //解析出url上的四个参数值，返回回调需要的明文
            return crypt.VerifyURL(msg_signature,timestamp,nonce,echostr);
        } catch (AesException e) {
            throw new ServiceException("微信初始化异常",e);
        }

    }

    /*1、服务器发出HTTP请求 HttpClient OkHttpClient
    2、解析字符串 JSON Gson
    3、缓存 Ehcache Guava*/

    //缓存
    private LoadingCache<String,String> cache = CacheBuilder.newBuilder()//new CacheBuilder<>();
            .maximumSize(10)//最大缓存数量(其实有且只有一个)
            .expireAfterWrite(7200, TimeUnit.SECONDS)//获取后两小时过期(微信中AccessToken的有效期)
            .build(new CacheLoader<String, String>() {//匿名局部内部类//每7200s秒调用一次
                @Override
                public String load(String s) throws Exception {//获取缓存,键名无所谓

                    String url = MessageFormat.format(ACCESS_TOKEN_URL,corpid,secret);//java定义的类，将字符串中占位符替换成指定数据，下标从0开始

                    OkHttpClient httpClient = new OkHttpClient();//创建一个OKHttpClient对象,对apache的HttpClient的封装
                    Request request = new Request.Builder()
                            .url(url)
                            .build();//创建一个Request,Builder是Request的内部类，然后链式调用
                    Response response = httpClient.newCall(request).execute();//发出一个请求,返回一个Response
                    String result = response.body().string();//string()将内容转成字符串，toString()是body对象的toString()
                    response.close();

                    /*正确的Json返回结果:
                    {
                        "access_token": "accesstoken000001",
                            "expires_in": 7200
                    }
                    错误的Json返回示例:
                    {
                        "errcode": 43003,
                            "errmsg": "require https"
                    }*/
                    Map<String,Object> map = new Gson().fromJson(result, HashMap.class);//将结果Json格式的键值对转换成Map集合
                    if(map.containsKey("errcode")) {
                        logger.error("获取微信AccessToken异常:{}",map.get("errcode"));
                        throw new ServiceException("获取AccessToken异常:" + map.get("errcode"));
                    } else {//map.get("xxx")获得的结果是Object
                        return map.get("access_token").toString();
                    }

                }
            });

    /**
     * 获取微信的AccessToken
     * @return
     */
    public String getAccessToken() {
        try {
            return cache.get("");//键名无所谓，主要使用缓存//传什么键都无所谓
        } catch (ExecutionException e) {
            throw new ServiceException("获取AccessToken异常",e);
        }
    }

    /**
     * 在微信中创建成员
     * @param user
     */
    public void saveUser(User user) {
        //获取AccessToken,作为url的一部分，构建出完整的请求地址
        String url = MessageFormat.format(CREATE_USER_URL,getAccessToken());

        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),new Gson().toJson(user));//Post请求，要求传入RequestBody（）两个参数，请求头JSON格式，数据json字符串
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            String result = response.body().string();
            response.close();

            //微信返回的结果（只有正确的没有错误的，一切皆有可能）
            /*{
                "errcode": 0,
                "errmsg": "created"
            }*/
            Map<String,Object> map = new Gson().fromJson(result,HashMap.class);
            Object errcode = map.get("errcode");
            if("0.0".equals(errcode.toString())) {//0.0 double类型  Intrger.parseInt(errcode.toString())) != 0
                logger.error("微信创建用户异常：{}",result);
                throw new ServiceException("微信创建用户异常：" + result);//抛异常是为了触发事务
            }
        } catch (IOException e) {
            throw new ServiceException("微信创建用户异常",e);
        }
    }

    /**
     * 修改微信中成员信息
     * 如果非必须的字段未指定，则不更新该字段之前的设置值
     * @param user
     */
    public void editUser(User user) {
        String url = MessageFormat.format(EDIT_USER_URL,getAccessToken());

        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),new Gson().toJson(user));//Post请求，要求传入RequestBody（）两个参数，请求头JSON格式，数据json字符串
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            String result = response.body().string();
            response.close();

            //微信返回的结果（只有正确的没有错误的）
            /*{
                "errcode": 0,
                "errmsg": "updated"
            }*/
            Map<String,Object> map = new Gson().fromJson(result,HashMap.class);
            Object errcode = map.get("errcode");
            if(!"0".equals(errcode.toString())) {//与新建用户中返回的值不一致
                logger.error("微信修改用户异常：{}",result);
                throw new ServiceException("微信修改用户异常：" + result);//抛异常是为了触发事务
            }
        } catch (IOException e) {
            throw new ServiceException("微信修改用户异常",e);
        }
    }

    /**
     * 微信给成员发送文本消息
     */
    public void sendTextMessage(TextMessage textMessage) {
        String url = MessageFormat.format(SEND_TEXT_MESSAGE_URL,getAccessToken());

        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),new Gson().toJson(textMessage));
        Request request = new Request.Builder().post(requestBody).url(url).build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            String resultJson = response.body().string();

            /*微信返回的结果（只有正确的没有错误的）*/
            /*{
                "errcode": 0,
                "errmsg": "ok",
                "invaliduser": "UserID1",
                "invalidparty":"PartyID1",
                "invalidtag":"TagID1"
            }*/
            Map<String,Object> map = new Gson().fromJson(resultJson,HashMap.class);
            String errorCode = map.get("errcode").toString();
            if(!"0.0".equals(errorCode)) {
                logger.error("微信发送文本信息失败.{}",resultJson);
                throw new ServiceException("微信发送文本信息失败:"+resultJson);
            }
        } catch (IOException e) {
            throw new ServiceException("发送微信文本消息异常",e);
        }
    }


}
