package info.androidhive.pathshala.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Dimpi on 06/06/2016.
 */
public class CenterLoginState {
    static final String PREF_USER_NAME= "username";
    static final String NGO_EMAIL= "ngo_email";
    static final String CENTER_LOCATION= "center_location";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setNgoEmail(Context ctx, String ngo_email)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(NGO_EMAIL, ngo_email);
        editor.commit();
    }

    public static String getNgoEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(NGO_EMAIL, "");
    }
    public static void setCenterLocation(Context ctx, String center_location)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(CENTER_LOCATION, center_location);
        editor.commit();
    }

    public static String getCenterLocation(Context ctx)
    {
        return getSharedPreferences(ctx).getString(CENTER_LOCATION, "");
    }
}
