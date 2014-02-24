package com.xceptance.xrt;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xrt.validation.DefaultValidation_Correct;
import com.xceptance.xrt.validation.DefaultValidation_CorrectStatusCode;
import com.xceptance.xrt.validation.DefaultValidation_DerivedMethod;
import com.xceptance.xrt.validation.DefaultValidation_DifferentReturnType;
import com.xceptance.xrt.validation.DefaultValidation_IncorrectArgument;
import com.xceptance.xrt.validation.DefaultValidation_IncorrectNoOfArguments;
import com.xceptance.xrt.validation.DefaultValidation_NonStaticMethod;
import com.xceptance.xrt.validation.DefaultValidation_PrivateMethod;
import com.xceptance.xrt.validation.DefaultValidation_ProtectedMethod;

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
    private final static String RESPONSE_BODY = "{\"id\":\"test\"}";

    /**
     * The mocked response status code.
     */
    private final static int STATUS_CODE = 200;

    /**
     * The mocked response status message.
     */
    private final static String STATUS_MESSAGE = "OK";

    /**
     * The mocked response content type.
     */
    private final static String CONTENT_TYPE = "application/json";

    /**
     * The mocked response HTTP headers.
     */
    private final static List<NameValuePair> HTTP_HEADERS = new ArrayList<>();

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

    /****************************************************************************************
     ************************ Default Validation Tests **************************************
     ****************************************************************************************/

    /**
     * The message of the default validation classes when the method was not
     * called.
     */
    private final static String VALIDATOR_NOT_CALLED_MSG = "Not yet executed.";

    /**
     * <p>
     * Validates if the default validation methods are called. These are the
     * static methods that can be implemented and are called via reflection:
     * </p>
     * <ul>
     * <li><i><b>public static void validateStatusCode( int )</b></i></li>
     * </ul>
     * 
     * @throws Throwable
     */
    @Test
    public void defaultValidation() throws Throwable
    {
        // Reset validation status code
        DefaultValidation_Correct.valStatusCode = VALIDATOR_NOT_CALLED_MSG;

        // Perform test REST call
        new RESTCall( DefaultValidation_Correct.class ).setPreviousAction( mockAction ).get();

        Assert.assertEquals( "Status code validation: ", DefaultValidation_Correct.expValStatusCode + STATUS_CODE,
                DefaultValidation_Correct.valStatusCode );
        Assert.assertEquals( "HTTP headers validation: ", DefaultValidation_Correct.expValHTTPHeaders
                + HTTP_HEADERS.get( 0 ).toString(), DefaultValidation_Correct.valHTTPHeaders );
        Assert.assertEquals( "Response body validation(String): ", DefaultValidation_Correct.expValBodyString
                + RESPONSE_BODY, DefaultValidation_Correct.valBodyString );
        Assert.assertEquals( "Response body validation(JSON): ", DefaultValidation_Correct.expValBodyJson,
                DefaultValidation_Correct.valBodyJson );
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

        Assert.assertEquals( DefaultValidation_Correct.expValStatusCode + STATUS_CODE,
                DefaultValidation_Correct.valStatusCode );
        Assert.assertEquals( DefaultValidation_CorrectStatusCode.expValStatusCode + STATUS_CODE,
                DefaultValidation_CorrectStatusCode.valStatusCode );
    }

    /**
     * All validation methods must be static because the definition classes
     * should be abstract and cannot be instantiated. Non static methods are
     * ignored (debug log entry is created).
     * 
     * @throws Throwable
     */
    @Test
    public void nonStaticMethodsAreNotCalled() throws Throwable
    {
        new RESTCall( DefaultValidation_NonStaticMethod.class ).setPreviousAction( mockAction ).get();

        Assert.assertEquals( VALIDATOR_NOT_CALLED_MSG, DefaultValidation_NonStaticMethod.valStatusCode );
    }

    /**
     * The access modifier for the validation methods must be public. Private
     * methods are not called.
     * 
     * @throws Throwable
     */
    @Test
    public void privateMethods() throws Throwable
    {
        new RESTCall( DefaultValidation_PrivateMethod.class ).setPreviousAction( mockAction ).get();

        Assert.assertEquals( VALIDATOR_NOT_CALLED_MSG, DefaultValidation_PrivateMethod.valStatusCode );
    }

    /**
     * The access modifier for the validation methods must be public. Protected
     * methods are not called.
     * 
     * @throws Throwable
     */
    @Test
    public void protectedMethods() throws Throwable
    {
        new RESTCall( DefaultValidation_ProtectedMethod.class ).setPreviousAction( mockAction ).get();

        Assert.assertEquals( VALIDATOR_NOT_CALLED_MSG, DefaultValidation_ProtectedMethod.valStatusCode );
    }

    /**
     * To have less redundancy it is allowed to derive the static methods from a
     * super class.
     * 
     * @throws Throwable
     */
    @Test
    public void derivedMethods() throws Throwable
    {
        // Reset validation status code
        DefaultValidation_Correct.valStatusCode = VALIDATOR_NOT_CALLED_MSG;

        // Perform test REST call
        new RESTCall( DefaultValidation_DerivedMethod.class ).setPreviousAction( mockAction ).get();

        Assert.assertEquals( DefaultValidation_DerivedMethod.expValStatusCode + STATUS_CODE,
                DefaultValidation_DerivedMethod.valStatusCode );
    }

    /**
     * Only validation methods with the correct set of method parameters are
     * executed. Other methods are ignored.
     * 
     * @throws Throwable
     */
    @Test
    public void methodHasIncorrectArgType() throws Throwable
    {
        new RESTCall( DefaultValidation_IncorrectArgument.class ).setPreviousAction( mockAction ).get();

        Assert.assertEquals( VALIDATOR_NOT_CALLED_MSG, DefaultValidation_IncorrectArgument.valStatusCode );
    }

    /**
     * Only validation methods with the correct set of method parameters are
     * executed. Other methods are ignored.
     * 
     * @throws Throwable
     */
    @Test
    public void methodHasIncorrectNumberOfArgs() throws Throwable
    {
        new RESTCall( DefaultValidation_IncorrectNoOfArguments.class ).setPreviousAction( mockAction ).get();

        Assert.assertEquals( VALIDATOR_NOT_CALLED_MSG, DefaultValidation_IncorrectNoOfArguments.valStatusCode );
    }

    /**
     * The return type of validation methods is not limited to the <b>VOID</b>
     * type by design. The static methods of a definition class can also be
     * called outside of the RESTCall class.
     * 
     * @throws Throwable
     */
    @Test
    public void returnTypeOfMethodDoesntMatter() throws Throwable
    {
        new RESTCall( DefaultValidation_DifferentReturnType.class ).setPreviousAction( mockAction ).get();

        Assert.assertEquals( DefaultValidation_DifferentReturnType.expValStatusCode + STATUS_CODE,
                DefaultValidation_DifferentReturnType.valStatusCode );
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
        // Reset validation status code
        DefaultValidation_Correct.valStatusCode = VALIDATOR_NOT_CALLED_MSG;

        // Perform test REST call
        new RESTCall( DefaultValidation_Correct.class ).defaultValidation( true ).setPreviousAction( mockAction ).get();

        Assert.assertEquals( DefaultValidation_Correct.expValStatusCode + STATUS_CODE,
                DefaultValidation_Correct.valStatusCode );
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
        // Reset validation status code
        DefaultValidation_Correct.valStatusCode = VALIDATOR_NOT_CALLED_MSG;

        // Perform test REST call
        new RESTCall( DefaultValidation_Correct.class ).defaultValidation( false ).setPreviousAction( mockAction )
                .get();

        Assert.assertEquals( VALIDATOR_NOT_CALLED_MSG, DefaultValidation_Correct.valStatusCode );
    }
}
