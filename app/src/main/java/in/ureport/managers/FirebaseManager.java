package in.ureport.managers;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.ureport.R;

//import com.firebase.client.Config;
//import com.firebase.client.core.RepoManager;
//import com.firebase.client.utilities.ParsedUrl;
//import com.firebase.client.utilities.Utilities;

/**
 * Created by johncordeiro on 15/08/15.
 */
public class FirebaseManager {

    private static DatabaseReference reference;

    public static void init(Context context, boolean proxyEnabled) {
        if (reference == null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);

//            Config config = new Config();
//            config.setPersistenceEnabled(true);
//            if (proxyEnabled) {
//                config.setAuthenticationServer(context.getString(R.string.firebase_proxy_auth));
//            }
//            Firebase.setDefaultConfig(config);

            String appUrl = proxyEnabled
                    ? context.getString(R.string.firebase_proxy_database) : context.getString(R.string.firebase_app);
//            reference = getInstanceWithCustomName(appUrl, context.getString(R.string.firebase_app_name));
            reference = FirebaseDatabase.getInstance().getReference();
        }
    }

    private static DatabaseReference getInstanceWithCustomName(String url, String name) {
//        ParsedUrl parsedUrl = Utilities.parseUrl(url);
//        parsedUrl.repoInfo.namespace = name;
//        return new Firebase(RepoManager.getRepo(Firebase.getDefaultConfig(), parsedUrl.repoInfo), parsedUrl.path);
        return FirebaseDatabase.getInstance().getReferenceFromUrl(url).child(name);
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static void authorizeCode(String code) {
        DatabaseReference authorization = reference.child("backend_authorization").child(code);
        authorization.child("checked").setValue(true);
        authorization.child("user").setValue(UserManager.getUserId());
    }

    public static DatabaseReference getReference() {
        return reference;
    }
}
