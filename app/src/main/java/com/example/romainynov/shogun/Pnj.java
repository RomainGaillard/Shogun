package com.example.romainynov.shogun;

/**
 * Created by RomainYnov on 29/01/2015.
 */
public class Pnj {
    private int idPnj;
    private String NomPnj;
    private String Sprite;
    private int Dimensions[]; // Zone cliquable (Rectangle)
    private int PositionX;
    private int PositionY;
    private Dialogue Dial; // TYPE: DIALOGUE ... A modifier.
    private boolean Visible;

    public Pnj(int idPnj_, String NomPnj_, String Sprite_, int DimLargeur, int DimHauteur,int PosX, int PosY, Dialogue Dial_,boolean Visible_)
    {
        idPnj = idPnj_;
        NomPnj = NomPnj_;
        Sprite = Sprite_;
        Dimensions = new int[2];
        Dimensions[0] = DimLargeur;
        Dimensions[1] = DimHauteur;
        PositionX = PosX;
        PositionY = PosY;
        Dial = Dial_;
        Visible = Visible_;
    }

    public void setVisible(boolean v){Visible = v;}

    public boolean getVisible(){return Visible;}

    public int getId(){return idPnj;}

    public String getNomPnj(){return NomPnj;}

    public String getSprite() {
        return Sprite;
    }

    public int[] getDimensions() {
        return Dimensions;
    }

    public int getPositionX() {
        return PositionX;
    }

    public int getPositionY() {
        return PositionY;
    }


    public void AfficherDial() {

        // if Dial.Text.size() > X alors prévoir un système de page pour le dialogue.
        // if nbChoix > X alors prévoir une autre page pour afficher les choix.
    }
}

