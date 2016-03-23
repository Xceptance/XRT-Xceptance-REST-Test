package com.xceptance.xrt.validation.preprocess;

import com.xceptance.xrt.PreProcessible;
import com.xceptance.xrt.RESTCall;

/**
 *
 *
 * Created by patrick on 3/21/16.
 */
public class DummyTokenProvider implements PreProcessible{

    private String token = "somerandomtoken";

    private final long creationTime;

    public DummyTokenProvider() {
        creationTime = System.currentTimeMillis();
    }

    public void preProcess(final RESTCall call){
        call.addHttpHeader("Authentication", "Bearer " + token + creationTime);
    }
}
