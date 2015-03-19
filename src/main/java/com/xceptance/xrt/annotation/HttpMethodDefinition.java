package com.xceptance.xrt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gargoylesoftware.htmlunit.HttpMethod;

/**
 * Annotation that defines the default HTTP method to call a REST resource.
 * 
 * @author Patrick Thaele
 *
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface HttpMethodDefinition
{
    /**
     * Defines the default HTTP method of the REST resource.
     * 
     * @return The HTTP method.
     */
    HttpMethod value();
}
