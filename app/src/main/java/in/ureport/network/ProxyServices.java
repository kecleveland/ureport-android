package in.ureport.network;

import android.content.Context;

import in.ureport.BuildConfig;
import in.ureport.R;
import in.ureport.models.ip.ProxyResponse;
import retrofit.RestAdapter;

/**
 * Created by johncordeiro on 26/10/15.
 */
public class ProxyServices {

    private final String authorization;
    private final ProxyApi proxyApi;

    public ProxyServices(Context context) {
        authorization = context.getString(R.string.proxy_api_key);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.proxy_url))
                .build();

        if(BuildConfig.DEBUG) restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        proxyApi = restAdapter.create(ProxyApi.class);
    }

    public ProxyResponse getAuthenticationTokenByCountry(String country) {
        return proxyApi.getAuthenticationToken(authorization, country);
    }
}
