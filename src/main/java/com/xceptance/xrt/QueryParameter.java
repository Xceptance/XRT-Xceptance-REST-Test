package com.xceptance.xrt;

/**
 * <p>The query parameter inside an URL used to define a default setting for a REST
 * resource.</p><br/>
 * 
 * <pre>
 * {@code
 * http://rest.test.com/xrt/resource?queryParameterName=queryParameterValue
 * }
 * </pre>
 * 
 * @author Patrick Thaele
 * 
 */
public @interface QueryParameter
{
    /**
     * Defines the name of the query parameter.
     * 
     * @return The name of the query parameter.
     */
    String name();
    
    /**
     * Defines the value of the query parameter.
     * 
     * @return The value of the query parameter.
     */
    String value();
}
