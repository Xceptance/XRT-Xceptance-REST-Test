package com.xceptance.xrt.annotation;

import com.xceptance.xrt.PreProcessible;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A class provided in this annotation is instantiated by XRT. With <b>reuseInstance</b> this instance is stored for the
 * whole test case. If <b>reuseInstance</b> is false, XRT instantiates a new object with every REST call.
 *
 * @author Patrick Thaele
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.TYPE } )
public @interface PreProcess
{
    /**
     * The class that is used to perform the pre-processing.
     *
     * @return The class that is used to perform the pre-processing.
     */
    Class<? extends PreProcessible> value();

    /**
     * With <b>reuseInstance</b> this instance is stored for the
     * whole test case. If <b>reuseInstance</b> is false, XRT instantiates a new object with every REST call.
     *
     * @return <b>true</b> if the instance used for pre-processing is used in the whole test, <b>false</b> if not.
     */
    boolean reuseInstance() default false;
}
