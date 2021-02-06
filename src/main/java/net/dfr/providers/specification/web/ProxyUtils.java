package net.dfr.providers.specification.web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

final class ProxyUtils {

	private ProxyUtils() {
	}

	@SuppressWarnings("unchecked")
	public static final <T> T proxy(Class<T> targetInterface, InvocationHandler invocationHandler) {
		return (T) Proxy.newProxyInstance(targetInterface.getClassLoader(), new Class[] { targetInterface }, invocationHandler);
	}

	@SuppressWarnings("unchecked")
	public static final <T> T proxy(Object target, Class<T> targetInterface) {
		if (target == null) {
			return null;
		} else if (targetInterface.equals(target.getClass())) {
			return (T) target;
		}
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[] { targetInterface },
				(proxy, method, args) -> method.invoke(target, args));
	}

}
