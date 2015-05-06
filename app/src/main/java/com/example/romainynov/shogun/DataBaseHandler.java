package com.example.romainynov.shogun;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by RomainYnov on 11/02/2015.
 */

public class DataBaseHandler extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Shogun";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // ATTENTION EN CAS DE MODIFICATION SUR CETTE FONCTION IL FAUT DESINSTALLER L'APPLICATION POUR POUVOIR TESTER LA NEW VERSION.

        String CREATE_TABLE_PNJ = "CREATE TABLE pnj (id_pnj INTEGER PRIMARY KEY, nom_pnj TEXT,sprite_pnj TEXT, largeur_pnj INTEGER, hauteur_pnj INTEGER)";
        String CREATE_TABLE_ITEM = "CREATE TABLE item (id_item INTEGER PRIMARY KEY, nom_item TEXT, description_item TEXT, sprite_item TEXT, largeur_item INTEGER, hauteur_item INTEGER, interactif_item Bool, sprite_icon TEXT)";
        //String CREATE_TABLE_ITEM_CRAFT = "CREATE TABLE item_craft (id_item_craft INTEGER PRIMARY KEY, nom_item_craft TEXT, description_item_craft TEXT, sprite_item_craft TEXT)";
        String CREATE_TABLE_SCENE = "CREATE TABLE scene (id_scene INTEGER PRIMARY KEY, nom_scene TEXT, background_scene TEXT, visited INTEGER)";
        String CREATE_TABLE_INVENTAIRE = "CREATE TABLE inventaire (id_inventaire INTEGER PRIMARY KEY)";
        // TABLE DE JOINTURE
        String CREATE_TABLE_SCENE_ITEM = "CREATE TABLE scene_item (id_scene INTEGER, id_item INTEGER, positionX INTEGER, positionY INTEGER, visible Bool,  PRIMARY KEY(id_scene,id_item))";
        String CREATE_TABLE_SCENE_PNJ = "CREATE TABLE scene_pnj (id_scene INTEGER, id_pnj INTEGER, positionX INTEGER, positionY INTEGER, visible Bool, PRIMARY KEY(id_scene,id_pnj))";
        String CREATE_TABLE_INVENTAIRE_ITEM = "CREATE TABLE inventaire_item(id_inventaire INTEGER, id_item INTEGER, PRIMARY KEY(id_inventaire, id_item))";
        String CREATE_TABLE_INVENTAIRE_ITEM_CRAFT = "";

        db.execSQL(CREATE_TABLE_SCENE);
        db.execSQL(CREATE_TABLE_ITEM);
        //db.execSQL(CREATE_TABLE_ITEM_CRAFT);
        db.execSQL(CREATE_TABLE_PNJ);
        db.execSQL(CREATE_TABLE_INVENTAIRE);
        db.execSQL(CREATE_TABLE_SCENE_ITEM);
        db.execSQL(CREATE_TABLE_SCENE_PNJ);
        db.execSQL(CREATE_TABLE_INVENTAIRE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public MyScene getScene(int idScene)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String rq = "SELECT * FROM scene WHERE id_scene = "+idScene;
        Cursor cursor = db.rawQuery(rq,null);
        MyScene result = new MyScene();
        if(cursor.moveToFirst())
        {
            boolean visited = false;
            if(cursor.getInt(3) == 1)
                visited = true;
            result = new MyScene(idScene,cursor.getString(1),cursor.getString(2),visited); // id, nom, background.
        }
        return result;
    }

    public int getNbScene()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String rq = "SELECT * FROM scene";
        Cursor cursor = db.rawQuery(rq,null);
        return cursor.getCount();
    }

    public ArrayList<Item> getAllItems(int idScene) {
        // Récupérer tous les items d'une scène.
        ArrayList<Item> result = new ArrayList<Item>();
        SQLiteDatabase db = this.getWritableDatabase();
        String rq = "SELECT * FROM scene_item WHERE id_scene = "+idScene;
        Cursor cursor = db.rawQuery(rq,null);
        if(cursor.moveToFirst())
        {
            do{
                int id_item = cursor.getInt(1);
                int posX_item = cursor.getInt(2);
                int posY_item = cursor.getInt(3);
                boolean visible_item = false;
                if(cursor.getInt(4) == 1)
                    visible_item = true;

                Cursor cursor2 = db.rawQuery("SELECT * FROM item WHERE id_item = "+id_item,null);

                String nom_item ="", description_item = "", sprite_item ="", sprite_icon="";
                int largeur_item =0, hauteur_item=0;
                boolean interactif_item = false;

                if(cursor2.moveToFirst())
                {
                    nom_item = cursor2.getString(1);
                    description_item = cursor2.getString(2);
                    sprite_item = cursor2.getString(3);
                    largeur_item = cursor2.getInt(4);
                    hauteur_item = cursor2.getInt(5);
                    if(cursor2.getInt(6) == 1)
                        interactif_item = true ;
                    sprite_icon = cursor2.getString(7);
                }

                if(interactif_item)
                    result.add(new Item(id_item,nom_item,description_item,sprite_item,posX_item,posY_item,visible_item,sprite_icon));
                else
                    result.add(new Item(id_item,nom_item,description_item,largeur_item,hauteur_item,posX_item,posY_item,visible_item));

                cursor2.close();
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<Pnj> getAllPnj(int idScene) {
        // Récupérer tous les pnj d'une scène
        ArrayList<Pnj> result = new ArrayList<Pnj>();
        SQLiteDatabase db = this.getWritableDatabase();
        String rq = "SELECT * FROM scene_pnj WHERE id_scene = "+idScene;
        Cursor cursor = db.rawQuery(rq,null);
        if(cursor.moveToFirst())
        {
            do{
                int id_pnj = cursor.getInt(1);
                int posX_pnj = cursor.getInt(2);
                int posY_pnj = cursor.getInt(3);
                boolean visible_pnj = false;
                if(cursor.getInt(4) == 1)
                    visible_pnj = true;

                Cursor cursor2 = db.rawQuery("SELECT * FROM pnj WHERE id_pnj = "+id_pnj,null);

                String nom_pnj ="", sprite_pnj ="";
                int largeur_pnj =0, hauteur_pnj = 0;
                Dialogue dial = new Dialogue();


                if(cursor2.moveToFirst())
                {
                    nom_pnj = cursor2.getString(1);
                    sprite_pnj = cursor2.getString(2);
                    largeur_pnj = cursor2.getInt(3);
                    hauteur_pnj = cursor2.getInt(4);
                }

                result.add(new Pnj(id_pnj,nom_pnj,sprite_pnj,largeur_pnj,hauteur_pnj,posX_pnj,posY_pnj,dial,visible_pnj));
                cursor2.close();
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    private Item getItem(int id) {
        return null;
    }

    private Pnj getPnj(int id) {
        return null;
    }

    private void AddItem_Interactif(int id, String nom, String description, String sprite) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO item VALUES (" + id + ",'" + nom + "','" + description + "','" + sprite + "',null,null,1,null)");
    }

    private void AddItem_Interactif(int id, String nom, String description, String sprite, String sprite_icon) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO item VALUES (" + id + ",'" + nom + "','" + description + "','" + sprite +"',null,null,1,'"+sprite_icon+"')");
    }

    private void AddItem_NonInteractif(int id, String nom, String description, int largeur, int hauteur) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO item VALUES (" + id + ",'" + nom + "','" + description + "','null'," + largeur + "," + hauteur + ",0,null)");
    }

    private void AddPnj(int id, String nom, String sprite, int largeur, int hauteur) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO pnj VALUES (" + id + ",'" + nom + "','" + sprite + "'," + largeur + "," + hauteur + ")");
    }

    private void AddScene(int id, String nom, String background,boolean visited)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO scene VALUES (" + id + ",'" + nom + "','" + background + "','"+ visited +"')");
    }

    public void VisitedScene(int id_scene,boolean visited)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int visited_ = 0;
        if(visited)
            visited_ = 1;
        db.execSQL("UPDATE scene SET visited = "+visited_+" WHERE id_scene ="+id_scene);
    }

    private void AddSceneItem(int id_scene, int id_item, int posX, int posY, boolean visible)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int visible_ = 0;
        if(visible)
            visible_ = 1;
        db.execSQL("INSERT INTO scene_item VALUES ("+id_scene+"," + id_item + "," + posX + ","+posY+","+visible_+")");
    }

    private void SuppSceneItem(int id_scene, int id_item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM scene_item WHERE id_scene ="+id_scene+" AND id_item ="+id_item);
    }

    private void AddScenePnj(int id_scene, int id_pnj, int posX, int posY, boolean visible)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int visible_ = 0;
        if(visible)
            visible_ = 1;
        db.execSQL("INSERT INTO scene_pnj VALUES ("+id_scene+"," + id_pnj + "," + posX + ","+posY+","+visible_+")");
    }

    private void AddInventare()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO inventaire VALUES (0)");
    }

    public void VisibiliteItem(int id_scene,int id_item, boolean visible)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int visible_ = 0;
        if(visible)
            visible_ = 1;
        db.execSQL("UPDATE scene_item SET visible = "+visible_+" WHERE id_scene ="+id_scene+" AND id_item ="+id_item);
    }

    public boolean CheckVisibiliteItem(int id_scene, int id_item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String rq = "SELECT visible FROM scene_item WHERE id_scene ="+id_scene+" AND id_item ="+id_item;
        Cursor cursor = db.rawQuery(rq,null);
        boolean result = false;
        if(cursor.moveToFirst()) {
            int visible = cursor.getInt(0);
            if(visible==1)
                result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    public void VisiblitePnj(int id_scene, int id_pnj, boolean visible)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int visible_ = 0;
        if(visible)
            visible_ = 1;
        db.execSQL("UPDATE scene_pnj SET visible = "+visible_+" WHERE id_scene ="+id_scene+" AND id_pnj ="+id_pnj);
    }

    public void AddInventaireItem(int idItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO inventaire_item VALUES (0," + idItem + ")");

        // PENSEZ A FAIRE LE CRECK VOIR SI Y A CRAFT OU PAS !!!!!!!
    }

    public void SuppInventaireItem(int idItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM inventaire_item WHERE id_item = "+idItem);
    }

    public boolean CheckItemInventaire(int idItem)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String rq = "SELECT * FROM inventaire_item WHERE id_item = "+idItem;
        Cursor cursor = db.rawQuery(rq,null);
        //db.close();

        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }

    public ArrayList<Item> getAllItemInventaire()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String rq = "SELECT * FROM inventaire_item";
        Cursor cursor = db.rawQuery(rq,null);
        ArrayList<Item> result = new ArrayList<Item>();
        if(cursor.moveToFirst())
        {
            do{
                int id_item = cursor.getInt(1);

                Cursor cursor2 = db.rawQuery("SELECT * FROM item WHERE id_item = "+id_item,null);

                String nom_item ="", description_item = "", sprite_item ="", sprite_icon="";
                int largeur_item =0, hauteur_item=0;
                boolean interactif_item = false;

                if(cursor2.moveToFirst())
                {
                    nom_item = cursor2.getString(1);
                    description_item = cursor2.getString(2);
                    sprite_item = cursor2.getString(3);
                    largeur_item = cursor2.getInt(4);
                    hauteur_item = cursor2.getInt(5);
                    if(cursor2.getInt(6) == 1)
                        interactif_item = true ;
                    sprite_icon = cursor2.getString(7);
                }

                if(interactif_item)
                    result.add(new Item(id_item,nom_item,description_item,sprite_item,0,0,true,sprite_icon));
                else
                    result.add(new Item(id_item,nom_item,description_item,largeur_item,hauteur_item,0,0,true));

                cursor2.close();
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public void Initialiser_Bdd() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Réinitialiser la BDD
        db.execSQL("DELETE FROM item");
        db.execSQL("DELETE FROM pnj");
        db.execSQL("DELETE FROM scene");
        db.execSQL("DELETE FROM inventaire");
        db.execSQL("DELETE FROM scene_item");
        db.execSQL("DELETE FROM scene_pnj");
        db.execSQL("DELETE FROM inventaire_item");

        // Add Item !
        // >> Items interactifs
        AddItem_Interactif(1, "café", "boison energisante", "player.png");
        AddItem_Interactif(2, "seau", "Pourquoi c’est tiède ?", "seau.png"); // ATTENTION NE PAS METTRE d'APOSTROPHE !
        AddItem_Interactif(3, "menotte", "Sert à menotter les prisonniers ou pimenter sa vie conjugale.", "menotte.png");
        AddItem_Interactif(4, "savon", "Houlà j’ai pris des risques, en ramassant cette savonnette.", "savon.png","savon-icon.png");
        AddItem_Interactif(5, "fleche", "fleche", "fleche_prison.png");

        AddItem_Interactif(6,"Bambou","ça pourrait être utile...","bambou-prison.png","bambou-prison-icon.png");
        AddItem_Interactif(7,"Cuillere","ça pourrait servir de grattoir !","cuillere-prison.png","cuillere-prison-icon.png");
        AddItem_Interactif(8,"Sulfure","Voilà qui pourrait faire un bon explosif !","sulfure.png","sulfure-icon.png");
        AddItem_Interactif(9,"BarreJour","Un jour de plus !","barre-jour-prison.png");
        AddItem_Interactif(10,"Fleche","Retour","fleche_coin_prison.png");
        AddItem_Interactif(11,"Lunette","Lunette","lunette.png","lunette-icon.png");
        AddItem_Interactif(12,"Clef","Clef","clef.png","clef.png");

        // >> Items non-interactifs
        AddItem_NonInteractif(100, "cadenas", "cette porte est verrouillée", 70, 130);  // largeur, hauteur = porte prison

        AddItem_NonInteractif(200, "mur-coin-prison","Il y a comme une odeur de sulfure, il me faudrait quelque chose pour gratter !",186,200);
        AddItem_NonInteractif(201,"mur-jour-prison","un jour de plus... ça ne va pas m’aider à sortir d’ici !",700,150);

        // Add Pnj !
        AddPnj(1, "Romain", "player.png", 195, 400);

        // Add Scene !
        AddScene(1,"Prison","background-prison.jpg",false);
        AddScene(2,"Coin de la prison","background-prison-coin.jpg",false);
        // >>> Scene 1: Prison
            // id_scene, id_item, posX, posY, visible
        AddSceneItem(1,2,600,450,true); // Seau
        AddSceneItem(1,3,1100,650,true); // Menotte
        AddSceneItem(1,4,400,550,true); // Savon
        AddSceneItem(1,5,1700,750,true); // Fleche_prison !
        AddSceneItem(1,11,260,420,true); // Lunette

        AddSceneItem(1, 100, 1520, 160,true); // porte prison

        AddScenePnj(1,1,140,280,true); // id_scene, id_pnj, posX, posY,affichage

        // >>> Scene 2: Coin de la prison
        AddSceneItem(2,6,1300,470,true); // Bambou
        AddSceneItem(2,7,1000,670, true); // Cuillere
        AddSceneItem(2,200,200,490, true); // mur sulfure
        AddSceneItem(2,201,1150,50, true); // mur nb jour
        AddSceneItem(2, 8, 100,530,false); // Sulfure
        AddSceneItem(2, 9, 1790,80,false); // barre jour prison
        AddSceneItem(2,10,110,990,true); // flèche

        db.close();
    }
}
