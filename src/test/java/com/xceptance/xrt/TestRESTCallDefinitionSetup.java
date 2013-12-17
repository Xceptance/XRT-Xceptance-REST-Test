package com.xceptance.xrt;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;

/**
 * <p>
 * The class {@link RESTCall} can be initialized by setting default values. The
 * values are retrieved from the following class annotations provided by the
 * class that is used as an argument:
 * </p>
 * <ul>
 * <li>{@link ResourceDefinition}</li>
 * <li>{@link HttpMethodDefinition}</li>
 * <li>{@link HttpHeaderDefinition}</li>
 * </ul>
 * <br/>
 * <p>
 * Testing the given definitions is the purpose of this class.
 * </p>
 * <br/>
 * 
 * @author Patrick Thaele
 * 
 */
public class TestRESTCallDefinitionSetup
{
    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class has no annotations.
     */
    @Test
    public void noDefinition()
    {
        // Define the resource definition
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertNull( "Expected null due to missing configuration.", call.getUrl() );
    }

    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines all properties of the REST Url via the
     * resource definition.
     */
    @Test
    public void resourceDefinition_complete()
    {
        // Define the resource definition
        @ResourceDefinition( protocol = "https", baseUrl = "my.url.test.com", port = 8080, basePath = "base/path", resourcePath = "resource", queryParams = {
                @QueryParameter( name = "param1", value = "foo" ), @QueryParameter( name = "param2", value = "bar" ) }, fragment = "fragment" )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertEquals( "Expected Url: ",
                "https://my.url.test.com:8080/base/path/resource?param1=foo&param2=bar#fragment", call.getUrl() );
    }

    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines the base Url only via the resource
     * definition.
     */
    @Test
    public void resourceDefinition_baseUrlOnly()
    {
        // Define the resource definition
        @ResourceDefinition( baseUrl = "my.url.test.com" )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com", call.getUrl() );
    }

    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines the base Url only via the resource
     * definition. The base Url also contains the port number.
     */
    @Test
    public void resourceDefinition_baseUrlWithPort()
    {
        // Define the resource definition
        @ResourceDefinition( baseUrl = "my.url.test.com:8080" )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com:8080", call.getUrl() );
    }

    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines the base Url only via the resource
     * definition. The base Url also contains the port number.
     */
    @Test
    public void resourceDefinition_baseUrlWithProtocol()
    {
        // Define the resource definition
        @ResourceDefinition( baseUrl = "https://my.url.test.com" )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertEquals( "Expected Url: ", "https://my.url.test.com", call.getUrl() );
    }
    
    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines a protocol with the separator <b>http://</b> via the resource
     * definition.
     */
    @Test
    public void resourceDefinition_protocolWithSeparator()
    {
        // Define the resource definition
        @ResourceDefinition( baseUrl = "my.url.test.com", protocol="https://" )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertEquals( "Expected Url: ", "https://my.url.test.com", call.getUrl() );
    }

    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines the base Url only via the resource
     * definition. The base Url has a trailing '/' character..
     */
    @Test
    public void resourceDefinition_baseUrlWithTrailingSlash()
    {
        // Define the resource definition
        @ResourceDefinition( baseUrl = "my.url.test.com/" )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com", call.getUrl() );
    }

    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines the base Url and the base path via the
     * resource definition. The base path has a leading and a trailing '/'
     * character.
     */
    @Test
    public void resourceDefinition_basePathWithLeadingAndTrailingSlash()
    {
        // Define the resource definition
        @ResourceDefinition( baseUrl = "my.url.test.com", basePath = "/base///path/" )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com/base/path", call.getUrl() );
    }

    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines the base Url and the base path via the
     * resource definition. The base path has a leading and a trailing '/'
     * character.
     */
    @Test
    public void resourceDefinition_resourcePathWithLeadingAndTrailingSlash()
    {
        // Define the resource definition
        @ResourceDefinition( baseUrl = "my.url.test.com", resourcePath = "/resource/" )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com/resource", call.getUrl() );
    }

    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines the base Url and the base path via the
     * resource definition. The base path has a leading and a trailing '/'
     * character.
     */
    @Test
    public void httpMethodDefinition()
    {
        // Define the resource definition
        @HttpMethodDefinition( HttpMethod.POST )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Assert.assertEquals( "Expected Http Method: ", HttpMethod.POST, call.getHttpMethod() );
    }

    /**
     * Calls the constructor that takes a class with definition annotations as
     * an argument. The class defines the base Url and the base path via the
     * resource definition. The base path has a leading and a trailing '/'
     * character.
     */
    @Test
    public void httpHeaderDefinition()
    {
        // Define the resource definition
        @HttpHeaderDefinition( { @HttpHeader( name = "Content-type", value = "application/json" ),
                @HttpHeader( name = "x-custom", value = "true" ) } )
        class DefinitionClass
        {
        }

        RESTCall call = new RESTCall( DefinitionClass.class );

        Map<String,String> expectedMap = new HashMap<String,String>();
        expectedMap.put( "Content-type", "application/json" );
        expectedMap.put( "x-custom", "true" );
        
        Assert.assertEquals( "Expected Http Headers: ", expectedMap, call.getHttpHeaders() );
    }
}
