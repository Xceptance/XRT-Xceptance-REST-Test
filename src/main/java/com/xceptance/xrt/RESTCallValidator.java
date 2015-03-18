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
     * Private default constructor. Objects of this class should be created by
     * XRT.
     */
    @SuppressWarnings( "unused" )
    private RESTCallValidator()
    {
        this.call = null;
    }

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
     * @see RESTCall#getActionName()
     */
    public String getActionName()
    {
        return call.getActionName();
    }
    
    /**
     * @see RESTCall#getProtocol()
     */
    public String getProtocol()
    {
        return call.getProtocol();
    }
    
    /**
     * @see RESTCall#getPort()
     */
    public int getPort()
    {
        return call.getPort();
    }
    
    /**
     * @see RESTCall#getHostName()
     */
    public String getHostName()
    {
        return call.getHostName();
    }
    
    /**
     * @see RESTCall#getBasePath()
     */
    public String getBasePath()
    {
        return call.getBasePath();
    }
    
    /**
     * @see RESTCall#getResourcePath()
     */
    public String getResourcePath()
    {
        return call.getResourcePath();
    }
    
    /**
     * @see RESTCall#getQueryParam(String)
     */
    public String getQueryParam( final String name )
    {
        return call.getQueryParam( name );
    }
    
    /**
     * @see RESTCall#getQueryParams
     */
    public Map<String, String> getQueryParams()
    {
        return call.getQueryParams();
    }
    
    /**
     * @see RESTCall#getFragment()
     */
    public String getFragment()
    {
        return call.getFragment();
    }
    
    /**
     * @see RESTCall#getUrl()
     */
    public String getUrl()
    {
        return call.getUrl();
    }
    
    /**
     * @see RESTCall#getHttpMethod()
     */
    public HttpMethod getHttpMethod()
    {
        return call.getHttpMethod();
    }
    
    /**
     * @see RESTCall#getHttpHeader(String)
     */
    public String getHttpHeader( final String name )
    {
        return call.getHttpHeader( name );
    }
    
    /**
     * @see RESTCall#getHttpHeaders()
     */
    public Map<String, String> getHttpHeaders()
    {
        return call.getHttpHeaders();
    }
    
    /**
     * @see RESTCall#getPlaceholderValue(String)
     */
    public String getPlaceholderValue( final String name )
    {
        return call.getPlaceholderValue( name );
    }
    
    /**
     * @see RESTCall#getPlaceholderValues
     */
    public Map<String, String> getPlaceholderValues()
    {
        return call.getPlaceholderValues();
    }
    
    /**
     * @see RESTCall#hasRequestBody()
     */
    public boolean hasRequestBody()
    {
        return call.hasRequestBody();
    }
    
    /**
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
     * @see RESTCall#getResponseBodyAsString()
     */
    public String getResponseBodyAsString()
    {
        return call.getResponseBodyAsString();
    }
    
    /**
     * @see RESTCall#getResponseBodyAsJSON()
     */
    public JSON getResponseBodyAsJSON()
    {
        return call.getResponseBodyAsJSON();
    }
    
    /**
     * @see RESTCall#getResponseStatusCode()
     */
    public int getResponseStatusCode()
    {
        return call.getResponseStatusCode();
    }
    
    /**
     * @see RESTCall#getResponseStatusMessage()
     */
    public String getResponseStatusMessage()
    {
        return call.getResponseStatusMessage();
    }
    
    /**
     * @see RESTCall#getResponseHttpHeaders
     */
    public List<NameValuePair> getResponseHttpHeaders()
    {
        return call.getResponseHttpHeaders();
    }
    
    /**
     * @see RESTCall#getResponseHttpHeader(String)
     */
    public String getResponseHttpHeader( final String name )
    {
        return call.getResponseHttpHeader( name );
    }
    
    /**
     * @see RESTCall#getResponseContentType()
     */
    public String getResponseContentType()
    {
        return call.getResponseContentType();
    }
    
    /**
     * @see RESTCall#getResponseContentType()
     */
    public String getResponseETag()
    {
        return call.getResponseETag();
    }
}
