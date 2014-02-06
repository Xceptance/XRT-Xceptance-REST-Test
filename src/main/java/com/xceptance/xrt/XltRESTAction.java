package com.xceptance.xrt;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.actions.AbstractLightWeightPageAction;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.engine.XltWebClient;

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
        super( restCall.getActionName() );
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
        // Setup the request.
        WebRequest request = new WebRequest( new URL( restCall.getUrl() ), restCall.getHttpMethod() );
        request.setAdditionalHeaders( restCall.getHttpHeaders() );

        // Set request body.
        if ( restCall.hasRequestBody() )
            request.setRequestBody( restCall.getRequestBody() );

        // Make the call.
        LightWeightPage page = ( (XltWebClient) getWebClient() ).getLightWeightPage( request );
        setLightWeightPage( page );

        // Store the response.
        restCall.setRESTResponse( page.getWebResponse() );
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
