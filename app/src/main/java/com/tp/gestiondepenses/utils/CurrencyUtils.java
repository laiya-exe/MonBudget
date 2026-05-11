package com.tp.gestiondepenses.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.ui.fragments.SettingsFragment;

public class CurrencyUtils {

    public static String getCurrency(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SettingsFragment.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("selected_currency", "FCFA");
    }

    public static void setCurrency(Context context, String currency) {
        SharedPreferences prefs = context.getSharedPreferences(SettingsFragment.PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("selected_currency", currency).apply();
    }

    public static String formatAmount(Context context, double amount) {
        String currency = getCurrency(context);
        return context.getString(R.string.format_montant, amount, currency);
    }

    public static String formatAmountNegatif(Context context, double amount) {
        String currency = getCurrency(context);
        return context.getString(R.string.format_montant_negatif, amount, currency);
    }

    public static String formatAmountPositif(Context context, double amount) {
        String currency = getCurrency(context);
        return context.getString(R.string.format_montant_positif, amount, currency);
    }
}
