package com.xceptance.xrt;

import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xrt.document.JSON;

/**
 * This class is used to encapsulate the getter methods for default validation.
 * The {@link RESTCall} class feeds all classes with response data that
 * implement the interface {@link AutoValidatable}.
 * 
 * @author Patrick Thaele
 * 
 */
public class RESTCallValidator
{
    /**
     * RESTCall object that is encapsulated by this adapter class.
     */
    private final RESTCall call;

    /****************************************************************************************
     ************************ Constructors **************************************************
     ****************************************************************************************/

    /**
     * Package private constructor for XRT only.
     * 
     * @param call
     */
    RESTCallValidator( RESTCall call )
    {
        this.call = call;
    }

    /****************************************************************************************
     ************************ Public Methods - Request Information **************************
     ****************************************************************************************/

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The name of the action.
     * 
     * @see RESTCall#getActionName()
     */
    public String getActionName()
    {
        return call.getActionName();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The protocol of the request.
     * 
     * @see RESTCall#getProtocol()
     */
    public String getProtocol()
    {
        return call.getProtocol();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The port of the request.
     * 
     * @see RESTCall#getPort()
     */
    public int getPort()
    {
        return call.getPort();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The host name of the request.
     * 
     * @see RESTCall#getHostName()
     */
    public String getHostName()
    {
        return call.getHostName();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The base path of the request.
     * 
     * @see RESTCall#getBasePath()
     */
    public String getBasePath()
    {
        return call.getBasePath();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The resource path of the request.
     * 
     * @see RESTCall#getResourcePath()
     */
    public String getResourcePath()
    {
        return call.getResourcePath();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @param name
     *            The name of the query parameter in the request.
     * @return The value of the query parameter.
     * 
     * @see RESTCall#getQueryParam(String)
     */
    public String getQueryParam( final String name )
    {
        return call.getQueryParam( name );
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return A map containing all query parameters of the request.
     * 
     * @see RESTCall#getQueryParams
     */
    public Map<String, String> getQueryParams()
    {
        return call.getQueryParams();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The fragment of the request URL.
     * 
     * @see RESTCall#getFragment()
     */
    public String getFragment()
    {
        return call.getFragment();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The URL of the request as String.
     * 
     * @see RESTCall#getUrl()
     */
    public String getUrl()
    {
        return call.getUrl();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The HTTP method of the request.
     * 
     * @see RESTCall#getHttpMethod()
     */
    public HttpMethod getHttpMethod()
    {
        return call.getHttpMethod();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @param name
     *            The name of a HTTP parameter in the request.
     * @return The value of the requested HTTP parameter.
     * 
     * @see RESTCall#getHttpHeader(String)
     */
    public String getHttpHeader( final String name )
    {
        return call.getHttpHeader( name );
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return A map of all HTTP parameters of the request.
     * 
     * @see RESTCall#getHttpHeaders()
     */
    public Map<String, String> getHttpHeaders()
    {
        return call.getHttpHeaders();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @param name
     *            The name of a placeholder configured in the REST call.
     * @return The value of the requested placeholder.
     * 
     * @see RESTCall#getPlaceholderValue(String)
     */
    public String getPlaceholderValue( final String name )
    {
        return call.getPlaceholderValue( name );
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return A map of all placeholders configured in the REST call.
     * 
     * @see RESTCall#getPlaceholderValues
     */
    public Map<String, String> getPlaceholderValues()
    {
        return call.getPlaceholderValues();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return Checks whether the request had a body.
     * 
     * @see RESTCall#hasRequestBody()
     */
    public boolean hasRequestBody()
    {
        return call.hasRequestBody();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The body of the request.
     * 
     * @see RESTCall#getRequestBody()
     */
    public String getRequestBody()
    {
        return call.getRequestBody();
    }

    /****************************************************************************************
     ************************ Public Methods - Response Information *************************
     ****************************************************************************************/

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The response body as string.
     * 
     * @see RESTCall#getResponseBodyAsString()
     */
    public String getResponseBodyAsString()
    {
        return call.getResponseBodyAsString();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The response body as JSON object.
     * 
     * @see RESTCall#getResponseBodyAsJSON()
     */
    public JSON getResponseBodyAsJSON()
    {
        return call.getResponseBodyAsJSON();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The status code of the response.
     * 
     * @see RESTCall#getResponseStatusCode()
     */
    public int getResponseStatusCode()
    {
        return call.getResponseStatusCode();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The status message of the response.
     * 
     * @see RESTCall#getResponseStatusMessage()
     */
    public String getResponseStatusMessage()
    {
        return call.getResponseStatusMessage();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return A list of HTTP response headers.
     * 
     * @see RESTCall#getResponseHttpHeaders
     */
    public List<NameValuePair> getResponseHttpHeaders()
    {
        return call.getResponseHttpHeaders();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @param name
     *            The name of a specific HTTP header of the response.
     * @return The value of the requested HTTP header.
     * 
     * @see RESTCall#getResponseHttpHeader(String)
     */
    public String getResponseHttpHeader( final String name )
    {
        return call.getResponseHttpHeader( name );
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The content type of the response.
     * 
     * @see RESTCall#getResponseContentType()
     */
    public String getResponseContentType()
    {
        return call.getResponseContentType();
    }

    /**
     * Encapsulated method of {@link RESTCall}.
     * 
     * @return The E-tag of the response.
     * 
     * @see RESTCall#getResponseETag()
     */
    public String getResponseETag()
    {
        return call.getResponseETag();
    }
}
