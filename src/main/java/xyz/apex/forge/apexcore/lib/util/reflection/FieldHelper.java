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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ExecutionException;

@Deprecated(forRemoval = true)
public class FieldHelper
{
	private static final Marker MARKER = MarkerManager.getMarker("FieldReflection");
	private static final Cache<String, Field> fieldsCache = CacheBuilder.newBuilder().weakValues().build();
	private static final Field MODIFIERS;

	static
	{
		try
		{
			MODIFIERS = Field.class.getDeclaredField("modifiers");
			MODIFIERS.setAccessible(true);
		}
		catch(NoSuchFieldException e)
		{
			throw new UnableToFindFieldException(e);
		}
	}

	@Nullable
	public static <E> E getPrivateValue(Class<?> classToAccess, @Nullable Object instance, String fieldName)
	{
		Validate.notNull(classToAccess, "Class to find field cannot be null");
		Validate.notNull(fieldName, "Name of field to find cannot be null");
		Validate.notEmpty(fieldName, "Name of field to find cannot be empty");
		Validate.notBlank(fieldName, "Name of field to find cannot be blank.");

		try
		{
			var field = findField(classToAccess, fieldName);
			return (E) field.get(instance);
		}
		catch(UnableToFindFieldException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to locate field {} on type {}", fieldName, classToAccess.getName(), e);
			throw e;
		}
		catch(IllegalAccessException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to locate field {} on type {}", fieldName, classToAccess.getName(), e);
			throw new UnableToFindFieldException(e);
		}
	}

	public static Field findField(Class<?> classToAccess, String fieldName)
	{
		Validate.notNull(classToAccess, "Class to find field cannot be null");
		Validate.notNull(fieldName, "Name of field to find cannot be null");
		Validate.notEmpty(fieldName, "Name of field to find cannot be empty");
		Validate.notBlank(fieldName, "Name of field to find cannot be blank.");

		try
		{
			return fieldsCache.get(fieldName, () -> cacheField(classToAccess, fieldName));
		}
		catch(ExecutionException e)
		{
			throw new UnableToFindFieldException(e);
		}
	}

	private static Field cacheField(Class<?> classToAccess, String fieldName)
	{
		Validate.notNull(classToAccess, "Class to find field cannot be null");
		Validate.notNull(fieldName, "Name of field to find cannot be null");
		Validate.notEmpty(fieldName, "Name of field to find cannot be empty");
		Validate.notBlank(fieldName, "Name of field to find cannot be blank.");

		try
		{
			var field = classToAccess.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		}
		catch(NoSuchFieldException e)
		{
			throw new UnableToFindFieldException(e);
		}
	}

	public static <E> E setPrivateValue(Class<?> classToAccess, @Nullable Object instance, @Nullable E newValue, String fieldName)
	{
		Validate.notNull(classToAccess, "Class to find field cannot be null");
		Validate.notNull(fieldName, "Name of field to find cannot be null");
		Validate.notEmpty(fieldName, "Name of field to find cannot be empty");
		Validate.notBlank(fieldName, "Name of field to find cannot be blank.");

		try
		{
			var field = findField(classToAccess, fieldName);
			// removeFinalModifier(field);
			var oldValue = (E) field.get(instance);
			field.set(instance, newValue);
			return oldValue;
		}
		catch(UnableToFindFieldException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to locate field {} on type {}", fieldName, classToAccess.getName(), e);
			throw e;
		}
		catch(IllegalAccessException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to locate field {} on type {}", fieldName, classToAccess.getName(), e);
			throw new UnableToFindFieldException(e);
		}
	}

	public static String getObfuscatedFieldName(String fieldName)
	{
		Validate.notNull(fieldName, "Name of field cannot be null");
		Validate.notEmpty(fieldName, "Name of field cannot be empty");
		Validate.notBlank(fieldName, "Name of field cannot be blank.");

		return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, fieldName);
	}

	public static void removeFinalModifier(Class<?> classToAccess, String fieldName)
	{
		Validate.notNull(classToAccess, "Class to find field cannot be null");
		Validate.notNull(fieldName, "Name of field to to remove final modifier from cannot be null");
		Validate.notEmpty(fieldName, "Name of field to to remove final modifier from cannot be empty");
		Validate.notBlank(fieldName, "Name of field to to remove final modifier from cannot be blank.");

		try
		{
			var field = findField(classToAccess, fieldName);
			removeFinalModifier(field);
		}
		catch(UnableToFindFieldException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to locate field {} on type {}", fieldName, classToAccess.getName(), e);
			throw e;
		}
	}

	public static void removeFinalModifier(Field field)
	{
		Validate.notNull(field, "Field to remove final modifier cannot be null");

		try
		{
			var modifiers = field.getModifiers();

			if(Modifier.isFinal(modifiers))
				MODIFIERS.setInt(field, modifiers & ~Modifier.FINAL);
		}
		catch(IllegalAccessException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to remove final modifier from Field '{}'", field.getName(), e);
			throw new UnableToFindFieldException(e);
		}
	}

	public static class UnableToFindFieldException extends RuntimeException
	{
		public UnableToFindFieldException(Exception e)
		{
			super(e);
		}
	}
}