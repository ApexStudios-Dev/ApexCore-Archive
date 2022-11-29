package xyz.apex.minecraft.apexagnostics.vanilla.util;

import org.jetbrains.annotations.NotNull;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NotNull
@TypeQualifierDefault(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsAreNotNullByDefault
{
}
