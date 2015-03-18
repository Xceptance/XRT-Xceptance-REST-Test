package com.xceptance.xrt;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xrt.validation.DefaultValidation_Correct;
import com.xceptance.xrt.validation.DefaultValidation_CorrectStatusCode;
import com.xceptance.xrt.validation.DefaultValidation_DerivedMethod;

/**
 * Unit tests for the method {@link RESTCall#processValidators()}.
 * 
 * @author Patrick Thaele
 * 
 */
public class TestDefaultValidation
{
    /****************************************************************************************
     ************************ Mock Settings *************************************************
     ****************************************************************************************/

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
    
    @After
    public void tearDown() throws Throwable
    {
        // Clean up status fields in definition classes
        DefaultValidation_Correct.valPerformed = false;
        DefaultValidation_CorrectStatusCode.valStatusCode = "Not yet executed.";
    }

    /****************************************************************************************
     ************************ Default Validation Tests **************************************
     ****************************************************************************************/
   
    /**
     * <p>
     * Validates if the default validation methods are called.
     * 
     * @throws Throwable
     */
    @Test
    public void defaultValidation() throws Throwable
    {
        new RESTCall( DefaultValidation_Correct.class ).setPreviousAction( mockAction ).get();

        Assert.assertTrue( DefaultValidation_Correct.valPerformed );
    }

    /**
     * Validates if the default validation methods of different definition
     * classes are called, e.g. a user can have more than one default validation
     * method to test the status code.
     * 
     * @throws Throwable
     */
    @Test
    public void severalDefinitionClassesWithDefaultValidation() throws Throwable
    {
        new RESTCall( DefaultValidation_Correct.class ).setDefinitionClass( DefaultValidation_CorrectStatusCode.class )
                .setPreviousAction( mockAction ).get();

        Assert.assertTrue( DefaultValidation_Correct.valPerformed );
        Assert.assertEquals( DefaultValidation_CorrectStatusCode.expValStatusCode + STATUS_CODE,
                DefaultValidation_CorrectStatusCode.valStatusCode );
    }

    /**
     * To have less redundancy it is allowed to derive the methods from a super
     * class.
     * 
     * @throws Throwable
     */
    @Test
    public void derivedMethods() throws Throwable
    {
        new RESTCall( DefaultValidation_DerivedMethod.class ).setPreviousAction( mockAction ).get();

        Assert.assertTrue( DefaultValidation_DerivedMethod.valPerformed );
    }

    /**
     * Default validation can be enabled via the method
     * {@link RESTCall#defaultValidation(boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void enableDefaultValidation() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_Correct.class ).setPreviousAction( mockAction );

        Assert.assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );
        call.defaultValidation( true );
        Assert.assertTrue( "Default validation should be enabled after using setter.",
                call.isDefaultValidationEnabled() );

        call.get();

        Assert.assertTrue( DefaultValidation_Correct.valPerformed );
    }

    /**
     * Default validation can be disabled via the method
     * {@link RESTCall#defaultValidation(boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void disableDefaultValidation() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_Correct.class ).setPreviousAction( mockAction );
        
        Assert.assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );
        call.defaultValidation( false );
        Assert.assertFalse( "Default validation should be disabled after using setter.", call.isDefaultValidationEnabled() );
        
        call.get();

        Assert.assertFalse( DefaultValidation_Correct.valPerformed );
    }
}
