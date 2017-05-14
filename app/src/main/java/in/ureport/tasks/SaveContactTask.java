package in.ureport.tasks;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import in.ureport.R;
import in.ureport.flowrunner.models.Contact;
import in.ureport.helpers.AnalyticsHelper;
import in.ureport.helpers.ContactBuilder;
import in.ureport.helpers.IOHelper;
import in.ureport.helpers.SntpClient;
import in.ureport.managers.CountryProgramManager;
import in.ureport.managers.UserManager;
import in.ureport.models.CountryProgram;
import in.ureport.models.User;
import in.ureport.models.geonames.CountryInfo;
import in.ureport.models.ip.ProxyResponse;
import in.ureport.models.rapidpro.Field;
import in.ureport.network.ProxyServices;
import in.ureport.network.RapidProServices;
import in.ureport.tasks.common.ProgressTask;
import io.rapidpro.sdk.FcmClient;
import io.rapidpro.sdk.UiConfiguration;
import retrofit.RetrofitError;

/**
 * Created by johncordeiro on 18/08/15.
 */
public class SaveContactTask extends ProgressTask<User, Void, Contact> {

    private static final String TAG = "SaveContactTask";

//    private RapidProServices rapidProServices;

    private CountryInfo countryInfo;
    private final boolean newUser;

    public SaveContactTask(Context context, CountryInfo countryInfo, boolean newUser) {
        super(context, R.string.load_message_save_user);
        this.countryInfo = countryInfo;
        this.newUser = newUser;
    }

    public SaveContactTask(Context context, boolean newUser) {
        super(context);
        this.newUser = newUser;
    }

    @Override
    protected Contact doInBackground(User... params) {
        try {
            User user = params[0];
            String countryProgramCode = countryInfo != null ? countryInfo.getIsoAlpha3() : user.getCountryProgram();

            String rapidproEndpoint = getContext().getString(CountryProgramManager
                    .getCountryProgramForCode(countryProgramCode).getRapidproEndpoint());

            CountryProgram countryProgram = CountryProgramManager.getCountryProgramForCode(countryProgramCode);
            String countryToken = getTokenFromProxy(countryProgram);
            UserManager.updateCountryToken(countryToken);
            if (countryToken != null && !countryToken.isEmpty()) {
                UserManager.initializeFcmClient(countryProgram);
                FcmClient.registerContact(user.getKey());

                ContactBuilder contactBuilder = new ContactBuilder();
                return contactBuilder.buildContactWithoutFields(user);

//                String countryCode = countryInfo != null ? countryInfo.getCountryCode() : user.getCountry();
//                Contact contact = getContactForUser(countryToken, user, getRegistrationDate()
//                        , getISO2CountryCode(countryCode), countryProgram);
//                updateContactsWithGroups(countryToken, contact);

//                try {
//                    return rapidProServices.saveContact(countryToken, contact);
//                } catch (RetrofitError exception) {
//                    AnalyticsHelper.sendException(exception);
//                    Log.e(TAG, "doInBackground ", exception);
//                }
//                return rapidProServices.saveContact(countryToken, contact);
            } else {
                ContactBuilder contactBuilder = new ContactBuilder();
                return contactBuilder.buildContactWithoutFields(user);
            }
        } catch (Exception exception) {
            AnalyticsHelper.sendException(exception);
            Log.e(TAG, "doInBackground ", exception);
        }
        return null;
    }

//    private void updateContactsWithGroups(String countryToken, Contact contact) {
//        try {
//            Contact contactResult = this.rapidProServices.loadContact(countryToken, contact.getUrns().get(0));
//            if (contactHasGroups(contactResult))
//                contact.setGroups(null);
//        } catch(Exception exception) {
//            Log.e(TAG, "updateContactsWithGroups: ", exception);
//        }
//    }
//
//    private boolean contactHasGroups(Contact contactResult) {
//        return contactResult != null && contactResult.getGroups() != null && !contactResult.getGroups().isEmpty();
//    }
//
//    private Date getRegistrationDate() {
//        if(!newUser) return null;
//
//        Date now = new Date();
//        try {
//            SntpClient client = new SntpClient();
//            if (client.requestTime("pool.ntp.org", 4000)) {
//                long ntpTimeMillis = client.getNtpTime();
//                Log.i(TAG, "getRegistrationDate: ntpTimeMillis: " + new Date(ntpTimeMillis));
//                if (ntpTimeMillis > 0)
//                    now = new Date(ntpTimeMillis);
//            }
//        } catch(Exception exception) {
//            Log.e(TAG, "getRegistrationDate: ", exception);
//        }
//        return now;
//    }
//
//    private String getISO2CountryCode(String countryCode) {
//        List<CountryInfo> countryInfo = getCountryInfoList();
//        if(countryInfo != null) {
//            for (CountryInfo info : countryInfo) {
//                if (info.getIsoAlpha3().equalsIgnoreCase(countryCode)) {
//                    countryCode = info.getCountryCode();
//                    break;
//                }
//            }
//        }
//        return countryCode;
//    }

    @Nullable
    private String getTokenFromProxy(CountryProgram countryProgram) {
        try {
            ProxyServices proxyServices = new ProxyServices(getContext());
            ProxyResponse response = proxyServices.getAuthenticationTokenByCountry(countryProgram.getCode());
            return response.getToken();
        } catch(Exception exception) {
            return null;
        }
    }

//    private Contact getContactForUser(String token, User user, Date registrationDate, String countryCode, CountryProgram countryProgram) {
//        List<Field> fields = rapidProServices.loadFields(token);
//
//        ContactBuilder contactBuilder = new ContactBuilder(fields);
//        return contactBuilder.buildContactWithFields(user, registrationDate, countryCode, countryProgram);
//    }
//
//    private List<CountryInfo> getCountryInfoList() {
//        try {
//            String json = IOHelper.loadJSONFromAsset(getContext(), "countryInfo.json");
//
//            Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
//            Type type = new TypeToken<List<CountryInfo>>(){}.getType();
//
//            JsonParser jsonParser = new JsonParser();
//            JsonObject jsonObject = (JsonObject) jsonParser.parse(json);
//
//            return gson.fromJson(jsonObject.get("geonames"), type);
//        } catch (Exception exception) {
//            Log.e(TAG, "doInBackground: ", exception);
//        }
//        return null;
//    }

}
