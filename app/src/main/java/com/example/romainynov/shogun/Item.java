package com.example.romainynov.shogun;

/**
 * Created by RomainYnov on 29/01/2015.
 */
public class Item {
    private int IdItem; // Possiblement null: si l'item n'est pas dans l'inventaire il n'a pas d'id.
    private String Nom;
    private String Description;
    private String Sprite; // Si interactif
    private String Sprite_icon; // Si on peut le mettre dans l'inventaire.
    private int Dimensions[]; // Sinon, faire rectangle 2 dimensions (largeur,hauteur)
    private int PositionX;
    private int PositionY;
    private boolean Interactif;
    private boolean Visible;

    // Constructeur ITEM Intéractif
    public Item(int IdItem_, String Nom_, String Description_, String Sprite_, int PositionX_, int PositionY_, boolean Visible_, String Sprite_icon_)
    {
        IdItem = IdItem_;
        Nom = Nom_;
        Description = Description_;
        Interactif = true;
        Sprite = Sprite_;
        PositionX = PositionX_;
        PositionY = PositionY_;
        Visible = Visible_;
        Sprite_icon = Sprite_icon_;
    }

    // CONSTRUCTEUR ITEM Non-Intéractif
    public Item(int IdItem_, String Nom_, String Description_, int DimLargeur, int DimHauteur, int PositionX_, int PositionY_, boolean Visible_)
    {
        IdItem = IdItem_;
        Nom = Nom_;
        Description = Description_;
        Interactif = false;
        Dimensions = new int[2];
        Dimensions[0] = DimLargeur;
        Dimensions[1] = DimHauteur;
        PositionX = PositionX_;
        PositionY = PositionY_;
        Visible = Visible_;
    }

    public boolean getVisible(){return Visible;}

    public void setVisible(boolean v){Visible = v;}

    public void setId(int id)
    {
        IdItem = id;
    }

    public int getId(){return IdItem;}

    public String getSprite() {
        return Sprite;
    }

    public String getSpriteIcon() {
        return Sprite_icon;
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

    public boolean isInteractif() {
        return Interactif;
    }

    public String getDescription() {
        return Description;
    }

    public String getNom(){return Nom;}

}


