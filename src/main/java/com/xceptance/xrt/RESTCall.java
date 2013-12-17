package com.xceptance.xrt;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.gargoylesoftware.htmlunit.HttpMethod;

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
    String protocol = "";

    /**
     * The port used in the REST call, e.g. <b>80</b> and <b>433</b>.
     */
    int port = -1;

    /**
     * The host name used in the REST call, e.g. <b>xceptance.com</b>.
     */
    String hostName = "";

    /**
     * The base path used in the REST call. The base path is the part of the Url
     * between host name and resource path, e.g.
     * <b>xceptance.com/base/path/resource</b>. In the example <b>base/path</b>
     * is the base path in the Url.
     */
    String basePath = "";

    /**
     * Identifies the resource of the REST call.
     */
    String resourcePath = "";

    /**
     * A map of query parameters that are used in the REST call, e.g.
     * <b>xceptance.com/base/path/resource?paramName=paramValue</b>.
     */
    Map<String, String> queryParams = new HashMap<String, String>();

    /**
     * The fragment used in the REST call, e.g.
     * <b>xceptance.com/base/path/resource?paramName=paramValue#fragment</b>.
     */
    String fragment = "";

    /**
     * The HTTP method used in the REST call, e.g. GET and POST.
     */
    HttpMethod httpMethod;

    /**
     * A map of http headers.
     */
    Map<String, String> httpHeaders = new HashMap<String, String>();

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
     * @param url The Url used for the REST call.
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
    public String getUrl()
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

    /****************************************************************************************
     ************************ Private Methods ***********************************************
     ****************************************************************************************/

    /**
     * Reads the global settings and applies them.
     */
    private final void readGlobalSettings()
    {

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
            this.basePath = def.basePath();

        if ( !def.resourcePath().isEmpty() )
            this.resourcePath = def.resourcePath();

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
            url = url.replace( "//", "/" );

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
            url = url.replace( "##", "#" );

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
            url = url.replace( "??", "?" );

        return url;
    }

    /**
     * Splits the query string of an Url into its individual parameters and adds them to the query parameter map.
     * 
     * @param queryString The part of the Url after '?' and before '#'. Should already be Url encoded.
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
}
