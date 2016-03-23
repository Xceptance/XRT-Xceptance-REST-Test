package com.xceptance.xrt.validation.preprocess;

import com.xceptance.xrt.PreProcessible;
import com.xceptance.xrt.RESTCall;

/**
 * Created by patrick on 3/23/16.
 */
public class DefinitionPreProcessor implements PreProcessible
{
    @Override
    public void preProcess( RESTCall call )
    {
        new DummyTokenProvider().preProcess( call );
    }
}
