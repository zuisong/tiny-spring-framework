package com.xilidou.framework.ioc.utils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * @author chen
 */
public class CligbBeanUtils {

    public static <T> T createInstance(Class<?> clz, Constructor<?> ctr, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallback(NoOp.INSTANCE);

        if(ctr == null){
            return (T) enhancer.create();
        }else {
            return (T) enhancer.create(ctr.getParameterTypes(),args);
        }
    }

    public static Class<?> getRawClass(Object bean){
        // 这里是 cligb 生成的子类  要加上getSuperclass 才是对的
        return bean.getClass().getSuperclass();
    }

}
