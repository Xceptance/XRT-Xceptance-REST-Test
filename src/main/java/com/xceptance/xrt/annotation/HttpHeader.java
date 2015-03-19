package com.xceptance.xrt.annotation;

/**
 * A HTTP header used to define a default setting for a REST resource.
 * 
 * @author Patrick Thaele
 *
 */
public @interface HttpHeader
{
    /**
     * Defines the name of the HTTP header.
     * 
     * @return The name of the HTTP header.
     */
    String name();
    
    /**
     * Defines the value of the HTTP header.
     * 
     * @return The value of the HTTP header.
     */
    String value();
}
