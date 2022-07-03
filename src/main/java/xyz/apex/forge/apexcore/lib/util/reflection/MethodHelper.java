package xyz.apex.forge.apexcore.lib.util.reflection;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cpw.mods.modlauncher.api.INameMappingService;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Nullable;

import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import xyz.apex.forge.apexcore.core.ApexCore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

@Deprecated(forRemoval = true)
public class MethodHelper
{
	private static final Marker MARKER = MarkerManager.getMarker("MethodReflection");
	private static final Cache<String, Method> methodsCache = CacheBuilder.newBuilder().weakValues().build();

	public static Method findMethod(Class<?> classToAccess, String methodName, @Nullable Class<?>[] parameterTypes)
	{
		Validate.notNull(classToAccess, "Class to find method cannot be null");
		Validate.notNull(methodName, "Name of method to find cannot be null");
		Validate.notEmpty(methodName, "Name of method to find cannot be empty");
		Validate.notBlank(methodName, "Name of method to find cannot be blank.");

		try
		{
			return methodsCache.get(methodName, () -> cacheMethod(classToAccess, methodName, parameterTypes));
		}
		catch(ExecutionException e)
		{
			throw new UnableToFindMethodException(e);
		}
	}

	private static Method cacheMethod(Class<?> classToAccess, String methodName, @Nullable Class<?>[] parameterTypes)
	{
		Validate.notNull(classToAccess, "Class to find method cannot be null");
		Validate.notNull(methodName, "Name of method to find cannot be null");
		Validate.notEmpty(methodName, "Name of method to find cannot be empty");
		Validate.notBlank(methodName, "Name of method to find cannot be blank");

		try
		{
			Method method;

			if(parameterTypes != null && parameterTypes.length > 0)
				method = classToAccess.getDeclaredMethod(methodName, parameterTypes);
			else
				method = classToAccess.getDeclaredMethod(methodName);

			method.setAccessible(true);
			return method;
		}
		catch(NoSuchMethodException e)
		{
			throw new UnableToFindMethodException(e);
		}
	}

	@Nullable
	public static <E> E invokeMethod(Class<?> classToAccess, @Nullable Object instance, String methodName, @Nullable Class<?>[] parameterTypes, @Nullable Object... parameters)
	{
		Validate.notNull(classToAccess, "Class to find method cannot be null");
		Validate.notNull(methodName, "Name of method to find cannot be null");
		Validate.notEmpty(methodName, "Name of method to find cannot be empty");
		Validate.notBlank(methodName, "Name of method to find cannot be blank.");

		try
		{
			var method = findMethod(classToAccess, methodName, parameterTypes);

			if(parameters != null && parameters.length > 0)
			{
				Validate.notNull(parameterTypes, "Parameters passed with no ParameterTypes");
				Validate.isTrue(parameterTypes.length == parameters.length, "Parameters and ParameterTypes length must match");
				return (E) method.invoke(instance, parameters);
			}
			else
				return (E) method.invoke(instance);
		}
		catch(UnableToFindMethodException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to locate method {} on type {}", methodName, classToAccess.getName(), e);
			throw e;
		}
		catch(InvocationTargetException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to invoke method {} on type {}", methodName, classToAccess.getName(), e);
			throw new RuntimeException(e);
		}
		catch(IllegalAccessException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to locate method {} on type {}", methodName, classToAccess.getName(), e);
			throw new UnableToFindMethodException(e);
		}
	}

	public static String getObfuscatedMethodName(String methodName)
	{
		Validate.notNull(methodName, "Name of method cannot be null");
		Validate.notEmpty(methodName, "Name of method cannot be empty");
		Validate.notBlank(methodName, "Name of method cannot be blank.");

		return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.METHOD, methodName);
	}

	public static class UnableToFindMethodException extends RuntimeException
	{
		public UnableToFindMethodException(Throwable failed)
		{
			super(failed);
		}
	}
}