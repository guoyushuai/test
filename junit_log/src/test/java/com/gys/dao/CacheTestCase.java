package com.gys.dao;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.junit.Test;

public class CacheTestCase {

    @Test
    public void cacheTest() {

        //创建类的对象的同时读取配置文件
        CacheManager cacheManager = new CacheManager();

        //获取策略名为user的配置  getEhcache与getCache区别？
        Ehcache ehcache = cacheManager.getEhcache("user");
        //Ehcache ehcache = cacheManager.getCache("user");

        //添加
        Element element = new Element("user:1","tom");
        ehcache.put(element);

        //删除
        //ehcache.remove("user:1");
        //ehcache.removeAll();

        //取值(获得的不是键key对应的值value,而是对应的Element对象)
        Element e = ehcache.get("user:1");
        System.out.println(e.getObjectValue());

    }

}
