package com.xceptance.xrt;

/**
 * Implement this interface in a resource definition to activate default
 * validation after your REST call. Because the {@link RESTCall} object creates
 * an instance of the implementing class to trigger the
 * {@link #validate(RESTCallValidator)} method there need to be a public default
 * constructor (not private).
 * 
 * @author Patrick Thaele
 * 
 */
public interface AutoValidatable
{
    /**
     * This method is called by the {@link RESTCall} instance after the call was
     * processed successfully.
     * 
     * @param call
     *            The callback of the REST call.
     */
    void validate( RESTCallValidator call );
}
