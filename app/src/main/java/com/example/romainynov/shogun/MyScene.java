package com.example.romainynov.shogun;

/**
 * Created by RomainYnov on 12/02/2015.
 */
public class MyScene {
    int id_Scene;
    String nom;
    String background;
    Son son;
    boolean visited; // Est-ce que la scène a déjà était visité ou pas ?

    public MyScene(){
        id_Scene = 0;
        nom="";
        background="";
        son = new Son();
    }

    public MyScene(int id_Scene, String nom, String background, boolean visited) {
        this.id_Scene = id_Scene;
        this.nom = nom;
        this.background = background;
        this.son = new Son();
        this.visited = visited;
    }

    public String getBackground() {return background;}

    public String getNom() {return nom;}

    public Son getSon(){return son;}

    public int getId(){return id_Scene;}

    public boolean getVisited(){return visited;}

    public void setVisited(boolean visit){visited = visit;}
}
