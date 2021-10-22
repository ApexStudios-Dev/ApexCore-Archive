package xyz.apex.forge.apexcore.lib.util.reflection;

import com.tterrag.registrate.AbstractRegistrate;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public enum Methods
{
	// 1.16.5: private static final void validateSafeReferent(Supplier<? extends SafeReferent> safeReferentSupplier)
	// DIST_EXEC_VALIDATE("validateSafeReferent", false, DistExecutor.class, Supplier.class),

	// 1.16.5: protected String currentName()
	REGISTRATE_CURRENT_NAME("currentName", false, AbstractRegistrate.class),
	;

	private final String methodName;
	private final boolean isObfuscated;
	private final Class<?> classToAccess;
	@Nullable private final Class<?>[] parameterTypes;

	Methods(String methodName, boolean isObfuscated, Class<?> classToAccess, @Nullable Class<?>... parameterTypes)
	{
		this.methodName = methodName;
		this.isObfuscated = isObfuscated;
		this.classToAccess = classToAccess;
		this.parameterTypes = parameterTypes;
	}

	Methods(String methodName, boolean isObfuscated, Class<?> classToAccess)
	{
		this.methodName = methodName;
		this.isObfuscated = isObfuscated;
		this.classToAccess = classToAccess;
		parameterTypes = null;
	}

	public Method findMethod()
	{
		return MethodHelper.findMethod(this);
	}

	@Nullable
	public <E> E invokeMethod(@Nullable Object instance, @Nullable Object... parameters)
	{
		return MethodHelper.invokeMethod(this, instance, parameters);
	}

	public Class<?> getClassToAccess()
	{
		return classToAccess;
	}

	@Nullable
	public Class<?>[] getParameterTypes()
	{
		return parameterTypes;
	}

	public String getMethodName()
	{
		return MethodHelper.getObfuscatedMethodName(this);
	}

	public String getMethodNameRaw()
	{
		return methodName;
	}

	public boolean isObfuscated()
	{
		return isObfuscated;
	}
}
