package org.geektimes.projects.user.web.security;

import java.lang.reflect.Field;
import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
 * @author xubin
 * @date 2021/4/28 20:58
 */
@Component
public class WebSecurityConfigurerBeanPostProcessor implements BeanPostProcessor {

    private HttpSecurity httpSecurity;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (WebSecurityConfigurerAdapter.class.isAssignableFrom(clazz)) {
            handleHttpSecurity(bean, clazz);
        }
        return null;
    }

    private void handleHttpSecurity(Object bean, Class<?> clazz) throws BeansException {
        try {
            Field http = clazz.getDeclaredField("http");
            http.setAccessible(true);
            if (Objects.isNull(httpSecurity)) {
                httpSecurity = (HttpSecurity)http.get(bean);
            } else {
                http.set(bean, httpSecurity);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new BeanCreationException("create bean fail, clazz：" + clazz.getTypeName());
        }
    }
}
