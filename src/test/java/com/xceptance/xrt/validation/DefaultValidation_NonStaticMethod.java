package com.xceptance.xrt.validation;

import com.xceptance.xrt.ResourceDefinition;

/**
 * Definition class that provides code for default validation with a non-static method.
 * 
 * @author Patrick Thaele
 *
 */
@ResourceDefinition( baseUrl="www.xrt.com" )
public class DefaultValidation_NonStaticMethod
{
    /****************************************************************************************
     ************************ Validate Status Code ******************************************
     ****************************************************************************************/
    
    /**
     * Static test property that is changed by validation method.
     */
    public static String valStatusCode = "Not yet executed.";
    
    /**
     * Static property with the expected value for the test property. 
     */
    public static String expValStatusCode = "This status code should not be visible for non-static methods: ";
    
    /**
     * Allows to validate the status code of the REST call response.
     *
     * @param statusCode The status code returned by the REST call, e.g. 200 or 404.
     */
    public void validateStatusCode( int statusCode )
    {
        valStatusCode = expValStatusCode + statusCode;
    }
}
