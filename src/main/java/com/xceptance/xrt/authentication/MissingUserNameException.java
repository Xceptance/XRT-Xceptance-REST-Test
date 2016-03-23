package com.xceptance.xrt.authentication;

/**
 * Exception thrown when a username was missing.
 *
 * Created by patrick on 3/23/16.
 */
public class MissingUserNameException extends RuntimeException
{
    public MissingUserNameException()
    {
        super( "Couldn't create object. Missing username." );
    }
}
