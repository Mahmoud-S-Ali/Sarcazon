package com.joyapeak.sarcazon.data.network.retrofit;

/**
 * Created by Mahmoud Ali on 4/18/2018.
 *
 * General result with every api request
 */

public interface ServerResult {
    void onSuccess(Object responseBody, int responseCode);
    void onFailure(Throwable t);
}
