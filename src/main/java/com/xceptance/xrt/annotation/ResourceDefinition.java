package com.xceptance.xrt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation that defines the default Url to call a REST resource. The
 * following example shows which settings can be done to create a REST URL:
 * </p>
 * <br>
 * 
 * <pre>
 * {@code
 * {baseUrl}/{basePath}/{resourcePath}?{queryParam1}&{queryParam2}#{fragment}
 * }
 * </pre>
 * <p>
 * A concrete example could look like this:
 * </p>
 * 
 * <pre>
 * {@code
 * https://valid.host.com:433/base/path/resource?foo=bar&foo2=bar2#fragment;
 * }
 * </pre>
 * 
 * @author Patrick Thaele
 * 
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface ResourceDefinition
{
    /**
     * The default value of the used action name. This value appears in the XLT
     * load test report once the REST call was performed.
     * 
     * @return The default action name used by the REST resource.
     */
    String actionName() default "";

    /**
     * The default value of the used protocol. This value overrides the protocol
     * of the {@link #baseUrl() baseUrl}.
     * 
     * @return The default protocol used by the REST resource.
     */
    String protocol() default "";

    /**
     * The default value of the used port. This value overrides the port of the
     * {@link #baseUrl() baseUrl}.
     * 
     * @return The default port used by the REST resource.
     */
    int port() default -1;

    /**
     * <p>
     * The default value of the base URL containing the host name and the
     * optional values for the protocol and port.
     * </p>
     * <p>
     * Here is an example of a valid base Url:
     * </p>
     * 
     * <pre>
     * {@code
     * http://valid.url.com:80
     * }
     * </pre>
     * 
     * <br>
     * 
     * <p>
     * Since <b>http</b> and <b>port 80</b> are the default values ( if nothing
     * else is specified globally ) the following Url would have the same
     * effect:
     * </p>
     * 
     * <pre>
     * {@code
     * valid.url.com
     * }
     * </pre>
     * 
     * <br>
     * 
     * @return The default base Url of the resource.
     */
    String baseUrl() default "";

    /**
     * The default value for the part of URL path between the host name and the
     * resource path. Leading and trailing slashes are not necessary. Here is an
     * example: <b>base/path</b>.
     * 
     * @return The default Url base path of the REST resource.
     */
    String basePath() default "";

    /**
     * The default value for the part of the URL path that refers to the REST
     * resource. A leading slash is not necessary.
     * 
     * @return The default resource path of the REST resource.
     */
    String resourcePath() default "";

    /**
     * Defines the default query parameters of the REST resource.
     * 
     * @return An array of the default query parameters.
     */
    QueryParameter[] queryParams() default {};

    /**
     * The default URL fragment. It is the optional element at the end of an URL
     * that is added by appending a '#' sign followed by the fragment. A leading
     * '#' character is not necessary.
     * 
     * @return The default URL fragment of the rest resource.
     */
    String fragment() default "";
}
