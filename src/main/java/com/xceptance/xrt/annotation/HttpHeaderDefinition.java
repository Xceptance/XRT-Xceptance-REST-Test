package com.xceptance.xrt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that defines the default HTTP headers to call a REST resource.
 * 
 * @author Patrick Thaele
 *
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface HttpHeaderDefinition
{
    /**
     * The default HTTP parameters of the REST resource.
     * 
     * @return An array of HTTP parameters.
     */
    HttpHeader[] value() default {};
}
