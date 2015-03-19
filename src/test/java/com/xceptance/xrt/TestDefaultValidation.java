package com.xceptance.xrt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xrt.validation.DefaultValidation_Correct;
import com.xceptance.xrt.validation.DefaultValidation_CorrectStatusCode;
import com.xceptance.xrt.validation.DefaultValidation_DerivedMethod;
import com.xceptance.xrt.validation.DefaultValidation_Disabled;
import com.xceptance.xrt.validation.DefaultValidation_Disabled2nd;

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
     * Global property to enable/disable default validation.
     */
    private final String GLOB_PROP = "com.xceptance.xrt.defaultValidation.enabled";

    /**
     * Convenience field for setting properties.
     */
    private final String FALSE = Boolean.FALSE.toString();

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

        // Define result dir to prevent timers.csv exceptions.
        XltProperties.getInstance().setProperty( "com.xceptance.xlt.result-dir", "tmp" );
    }

    @After
    public void tearDown() throws Throwable
    {
        // Clean up status fields in definition classes
        DefaultValidation_Correct.valPerformed = false;
        DefaultValidation_CorrectStatusCode.valStatusCode = "Not yet executed.";
        DefaultValidation_Disabled.valPerformed = false;
        DefaultValidation_Disabled2nd.valPerformed = false;

        // Cleanup property changes
        XltProperties.reset();
    }

    /**
     * Cleanup the timer output.
     */
    @AfterClass
    public static void tearDownFinally() throws Throwable
    {
        FileUtils.deleteDirectory( new File( "tmp" ) );
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

        assertTrue( DefaultValidation_Correct.valPerformed );
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

        assertTrue( DefaultValidation_Correct.valPerformed );
        assertEquals( DefaultValidation_CorrectStatusCode.expValStatusCode + STATUS_CODE,
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

        assertTrue( DefaultValidation_DerivedMethod.valPerformed );
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

        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );
        call.defaultValidation( true );
        assertTrue( "Default validation should be enabled after using setter.", call.isDefaultValidationEnabled() );

        call.get();

        assertTrue( DefaultValidation_Correct.valPerformed );
    }

    /**
     * Default validation can be disabled via the method
     * {@link RESTCall#defaultValidation(boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void disable4RESTCall() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_Correct.class ).setPreviousAction( mockAction );

        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );
        call.defaultValidation( false );
        assertFalse( "Default validation should be disabled after using setter.", call.isDefaultValidationEnabled() );

        call.get();

        assertFalse( DefaultValidation_Correct.valPerformed );
    }

    /**
     * Default validation can be disabled via global setting in *.properties
     * file.
     * 
     * @throws Throwable
     */
    @Test
    public void disableGlobally() throws Throwable
    {
        // Disable default validation globally via property
        XltProperties.getInstance().setProperty( GLOB_PROP, FALSE );

        // Perform call
        RESTCall call = new RESTCall( DefaultValidation_Correct.class ).setPreviousAction( mockAction );

        assertFalse( "Default validation should be disabled after using setter.", call.isDefaultValidationEnabled() );

        call.get();

        assertFalse( DefaultValidation_Correct.valPerformed );
    }

    /**
     * Override of global setting by {@link RESTCall#defaultValidation(boolean)}
     * .
     * 
     * @throws Throwable
     */
    @Test
    public void overrideGlobalSettingByRESTCall() throws Throwable
    {
        // Disable default validation globally via property
        XltProperties.getInstance().setProperty( GLOB_PROP, FALSE );

        // Perform call
        RESTCall call = new RESTCall( DefaultValidation_Correct.class ).setPreviousAction( mockAction );

        // Override global setting on RESTCall level
        assertFalse( "Default validation should be disabled after using setter.", call.isDefaultValidationEnabled() );
        call.defaultValidation( true );
        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );

        call.get();

        assertTrue( "DefaultValidation_Correct: not performed.", DefaultValidation_Correct.valPerformed );
    }

    /**
     * Default validation can be disabled for a single resource definition only.
     * All other definitions should have their default validation enabled.
     * 
     * @throws Throwable
     */
    @Test
    public void disableByResourceDef() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_Correct.class, DefaultValidation_Disabled.class )
                .setPreviousAction( mockAction );

        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );

        call.get();

        assertTrue( "DefaultValidation_Correct: not performed.", DefaultValidation_Correct.valPerformed );
        assertFalse( "DefaultValidation_Disabled: performed.", DefaultValidation_Disabled.valPerformed );
    }

    /**
     * Disabled default validation in resource definition cannot be overridden
     * by {@link RESTCall#defaultValidation(boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void disableByResourceDefNoOverrideByRESTCall() throws Throwable
    {
        // Disable default validation globally via property
        XltProperties.getInstance().setProperty( GLOB_PROP, FALSE );

        // Perform call
        RESTCall call = new RESTCall( DefaultValidation_Correct.class, DefaultValidation_Disabled.class )
                .setPreviousAction( mockAction );

        // Override global setting on RESTCall level
        assertFalse( "Default validation should be disabled.", call.isDefaultValidationEnabled() );
        call.defaultValidation( true );
        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );

        call.get();

        assertTrue( "DefaultValidation_Correct: not performed.", DefaultValidation_Correct.valPerformed );
        assertFalse( "DefaultValidation_Disabled: performed.", DefaultValidation_Disabled.valPerformed );
    }

    /**
     * A disabled default validation at resource definition level can be enabled
     * via {@link RESTCall#RESTCall(Class, boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void enableDisabledResourceDefByRESTCall_Constructor() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_Disabled.class, true ).setDefinitionClass(
                DefaultValidation_Disabled2nd.class ).setPreviousAction( mockAction );

        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );

        call.get();

        assertTrue( "DefaultValidation_Disabled: not performed.", DefaultValidation_Disabled.valPerformed );
        assertFalse( "DefaultValidation_Disabled2nd: performed.", DefaultValidation_Disabled2nd.valPerformed );
    }

    /**
     * A disabled default validation at resource definition level can be enabled
     * via {@link RESTCall#setDefinitionClass(Class, boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void enableDisabledResourceDefByRESTCall_Method() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_Disabled2nd.class ).setPreviousAction( mockAction );

        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );

        call.setDefinitionClass( DefaultValidation_Disabled.class, true ).get();

        assertTrue( "DefaultValidation_Disabled: not performed.", DefaultValidation_Disabled.valPerformed );
        assertFalse( "DefaultValidation_Disabled2nd: performed.", DefaultValidation_Disabled2nd.valPerformed );
    }

    /**
     * A disabled default validation at resource definition level can be enabled
     * via {@link RESTCall#defaultValidation(Class, boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void enableDisabledResourceDefByRESTCall_Method2() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_Disabled.class, DefaultValidation_Disabled2nd.class )
                .setPreviousAction( mockAction );

        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );

        call.defaultValidation( DefaultValidation_Disabled.class, true ).get();

        assertTrue( "DefaultValidation_Disabled: not performed.", DefaultValidation_Disabled.valPerformed );
        assertFalse( "DefaultValidation_Disabled2nd: performed.", DefaultValidation_Disabled2nd.valPerformed );
    }

    /**
     * An enabled default validation at resource definition level can be
     * disabled via {@link RESTCall#RESTCall(Class, boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void disableEnabledResourceDefByRESTCall_Constructor() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_Correct.class, false ).setDefinitionClass(
                DefaultValidation_CorrectStatusCode.class ).setPreviousAction( mockAction );

        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );

        call.get();

        assertFalse( "DefaultValidation_Correct: not performed.", DefaultValidation_Correct.valPerformed );
        assertEquals( "DefaultValidation_Disabled: performed.", DefaultValidation_CorrectStatusCode.expValStatusCode
                + STATUS_CODE, DefaultValidation_CorrectStatusCode.valStatusCode );
    }

    /**
     * An enabled default validation at resource definition level can be
     * disabled via {@link RESTCall#setDefinitionClass(Class, boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void disableEnabledResourceDefByRESTCall_Method() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_CorrectStatusCode.class ).setPreviousAction( mockAction );

        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );

        call.setDefinitionClass( DefaultValidation_Correct.class, false ).get();

        assertFalse( "DefaultValidation_Correct: not performed.", DefaultValidation_Correct.valPerformed );
        assertEquals( "DefaultValidation_Disabled: performed.", DefaultValidation_CorrectStatusCode.expValStatusCode
                + STATUS_CODE, DefaultValidation_CorrectStatusCode.valStatusCode );
    }

    /**
     * An enabled default validation at resource definition level can be
     * disabled via {@link RESTCall#defaultValidation(Class, boolean)}.
     * 
     * @throws Throwable
     */
    @Test
    public void disableEnabledResourceDefByRESTCall_Method2() throws Throwable
    {
        RESTCall call = new RESTCall( DefaultValidation_Correct.class, DefaultValidation_CorrectStatusCode.class )
                .setPreviousAction( mockAction );

        assertTrue( "Default validation should be enabled by default.", call.isDefaultValidationEnabled() );

        call.defaultValidation( DefaultValidation_Correct.class, false ).get();

        assertFalse( "DefaultValidation_Correct: not performed.", DefaultValidation_Correct.valPerformed );
        assertEquals( "DefaultValidation_Disabled: performed.", DefaultValidation_CorrectStatusCode.expValStatusCode
                + STATUS_CODE, DefaultValidation_CorrectStatusCode.valStatusCode );
    }
}
