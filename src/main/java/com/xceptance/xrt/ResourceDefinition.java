package com.xceptance.xrt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gargoylesoftware.htmlunit.HttpMethod;

import com.xceptance.xrt.QueryParameter;

/**
 * Annotation that defines the default way of calling a REST resource.
 * 
 * @author Patrick Thaele
 * 
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface ResourceDefinition
{
    /**
     * The piece of URL path that refers to the REST resource.
     * 
     * @return The resource path.
     */
    String resourcePath();

    /**
     * Defines the default HTTP method of the REST resource.
     * 
     * @return The HTTP method.
     */
    HttpMethod httpMethod();

    /**
     * Defines the default query parameters of the REST resource.
     * 
     * @return
     */
    QueryParameter[] queryParameters() default {};

    /**
     * The default URL fragment. It is the optional element at the end of an URL
     * that is added by appending a '#' sign followed by the fragment.
     * 
     * @return The URL fragment.
     */
    String fragment();

    /**
     * The default HTTP parameters of the REST resource.
     * 
     * @return An array of HTTP parameters.
     */
    HttpHeader[] httpHeaders() default {};
}
