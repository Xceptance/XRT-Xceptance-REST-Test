package com.xceptance.xrt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xrt.document.JSON;

/**
 * <p>
 * Performs a REST call based on the information retrieved from its internal and
 * global settings.
 * </p>
 * <br/>
 * <p>
 * All writing methods return the updated instance to ensure an efficient
 * configuration of the REST call.
 * </p>
 * <br/>
 * 
 * @author Patrick Thaele
 * 
 */
public class RESTCall
{
    // TODO Url encoding

    /****************************************************************************************
     ************************ Private Properties ********************************************
     ****************************************************************************************/

    /**
     * Implements an automatic approach for action handling.
     */
    private static volatile ConcurrentHashMap<ThreadGroup, XltRESTAction> prevActionMap = new ConcurrentHashMap<ThreadGroup, XltRESTAction>();

    // All properties are initialized with an empty String to avoid NULL pointer
    // exceptions.

    /**
     * The action name that appears in the load test report after the REST call
     * was performed.
     */
    private String actionName = "";

    /**
     * The protocol used in the REST call, e.g. <b>http</b> and <b>https</b>.
     */
    private String protocol = "http";

    /**
     * The port used in the REST call, e.g. <b>80</b> and <b>433</b>.
     */
    private int port = -1;

    /**
     * The host name used in the REST call, e.g. <b>xceptance.com</b>.
     */
    private String hostName = "";

    /**
     * The base path used in the REST call. The base path is the part of the Url
     * between host name and resource path, e.g.
     * <b>xceptance.com/base/path/resource</b>. In the example <b>base/path</b>
     * is the base path in the Url.
     */
    private String basePath = "";

    /**
     * Identifies the resource of the REST call.
     */
    private String resourcePath = "";

    /**
     * A map of query parameters that are used in the REST call, e.g.
     * <b>xceptance.com/base/path/resource?paramName=paramValue</b>.
     */
    private Map<String, String> queryParams = new HashMap<String, String>();

    /**
     * The fragment used in the REST call, e.g.
     * <b>xceptance.com/base/path/resource?paramName=paramValue#fragment</b>.
     */
    private String fragment = "";

    /**
     * The HTTP method used in the REST call, e.g. GET and POST.
     */
    private HttpMethod httpMethod;

    /**
     * A map of http headers.
     */
    private Map<String, String> httpHeaders = new HashMap<String, String>();

    /**
     * The request body of the REST call. Certain HTTP methods, e.g. POST, can
     * send a body.
     */
    private String requestBody;

    /**
     * The response of the REST call.
     */
    private WebResponse response;

    /**
     * The map of placeholders that are replaced by their values in the final
     * REST call.
     */
    private Map<String, String> placeholders = new HashMap<String, String>();

    /****************************************************************************************
     ************************ Constructors **************************************************
     ****************************************************************************************/

    /**
     * Default constructor. Uses global settings and can be adjusted by public
     * methods.
     */
    public RESTCall()
    {
        readGlobalSettings();
    }

    /**
     * <p>
     * Constructor that takes a resource definition as an argument. The resource
     * definition overrides global settings but can be adjusted by public
     * setters.
     * </p>
     * <br/>
     * <p>
     * A resource definition can be any class that uses any of the following
     * definition annotations:
     * </p>
     * <ul>
     * <li>{@link ResourceDefinition}</li>
     * <li>{@link HttpMethodDefinition}</li>
     * <li>{@link HttpHeaderDefinition}</li>
     * </ul>
     * <p>
     * Providing a class with non of the mentioned annotations has the same
     * effect as calling the default constructor {@link #RESTCall() }.
     * </p>
     * <br/>
     * 
     * @param resourceDef
     *            The class that provides default values for a REST resource.
     */
    public RESTCall( final Class<?> resourceDef )
    {
        readGlobalSettings();
        setDefinitionClass( resourceDef );
    }

    /**
     * Takes an Url as a String argument. The <b>RESTCall</b> class tries to map
     * the Url into the internal representation. It assumes that the last path
     * element is the identifier for the resource.
     * 
     * @param url
     *            The Url used for the REST call.
     */
    public RESTCall( final String url )
    {
        readGlobalSettings();

        splitUrl( url );
    }

    /****************************************************************************************
     ************************ Public Methods ************************************************
     ****************************************************************************************/

    /**
     * Sets the action name for the REST call. This name appears in the XLT load
     * test report once the REST call was performed during a load test.
     * 
     * @param actionName
     *            The name of the REST call action.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setActionName( final String actionName )
    {
        if ( actionName != null )
            this.actionName = actionName;

        return this;
    }

    /**
     * Returns the action name of the REST call. If no action name was provided
     * the resource path is returned.
     * 
     * @return The action name of the REST call.
     */
    public String getActionName()
    {
        // If no action name was provided return the resource path.
        if ( this.actionName.isEmpty() )
            return this.resourcePath;

        return this.actionName;
    }

    /**
     * Sets the previously performed REST action. XLT handles everything around
     * sessions and cookies automatically when the previous action is known.
     * Removing the previous session by providing <b>null</b> as the value
     * forces XLT to open a new web client with a new session.
     * 
     * @param previousAction
     *            The previously performed REST action. <b>null</b> removes the
     *            old previous action and starts a new session.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setPreviousAction( XltRESTAction previousAction )
    {
        if ( previousAction == null )
            prevActionMap.remove( Thread.currentThread().getThreadGroup() );
        else
            prevActionMap.put( Thread.currentThread().getThreadGroup(), previousAction );
        
        return this;
    }

    /**
     * Returns the previously performed REST action. This action can be set via
     * {@link #setPreviousAction(XltRESTAction)} or is set after this REST call
     * was performed. Returns <b>null</b> if none of the two conditions applies.
     * 
     * @return The previously performed REST action.
     */
    public XltRESTAction getPreviousAction()
    {
        return prevActionMap.get( Thread.currentThread().getThreadGroup() );
    }

    /**
     * Sets the protocol of this REST call, e.g. <b>http</b>. It's not necessary
     * to set the protocol separator <b>://</b> because it is added
     * automatically when creating the Url.
     * 
     * @param protocol
     *            The new protocol of this REST call.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setProtocol( final String protocol )
    {
        // Do nothing if the protocol is null.
        if ( protocol != null )
        {
            this.protocol = protocol;

            // Remove the protocol separator
            this.protocol = this.protocol.replaceAll( "/", "" );
            this.protocol = this.protocol.replaceAll( ":", "" );
        }

        return this;
    }

    /**
     * Returns the protocol of the REST call configuration, e.g. <b>http</b>.
     * 
     * @return The protocol of the REST call configuration.
     */
    public final String getProtocol()
    {
        return this.protocol;
    }

    /**
     * Sets the port of this REST call, e.g. <b>80</b>.
     * 
     * @param port
     *            The new port of this REST call.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setPort( final int port )
    {
        // Do nothing if the port is negative or 0.
        if ( port > 0 )
            this.port = port;

        return this;
    }

    /**
     * Returns the port of the REST call configuration, e.g. <b>80</b>.
     * 
     * @return The port of the REST call configuration.
     */
    public final int getPort()
    {
        return this.port;
    }

    /**
     * Sets the host name of this REST call, e.g. <b>xceptance.com</b>.
     * 
     * @param hostName
     *            The new host name of this REST call.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setHostName( final String hostName )
    {
        // Do nothing if the host name is null.
        if ( hostName != null )
            this.hostName = hostName;

        return this;
    }

    /**
     * Returns the host name of the REST call configuration, e.g.
     * <b>xceptance.com</b>.
     * 
     * @return The host name of the REST call configuration.
     */
    public final String getHostName()
    {
        return this.hostName;
    }

    /**
     * Sets the base path of this REST call. The base path is the part of the
     * Url between host name and resource path, e.g.
     * <b>xceptance.com/base/path/resource</b>. In the example <b>base/path</b>
     * is the base path in the Url.
     * 
     * @param basePath
     *            The new base path of this REST call.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setBasePath( final String basePath )
    {
        // Do nothing if the base path is null.
        if ( basePath != null )
            this.basePath = sanitizeUrlPathSegment( basePath );

        return this;
    }

    /**
     * Returns the base path of the REST call configuration, e.g.
     * <b>base/path</b>.
     * 
     * @return The base path of the REST call configuration.
     */
    public final String getBasePath()
    {
        return this.basePath;
    }

    /**
     * Sets the resource path of this REST call. The resource path is the part
     * of the Url after the base path, e.g.
     * <b>xceptance.com/base/path/resource/subresource</b>. In the example
     * <b>resource/subresource</b> is the resource path in the Url.
     * 
     * @param resourcePath
     *            The new resource path of this REST call.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setResourcePath( final String resourcePath )
    {
        // Do nothing if the resource path is null.
        if ( resourcePath != null )
            this.resourcePath = sanitizeUrlPathSegment( resourcePath );

        return this;
    }

    /**
     * Returns the resource path of the REST call configuration, e.g.
     * <b>resource/subresource</b>.
     * 
     * @return The resource path of the REST call configuration.
     */
    public final String getResourcePath()
    {
        return resourcePath;
    }

    /**
     * Adds/updates a query parameter to the REST call configuration. Since
     * query parameters must be unique the new parameter replaces an already
     * existing one with the same name.
     * 
     * @param name
     *            The name of the query parameter, e.g. <b>color</b>.
     * @param value
     *            The value of the query parameter, e.g. <b>red</b>.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall addQueryParam( String name, String value )
    {
        this.queryParams.put( name, value );
        return this;
    }

    /**
     * Adds/updates several query parameters to the REST call configuration.
     * Since query parameters must be unique the new parameters replace already
     * existing ones with the same name.
     * 
     * @param queryParams
     *            A map of query parameters. The key element in the map should
     *            be the name of the parameter, the value element should be its
     *            value.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall addAllQueryParams( Map<String, String> queryParams )
    {
        this.queryParams.putAll( queryParams );
        return this;
    }

    /**
     * Returns the value of the query parameter with the given name.
     * 
     * @param name
     *            The name of the query parameter, e.g. <b>color</b>.
     * 
     * @return The value of the query parameter with the given name. If no value
     *         was found <b>null</b> is returned.
     */
    public final String getQueryParam( String name )
    {
        return this.queryParams.get( name );
    }

    /**
     * Returns a map of the configured query parameters for this REST call, e.g.
     * <b>...?color=red</b>.
     * 
     * @return A map of the configured query parameters for this REST call.
     */
    public final Map<String, String> getQueryParams()
    {
        return this.queryParams;
    }

    /**
     * Removes a query parameter from the REST call configuration by its name.
     * 
     * @param name
     *            The name of the query parameter, e.g. <b>color</b>.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removeQueryParam( String name )
    {
        this.queryParams.remove( name );
        return this;
    }

    /**
     * Removes several query parameters from the REST call configuration by
     * their names.
     * 
     * @param names
     *            An array of names of the query parameters, e.g. <b>color</b>.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removeQueryParams( String... names )
    {
        // Loop through all names of the array and remove the corresponding
        // parameters from the map.
        for ( String name : names )
            this.queryParams.remove( name );

        return this;
    }

    /**
     * Removes all query parameters from the REST call configuration.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removeAllQueryParams()
    {
        this.queryParams = new HashMap<String, String>();
        return this;
    }

    /**
     * Sets the fragment of this REST call. It is added to the end of the Url
     * separated from the rest by the '#' sign.
     * 
     * @param fragment
     *            The new fragment of this REST call.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setFragment( final String fragment )
    {
        // Do nothing if the fragmant is null.
        if ( fragment != null )
            this.fragment = fragment;

        return this;
    }

    /**
     * Returns the fragment of the REST call configuration.
     * 
     * @return The fragment of the REST call configuration.
     */
    public final String getFragment()
    {
        return this.fragment;
    }

    /**
     * <p>
     * Method that takes a resource definition as an argument. The resource
     * definition overrides global settings but can be adjusted by public
     * setters.
     * </p>
     * <br/>
     * <p>
     * A resource definition can be any class that uses any of the following
     * definition annotations:
     * </p>
     * <ul>
     * <li>{@link ResourceDefinition}</li>
     * <li>{@link HttpMethodDefinition}</li>
     * <li>{@link HttpHeaderDefinition}</li>
     * </ul>
     * <br/>
     * 
     * @param resourceDef
     *            The class that provides default values for a REST resource.
     */
    public RESTCall setDefinitionClass( final Class<?> resourceDef )
    {
        // Do nothing if the class is null
        if ( resourceDef != null )
        {
            readResourceDefinition( resourceDef );
            readHttpMethodDefinition( resourceDef );
            readHttpHeaderDefinition( resourceDef );
            readPlaceholderDefinition( resourceDef );
        }

        return this;
    }

    /**
     * Takes an Url as a String argument. The <b>RESTCall</b> class tries to map
     * the Url into the internal representation. It assumes that the last path
     * element is the identifier for the resource.
     * 
     * @param url
     *            The Url used for the REST call.
     */
    public RESTCall setUrl( final String url )
    {
        // Do nothing if the Url is null.
        if ( url != null )
            splitUrl( url );

        return this;
    }

    /**
     * Creates the REST Url. All elements are Url encoded. The host name is
     * mandatory. The default protocol is <b>http</b> and its default port is
     * <b>80</b>. If the protocol is <b>https</b> its default port is
     * <b>433</b>. All placeholders were replaced by their corresponding values.
     * 
     * @return The Url ready to call. Returns <b>null</b> if the host name is
     *         missing.
     */
    public final String getUrl()
    {
        // String builder that builds the Url.
        StringBuilder builder = new StringBuilder();

        if ( !this.protocol.isEmpty() )
            builder.append( this.protocol ).append( "://" );

        // A Url without host name does not make any sense.
        if ( this.hostName.isEmpty() )
            return null;

        builder.append( hostName );

        if ( this.port > 0 && !( this.port == 80 && this.protocol.equals( "http" ) )
                && !( this.port == 443 && this.protocol.equals( "https" ) ) )
            builder.append( ":" ).append( this.port );

        if ( !this.basePath.isEmpty() )
            builder.append( "/" ).append( this.basePath );

        if ( !this.resourcePath.isEmpty() )
            builder.append( "/" ).append( this.resourcePath );

        if ( !this.queryParams.isEmpty() )
        {
            builder.append( "?" );
            for ( Entry<String, String> queryParam : this.queryParams.entrySet() )
            {
                builder.append( queryParam.getKey() ).append( "=" ).append( queryParam.getValue() ).append( "&" );
            }
            // Remove the last '&' character.
            builder.deleteCharAt( builder.length() - 1 );
        }

        if ( !this.fragment.isEmpty() )
            builder.append( "#" ).append( this.fragment );

        return replacePlaceholders( builder.toString() );
    }

    /**
     * Sets the HTTP method for this REST call.
     * 
     * @param httpMethod
     *            The HTTP method used in the REST call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @see com.gargoylesoftware.htmlunit.HttpMethod
     */
    public RESTCall setHttpMethod( HttpMethod httpMethod )
    {
        this.httpMethod = httpMethod;
        return this;
    }

    /**
     * Returns the configured HTTP method for this REST call, e.g. POST or GET.
     * 
     * @return The configured HTTP method for this REST call.
     * 
     * @see com.gargoylesoftware.htmlunit.HttpMethod
     */
    public final HttpMethod getHttpMethod()
    {
        return this.httpMethod;
    }

    /**
     * Adds/updates a HTTP header to the REST call configuration. Since HTTP
     * headers must be unique the new header replaces an already existing one
     * with the same name.
     * 
     * @param name
     *            The name of the HTTP header, e.g. <b>Content-type</b>.
     * @param value
     *            The value of the HTTP header, e.g. <b>application/json</b>.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall addHttpHeader( String name, String value )
    {
        this.httpHeaders.put( name, value );
        return this;
    }

    /**
     * Adds/updates several HTTP headers to the REST call configuration. Since
     * HTTP headers must be unique the new headers replace already existing ones
     * with the same name.
     * 
     * @param httpHeaders
     *            A map of HTTP headers. The key element in the map should be
     *            the name of the header, the value element should be its value.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall addAllHttpHeaders( Map<String, String> httpHeaders )
    {
        this.httpHeaders.putAll( httpHeaders );
        return this;
    }

    /**
     * Returns the value of the HTTP header with the given name.
     * 
     * @param name
     *            The name of the HTTP header, e.g. <b>Content-type</b>.
     * 
     * @return The value of the HTTP header with the given name. If no value was
     *         found <b>null</b> is returned.
     */
    public final String getHttpHeader( String name )
    {
        return this.httpHeaders.get( name );
    }

    /**
     * Returns a map of the configured HTTP headers for this REST call, e.g.
     * Content-type:application/json.
     * 
     * @return A map of the configured HTTP headers for this REST call.
     */
    public final Map<String, String> getHttpHeaders()
    {
        return this.httpHeaders;
    }

    /**
     * Removes a HTTP header from the REST call configuration by its name.
     * 
     * @param name
     *            The name of the HTTP header, e.g. <b>Content-type</b>.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removeHttpHeader( String name )
    {
        this.httpHeaders.remove( name );
        return this;
    }

    /**
     * Removes several HTTP headers from the REST call configuration by their
     * names.
     * 
     * @param names
     *            An array of names of the HTTP headers, e.g.
     *            <b>Content-type</b>.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removeHttpHeaders( String... names )
    {
        // Loop through all names of the array and remove the corresponding
        // headers from the map.
        for ( String name : names )
            this.httpHeaders.remove( name );

        return this;
    }

    /**
     * Removes all HTTP headers from the REST call configuration.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removeAllHttpHeaders()
    {
        this.httpHeaders = new HashMap<String, String>();
        return this;
    }

    /**
     * Convenience method. Adds the <b>If-Match</b> header to the list of HTTP
     * headers. The same action can be done by calling the method
     * {@link #addHttpHeader(String, String)} with the header name
     * <b>If-Match</b>.
     * 
     * @param ifMatchHeader
     *            The value of the <b>If-Match</b> HTTP header.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setIfMatchHeader( final String ifMatchHeader )
    {
        addHttpHeader( "If-Match", ifMatchHeader );
        return this;
    }

    /**
     * Convenience method. Adds the <b>If-None-Match</b> header to the list of
     * HTTP headers. The same action can be done by calling the method
     * {@link #addHttpHeader(String, String)} with the header name
     * <b>If-None-Match</b>.
     * 
     * @param ifNoneMatchHeader
     *            The value of the <b>If-None-Match</b> HTTP header.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setIfNoneMatchHeader( final String ifNoneMatchHeader )
    {
        addHttpHeader( "If-None-Match", ifNoneMatchHeader );
        return this;
    }

    /**
     * <p>
     * XRT supports the concepts of placeholders. Any part of the URL can
     * contain a placeholder that is replaced by a value right before the REST
     * call is performed. Here is an example:
     * </p>
     * <br/>
     * 
     * <p>
     * <i>anyhost.com/base/path/resource/${id}?query=value</i>
     * </p>
     * <br/>
     * 
     * <p>
     * If the REST call instance knows about the placeholder, this can be
     * replaced:
     * </p>
     * <br/>
     * <p>
     * <i>id:foobar => anyhost.com/base/path/resource/foobar?query=value</i>
     * </p>
     * 
     * <br/>
     * <p>
     * This method adds/updates a placeholder value to the REST call
     * configuration. Since placeholders must be unique the new placeholder
     * value replaces an already existing one with the same name.
     * </p>
     * 
     * @param name
     *            The name of the placeholder, e.g. <b>id</b>.
     * @param value
     *            The value of the placeholder, e.g. <b>foobar</b>.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall addPlaceholderValue( String name, String value )
    {
        this.placeholders.put( name, value );
        return this;
    }

    /**
     * Adds/updates several placeholder values to the REST call configuration.
     * Since placeholders must be unique the new placeholder values replace
     * already existing ones with the same name.
     * 
     * @param placeholderValues
     *            A map of placeholder name-value pairs. The key element in the
     *            map should be the name of the placeholder, the value element
     *            should be its value.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall addAllPlaceholderValues( Map<String, String> placeholderValues )
    {
        this.placeholders.putAll( placeholderValues );
        return this;
    }

    /**
     * Returns the value of the placeholder with the given name.
     * 
     * @param name
     *            The name of the placeholder, e.g. <b>id</b>.
     * 
     * @return The value of the placeholder with the given name. If no value was
     *         found <b>null</b> is returned.
     */
    public final String getPlaceholderValue( String name )
    {
        return this.placeholders.get( name );
    }

    /**
     * Returns a map of the configured placeholder values for this REST call.
     * 
     * @return A map of the configured placeholder values for this REST call.
     */
    public final Map<String, String> getPlaceholderValues()
    {
        return this.placeholders;
    }

    /**
     * Removes a placeholder value from the REST call configuration by its name.
     * 
     * @param name
     *            The name of the placeholder, e.g. <b>id</b>.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removePlaceholderValue( String name )
    {
        this.placeholders.remove( name );
        return this;
    }

    /**
     * Removes several placeholder values from the REST call configuration by
     * their names.
     * 
     * @param names
     *            An array of names of the placeholders, e.g. <b>id</b>.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removePlaceholderValues( String... names )
    {
        // Loop through all names of the array and remove the corresponding
        // placeholder values from the map.
        for ( String name : names )
            this.placeholders.remove( name );

        return this;
    }

    /**
     * Removes all placeholder values from the REST call configuration.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removeAllPlaceholderValues()
    {
        this.placeholders = new HashMap<String, String>();
        return this;
    }

    /**
     * Returns <b>true</b> if the REST call configuration contains a request
     * body, <b>false</b> if not.
     * 
     * @return <b>true</b> if the REST call configuration contains a request
     *         body, <b>false</b> if not.
     */
    public boolean hasRequestBody()
    {
        return this.requestBody != null ? true : false;
    }

    /**
     * Sets the request body of the REST call.
     * 
     * @param requestBody
     *            The body of the REST call.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setRequestBody( final String requestBody )
    {
        this.requestBody = requestBody;
        return this;
    }

    /**
     * Sets the request body of the REST call.
     * 
     * @param requestBody
     *            The body of the REST call. Uses the toString() method of the
     *            object to set the body.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setRequestBody( Object requestBody )
    {
        this.requestBody = requestBody.toString();
        return this;
    }

    /**
     * Returns the request body of this REST call configuration. All
     * placeholders in the body were replaced by their corresponding values.
     * 
     * @return The request body of this REST call configuration. If no request
     *         body was set, <b>null</b> is returned.
     */
    public String getRequestBody()
    {
        return replacePlaceholders( this.requestBody );
    }

    /**
     * Removes the request body from the REST call configuration.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall removeRequestBody()
    {
        this.requestBody = null;
        return this;
    }

    /**
     * Makes the call using the configured Url ( {@link #getUrl()} ), the HTTP
     * headers, and the HTTP method.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall process() throws Throwable
    {
        new XltRESTAction( this ).run();
        return this;
    }

    /**
     * This method is similar to {@link #process()}. It overrides the setting
     * for the HTTP method with GET and performs the call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall get() throws Throwable
    {
        this.httpMethod = HttpMethod.GET;
        return process();
    }

    /**
     * This method is similar to {@link #process()}. It overrides the setting
     * for the HTTP method with POST and performs the call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall post() throws Throwable
    {
        this.httpMethod = HttpMethod.POST;
        return process();
    }

    /**
     * This method is similar to {@link #process()}. It overrides the setting
     * for the HTTP method with POST, sets the request body, and performs the
     * call.
     * 
     * @param requestBody
     *            The request body used in the REST call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall post( final String requestBody ) throws Throwable
    {
        this.requestBody = requestBody;
        return post();
    }

    /**
     * This method is similar to {@link #process()}. It overrides the setting
     * for the HTTP method with PUT and performs the call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall put() throws Throwable
    {
        this.httpMethod = HttpMethod.PUT;
        return process();
    }

    /**
     * This method is similar to {@link #process()}. It overrides the setting
     * for the HTTP method with PUT, sets the request body, and performs the
     * call.
     * 
     * @param requestBody
     *            The request body used in the REST call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall put( final String requestBody ) throws Throwable
    {
        this.requestBody = requestBody;
        return put();
    }

    /**
     * This method is similar to {@link #process()}. It overrides the setting
     * for the HTTP method with DELETE and performs the call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall delete() throws Throwable
    {
        this.httpMethod = HttpMethod.DELETE;
        return process();
    }

    /**
     * This method is similar to {@link #process()}. It overrides the setting
     * for the HTTP method with HEAD and performs the call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall head() throws Throwable
    {
        this.httpMethod = HttpMethod.HEAD;
        return process();
    }

    /**
     * This method is similar to {@link #process()}. It overrides the setting
     * for the HTTP method with OPTIONS and performs the call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall options() throws Throwable
    {
        this.httpMethod = HttpMethod.OPTIONS;
        return process();
    }

    /**
     * This method is similar to {@link #process()}. It overrides the setting
     * for the HTTP method with TRACE and performs the call.
     * 
     * @return The updated RESTCall instance.
     * 
     * @throws Throwable
     */
    public RESTCall trace() throws Throwable
    {
        this.httpMethod = HttpMethod.TRACE;
        return process();
    }

    /****************************************************************************************
     ************************ Public Methods - Response Handling ****************************
     ****************************************************************************************/

    /**
     * Sets the response of this REST call. When performing the configured REST
     * call ( {@link #process() }) the REST response is set automatically.
     * 
     * @param response
     *            The response of the REST call.
     * 
     * @return The updated RESTCall instance.
     */
    public RESTCall setRESTResponse( WebResponse response )
    {
        this.response = response;
        return this;
    }

    /**
     * Returns the response body as String. The REST call must be performed
     * before this method can return a body. Otherwise this method returns null.
     * 
     * @return The response body as String.
     */
    public String getResponseBodyAsString()
    {
        // Return null if the REST call has no response yet.
        if ( this.response == null )
            return null;

        return this.response.getContentAsString();
    }

    /**
     * Returns the response body as {@link com.xceptance.xrt.document.JSON JSON}
     * . The REST call must be performed before this method can return a body.
     * Otherwise this method returns null.
     * 
     * @return The response body as String.
     */
    public JSON getResponseBodyAsJSON()
    {
        // Return null if the REST call has no response yet.
        if ( this.response == null )
            return null;

        return new JSON( this.response.getContentAsString() );
    }

    /**
     * Returns the response status code, e.g. 200 or 400. The REST call must be
     * performed before this method can return a status code. Otherwise -1 is
     * returned.
     * 
     * @return The response status code.
     */
    public final int getResponseStatusCode()
    {
        // Return null if the REST call has no response yet.
        if ( this.response == null )
            return -1;

        return this.response.getStatusCode();
    }

    /**
     * Returns the response HTTP headers as a list of name-value pairs. The REST
     * call must be performed before this method can return a status code.
     * Otherwise null is returned.
     * 
     * @return The response status code.
     */
    public final List<NameValuePair> getResponseHttpHeaders()
    {
        // Return null if the REST call has no response yet.
        if ( this.response == null )
            return null;

        return this.response.getResponseHeaders();
    }

    /**
     * Returns the response HTTP header where the given name matches. The REST
     * call must be performed before this method can return a status code.
     * Otherwise null is returned. Also null is returned when the response
     * header can not be found.
     * 
     * @param name
     *            The name of the header to be found.
     * 
     * @return The response HTTP header value that was searched by its name.
     *         Null if the header is not present.
     */
    public final String getResponseHttpHeader( final String name )
    {
        // Loop through all response HTTP headers and return the value where the
        // name matches the first time.
        for ( NameValuePair pair : getResponseHttpHeaders() )
        {
            if ( pair.getName().equals( name ) )
                return pair.getValue();
        }

        return null;
    }

    /**
     * Returns the content type of the response. The REST call must be performed
     * before this method can return a status code. Otherwise null is returned.
     * 
     * @return The content type of the response.
     */
    public final String getResponseContentType()
    {
        return getResponseHttpHeader( "Content-Type" );
    }

    /**
     * <p>
     * Convenience method. Returns the <b>ETag</b> header of the response. The
     * same effect can be achieved by calling the method
     * {@link #getResponseHttpHeader(String)}.
     * </p>
     * <br/>
     * <p>
     * The REST call must be performed before this method can return a status
     * code. Otherwise null is returned.
     * </p>
     * 
     * @return The <b>ETag</b> header value of the response. Returns <b>null</b>
     *         if no <b>ETag</b> header was found.
     */
    public final String getResponseETag()
    {
        return getResponseHttpHeader( "ETag" );
    }

    /****************************************************************************************
     ************************ Private Methods ***********************************************
     ****************************************************************************************/

    /**
     * Reads the global settings and applies them.
     */
    private final void readGlobalSettings()
    {
        // Read regular global settings
        XltProperties globSettings = XltProperties.getInstance();

        this.hostName = globSettings.getProperty( "com.xceptance.xrt.host", this.hostName );
        this.protocol = globSettings.getProperty( "com.xceptance.xrt.protocol", this.protocol );
        this.port = globSettings.getProperty( "com.xceptance.xrt.port", this.port );
        this.basePath = globSettings.getProperty( "com.xceptance.xrt.basePath", this.basePath );
        this.resourcePath = globSettings.getProperty( "com.xceptance.xrt.resourcePath", this.resourcePath );
        this.fragment = globSettings.getProperty( "com.xceptance.xrt.fragment", this.fragment );

        // Read settings that contain a list of key-value pairs.
        readGlobalListProperty( "com.xceptance.xrt.queryParams", this.queryParams );
        readGlobalListProperty( "com.xceptance.xrt.http.headers", this.httpHeaders );
        readGlobalListProperty( "com.xceptance.xrt.placeholders", this.placeholders );

        // Read the HTTP method property
        String httpMethod = globSettings.getProperty( "com.xceptance.xrt.http.method" );
        if ( httpMethod == null )
            return;

        httpMethod = httpMethod.toLowerCase();
        switch ( httpMethod )
        {
            case "get":
                this.httpMethod = HttpMethod.GET;
                break;
            case "post":
                this.httpMethod = HttpMethod.POST;
                break;
            case "put":
                this.httpMethod = HttpMethod.PUT;
                break;
            case "delete":
                this.httpMethod = HttpMethod.DELETE;
                break;
            case "head":
                this.httpMethod = HttpMethod.HEAD;
                break;
            case "options":
                this.httpMethod = HttpMethod.OPTIONS;
                break;
            case "trace":
                this.httpMethod = HttpMethod.TRACE;
                break;
        }
    }

    /**
     * Reads the global settings of a property that contains a list of key-value
     * pairs and stores them in the corresponding map.
     * 
     * @param key
     *            The identifier of the property in the global settings.
     * @param propertyMap
     *            The map that needs to be filled with the key-value pairs of
     *            the property.
     */
    private final void readGlobalListProperty( final String key, Map<String, String> propertyMap )
    {
        String list = XltProperties.getInstance().getProperty( key );

        // if no value was found for that property, skip next steps
        if ( list == null )
            return;

        // Get all key-value pairs and iterate through them.
        String[] valPairArray = list.split( "," );
        for ( String pairString : valPairArray )
        {
            // Separate key from value and check if both are valid.
            String[] keyValueArray = pairString.split( ":" );
            if ( keyValueArray.length != 2 || keyValueArray[0].isEmpty() || keyValueArray[1].isEmpty() )
                continue;

            // Add the key value pair to the property map.
            propertyMap.put( keyValueArray[0], keyValueArray[1] );
        }
    }

    /**
     * Reads the Url settings from the resource definition class and applies
     * them.
     * 
     * @param resourceDef
     *            A class that has the annotation {@link ResourceDefinition}.
     */
    private final void readResourceDefinition( final Class<?> resourceDef )
    {
        ResourceDefinition def = resourceDef.getAnnotation( ResourceDefinition.class );

        // If there is no annotation stop processing.
        if ( def == null )
            return;

        // Read the properties of the annotation.
        // First split the url and save each part.
        if ( !def.baseUrl().isEmpty() )
            splitUrl( def.baseUrl() );

        if ( !def.protocol().isEmpty() )
        {
            this.protocol = def.protocol();

            // Remove the protocol separator
            this.protocol = this.protocol.replaceAll( "/", "" );
            this.protocol = this.protocol.replaceAll( ":", "" );
        }

        if ( def.port() >= 0 )
            this.port = def.port();

        if ( !def.basePath().isEmpty() )
            this.basePath = sanitizeUrlPathSegment( def.basePath() );

        if ( !def.resourcePath().isEmpty() )
            this.resourcePath = sanitizeUrlPathSegment( def.resourcePath() );

        if ( def.queryParams().length != 0 )
        {
            // Loop through the list of parameters and add them to the query
            // parameter map.
            for ( int i = 0; i < def.queryParams().length; i++ )
            {
                QueryParameter param = def.queryParams()[i];
                this.queryParams.put( param.name(), param.value() );
            }
        }

        if ( !def.fragment().isEmpty() )
            this.fragment = def.fragment();

        if ( !def.actionName().isEmpty() )
            this.actionName = def.actionName();
    }

    /**
     * Reads the HTTP method settings from the resource definition class and
     * applies them.
     * 
     * @param resourceDef
     *            A class that has the annotation {@link HttpMethodDefinition}.
     */
    private final void readHttpMethodDefinition( final Class<?> resourceDef )
    {
        HttpMethodDefinition def = resourceDef.getAnnotation( HttpMethodDefinition.class );

        // If there is no annotation stop processing.
        if ( def == null )
            return;

        this.httpMethod = def.value();
    }

    /**
     * Reads the HTTP header settings from the resource definition class and
     * applies them.
     * 
     * @param resourceDef
     *            A class that has the annotation {@link HttpHeaderDefinition}.
     */
    private final void readHttpHeaderDefinition( final Class<?> resourceDef )
    {
        HttpHeaderDefinition def = resourceDef.getAnnotation( HttpHeaderDefinition.class );

        // If there is no annotation stop processing.
        if ( def == null )
            return;

        // Loop through all headers of the definition and add them to the HTTP
        // header map.
        for ( HttpHeader header : def.value() )
        {
            this.httpHeaders.put( header.name(), header.value() );
        }
    }

    /**
     * Reads the placeholder settings from the resource definition class and
     * applies them.
     * 
     * @param resourceDef
     *            A class that has the annotation {@link PlaceholderDefinition}.
     */
    private final void readPlaceholderDefinition( final Class<?> resourceDef )
    {
        PlaceholderDefinition def = resourceDef.getAnnotation( PlaceholderDefinition.class );

        // If there is no annotation stop processing.
        if ( def == null )
            return;

        // Loop through all placeholders of the definition and add them to the
        // placeholder map
        for ( Placeholder placeholder : def.value() )
        {
            this.placeholders.put( placeholder.name(), placeholder.value() );
        }
    }

    /**
     * Splits the Url into its individual elements, e.g. host name, port, query
     * parameters, ...
     * 
     * @param url
     *            The url that needs to be processes. Should already be Url
     *            encoded.
     */
    private final void splitUrl( final String url )
    {
        // If there is no url or if it's an empty String abort without doing
        // anything
        if ( url == null || url.isEmpty() )
            return;

        String restUrl = url;

        // Check if the url has a protocol, if yes extract it
        int index = restUrl.indexOf( "://" );
        if ( index != -1 )
        {
            this.protocol = restUrl.substring( 0, index );
            restUrl = restUrl.substring( index + 3 );
        }

        // Check if the rest of the Url is empty.
        if ( restUrl.isEmpty() )
            return;

        // Remove multiple separators that are not allowed.
        restUrl = removeMultipleSlashes( restUrl );
        restUrl = removeMultipleQueryParamSeparators( restUrl );
        restUrl = removeMultipleFragmentSeparators( restUrl );

        // Check if there is a fragment at the end of the Url
        index = restUrl.indexOf( '#' );
        if ( index != -1 )
        {
            this.fragment = restUrl.substring( index + 1 );
            restUrl = restUrl.substring( 0, index );

            // Check if there more than 1 fragment
            index = this.fragment.indexOf( '#' );
            if ( index != -1 )
                this.fragment = this.fragment.substring( 0, index );
        }

        // Check if the rest of the Url is empty.
        if ( restUrl.isEmpty() )
            return;

        // Check if there are query parameters.
        index = restUrl.indexOf( '?' );
        if ( index != -1 )
        {
            String queryParamString = restUrl.substring( index + 1 );
            splitQueryString( queryParamString );

            restUrl = restUrl.substring( 0, index );
        }

        // Check if the rest of the Url is empty.
        if ( restUrl.isEmpty() )
            return;

        // Get the host name and port if present.
        index = restUrl.indexOf( "/" );
        String hostAndPort;
        if ( index != -1 )
        {
            hostAndPort = restUrl.substring( 0, index );
            restUrl = restUrl.substring( index + 1 );
        }
        else
        {
            // Only the host name was provided.
            hostAndPort = restUrl;
            restUrl = "";
        }

        // Check if there is a port.
        index = hostAndPort.indexOf( ':' );
        if ( index != -1 )
        {
            if ( index != 0 )
                this.hostName = hostAndPort.substring( 0, index );

            String portStr = hostAndPort.substring( index + 1 );

            // Check if there is a port and if it's a numerical value.
            if ( portStr.length() > 0 && portStr.matches( "^[0-9]+$" ) )
                this.port = new Integer( portStr );
        }
        else
            this.hostName = hostAndPort;

        // Check if the rest of the Url is empty.
        if ( restUrl.isEmpty() )
            return;

        // Check if there is at least one element in the Url path after the host
        // name. If so take it as the resource path.
        index = restUrl.lastIndexOf( '/' );
        if ( index != -1 )
        {
            this.resourcePath = restUrl.substring( index + 1 );
            restUrl = restUrl.substring( 0, index );
        }

        // Check if the rest of the Url is empty.
        if ( restUrl.isEmpty() )
            return;

        // The rest of the Url must be the base path. Remove leading or trailing
        // slashes.
        if ( restUrl.startsWith( "/" ) )
            restUrl = restUrl.substring( 1 );

        if ( restUrl.endsWith( "/" ) )
            restUrl = restUrl.substring( 0, restUrl.length() );

        this.basePath = restUrl;
    }

    /**
     * Removes multiple slashes from an Url without protocol.
     * 
     * @param url
     *            The Url that contains multiple slashes.
     * 
     * @return An Url that only has single slashes.
     */
    private String removeMultipleSlashes( String url )
    {
        // Replace double slashes with single slashes until only single slashes
        // exist.
        while ( url.contains( "//" ) )
            url = url.replaceAll( "//", "/" );

        return url;
    }

    /**
     * Removes multiple fragment separators ( '#' ) from an Url without
     * protocol.
     * 
     * @param url
     *            The Url that contains multiple fragment separators.
     * 
     * @return An Url that only has single fragment separator.
     */
    private String removeMultipleFragmentSeparators( String url )
    {
        // Replace double fragment separators with single slashes until only
        // single slashes exist.
        while ( url.contains( "##" ) )
            url = url.replaceAll( "##", "#" );

        return url;
    }

    /**
     * Removes multiple query param separators ( '#' ) from an Url without
     * protocol.
     * 
     * @param url
     *            The Url that contains multiple query param separators.
     * 
     * @return An Url that only has single query param separator.
     */
    private String removeMultipleQueryParamSeparators( String url )
    {
        // Replace double query param separators with single slashes until only
        // single slashes exist.
        while ( url.contains( "??" ) )
            url = url.replaceAll( "\\?\\?", "?" );

        return url;
    }

    /**
     * Splits the query string of an Url into its individual parameters and adds
     * them to the query parameter map.
     * 
     * @param queryString
     *            The part of the Url after '?' and before '#'. Should already
     *            be Url encoded.
     */
    private final void splitQueryString( final String queryString )
    {
        // Split the query string at the occurance of '&'. As a result we get
        // the key - value pairs of the quers parameters.
        String[] queryPairs = queryString.split( "&" );

        // Loop through all key - value pairs and split at '='.
        for ( String keyValue : queryPairs )
        {
            int index = keyValue.indexOf( '=' );
            if ( index != -1 && !keyValue.startsWith( "=" ) )
            {
                this.queryParams.put( keyValue.substring( 0, index ), keyValue.substring( index + 1 ) );
            }
        }
    }

    /**
     * All Url path segments like {@link #basePath} or {@link #resourcePath} are
     * saved without leading and trailing slashes. Also double slashes should be
     * replaced by single ones.
     * 
     * @param urlPathSegment
     *            A part of the url path.
     * 
     * @return The sanitized Url path segment.
     */
    private String sanitizeUrlPathSegment( final String urlPathSegment )
    {
        // Remove double slashes.
        String sanitizedUrl = removeMultipleSlashes( urlPathSegment );

        // Remove a potential leading slash.
        if ( sanitizedUrl.startsWith( "/" ) )
            sanitizedUrl = sanitizedUrl.substring( 1 );

        // Remove a potential trailing slash.
        if ( sanitizedUrl.endsWith( "/" ) )
            sanitizedUrl = sanitizedUrl.substring( 0, sanitizedUrl.length() - 1 );

        return sanitizedUrl;
    }

    /**
     * Replaces all placeholders with the given key-value map.
     * 
     * @param content
     *            The content that contains placeholders that need to be
     *            replaced.
     * 
     * @return The content with the replaced placeholders.
     */
    private String replacePlaceholders( final String content )
    {
        String retContent = content;

        for ( Entry<String, String> placeholder : this.placeholders.entrySet() )
        {
            retContent = retContent.replaceAll( "\\$\\{" + placeholder.getKey() + "\\}", placeholder.getValue() );
        }

        return retContent;
    }
}
