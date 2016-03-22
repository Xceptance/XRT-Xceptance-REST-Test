package com.xceptance.xrt.validation.preprocess;

import com.xceptance.xrt.PreProcessible;
import com.xceptance.xrt.RESTCall;

import java.time.Instant;

/**
 *
 *
 * Created by patrick on 3/21/16.
 */
public class DummyTokenProvider implements PreProcessible{

    private String token = "somerandomtoken";

    private final Instant creationTime;

    public DummyTokenProvider() {
        creationTime = Instant.now();
    }

    public void preProcess(final RESTCall call){
        call.addHttpHeader("Authentication", "Bearer " + token + creationTime);
    }
}
