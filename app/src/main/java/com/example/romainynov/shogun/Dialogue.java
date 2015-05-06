package com.example.romainynov.shogun;

import org.anddev.andengine.engine.Engine;

import java.util.ArrayList;

/**
 * Created by RomainYnov on 29/01/2015.
 */
public class Dialogue {
    ArrayList<ArrayList<String>> Text;
    ArrayList<ArrayList<Choix>> Choix;

    public Dialogue()
    {
        Text = new ArrayList<ArrayList<String>>();
    }

    public void Text_Base(String texte)
    {
        // Exemple: Bonjour, je suis PNJ1. Que voulez-vous faire ?
        ArrayList<String> base = new ArrayList<String>();
        base.add(texte);
        Text.add(base);
        Text.get(0).get(0);
    }

    public void Choix(String Texte,int Etape)
    {
        // EX: - Non, rien c'est bon, je vais continuer ma route. (Etape 0)
        // Ex: - Quel est ce lieu, j'aimerai des rensiengmeents ? (Etape 0)
        ArrayList<String> c = new ArrayList<String>();
        c.add(Texte);
        //Choix.add(Etape,c);


        Choix.get(0).get(0);
        Choix.get(0).get(1);

      //  Text.get(1).get(0)
       // Choix.get(1).get(1).get(X)
    }
}


