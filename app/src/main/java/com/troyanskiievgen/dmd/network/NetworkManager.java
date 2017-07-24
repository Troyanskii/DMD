package com.troyanskiievgen.dmd.network;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.troyanskiievgen.dmd.network.ConstantsAPI.BASE_URL;

/**
 * Created by Relax on 24.07.2017.
 */

public class NetworkManager {

    /*timeout values in seconds*/
    private static final int CONNECTION_TIMEOUT = 5;
    private static final int WRITE_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 10;

    private static RESTService restService;

    public static RESTService getRESTService() {
        if (restService == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    // TODO: 24.07.2017 remove interceptor if logs no need anymore or setup Flavors for relise version
                    .addInterceptor(new RESTInterceptor())
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            restService = retrofit.create(RESTService.class);
        }
        return restService;
    }

    private static class RESTInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Buffer buffer = new Buffer();
            if (request.body() != null)
                request.body().writeTo(buffer);

            Log.i("HTTP Request", "Request to " + request.url().toString()
                    + "\n" + request.headers().toString()
                    + "\n" + buffer.readUtf8());

            long t1 = System.nanoTime();
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            String msg = response.body().string();
            msg = msg.replace("\r", ""); // Note: Messages with '\r' not displayed correctly in logcat

            Log.i("HTTP Response", String.format("Response from %s in %.1fms%n\n%s",
                    response.request().url().toString(), (t2 - t1) / 1e6d, msg));

            Log.i("HTTP Response", "Response code = " + response.code());

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), msg))
                    .build();
        }
    }
}
