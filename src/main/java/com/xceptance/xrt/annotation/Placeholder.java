package com.xceptance.xrt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A placeholder used to define a default setting for a REST resource.
 * 
 * @author Patrick Thaele
 *
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface Placeholder
{
    /**
     * Defines the name of the placeholder.
     * 
     * @return The name of the placeholder.
     */
    String name();
    
    /**
     * Defines the value of the placeholder.
     * 
     * @return The value of the placeholder.
     */
    String value();
}
