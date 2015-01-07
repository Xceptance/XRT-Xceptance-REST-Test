package com.xceptance.xrt.validation;

import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xrt.DefaultValidation;
import com.xceptance.xrt.ResourceDefinition;
import com.xceptance.xrt.document.JSON;

/**
 * Definition class that provides code for default validation.
 * 
 * @author Patrick Thaele
 * 
 */
@ResourceDefinition( baseUrl = "www.xrt.com" )
public class DefaultValidation_Correct extends DefaultValidation
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
    public static String expValStatusCode = "The returned status code is: ";

    /**
     * Allows to validate the status code of the REST call response.
     * 
     * @param statusCode
     *            The status code returned by the REST call, e.g. 200 or 404.
     */
    @Override
    public void validateStatusCode( int statusCode )
    {
        valStatusCode = expValStatusCode + statusCode;
    }

    /****************************************************************************************
     ************************ Validate HTTP Headers *****************************************
     ****************************************************************************************/

    /**
     * Static test property that is changed by validation method.
     */
    public static String valHTTPHeaders = "Not yet executed.";

    /**
     * Static property with the expected value for the test property.
     */
    public static String expValHTTPHeaders = "The first HTTP header is: ";

    /**
     * Allows to validate the response HTTP headers of the REST call.
     * 
     * @param headerList
     *            The list of the response HTTP headers, e.g. "Content-Type".
     */
    @Override
    public void validateResponseHeaders( final List<NameValuePair> headerList )
    {
        valHTTPHeaders = expValHTTPHeaders + headerList.get( 0 ).toString();
    }

    /****************************************************************************************
     ************************ Validate Response Body (String) *******************************
     ****************************************************************************************/

    /**
     * Static test property that is changed by validation method.
     */
    public static String valBodyString = "Not yet executed.";

    /**
     * Static property with the expected value for the test property.
     */
    public static String expValBodyString = "The returned response body is: ";

    /**
     * Allows to validate the response body of the REST call.
     * 
     * @param body
     *            The body of the response as String.
     */
    @Override
    public void validateResponseBody( final String body )
    {
        valBodyString = expValBodyString + body;
    }

    /****************************************************************************************
     ************************ Validate Response Body (JSON) *******************************
     ****************************************************************************************/

    /**
     * Static test property that is changed by validation method.
     */
    public static String valBodyJson = "Not yet executed.";

    /**
     * Static property with the expected value for the test property.
     */
    public static String expValBodyJson = "The returned response body is not null.";

    /**
     * Allows to validate the response body of the REST call.
     * 
     * @param body
     *            The body of the response as JSON.
     */
    @Override
    public void validateResponseBody( final JSON body )
    {
        if ( body != null )
            valBodyJson = expValBodyJson;
        else
            valBodyJson = "The JSON document is null.";
    }
}
