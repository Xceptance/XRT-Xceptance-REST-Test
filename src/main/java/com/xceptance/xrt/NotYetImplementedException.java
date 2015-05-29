package com.xceptance.xrt;

/**
 * Exception that is thrown if something is planned to be implemented but is not
 * ready yet to be used, e.g. when waiting for a 3rd-party update.
 * 
 * @author Patrick Thaele
 * 
 */
public class NotYetImplementedException extends RuntimeException
{

    /**
     * Serial Number
     */
    private static final long serialVersionUID = -1614171947654702788L;

    /**
     * Private field containing the message of the exception.
     */
    private String message;

    /**
     * Constructor.
     * 
     * @param message
     *            The message of the exception.
     */
    public NotYetImplementedException( String message )
    {
        this.message = message;
    }

    /**
     * Get the message of the exception.
     */
    @Override
    public String getMessage()
    {
        return message;
    }
}
