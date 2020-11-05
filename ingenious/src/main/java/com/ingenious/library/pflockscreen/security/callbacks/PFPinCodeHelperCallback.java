package com.ingenious.library.pflockscreen.security.callbacks;

import com.ingenious.library.pflockscreen.security.PFResult;

public interface PFPinCodeHelperCallback<T> {
    void onResult(PFResult<T> result);
}
