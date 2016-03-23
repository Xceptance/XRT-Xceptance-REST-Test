package com.xceptance.xrt;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xrt.annotation.PreProcess;
import com.xceptance.xrt.matcher.RegexMatcher;
import com.xceptance.xrt.validation.preprocess.DefinitionPreProcessor;
import com.xceptance.xrt.validation.preprocess.DummyTokenProvider;
import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 3/21/16.
 */
public class TestPreprocessing
{

    /**
     * The regex used to check the add auth header.
     */
    private final String REGEX = "Bearer somerandomtoken.*";

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
     * Action that prepared the WebClient to use a mocked web connection. Allows to test the RESTCall without sending a
     * request into the real world.
     */
    private XltRESTAction mockAction;

    /**
     * Test setup. Creates the mocked action that needs to be used in every test as previous action.
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

    @After
    public void cleanUp() throws Throwable
    {
        new RESTCall().setPreprocessor( null, false );
    }

    /**
     * Cleanup the timer output.
     */
    @AfterClass
    public static void tearDownFinally() throws Throwable
    {
        FileUtils.deleteDirectory( new File( "tmp" ) );
        XltProperties.reset();
    }

    @Test
    public void noPreprocessor() throws Throwable
    {
        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertNull( authHeader );
    }

    @Test
    public void classAnnotation() throws Throwable
    {
        @PreProcess( DummyTokenProvider.class )
        class Dummy
        {

        }

        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).setDefinitionClass( Dummy.class ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).get();
        String authHeader2 = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertNotEquals( authHeader, authHeader2 );
    }

    @Test
    public void classAnnotation_reuseInstance() throws Throwable
    {
        @PreProcess( value = DummyTokenProvider.class, reuseInstance = true )
        class Dummy
        {

        }

        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).setDefinitionClass( Dummy.class ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).get();
        String authHeader2 = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertEquals( authHeader, authHeader2 );
    }

    @Test
    public void classAnnotation_reuseInstance_reuseDefClass() throws Throwable
    {
        @PreProcess( value = DummyTokenProvider.class, reuseInstance = true )
        class Dummy
        {

        }

        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).setDefinitionClass( Dummy.class ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).setDefinitionClass( Dummy.class ).get();
        String authHeader2 = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertEquals( authHeader, authHeader2 );
    }

    /**
     * Use interface at definition class
     */
    @Test
    public void preProcessible() throws Throwable
    {
        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).setDefinitionClass( DefinitionPreProcessor.class ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).get();
        String authHeader2 = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertNotEquals( authHeader, authHeader2 );
    }

    @Test
    public void properties() throws Throwable
    {
        // Set Property
        XltProperties.getInstance()
                .setProperty( "com.xceptance.xrt.preprocess.class", "com.xceptance.xrt.validation.preprocess.DummyTokenProvider" );

        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).get();
        String authHeader2 = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertNotEquals( authHeader, authHeader2 );

        // Property cleanup
        XltProperties.getInstance().removeProperty( "com.xceptance.xrt.preprocess.class" );
    }

    @Test
    public void properties_reuseInstance() throws Throwable
    {
        // Set Property
        XltProperties.getInstance()
                .setProperty( "com.xceptance.xrt.preprocess.class", "com.xceptance.xrt.validation.preprocess.DummyTokenProvider" );
        XltProperties.getInstance().setProperty( "com.xceptance.xrt.preprocess.reuseInstance", "true" );

        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).get();
        String authHeader2 = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertEquals( authHeader, authHeader2 );

        // Property cleanup
        XltProperties.getInstance().removeProperty( "com.xceptance.xrt.preprocess.class" );
        XltProperties.getInstance().removeProperty( "com.xcpetance.xrt.preprocess.reuseInstance" );
    }

    @Test
    public void properties_classNotFound() throws Throwable
    {
        // Set Property
        XltProperties.getInstance()
                .setProperty( "com.xceptance.xrt.preprocess.class", "com.xceptance.xrt.DoesNotExist" );

        Assert.assertNull( new RESTCall( URL ).getPreprocessor() );

        // Property cleanup
        XltProperties.getInstance().removeProperty( "com.xceptance.xrt.preprocess.class" );
    }

    @Test
    public void setPreprocessor() throws Throwable
    {
        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).setPreprocessor( new DummyTokenProvider(), false );
        call.get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).get();
        authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertNull( authHeader );
    }

    @Test
    public void setPreprocessor_reuseInstance() throws Throwable
    {
        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).setPreprocessor( new DummyTokenProvider(), true ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).get();
        String authHeader2 = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertEquals( authHeader, authHeader2 );
    }

    @Test
    public void setPreprocessor_reuseInstance_reusePreprocessor() throws Throwable
    {
        // Check if DummyTokenProvider can set a HTTP header
        PreProcessible preprocessor = new DummyTokenProvider();
        RESTCall call = new RESTCall( URL ).setPreprocessor( preprocessor, true ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).setPreprocessor( preprocessor, true ).get();
        String authHeader2 = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertEquals( authHeader, authHeader2 );
    }

    @Test
    public void setPreprocessor_reuseInstance_differentPreprocessors() throws Throwable
    {
        // Check if DummyTokenProvider can set a HTTP header
        RESTCall call = new RESTCall( URL ).setPreprocessor( new DummyTokenProvider(), true ).get();
        String authHeader = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertThat( authHeader, RegexMatcher.matchesRegex( REGEX ) );

        // Check that the authHeader changes with every call
        call = new RESTCall( URL ).setPreprocessor( new DummyTokenProvider(), true ).get();
        String authHeader2 = call.getRESTResponse().getWebRequest().getAdditionalHeaders().get( "Authentication" );

        Assert.assertNotEquals( authHeader, authHeader2 );
    }
}
