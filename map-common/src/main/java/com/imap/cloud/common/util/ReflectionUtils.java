package com.imap.cloud.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;

public abstract class ReflectionUtils {
	public static Field findField(Class<?> type, String name) {
		Class clz = type;
		while (clz != null) {
			if (clz == Object.class)
				break;
			for (Field field : clz.getDeclaredFields()) {
				if (field.getName().equals(name)) {
					return field;
				}
			}
			clz = clz.getSuperclass();
		}
		return null;
	}

	public static Method findMethod(Class<?> type, String name,
			Class<?>[] paramTypes) {
		Class searchType = type;
		while ((!Object.class.equals(searchType)) && (searchType != null)) {
			Method[] methods = searchType.isInterface() ? searchType
					.getMethods() : searchType.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if ((name.equals(method.getName()))
						&& (isCompatible(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	public static boolean isCompatible(Class<?>[] classArray,
			Class<?>[] toClassArray) {
		if (!ArrayUtils.isSameLength(classArray, toClassArray)) {
			return false;
		}
		if (classArray == null) {
			classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
		}
		if (toClassArray == null) {
			toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
		}
		for (int i = 0; i < classArray.length; i++) {
			if (!isCompatible(classArray[i], toClassArray[i])) {
				return false;
			}
		}
		return true;
	}

	public static boolean isCompatible(Class<?> cls, Class<?> toClass) {
		if (toClass == null) {
			return false;
		}

		if (cls == null) {
			return !toClass.isPrimitive();
		}
		if (cls.equals(toClass)) {
			return true;
		}
		if (toClass.isPrimitive()) {
			if (Integer.TYPE.equals(toClass)) {
				return cls.equals(Integer.class);
			}
			if (Long.TYPE.equals(toClass)) {
				return cls.equals(Long.class);
			}
			if (Boolean.TYPE.equals(toClass)) {
				return cls.equals(Boolean.class);
			}
			if (Double.TYPE.equals(toClass)) {
				return cls.equals(Double.class);
			}
			if (Float.TYPE.equals(toClass)) {
				return cls.equals(Float.class);
			}
			if (Character.TYPE.equals(toClass)) {
				return cls.equals(Character.class);
			}
			if (Short.TYPE.equals(toClass)) {
				return cls.equals(Short.class);
			}
			if (Byte.TYPE.equals(toClass)) {
				return cls.equals(Byte.class);
			}

			return false;
		}
		return toClass.isAssignableFrom(cls);
	}
}
