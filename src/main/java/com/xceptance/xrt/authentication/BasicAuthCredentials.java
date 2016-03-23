package com.xceptance.xrt.authentication;

/**
 * POJO for basic authentication credentials.
 * <p>
 * Created by patrick on 3/23/16.
 */
public class BasicAuthCredentials
{
    /**
     * The user name used for basic authentication.
     */
    private String username;

    /**
     * The password used for basic authentication.
     */
    private String password = "";

    /**
     * Constructor that requires a username. The password can be an empty string or <b>null</b>.
     *
     * @param username
     *         Required field. Empty strings or <b>null</b> throws a {@link MissingUserNameException}.
     * @param password
     *         The password is optional.
     *
     * @throws MissingUserNameException
     *         Thrown if no username was provided.
     */
    public BasicAuthCredentials( final String username, final String password )
    {
        setUsername( username );
        setPassword( password );
    }

    /**
     * Returns the configured username.
     *
     * @return The username.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Updates the username for basic authentication.
     *
     * @param username
     *         Required field. Empty strings or <b>null</b> throws a {@link MissingUserNameException}.
     *
     * @throws MissingUserNameException
     *         Thrown if no username was provided.
     */
    public void setUsername( String username )
    {
        // A username is required
        if ( username == null || "".equals( username ) )
            throw new MissingUserNameException();

        this.username = username;
    }

    /**
     * Returns the configured password. If no password was provided this method returns an empty string.
     *
     * @return The password. Cannot be null.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Updates the password for basic authentication. <b>null</b> will be converted into an empty string.
     *
     * @param password
     *         The password.
     */
    public void setPassword( String password )
    {
        if ( password != null )
            this.password = password;
    }
}
