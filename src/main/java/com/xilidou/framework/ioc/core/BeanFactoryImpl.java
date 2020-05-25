package com.xilidou.framework.ioc.core;

import com.xilidou.framework.ioc.bean.*;
import com.xilidou.framework.ioc.utils.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author chen
 */
public class BeanFactoryImpl implements BeanFactory {

    private static final ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, BeanDefinition> beanDefineMap = new ConcurrentHashMap<>();

    private static final Set<String> beanNameSet = new ConcurrentSkipListSet<>();

    @Override
    public Object getBean(String name) throws Exception {
        //查找对象是否已经实例化过
        Object bean = beanMap.get(name);
        if (bean != null) {
            return bean;
        }
        //如果没有实例化，那就需要调用createBean来创建对象
        BeanDefinition beanDefinition = beanDefineMap.get(name);
        bean = createBean(beanDefinition);

        if (bean != null) {

            setProperty(bean, beanDefinition.getPropertyArgs());

            //对象创建成功以后，注入对象需要的参数
            populateBean(bean);

            //再吧对象存入Map中方便下次使用。
            beanMap.put(name, bean);
        }

        //结束返回
        return bean;
    }

    @SneakyThrows
    private void setProperty(Object bean, List<PropertyArg> propertyArgs) {
        if (propertyArgs == null) {
            return;
        }

        for (PropertyArg proper : propertyArgs) {

            Field field = CligbBeanUtils.getRawClass(bean)
                    .getDeclaredField(proper.getName());
            if (StringUtils.isNotBlank(proper.getRef())) {
                ReflectionUtils.injectField(field, bean, getBean(proper.getRef()));
            } else {
                ReflectionUtils.injectField(field, bean, proper.getValue());
            }

        }


    }

    protected void registerBean(String name, BeanDefinition bd) {
        beanDefineMap.put(name, bd);
        beanNameSet.add(name);
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        String beanName = beanDefinition.getClassName();
        Class<?> clz = ClassUtils
                .loadClass(beanName)
                .orElseThrow(() -> new Exception("can not find bean by beanName"));
        List<ConstructorArg> constructorArgs = beanDefinition.getConstructorArgs();
        if (constructorArgs == null || constructorArgs.isEmpty()) {
            return CligbBeanUtils.createInstance(clz, null, null);
        } else {
            List<Object> objects = new ArrayList<>();
            List<Class<?>> classes = new ArrayList<>();

            constructorArgs.sort(Comparator.comparingInt(ConstructorArg::getIndex));
            for (ConstructorArg constructorArg : constructorArgs) {
                if (StringUtils.isBlank(constructorArg.getRef())) {
                    //todo 这里现在只有字符串
                    Object value = constructorArg.getValue();
                    objects.add(value);
                    classes.add(value.getClass());
                } else {
                    Object bean = getBean(constructorArg.getRef());
                    objects.add(bean);
                    classes.add(CligbBeanUtils.getRawClass(bean));
                }
            }
            Class<?>[] constructorArgTypes = classes.toArray(new Class[]{});
            Constructor<?> constructor = clz.getConstructor(constructorArgTypes);
            return CligbBeanUtils.createInstance(clz, constructor, objects.toArray());
        }
    }

    private void populateBean(Object bean) throws Exception {

        Field[] fields = CligbBeanUtils.getRawClass(bean)
                .getDeclaredFields();

        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    try {
                        String beanName = field.getName();
                        beanName = StringUtils.uncapitalize(beanName);
                        if (beanNameSet.contains(field.getName())) {
                            Object fieldBean = getBean(beanName);
                            if (fieldBean != null) {
                                ReflectionUtils.injectField(field, bean, fieldBean);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
    }
}
