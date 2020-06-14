package com.scut.es.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BeanUtils extends org.springframework.beans.BeanUtils {

    public static DefaultConversionService defaultConversionService = new DefaultConversionService();

    static {
        DateFormatterRegistrar.addDateConverters(defaultConversionService);
    }

    public static <T> T convert(Object source, Class<T> targetClass, String... ignoreProperties) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copyNotNullProperties(source, target, ignoreProperties);
            return target;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error(e.getMessage());
        }
        throw new IllegalArgumentException("数据拷贝出错");
    }


    public static void copyNotNullProperties(Object source, Object target) throws BeansException {
        copyNotNullProperties(source, target, null, (String[]) null);
    }

    public static void copyNotNullProperties(Object source, Object target, Class<?> editable) throws BeansException {
        copyNotNullProperties(source, target, editable, (String[]) null);
    }

    public static void copyNotNullProperties(Object source, Object target, String... ignoreProperties) throws BeansException {
        copyNotNullProperties(source, target, null, ignoreProperties);
    }


    private static void copyNotNullProperties(Object source, Object target, Class<?> editable, String... ignoreProperties)
            throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {

                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (value == null) {
                                continue;
                            }
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            if (value instanceof Collection) {
                                ResolvableType readResolvableType = ResolvableType.forType(readMethod.getGenericReturnType());
                                ResolvableType writeResolvableType = ResolvableType.forType(writeMethod.getGenericParameterTypes()[0]);
                                if (readResolvableType.resolveGeneric(0).equals(writeResolvableType.resolveGeneric(0))) {

                                } else if (defaultConversionService.canConvert(readResolvableType.resolveGeneric(0), writeResolvableType.resolveGeneric(0))) {
                                    value = ((Collection) value).stream().map(v ->
                                            defaultConversionService.convert(v, writeResolvableType.resolveGeneric(0))).collect(Collectors.toList());
                                } else {
                                    value = ((Collection) value).stream().map(v ->
                                            BeanUtils.convert(v, writeResolvableType.resolveGeneric(0))).collect(Collectors.toList());
                                }
                            }
                            writeMethod.invoke(target, value);
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    } else if (readMethod != null && defaultConversionService.canConvert(readMethod.getReturnType(), writeMethod.getParameterTypes()[0])) {

                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (value == null) {
                                continue;
                            }

                            ResolvableType resolvableType = ResolvableType.forType(writeMethod.getGenericParameterTypes()[0]);

                            value = defaultConversionService.convert(value, new TypeDescriptor(resolvableType, null, null));

                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }
}
