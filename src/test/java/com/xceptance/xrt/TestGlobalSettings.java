package com.xceptance.xrt;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.xceptance.xlt.api.util.XltProperties;

/**
 * Tests the XRT specific global settings added in the *.properties files, e.g.
 * project.properties.
 * 
 * @author Patrick Thaele
 * 
 */
public class TestGlobalSettings
{
    @BeforeClass
    public static void setupOnce() throws Throwable
    {
        XltProperties.getInstance().setProperties( new File( "src/test/resources/properties/project.properties" ) );
    }
    
    @AfterClass
    public static void tearDownFinally() throws Throwable
    {
        XltProperties.reset();
    }

    @Test
    public void host() throws Throwable
    {
        assertEquals( "www.xrt.com", new RESTCall().getHostName() );
    }

    @Test
    public void protocol() throws Throwable
    {
        assertEquals( "ftp", new RESTCall().getProtocol() );
    }

    @Test
    public void port() throws Throwable
    {
        assertEquals( 8080, new RESTCall().getPort() );
    }

    @Test
    public void basePath() throws Throwable
    {
        assertEquals( "rest/${ph1}/${ph2}/${ph3}/${ph4}", new RESTCall().getBasePath() );
    }

    @Test
    public void resourcePath() throws Throwable
    {
        assertEquals( "resource/subresource", new RESTCall().getResourcePath() );
    }

    @Test
    public void queryParams() throws Throwable
    {
        Map<String, String> queryParams = new RESTCall().getQueryParams();
        assertEquals( "Incorrect count.", 5, queryParams.size() );

        assertThat( "1st multi definition entry failed.", queryParams, hasEntry( "qp1", "val1" ) );
        assertThat( "2nd multi definition entry failed.", queryParams, hasEntry( "qp2", "val2" ) );
        assertThat( "Whitespace in comma separated list was not ignored.", queryParams, hasEntry( "qp3", "val3" ) );
        assertThat( "1st single definition failed.", queryParams, hasEntry( "qp4", "val4" ) );
        assertThat( "2nd single definition failed.", queryParams, hasEntry( "qp5", "val5" ) );
    }

    @Test
    public void fragment() throws Throwable
    {
        assertEquals( "testFragment", new RESTCall().getFragment() );
    }

    @Test
    public void placeholders() throws Throwable
    {
        Map<String, String> placeholders = new RESTCall().getPlaceholderValues();
        assertEquals( "Incorrect count.", 4, placeholders.size() );

        assertThat( "1st multi definition entry failed.", placeholders, hasEntry( "ph1", "value1" ) );
        assertThat( "Overriding multi definition with single definition failed.", placeholders,
                hasEntry( "ph2", "valueX" ) );
        assertThat( "3rd single definition failed.", placeholders, hasEntry( "ph3", "value3" ) );
        assertThat( "4th single definition failed.", placeholders, hasEntry( "ph4", "value4" ) );
    }
    
    @Test
    public void httpHeaders() throws Throwable
    {
        Map<String, String> httpHeaders = new RESTCall().getHttpHeaders();
        assertEquals( "Incorrect count.", 4, httpHeaders.size() );

        assertThat( "1st multi definition entry failed.", httpHeaders, hasEntry( "header1", "hvalue1" ) );
        assertThat( "2nd multi definition entry failed.", httpHeaders,
                hasEntry( "header2", "hvalue2" ) );
        assertThat( "3rd single definition failed.", httpHeaders, hasEntry( "header3", "hvalue3" ) );
        assertThat( "4th single definition failed.", httpHeaders, hasEntry( "header4", "hvalue4" ) );
    }
    
    @Test
    public void httpMethodPost() throws Throwable
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.http.method", "POST" );
        assertEquals( HttpMethod.POST, new RESTCall().getHttpMethod() );
    }
    
    @Test
    public void httpMethodGet() throws Throwable
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.http.method", "get" );
        assertEquals( HttpMethod.GET, new RESTCall().getHttpMethod() );
    }
    
    @Test
    public void httpMethodHead() throws Throwable
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.http.method", "HEAD" );
        assertEquals( HttpMethod.HEAD, new RESTCall().getHttpMethod() );
    }
    
    @Test
    public void httpMethodOptions() throws Throwable
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.http.method", "OPTIONS" );
        assertEquals( HttpMethod.OPTIONS, new RESTCall().getHttpMethod() );
    }
    
    @Test
    public void httpMethodPut() throws Throwable
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.http.method", "PUT" );
        assertEquals( HttpMethod.PUT, new RESTCall().getHttpMethod() );
    }
    
    @Test
    public void httpMethodDelete() throws Throwable
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.http.method", "DELETE" );
        assertEquals( HttpMethod.DELETE, new RESTCall().getHttpMethod() );
    }
    
    @Test
    public void httpMethodPatch() throws Throwable
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.http.method", "PATCH" );
        assertEquals( HttpMethod.PATCH, new RESTCall().getHttpMethod() );
    }
    
    @Test
    public void httpMethodMixedCase() throws Throwable
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.http.method", "tRaCe" );
        assertEquals( HttpMethod.TRACE, new RESTCall().getHttpMethod() );
    }
    
    @Test
    public void httpMethodNotExisting() throws Throwable
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.http.method", "foo" );
        assertNull(new RESTCall().getHttpMethod());
    }
    
    @Test
    public void defaultValidationEnabled() throws Throwable
    {
        assertFalse( new RESTCall().isDefaultValidationEnabled() );
    }
}
