package org.example.di;

import org.assertj.core.api.Assertions;
import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {
    public static final String BASE_PACKAGE = "org.example";
    private Reflections reflections;
    private BeanFactory beanFactory;

    //테스트 전 초기화
    @BeforeEach
    void setUp(){
        reflections = new Reflections(BASE_PACKAGE);
        //UserController, UserService가 반환됨
        Set<Class<?>> preInstantiatedClazz = getTypesAnnotatedWith(Controller.class, Service.class);
        beanFactory = new BeanFactory(preInstantiatedClazz);
    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends  Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    @Test
    void diTest(){
        UserController userController = beanFactory.getBean(UserController.class);
        Assertions.assertThat(userController).isNotNull();
        Assertions.assertThat(userController.getUserService()).isNotNull();
    }
}