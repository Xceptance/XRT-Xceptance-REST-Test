package com.xceptance.xrt;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.actions.AbstractLightWeightPageAction;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * 
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
    
    public XltRESTAction( RESTCall restCall )
    {
        // TODO Action needs a name.
        super( "" );
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
        WebRequest request = new WebRequest( new URL(restCall.getUrl()), restCall.getHttpMethod() );
        request.setAdditionalHeaders( restCall.getHttpHeaders() );
        
        // TODO Set POST document.
        
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
