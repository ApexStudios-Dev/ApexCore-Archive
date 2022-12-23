package xyz.apex.minecraft.apexcore.shared.platform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Elements marked with this annotation are commonly used on a specific platform
// but should function perfectly fine no matter the platform
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE, ElementType.MODULE, ElementType.PARAMETER, ElementType.TYPE })
public @interface ForPlatform
{
    PlatformType[] value();
}
