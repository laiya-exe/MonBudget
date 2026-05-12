package com.tp.gestiondepenses.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategorieWithRubriques {
    @Embedded
    public Categorie categorie;

    @Relation(
            parentColumn = "id",
            entityColumn = "categorie_id"
    )
    public List<Rubrique> rubriques;
}