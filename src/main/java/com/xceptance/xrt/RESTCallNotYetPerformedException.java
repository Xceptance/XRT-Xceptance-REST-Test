package com.xceptance.xrt;

/**
 * Thrown if RESTCall did not perform the call but response information is
 * already requested.
 * 
 * @author Patrick Thaele
 * 
 */
public class RESTCallNotYetPerformedException extends RuntimeException
{
    private final String methodName;

    /**
     * Constructor that incorporates the name of the throwing method into the
     * exception message.
     * 
     * @param methodName
     *            The name of the method that is incorporated into the exception
     *            message.
     */
    public RESTCallNotYetPerformedException( String methodName )
    {
        this.methodName = methodName;
    }

    /**
     * Generated serial version.
     */
    private static final long serialVersionUID = 7824431944860264792L;

    @Override
    public String getMessage()
    {
        if ( methodName != null && !methodName.isEmpty() )
            return "RESTCall did not process the request yet. Therefore the method 'com.xceptance.xrt.RESTCall#" + methodName
                    + "' is not available for further processing.";
        else
            return "RESTCall did not process the request yet. Therefore the response methods are not available for further processing.";
    }

}
