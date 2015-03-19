package com.xceptance.xrt.validation;

import com.xceptance.xrt.AutoValidatable;
import com.xceptance.xrt.RESTCallValidator;
import com.xceptance.xrt.annotation.ResourceDefinition;

/**
 * Definition class that provides code for default validation.
 * 
 * @author Patrick Thaele
 * 
 */
@ResourceDefinition( baseUrl = "www.xrt.com" )
public class DefaultValidation_Correct implements AutoValidatable
{
    /****************************************************************************************
     ************************ Validate Status Code ******************************************
     ****************************************************************************************/

    /**
     * Static test property that is changed by validation method.
     */
    public static boolean valPerformed = false;

    /**
     * Allows to validate the status code of the REST call response.
     * 
     * @param statusCode
     *            The status code returned by the REST call, e.g. 200 or 404.
     */
    @Override
    public void validate( RESTCallValidator call )
    {
        valPerformed = true;
    }
}
