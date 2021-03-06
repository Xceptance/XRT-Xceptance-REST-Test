package com.xceptance.xrt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xrt.RESTCall;
import com.xceptance.xrt.RESTCallNotYetPerformedException;

/**
 * This class validates the response behavior of the RESTCall class.
 * 
 * @author Patrick Thaele
 * 
 */
public class TestRESTCallResponse
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /****************************************************************************************
     ************************ Mock Settings *************************************************
     ****************************************************************************************/

    /**
     * URL used for mocked request.
     */
    private final String URL = "www.xrt.com";

    /**
     * The mocked response body.
     */
    private final String RESPONSE_BODY = "{\"id\":\"test\"}";

    /**
     * The mocked response status code.
     */
    private final int STATUS_CODE = 200;

    /**
     * The mocked response status message.
     */
    private final String STATUS_MESSAGE = "OK";

    /**
     * The mocked response content type.
     */
    private final String CONTENT_TYPE = "application/json";

    /**
     * The mocked response HTTP headers.
     */
    private final List<NameValuePair> HTTP_HEADERS = new ArrayList<>();

    /**
     * Action that prepared the WebClient to use a mocked web connection. Allows
     * to test the RESTCall without sending a request into the real world.
     */
    private XltRESTAction mockAction;

    /**
     * Define result dir to prevent timers.csv exceptions.
     */
    @BeforeClass
    public static void setUpOnce()
    {
        XltProperties.getInstance().setProperty( "com.xceptance.xlt.result-dir", "tmp" );
    }

    /**
     * Cleanup the timer output.
     */
    @AfterClass
    public static void tearDownFinally() throws Throwable
    {
        FileUtils.deleteDirectory( new File( "tmp" ) );
    }

    /**
     * Test setup. Creates the mocked action that needs to be used in every test
     * as previous action.
     * 
     * @throws Throwable
     */
    @Before
    public void setUp() throws Throwable
    {
        // Adding HTTP headers to the list.
        HTTP_HEADERS.add( new NameValuePair( "Custom-Header", "custom/value" ) );

        // The mocked web connection.
        MockWebConnection connection = new MockWebConnection();
        connection.setDefaultResponse( RESPONSE_BODY, STATUS_CODE, STATUS_MESSAGE, CONTENT_TYPE, HTTP_HEADERS );

        // Creating the mock action using a WebClient with a mocked web
        // connection.
        mockAction = new XltRESTAction( new RESTCall() );
        mockAction.getWebClient().setWebConnection( connection );
    }

    @Test
    public void getResponseBodyAsString_Positive() throws Throwable
    {
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertEquals( RESPONSE_BODY, call.getResponseBodyAsString() );
    }

    @Test
    public void getResponseBodyAsString_EmptyBody() throws Throwable
    {
        // The mocked web connection.
        MockWebConnection connection = new MockWebConnection();
        connection.setDefaultResponse( "", STATUS_CODE, STATUS_MESSAGE, CONTENT_TYPE, HTTP_HEADERS );

        mockAction.getWebClient().setWebConnection( connection );

        // Test for an empty response body.
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertEquals( "", call.getResponseBodyAsString() );
    }

    @Test
    public void getResponseBodyAsString_NoCallPerformed()
    {
        expectedException.expect( RESTCallNotYetPerformedException.class );
        expectedException.expectMessage( "getResponseBodyAsString()" );

        new RESTCall().getResponseBodyAsString();
    }

    @Test
    public void getResponseBodyAsJSON_Positive() throws Throwable
    {
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertEquals( RESPONSE_BODY, call.getResponseBodyAsJSON().toString() );
    }

    @Test
    public void getResponseBodyAsJSON_EmptyBody() throws Throwable
    {
        // The mocked web connection.
        MockWebConnection connection = new MockWebConnection();
        connection.setDefaultResponse( "", STATUS_CODE, STATUS_MESSAGE, CONTENT_TYPE, HTTP_HEADERS );

        mockAction.getWebClient().setWebConnection( connection );

        // Test for an empty response body.
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertEquals( "", call.getResponseBodyAsJSON().toString() );
    }

    @Test
    public void getResponseBodyAsJSON_NoCallPerformed()
    {
        expectedException.expect( RESTCallNotYetPerformedException.class );
        expectedException.expectMessage( "getResponseBodyAsJSON()" );

        new RESTCall().getResponseBodyAsJSON();
    }

    @Test
    public void getResponseStatusCode_Positive() throws Throwable
    {
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertEquals( STATUS_CODE, call.getResponseStatusCode() );
    }

    @Test
    public void getResponseStatusCode_NoCallPerformed()
    {
        expectedException.expect( RESTCallNotYetPerformedException.class );
        expectedException.expectMessage( "getResponseStatusCode()" );

        new RESTCall().getResponseStatusCode();
    }

    @Test
    public void getResponseStatusMessage_Positive() throws Throwable
    {
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertEquals( STATUS_MESSAGE, call.getResponseStatusMessage() );
    }

    @Test
    public void getResponseStatusMessage_NoCallPerformed()
    {
        expectedException.expect( RESTCallNotYetPerformedException.class );
        expectedException.expectMessage( "getResponseStatusMessage()" );

        new RESTCall().getResponseStatusMessage();
    }

    @Test
    public void getResponseHttpHeaders_Positive() throws Throwable
    {
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        List<NameValuePair> headerList = call.getResponseHttpHeaders();
        Assert.assertEquals( 2, headerList.size() );
        Assert.assertEquals( "Custom-Header", headerList.get( 0 ).getName() );
        Assert.assertEquals( "Content-Type", headerList.get( 1 ).getName() );
    }

    @Test
    public void getResponseHttpHeaders_NoHeaders() throws Throwable
    {
        // The mocked web connection.
        MockWebConnection connection = new MockWebConnection();
        connection.setDefaultResponse( RESPONSE_BODY, STATUS_CODE, STATUS_MESSAGE, null, null );

        mockAction.getWebClient().setWebConnection( connection );

        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertEquals( 0, call.getResponseHttpHeaders().size() );
    }

    @Test
    public void getResponseHttpHeaders_NoCallPerformed()
    {
        expectedException.expect( RESTCallNotYetPerformedException.class );
        expectedException.expectMessage( "getResponseHttpHeaders()" );

        new RESTCall().getResponseHttpHeaders();
    }

    @Test
    public void getResponseHttpHeader_Positive() throws Throwable
    {
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertEquals( "custom/value", call.getResponseHttpHeader( "Custom-Header" ) );
    }

    @Test
    public void getResponseHttpHeader_NoHeaderFound() throws Throwable
    {
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertNull( "No return value expected for a non existing header.", call.getResponseHttpHeader( "foo" ) );
    }

    @Test
    public void getResponseHttpHeader_NoCallPerformed()
    {
        expectedException.expect( RESTCallNotYetPerformedException.class );
        expectedException.expectMessage( "getResponseHttpHeader(String)" );

        new RESTCall().getResponseHttpHeader( "foo" );
    }

    @Test
    public void getResponseContentType_Positive() throws Throwable
    {
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertEquals( CONTENT_TYPE, call.getResponseContentType() );
    }

    @Test
    public void getResponseContentType_NoContentType() throws Throwable
    {
        // The mocked web connection.
        MockWebConnection connection = new MockWebConnection();
        connection.setDefaultResponse( RESPONSE_BODY, STATUS_CODE, STATUS_MESSAGE, null, HTTP_HEADERS );

        mockAction.getWebClient().setWebConnection( connection );

        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertNull( "No content type expected.", call.getResponseContentType() );
    }

    @Test
    public void getResponseContentType_NoCallPerformed()
    {
        expectedException.expect( RESTCallNotYetPerformedException.class );
        expectedException.expectMessage( "getResponseContentType()" );

        new RESTCall().getResponseContentType();
    }

    @Test
    public void getResponseETag_Positive() throws Throwable
    {
        // The mocked web connection.
        final String ETAG = "Some kind of Hash Value.";
        HTTP_HEADERS.add( new NameValuePair( "ETag", ETAG ) );

        MockWebConnection connection = new MockWebConnection();
        connection.setDefaultResponse( RESPONSE_BODY, STATUS_CODE, STATUS_MESSAGE, CONTENT_TYPE, HTTP_HEADERS );

        mockAction.getWebClient().setWebConnection( connection );

        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).post( "{}" );
        Assert.assertEquals( ETAG, call.getResponseETag() );
    }

    @Test
    public void getResponseETag_NoETag() throws Throwable
    {
        RESTCall call = new RESTCall( URL ).setPreviousAction( mockAction ).get();
        Assert.assertNull( "No ETag expected.", call.getResponseETag() );
    }

    @Test
    public void getResponseETag_NoCallPerformed()
    {
        expectedException.expect( RESTCallNotYetPerformedException.class );
        expectedException.expectMessage( "getResponseETag()" );

        new RESTCall().getResponseETag();
    }
}
