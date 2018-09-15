package com.engineerskasa.smswatch.Remote;

import com.engineerskasa.smswatch.Interface.Services;

public class APIUtils {
    public APIUtils() {
    }

    public static final String BASE_URL = "http://jsonplaceholder.typicode.com/";

    public static Services getAPIService(){
        return RetrofitBuilder.getClient(BASE_URL).create(Services.class);
    }
}
