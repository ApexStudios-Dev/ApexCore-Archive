package xyz.apex.forge.apexcore.lib.util.reflection;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import xyz.apex.forge.apexcore.core.ApexCore;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ExecutionException;

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

	// region: Getters
	@Nullable
	public static <E> E getPrivateValue(@Nullable Object instance, Fields field)
	{
		Validate.notNull(field, "Field to find cannot be null");
		return getPrivateValue(field.getClassToAccess(), instance, field.getFieldName());
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
			Field field = findField(classToAccess, fieldName);
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
	// endregion

	// region: Finders
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

	public static Field findField(Fields field)
	{
		Validate.notNull(field, "Field to find cannot be null");
		return findField(field.getClassToAccess(), field.getFieldName());
	}
	// endregion

	// region: Caching
	private static Field cacheField(Class<?> classToAccess, String fieldName)
	{
		Validate.notNull(classToAccess, "Class to find field cannot be null");
		Validate.notNull(fieldName, "Name of field to find cannot be null");
		Validate.notEmpty(fieldName, "Name of field to find cannot be empty");
		Validate.notBlank(fieldName, "Name of field to find cannot be blank.");

		try
		{
			Field field = classToAccess.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		}
		catch(NoSuchFieldException e)
		{
			throw new UnableToFindFieldException(e);
		}
	}
	// endregion

	// region: Setters
	public static <E> E setPrivateValue(@Nullable Object instance, @Nullable E newValue, Fields field)
	{
		Validate.notNull(field, "Field to find cannot be null");
		return setPrivateValue(field.getClassToAccess(), instance, newValue, field.getFieldName());
	}

	public static <E> E setPrivateValue(Class<?> classToAccess, @Nullable Object instance, @Nullable E newValue, String fieldName)
	{
		Validate.notNull(classToAccess, "Class to find field cannot be null");
		Validate.notNull(fieldName, "Name of field to find cannot be null");
		Validate.notEmpty(fieldName, "Name of field to find cannot be empty");
		Validate.notBlank(fieldName, "Name of field to find cannot be blank.");

		try
		{
			Field field = findField(classToAccess, fieldName);
			// removeFinalModifier(field);
			E oldValue = (E) field.get(instance);
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
	// endregion

	// region: Names
	public static String getObfuscatedFieldName(Fields field)
	{
		Validate.notNull(field, "Field cannot be null");
		String fieldName = field.getFieldNameRaw();

		if(field.isObfuscated())
			return getObfuscatedFieldName(fieldName);
		return fieldName;
	}

	public static String getObfuscatedFieldName(String fieldName)
	{
		Validate.notNull(fieldName, "Name of field cannot be null");
		Validate.notEmpty(fieldName, "Name of field cannot be empty");
		Validate.notBlank(fieldName, "Name of field cannot be blank.");

		return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, fieldName);
	}
	// endregion

	// region: Final
	public static void removeFinalModifier(Class<?> classToAccess, String fieldName)
	{
		Validate.notNull(classToAccess, "Class to find field cannot be null");
		Validate.notNull(fieldName, "Name of field to to remove final modifier from cannot be null");
		Validate.notEmpty(fieldName, "Name of field to to remove final modifier from cannot be empty");
		Validate.notBlank(fieldName, "Name of field to to remove final modifier from cannot be blank.");

		try
		{
			Field field = findField(classToAccess, fieldName);
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
			int modifiers = field.getModifiers();

			if(Modifier.isFinal(modifiers))
				MODIFIERS.setInt(field, modifiers & ~Modifier.FINAL);
		}
		catch(IllegalAccessException e)
		{
			ApexCore.LOGGER.error(MARKER, "Unable to remove final modifier from Field '{}'", field.getName(), e);
			throw new UnableToFindFieldException(e);
		}
	}
	// endregion

	// region: Exceptions
	public static class UnableToFindFieldException extends RuntimeException
	{
		public UnableToFindFieldException(Exception e)
		{
			super(e);
		}
	}
	// endregion
}
