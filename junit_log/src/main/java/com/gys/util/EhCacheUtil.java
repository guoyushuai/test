package com.gys.util;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.io.Serializable;

public class EhCacheUtil {

    //每new一次获得对象,就读取一次配置文件，为了性能,static保证只new一次,只读取一次,cacheManager对象只有一个
    private static CacheManager cacheManager = new CacheManager();

    public Ehcache getEhCache(String cacheName) {
        return cacheManager.getCache(cacheName);
    }


    public void set(Ehcache ehcache, Serializable key,Serializable value) {
        Element element = new Element(key,value);
        ehcache.put(element);
    }
    //方法的重载
    public void set(String cacheName,Serializable key,Serializable value) {
        Element element = new Element(key,value);
        getEhCache(cacheName).put(element);
    }
    //针对List未实现序列化 Object key
    public void set(String cacheName,Object key,Object value) {
        Element element = new Element(key,value);
        getEhCache(cacheName).put(element);
    }


    public Object get(Ehcache ehcache,Serializable key) {
        Element element = ehcache.get(key);
        return element == null ? null : element.getObjectValue();
    }
    //三元表达式取代if else
    public Object get(String cacheName,Serializable key) {
        Element element = getEhCache(cacheName).get(key);
        return element == null ? null : element.getObjectValue();
    }


    public void removeAll(Ehcache ehcache) {
        ehcache.removeAll();
    }
    public void removeAll(String cacheName) {
        getEhCache(cacheName).removeAll();
    }


    public void remove(Ehcache ehcache,Serializable key) {
        ehcache.remove(key);
    }
    public void remove(String cacheName,Serializable key) {
        getEhCache(cacheName).remove(key);
    }
}
