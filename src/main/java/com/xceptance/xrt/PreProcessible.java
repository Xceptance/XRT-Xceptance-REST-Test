package com.xceptance.xrt;

/**
 * The interface of the class interpreted by the pre-processor.
 * 
 * @author Patrick Thaele
 * 
 */
public interface PreProcessible
{
    /**
     * Method that is called by XRT during pre-processing.
     *
     * @param call
     *          The RESTCall instance used for manipulation by the pre-processing step.
     */
    void preProcess( final RESTCall call );
}
