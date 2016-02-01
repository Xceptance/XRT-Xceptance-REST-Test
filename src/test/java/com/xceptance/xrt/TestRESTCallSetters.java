package com.xceptance.xrt;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;

import static org.junit.Assert.*;

/**
 * The {@link RESTCall} class has various setters to configure the RESTCall.
 * These setters and the corresponding getters are tested in this class.
 * 
 * @author Patrick Thaele
 * 
 */
public class TestRESTCallSetters
{
    @Test
    public void actionNameDefault() throws Throwable
    {
        assertEquals( "anonymous action", new RESTCall().getActionName() );
    }

    @Test
    public void actionNameDefaultWithResourcePath() throws Throwable
    {
        assertEquals( "testResource", new RESTCall().setResourcePath( "testResource" ).getActionName() );
    }

    @Test
    public void actionName() throws Throwable
    {
        assertEquals( "myActionName", new RESTCall().setResourcePath( "testResource" ).setActionName( "myActionName" )
                .getActionName() );
    }

    @Test
    public void actionNameNull() throws Throwable
    {
        assertEquals( "anonymous action", new RESTCall().setActionName( null ).getActionName() );
    }

    @Test
    public void protocolDefault() throws Throwable
    {
        assertEquals( "http", new RESTCall().getProtocol() );
    }

    @Test
    public void protocol() throws Throwable
    {
        assertEquals( "ftp", new RESTCall().setProtocol( "ftp" ).getProtocol() );
    }

    @Test
    public void protocolWithSeparators() throws Throwable
    {
        assertEquals( "https", new RESTCall().setProtocol( "https://" ).getProtocol() );
    }

    @Test
    public void protocolNull() throws Throwable
    {
        assertEquals( "http", new RESTCall().setProtocol( null ).getProtocol() );
    }

    @Test
    public void protocolEmpty() throws Throwable
    {
        assertEquals( "http", new RESTCall().setProtocol( "" ).getProtocol() );
    }

    @Test
    public void portDefault() throws Throwable
    {
        assertEquals( -1, new RESTCall().getPort() );
    }

    @Test
    public void portZero() throws Throwable
    {
        assertEquals( -1, new RESTCall().setPort( 0 ).getPort() );
    }

    @Test
    public void portMin() throws Throwable
    {
        assertEquals( 1, new RESTCall().setPort( 1 ).getPort() );
    }

    @Test
    public void portBackToDefault() throws Throwable
    {
        RESTCall call = new RESTCall().setPort( 8080 );
        assertEquals( 8080, call.getPort() );

        call.setPort( -1 );
        assertEquals( -1, call.getPort() );
    }

    @Test
    public void hostNameDefault() throws Throwable
    {
        assertEquals( "", new RESTCall().getHostName() );
    }

    @Test
    public void hostName() throws Throwable
    {
        assertEquals( "my.host.com", new RESTCall().setHostName( "my.host.com" ).getHostName() );
    }

    @Test
    public void hostNameNull() throws Throwable
    {
        assertEquals( "", new RESTCall().setHostName( null ).getHostName() );
    }

    @Test
    public void basePathDefault() throws Throwable
    {
        assertEquals( "", new RESTCall().getBasePath() );
    }

    @Test
    public void basePath() throws Throwable
    {
        assertEquals( "some/base/path", new RESTCall().setBasePath( "some/base/path" ).getBasePath() );
    }

    @Test
    public void basePathNull() throws Throwable
    {
        assertEquals( "", new RESTCall().setBasePath( null ).getBasePath() );
    }

    @Test
    public void basePathLeadingTrailingSlash() throws Throwable
    {
        assertEquals( "some/base/path", new RESTCall().setBasePath( "/some/base/path/" ).getBasePath() );
    }

    public void resourcePathDefault() throws Throwable
    {
        assertEquals( "", new RESTCall().getResourcePath() );
    }

    @Test
    public void resourcePath() throws Throwable
    {
        assertEquals( "some/resource/path", new RESTCall().setResourcePath( "some/resource/path" ).getResourcePath() );
    }

    @Test
    public void resourcePathNull() throws Throwable
    {
        assertEquals( "", new RESTCall().setResourcePath( null ).getResourcePath() );
    }

    @Test
    public void resourcePathLeadingTrailingSlash() throws Throwable
    {
        assertEquals( "some/resource/path", new RESTCall().setResourcePath( "/some/resource/path/" ).getResourcePath() );
    }

    @Test
    public void addQueryParams() throws Throwable
    {
        // Add some query parameters
        RESTCall call = new RESTCall();
        call.addQueryParam( "first", "someValue" ).addQueryParam( "second", "otherValue" );

        // Assertions
        assertEquals( 2, call.getQueryParams().size() );
        assertEquals( "someValue", call.getQueryParam( "first" ) );
        assertEquals( "otherValue", call.getQueryParam( "second" ) );
    }

    @Test
    public void addAllQueryParams() throws Throwable
    {
        // Add some query parameters
        RESTCall call = new RESTCall();
        call.addQueryParam( "first", "someValue" );

        Map<String, String> someMap = new HashMap<>();
        someMap.put( "second", "mapVal1" );
        someMap.put( "third", "mapVal2" );

        call.addAllQueryParams( someMap ).addQueryParam( "last", "otherValue" );

        // Assertions
        Map<String, String> queryParams = call.getQueryParams();
        assertEquals( 4, queryParams.size() );
        assertThat( queryParams, Matchers.hasEntry( "first", "someValue" ) );
        assertThat( queryParams, Matchers.hasEntry( "second", "mapVal1" ) );
        assertThat( queryParams, Matchers.hasEntry( "third", "mapVal2" ) );
        assertThat( queryParams, Matchers.hasEntry( "last", "otherValue" ) );
    }

    @Test
    public void removeQueryParam() throws Throwable
    {
        // Add some query parameters
        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put( "first", "val1" );
        queryParamMap.put( "second", "val2" );
        queryParamMap.put( "third", "val3" );
        RESTCall call = new RESTCall().addAllQueryParams( queryParamMap );

        // Assertions
        // Remove a query parameter
        Map<String, String> queryParams = call.removeQueryParam( "second" ).getQueryParams();
        assertEquals( 2, queryParams.size() );
        assertThat( queryParams, Matchers.hasKey( "first" ) );
        assertThat( queryParams, Matchers.hasKey( "third" ) );
    }

    @Test
    public void removeQueryParams() throws Throwable
    {
        // Add some query parameters
        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put( "first", "val1" );
        queryParamMap.put( "second", "val2" );
        queryParamMap.put( "third", "val3" );
        RESTCall call = new RESTCall().addAllQueryParams( queryParamMap );

        // Assertions
        // Remove some query parameters
        Map<String, String> queryParams = call.removeQueryParams( "first", "third" ).getQueryParams();
        assertEquals( 1, queryParams.size() );
        assertThat( queryParams, Matchers.hasKey( "second" ) );
    }

    @Test
    public void removeAllQueryParams() throws Throwable
    {
        // Add some query parameters
        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put( "first", "val1" );
        queryParamMap.put( "second", "val2" );
        queryParamMap.put( "third", "val3" );
        RESTCall call = new RESTCall().addAllQueryParams( queryParamMap );

        // Assertions
        // Remove all query parameters
        Map<String, String> queryParams = call.removeAllQueryParams().getQueryParams();
        assertEquals( 0, queryParams.size() );
    }

    @Test
    public void fragmentDefault() throws Throwable
    {
        assertEquals( "", new RESTCall().getFragment() );
    }

    @Test
    public void fragment() throws Throwable
    {
        assertEquals( "someFragment", new RESTCall().setFragment( "someFragment" ).getFragment() );
    }

    @Test
    public void fragmentNull() throws Throwable
    {
        assertEquals( "", new RESTCall().setFragment( null ).getFragment() );
    }

    @Test
    public void httpMethodDefault() throws Throwable
    {
        assertNull( new RESTCall().getHttpMethod() );
    }

    @Test
    public void httpMethod() throws Throwable
    {
        assertEquals( HttpMethod.TRACE, new RESTCall().setHttpMethod( HttpMethod.TRACE ).getHttpMethod() );
    }

    @Test
    public void httpMethodNull() throws Throwable
    {
        assertNull( new RESTCall().setHttpMethod( HttpMethod.OPTIONS ).setHttpMethod( null ).getHttpMethod() );
    }

    @Test
    public void addHttpHeaders() throws Throwable
    {
        // Add some HTTP headers
        RESTCall call = new RESTCall();
        call.addHttpHeader( "first", "someValue" ).addHttpHeader( "second", "otherValue" );

        // Assertions
        assertEquals( 2, call.getHttpHeaders().size() );
        assertEquals( "someValue", call.getHttpHeader( "first" ) );
        assertEquals( "otherValue", call.getHttpHeader( "second" ) );
    }

    @Test
    public void addAllHttpHeaders() throws Throwable
    {
        // Add some HTTP headers
        RESTCall call = new RESTCall();
        call.addHttpHeader( "first", "someValue" );

        Map<String, String> someMap = new HashMap<>();
        someMap.put( "second", "mapVal1" );
        someMap.put( "third", "mapVal2" );

        call.addAllHttpHeaders( someMap ).addHttpHeader( "last", "otherValue" );

        // Assertions
        Map<String, String> httpHeaders = call.getHttpHeaders();
        assertEquals( 4, httpHeaders.size() );
        assertThat( httpHeaders, Matchers.hasEntry( "first", "someValue" ) );
        assertThat( httpHeaders, Matchers.hasEntry( "second", "mapVal1" ) );
        assertThat( httpHeaders, Matchers.hasEntry( "third", "mapVal2" ) );
        assertThat( httpHeaders, Matchers.hasEntry( "last", "otherValue" ) );
    }

    @Test
    public void removehttpHeader() throws Throwable
    {
        // Add some HTTP headers
        Map<String, String> httpHeaderMap = new HashMap<>();
        httpHeaderMap.put( "first", "val1" );
        httpHeaderMap.put( "second", "val2" );
        httpHeaderMap.put( "third", "val3" );
        RESTCall call = new RESTCall().addAllHttpHeaders( httpHeaderMap );

        // Assertions
        // Remove a HTTP parameter
        Map<String, String> httpHeaders = call.removeHttpHeader( "second" ).getHttpHeaders();
        assertEquals( 2, httpHeaders.size() );
        assertThat( httpHeaders, Matchers.hasKey( "first" ) );
        assertThat( httpHeaders, Matchers.hasKey( "third" ) );
    }

    @Test
    public void removeHttpHeaders() throws Throwable
    {
        // Add some HTTP headers
        Map<String, String> httpHeaderMap = new HashMap<>();
        httpHeaderMap.put( "first", "val1" );
        httpHeaderMap.put( "second", "val2" );
        httpHeaderMap.put( "third", "val3" );
        RESTCall call = new RESTCall().addAllHttpHeaders( httpHeaderMap );

        // Assertions
        // Remove some HTTP headers
        Map<String, String> httpHeaders = call.removeHttpHeaders( "first", "third" ).getHttpHeaders();
        assertEquals( 1, httpHeaders.size() );
        assertThat( httpHeaders, Matchers.hasKey( "second" ) );
    }

    @Test
    public void removeAllHttpHeaders() throws Throwable
    {
        // Add some HTTP headers
        Map<String, String> httpHeaderMap = new HashMap<>();
        httpHeaderMap.put( "first", "val1" );
        httpHeaderMap.put( "second", "val2" );
        httpHeaderMap.put( "third", "val3" );
        RESTCall call = new RESTCall().addAllHttpHeaders( httpHeaderMap );

        // Assertions
        // Remove all HTTP headers
        Map<String, String> httpHeaders = call.removeAllHttpHeaders().getHttpHeaders();
        assertEquals( 0, httpHeaders.size() );
    }

    @Test
    public void setIfMatchHeader() throws Throwable
    {
        assertEquals( "someIfMatchValue",
                new RESTCall().setIfMatchHeader( "someIfMatchValue" ).getHttpHeader( "If-Match" ) );
    }

    @Test
    public void setIfNoneMatchHeader() throws Throwable
    {
        assertEquals( "someIfNoneMatchValue", new RESTCall().setIfNoneMatchHeader( "someIfNoneMatchValue" )
                .getHttpHeader( "If-None-Match" ) );
    }

    @Test
    public void addPlaceholderValues() throws Throwable
    {
        // Add some placeholder values
        RESTCall call = new RESTCall();
        call.addPlaceholderValue( "first", "someValue" ).addPlaceholderValue( "second", "otherValue" );

        // Assertions
        assertEquals( 2, call.getPlaceholderValues().size() );
        assertEquals( "someValue", call.getPlaceholderValue( "first" ) );
        assertEquals( "otherValue", call.getPlaceholderValue( "second" ) );
    }

    @Test
    public void addAllPlaceholderValues() throws Throwable
    {
        // Add some placeholder values
        RESTCall call = new RESTCall();
        call.addPlaceholderValue( "first", "someValue" );

        Map<String, String> someMap = new HashMap<>();
        someMap.put( "second", "mapVal1" );
        someMap.put( "third", "mapVal2" );

        call.addAllPlaceholderValues( someMap ).addPlaceholderValue( "last", "otherValue" );

        // Assertions
        Map<String, String> placeholderValues = call.getPlaceholderValues();
        assertEquals( 4, placeholderValues.size() );
        assertThat( placeholderValues, Matchers.hasEntry( "first", "someValue" ) );
        assertThat( placeholderValues, Matchers.hasEntry( "second", "mapVal1" ) );
        assertThat( placeholderValues, Matchers.hasEntry( "third", "mapVal2" ) );
        assertThat( placeholderValues, Matchers.hasEntry( "last", "otherValue" ) );
    }

    @Test
    public void removePlaceholderValue() throws Throwable
    {
        // Add some placeholder values
        Map<String, String> phValueMap = new HashMap<>();
        phValueMap.put( "first", "val1" );
        phValueMap.put( "second", "val2" );
        phValueMap.put( "third", "val3" );
        RESTCall call = new RESTCall().addAllPlaceholderValues( phValueMap );

        // Assertions
        // Remove a placeholder value
        Map<String, String> placeholderValues = call.removePlaceholderValue( "second" ).getPlaceholderValues();
        assertEquals( 2, placeholderValues.size() );
        assertThat( placeholderValues, Matchers.hasKey( "first" ) );
        assertThat( placeholderValues, Matchers.hasKey( "third" ) );
    }

    @Test
    public void removePlaceholderValues() throws Throwable
    {
        // Add some placeholder values
        Map<String, String> phValueMap = new HashMap<>();
        phValueMap.put( "first", "val1" );
        phValueMap.put( "second", "val2" );
        phValueMap.put( "third", "val3" );
        RESTCall call = new RESTCall().addAllPlaceholderValues( phValueMap );

        // Assertions
        // Remove some placeholder values
        Map<String, String> placeholderValues = call.removePlaceholderValues( "first", "third" ).getPlaceholderValues();
        assertEquals( 1, placeholderValues.size() );
        assertThat( placeholderValues, Matchers.hasKey( "second" ) );
    }

    @Test
    public void removeAllPlaceholderValues() throws Throwable
    {
        // Add some placeholder values
        Map<String, String> phValueMap = new HashMap<>();
        phValueMap.put( "first", "val1" );
        phValueMap.put( "second", "val2" );
        phValueMap.put( "third", "val3" );
        RESTCall call = new RESTCall().addAllPlaceholderValues( phValueMap );

        // Assertions
        // Remove all placeholder values
        Map<String, String> placeholderValues = call.removeAllPlaceholderValues().getPlaceholderValues();
        assertEquals( 0, placeholderValues.size() );
    }

    @Test
    public void requestBodyDefault() throws Throwable
    {
        assertNull( new RESTCall().getRequestBody() );
    }

    @Test
    public void requestBodyWithString() throws Throwable
    {
        assertEquals( "body content", new RESTCall().setRequestBody( "body content" ).getRequestBody() );
    }

    @Test
    public void requestBodyWithObject() throws Throwable
    {
        class Foo
        {
            @Override
            public String toString()
            {
                return "body content from obj.";
            }
        }

        Foo testClazz = new Foo();

        assertEquals( "body content from obj.", new RESTCall().setRequestBody( testClazz ).getRequestBody() );
    }

    @Test
    public void requestBodyWithObjectNull() throws Throwable
    {
        assertNull( new RESTCall().setRequestBody( (Object) null ).getRequestBody() );
    }

    @Test
    public void requestBodyWithPlaceholder() throws Throwable
    {
        assertEquals( "body content with placeholder.", new RESTCall().addPlaceholderValue( "ph", "placeholder" )
                .setRequestBody( "body content with ${ph}." ).getRequestBody() );
    }

    @Test
    public void removeRequestBody() throws Throwable
    {
        assertNull( new RESTCall().setRequestBody( "body content" ).removeRequestBody().getRequestBody() );
    }

    @Test
    public void urlWithDefaultProtocol() throws Throwable
    {
        assertEquals( "http://www.myRestApi.com", new RESTCall().setHostName( "www.myRestApi.com" ).getUrl() );
    }

    @Test
    public void urlWithDefaultProtocolAndSpecialPort() throws Throwable
    {
        assertEquals( "http://www.myRestApi.com:1", new RESTCall().setHostName( "www.myRestApi.com" ).setPort( 1 )
                .getUrl() );
    }

    @Test
    public void urlWithExplicitDefaultPort() throws Throwable
    {
        assertEquals( "http://www.myRestApi.com:80", new RESTCall().setHostName( "www.myRestApi.com" ).setPort( 80 )
                .getUrl() );
    }

    @Test
    public void urlWithFTP() throws Throwable
    {
        assertEquals( "ftp://www.myRestApi.com", new RESTCall().setUrl( "www.myRestApi.com" ).setProtocol( "ftp" )
                .getUrl() );
    }
}
