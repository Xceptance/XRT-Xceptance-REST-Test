package com.xceptance.xrt;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Performs a REST call based on the information retrieved from its internal and
 * global settings.
 * 
 * @author Patrick Thaele
 * 
 */
public class RESTCall
{
    /****************************************************************************************
     ************************ Private Properties ********************************************
     ****************************************************************************************/

    // All properties are initialized with an empty String to avoid NULL pointer
    // exceptions.

    /**
     * The protocol used in the REST call, e.g. <b>http</b> and <b>https</b>.
     */
    private String protocol = "";

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
     * The response of the REST call.
     */
    private WebResponse response;

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

        readResourceDefinition( resourceDef );
        readHttpMethodDefinition( resourceDef );
        readHttpHeaderDefinition( resourceDef );
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
     * Creates the REST Url. All elements are Url encoded. The host name is
     * mandatory.
     * 
     * @return The Url ready to call. Returns <b>null</b> if the host name is
     *         missing.
     */
    public final String getUrl()
    {
        // String builder that builds the Url.
        StringBuilder builder = new StringBuilder();

        // If the protocol and the port appear in following combinations, ignore
        // them: http => 80, https => 433.
        boolean skipProtocolAndPort = ( this.protocol.equals( "http" ) && this.port == 80 )
                || ( this.protocol.equals( "https" ) && this.port == 433 );

        if ( !this.protocol.isEmpty() && !skipProtocolAndPort )
            builder.append( this.protocol ).append( "://" );

        // A Url without host name does not make any sense.
        if ( this.hostName.isEmpty() )
            return null;

        builder.append( hostName );

        if ( this.port != -1 && !skipProtocolAndPort )
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

        return builder.toString();
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

    /****************************************************************************************
     ************************ Private Methods ***********************************************
     ****************************************************************************************/

    /**
     * Reads the global settings and applies them.
     */
    private final void readGlobalSettings()
    {
        // TODO Implement global settings.
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
            this.protocol = def.protocol();

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
}
