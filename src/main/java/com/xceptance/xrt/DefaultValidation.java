/* Design Decisions:
 * 
 * 1) No Interface "AutoValidatable": Interfaces are less flexible in terms of 
 *    expandability. Everytime a new method is added to the interface a user of the 
 *    framework would be forced to update all REST configs that use this interface.
 * 2) Methods are not abstract: Same reasoning as 1).
 * 3) No static methods called via reflection: The user of the framework would not have 
 *    any guidance, which methods are available besides the Java Doc. Besides that reflection
 *    is a concept that should be avoided if possible because it works against the OOP paradigm. 
 * 4) Abstract class with public methods: 
 *       a) The user of the framework is not forced to implement all methods.
 *       b) New methods can be added without affecting existing code.
 *       c) The author of this framework is free to implement default behavior that 
 *          can be overridden. 
 */

package com.xceptance.xrt;

import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xrt.document.JSON;

/**
 * Abstract class that defines methods for default validation. Override them to
 * add default validation to your REST resource settings.
 * 
 * @author Patrick Thaele
 * 
 */
public abstract class DefaultValidation
{
    /**
     * Allows to validate the status code of the REST call response.
     * 
     * @param statusCode
     *            The status code returned by the REST call, e.g. 200 or 404.
     */
    public void validateStatusCode( int statusCode )
    {
    }

    /**
     * Allows to validate the response HTTP headers of the REST call.
     * 
     * @param headerList
     *            The list of the response HTTP headers, e.g. "Content-Type".
     */
    public void validateResponseHeaders( final List<NameValuePair> headerList )
    {
    }

    /**
     * Allows to validate the response body of the REST call.
     * 
     * @param body
     *            The body of the response as String.
     */
    public void validateResponseBody( final String body )
    {
    }

    /**
     * Allows to validate the response body of the REST call.
     * 
     * @param body
     *            The body of the response as JSON.
     */
    public void validateResponseBody( final JSON body )
    {
    }
}
