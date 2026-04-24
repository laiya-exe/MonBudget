package com.tp.gestiondepenses.model;

public interface Transaction {
    double getMontant();

    long getDate();

    String getDescription();

    /**
     * Pour connaitre le type d'operation. Facilite l'affichage dans dasboard
     * @return "DEPENSE" ou "REVENU"
     */
    String getType();
}