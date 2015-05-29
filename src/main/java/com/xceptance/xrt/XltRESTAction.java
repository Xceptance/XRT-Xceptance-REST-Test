package com.xceptance.xrt;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.actions.AbstractLightWeightPageAction;
import com.xceptance.xlt.api.util.XltLogger;

/**
 * This class performs the REST call as configured in the REST call instance.
 * 
 * @author Patrick Thaele
 * 
 */
public class XltRESTAction extends AbstractLightWeightPageAction
{
    /**
     * The settings for the REST call.
     */
    private final RESTCall restCall;

    /**
     * Constructor with minimum settings.
     * 
     * @param restCall
     *            The instance that provides all settings for the REST call.
     */
    public XltRESTAction( RESTCall restCall )
    {
        super( restCall.getPreviousAction(), restCall.getActionName() );
        this.restCall = restCall;
    }

    /**
     * Pre-validation step before every REST call.
     * 
     * @see com.xceptance.xlt.api.actions.AbstractAction#preValidate()
     */
    @Override
    public void preValidate() throws Exception
    {
        // Nothing to pre-validate
    }

    /**
     * Makes the REST call and saves the response.
     * 
     * @see com.xceptance.xlt.api.actions.AbstractAction#execute()
     */
    @Override
    protected void execute() throws Exception
    {
        // Call it once for debugging and execution for better performance.
        String url = restCall.getUrl();

        // DEBUGGING - log URL, HTTP method, and HTTP headers
        XltLogger.runTimeLogger.debug( "Start REST call..." );
        XltLogger.runTimeLogger.debug( "# Request - URL:\t\t" + url );
        XltLogger.runTimeLogger.debug( "# Request - HTTP method:\t" + restCall.getHttpMethod() );
        XltLogger.runTimeLogger.debug( "# Request - HTTP headers:\t" + restCall.getHttpHeaders().toString() );

        // Setup the request.
        WebRequest request = new WebRequest( new URL( url ), restCall.getHttpMethod() );
        request.setAdditionalHeaders( restCall.getHttpHeaders() );

        // Set request body.
        if ( restCall.hasRequestBody() )
        {
            // Call it once for debugging and execution for better performance.
            String requestBody = restCall.getRequestBody();

            // DEBUGGING - log request body
            XltLogger.runTimeLogger.debug( "# Request - Body:\t" + requestBody );

            request.setRequestBody( requestBody );
        }
        
        // Avoid caching of REST documents
        request.setDocumentRequest();

        // Make the call and store the response and previous action.
        WebResponse response = getWebClient().loadWebResponse( request );
        restCall.setRESTResponse( response );
        restCall.setPreviousAction( this );

        // DEBUGGING - log response code, response HTTP headers, and response
        // body
        XltLogger.runTimeLogger.debug( "Getting response..." );
        XltLogger.runTimeLogger.debug( "# Response - Status code:\t" + response.getStatusCode() );
        XltLogger.runTimeLogger.debug( "# Response - HTTP header:\t" + response.getResponseHeaders().toString() );
        XltLogger.runTimeLogger.debug( "# Response - Body:\t" + response.getContentAsString() );
    }

    /**
     * Post-validation step after every REST call.
     * 
     * @see com.xceptance.xlt.api.actions.AbstractAction#postValidate()
     */
    @Override
    protected void postValidate() throws Exception
    {
        // Nothing to post-validate

    }
}
