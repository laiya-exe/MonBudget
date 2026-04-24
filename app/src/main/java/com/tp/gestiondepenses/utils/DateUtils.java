package com.tp.gestiondepenses.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    /**
     * Formate un timestamp/long (en millisecondes) en une date lisible.
     *
     * @param timestamp Le temps en millisecondes depuis le 1er janvier 1970 (epoch Unix)
     * @return Une chaîne de caractères représentant la date au format "dd/MM/yyyy"
     *         (ex : 24/04/2026)
     */
    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        return sdf.format(new Date(timestamp));
    }
}