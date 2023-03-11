package org.example.di;

import org.example.annotation.Inject;
import org.example.controller.UserController;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;

//Bean을 등록해주는 클래스
public class BeanFactory {
    private final Set<Class<?>> clazzs;
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(Set<Class<?>> clazzs) {
        this.clazzs = clazzs;
        initialize();
    }

    private void initialize() {
        for(Class<?> clazz : clazzs) {
            Object instance = createInstance(clazz);
            beans.put(clazz, instance);
        }
    }

    private Object createInstance(Class<?> clazz) {
        Object obj = null;
        try {
            // 생성자 정보
            Constructor<?> declaredConstructor = findConstructor(clazz);

            // parameter 정보
            List<Object> parameters = new ArrayList<>();
            for(Class<?> typeClass : declaredConstructor.getParameterTypes()) {
                parameters.add(getParameterByClass(typeClass));
            }

            // 인스턴스 생성
            obj = declaredConstructor.newInstance(parameters.toArray());
        }catch(Exception e) {

        }
        return obj;

    }

    private Constructor<?> findConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if(Objects.nonNull(constructor)) return constructor;

        return clazz.getConstructors()[0];
    }

    private Object getParameterByClass(Class<?> typeClass) {
        Object instanceBean = getBean(typeClass);

        if(Objects.nonNull(instanceBean))
            return instanceBean;
        return createInstance(typeClass);
    }

    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }


}
