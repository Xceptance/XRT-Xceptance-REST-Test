package com.xceptance.xrt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that allows to disable default validation at resource
 * definition level.
 * 
 * @author Patrick Thaele
 * 
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface DisableDefaultValidation
{
}
