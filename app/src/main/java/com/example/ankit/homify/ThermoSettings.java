package com.example.ankit.homify;

/**
 * Created by ankit on 4/5/16.
 */
import android.content.Context;
import android.content.SharedPreferences;

import com.nestlabs.sdk.NestToken;

public class ThermoSettings {
    private static final String TOKEN_KEY = "token";
    private static final String EXPIRATION_KEY = "expiration";

    public static void saveAuthToken(Context context, NestToken token) {
        //System.out.println("trying to save token");
        if (token == null) {
            getPrefs(context).edit().remove(TOKEN_KEY).remove(EXPIRATION_KEY).commit();
            return;
        }
        getPrefs(context).edit()
                .putString(TOKEN_KEY, token.getToken())
                .putLong(EXPIRATION_KEY, token.getExpiresIn())
                .commit();
    }

    public static NestToken loadAuthToken(Context context) {
        //System.out.println("Load the token");
        final SharedPreferences prefs = getPrefs(context);
        final String token = prefs.getString(TOKEN_KEY, null);
        final long expirationDate = prefs.getLong(EXPIRATION_KEY, -1);

        if (token == null || expirationDate == -1) {
            return null;
        }

        return new NestToken(token, expirationDate);
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(NestToken.class.getSimpleName(), 0);
    }

}

