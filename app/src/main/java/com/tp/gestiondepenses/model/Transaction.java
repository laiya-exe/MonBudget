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

    /**
     * @return Le nom de la ressource drawable pour l'icône
     */
    String getIconName();

    /**
     * @return Le nom de la catégorie ou de la source
     */
    String getCategoryName();
}
