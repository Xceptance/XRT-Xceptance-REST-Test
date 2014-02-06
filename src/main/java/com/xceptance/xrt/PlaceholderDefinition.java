package com.xceptance.xrt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that defines the default list of placeholders and their values.
 * 
 * @author Patrick Thaele
 *
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface PlaceholderDefinition
{
    /**
     * The default placeholders and their values of the REST resource.
     * 
     * @return An array of placeholders.
     */
    Placeholder[] value() default {};
}