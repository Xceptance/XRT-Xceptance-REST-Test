package com.xceptance.xrt;

import junit.framework.Assert;

import org.junit.Test;

/**
 * <p>
 * Unit tests for the method {@link RESTCall#splitUrl(String) }. This method can
 * only be tested indirectly by getting the Url after initializing a REST call.
 * </p>
 * <br/>
 * <p>
 * {@link RESTCall} manages all components of an Url individually. That allows
 * adjusting each component individually. But that also means that an Url needs
 * to be split into its components. The splitting needs to be tested carefully.
 * </p>
 * 
 * @author Patrick Thaele
 * 
 */
public class TestUrlSplitter
{
    /**
     * Initiates a REST call with an Url that has the maximum complexity. The
     * <b>getUrl()</b> method should return the same Url as entered.
     */
    @Test
    public void splitComplexUrl()
    {
        String url = "https://my.url.test.com:8080/base/path/resource?param1=foo&param2=bar#fragment";
        RESTCall call = new RESTCall( url );

        Assert.assertEquals( "Expected Url: ", url, call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and multiple query parameter
     * separators '?'. Only one separator is allowed so removed the other.
     */
    @Test
    public void splitUrl_MultipleSlashes()
    {
        RESTCall call = new RESTCall(
                "https://my.url.test.com:8080///base//path//resource?param1=foo&param2=bar#fragment" );

        Assert.assertEquals( "Expected Url: ",
                "https://my.url.test.com:8080/base/path/resource?param1=foo&param2=bar#fragment", call.getUrl() );
    }

    /**
     * Initiates a REST call with the protocol only.
     */
    @Test
    public void splitUrl_protocolOnly()
    {
        RESTCall call = new RESTCall( "https://" );

        Assert.assertNull( "Expected null due to missing host name.", call.getUrl() );
    }

    /**
     * Initiates a REST call with the protocol separator '://' only.
     */
    @Test
    public void splitUrl_protocolSeparatorOnly()
    {
        RESTCall call = new RESTCall( "://" );

        Assert.assertNull( "Expected null due to missing host name.", call.getUrl() );
    }

    /**
     * Initiates a REST call with the protocol separator '://' and host name
     * only.
     */
    @Test
    public void splitUrl_hostNameAndProtocolSeparator()
    {
        RESTCall call = new RESTCall( "://my.url.test.com" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com", call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name only.
     */
    @Test
    public void splitUrl_hostNameOnly()
    {
        String url = "my.url.test.com";
        RESTCall call = new RESTCall( url );

        Assert.assertEquals( "Expected Url: ", url, call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name, protocol, and port.
     */
    @Test
    public void splitUrl_hostNameWithProtocolAndPort()
    {
        String url = "https://my.url.test.com:8080";
        RESTCall call = new RESTCall( url );

        Assert.assertEquals( "Expected Url: ", url, call.getUrl() );
    }

    /**
     * Initiates a REST call with a port that is alphabetical.
     */
    @Test
    public void splitUrl_PortAsString()
    {
        RESTCall call = new RESTCall( "my.url.test.com:abc" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com", call.getUrl() );
    }

    /**
     * Initiates a REST call with a missing port but with the port separator
     * ':'.
     */
    @Test
    public void splitUrl_MissingPort()
    {
        RESTCall call = new RESTCall( "my.url.test.com:" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com", call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and an appended '/'.
     */
    @Test
    public void splitUrl_hostName_AppendedSlash()
    {
        RESTCall call = new RESTCall( "my.url.test.com/" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com", call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and query parameters.
     */
    @Test
    public void splitUrl_hostNameAndQueryParameters()
    {
        String url = "my.url.test.com?param1=foo&param2=bar";
        RESTCall call = new RESTCall( url );

        Assert.assertEquals( "Expected Url: ", url, call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and query parameters. The query
     * parameters contain several '&' characters. After the splitter they got
     * removed.
     */
    @Test
    public void splitUrl_queryParametersWithMultipleAmpersands()
    {
        RESTCall call = new RESTCall( "my.url.test.com?param1=foo&&param2=bar" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com?param1=foo&param2=bar", call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and query parameters. One of the
     * query parameters has no value.
     */
    @Test
    public void splitUrl_queryParameterWithMissingValue()
    {
        String url = "my.url.test.com?param1=&param2=bar";
        RESTCall call = new RESTCall( url );

        Assert.assertEquals( "Expected Url: ", url, call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and query parameters. One of the
     * query parameters has no key. The value will be ignored
     */
    @Test
    public void splitUrl_queryParameterWithMissingKey()
    {
        RESTCall call = new RESTCall( "my.url.test.com?=foo&param2=bar" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com?param2=bar", call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and the query parameter
     * separator '?' only. The separator should be removed.
     */
    @Test
    public void splitUrl_hostNameAndQueryParameterSeparator()
    {
        RESTCall call = new RESTCall( "my.url.test.com?" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com", call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and multiple query parameter
     * separators '?'. Only one separator is allowed so removed the other.
     */
    @Test
    public void splitUrl_MultipleQueryParameterSeparators()
    {
        RESTCall call = new RESTCall( "my.url.test.com??param1=foo&param2=bar" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com?param1=foo&param2=bar", call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and a fragment.
     */
    @Test
    public void splitUrl_hostNameAndFragment()
    {
        String url = "my.url.test.com#fragment";
        RESTCall call = new RESTCall( url );

        Assert.assertEquals( "Expected Url: ", url, call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and the fragment separator '#'
     * only. The separator should be removed.
     */
    @Test
    public void splitUrl_hostNameAndFragmentSeparator()
    {
        RESTCall call = new RESTCall( "my.url.test.com#" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com", call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and two fragments. Only one is
     * allowed so only take the first one and ignore the rest.
     */
    @Test
    public void splitUrl_SeveralFragments()
    {
        RESTCall call = new RESTCall( "my.url.test.com/resource?q=search#fragment1#fragment2" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com/resource?q=search#fragment1", call.getUrl() );
    }

    /**
     * Initiates a REST call with the host name and two fragment separators.
     * Only one is allowed so only take the last one and ignore the rest.
     */
    @Test
    public void splitUrl_MultipleFragmentSeparators()
    {
        RESTCall call = new RESTCall( "my.url.test.com/resource?q=search##fragment" );

        Assert.assertEquals( "Expected Url: ", "my.url.test.com/resource?q=search#fragment", call.getUrl() );
    }
}
