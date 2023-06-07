package xyz.apex.minecraft.apexcore.common.lib;

import java.lang.annotation.*;

/**
 * Elements annotated with this annotation are marked as only being accessible or usable on a particular physical side.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE})
public @interface SideOnly
{
    /**
     * @return 1 or more physical sides stating which side the annotated element is accessible or usable on.
     */
    PhysicalSide[] value();
}
