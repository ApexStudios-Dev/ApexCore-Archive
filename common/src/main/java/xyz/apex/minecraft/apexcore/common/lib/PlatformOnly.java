package xyz.apex.minecraft.apexcore.common.lib;

import java.lang.annotation.*;

/**
 * Elements annotated with this annotation are marked as being accessible or usable on a particular platform only.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
public @interface PlatformOnly
{
    /**
     * @return 1 or more platform id's stating which platform the annotated element is accessible or usable on.
     */
    String[] value();
}
