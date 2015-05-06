package com.example.romainynov.shogun;

import android.content.Context;
import android.graphics.Rect;
import android.widget.Toast;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import java.util.ArrayList;

/**
 * Created by RomainYnov on 29/01/2015.
 */

public class Level {

    private static final float ALPHA = 0.1f;  // Défini l'alpha des RECTANGLES des zones cliquables pour les items non-interactifs.
    public Scene mScene;
    private Scene scene_coin_prison, scene_prison;
    private ArrayList<Pnj> lPnj;
    private ArrayList<Item> lItems;
    private BitmapTextureAtlas mBitmapTextureAtlasBackground;
    private BitmapTextureAtlas mBitmapTextureAtlasItems;
    private BitmapTextureAtlas mBitmapTextureAtlasPnj;
    private BitmapTextureAtlas mBitmapTextureAtlasInventaire;
    private TextureRegion mBackgroundTextureRegion;
    private ArrayList<TextureRegion> mItemsTextureRegion;
    private ArrayList<TextureRegion> mPnjTextureRegion;
    public ArrayList<TextureRegion> mInventaireTextureRegion;
    private MyScene myScene;
    public DataBaseHandler bdd;
    public Engine mEngine;
    public Context AppContext;
    public Boolean EtatInventaire;


    public Level(MyScene myScene, ArrayList<Pnj> lPnj_,ArrayList<Item> lItems_)
    {
        // Faire appel à une fonction BDD pour récupérer les éléments de la scène en fonction de son nom ?!
        this.myScene = myScene;
        lPnj = lPnj_;
        lItems = lItems_;
        EtatInventaire = false;
    }

    /* ======================================================================================
     ==========                             LoadRessources                         ==========
     ====================================================================================== */

    public void LoadRessources(Context c, Engine mEngine,final DataBaseHandler bdd)
    {
        this.bdd = bdd;
        this.mEngine = mEngine;
        mBitmapTextureAtlasBackground = new BitmapTextureAtlas(2048, 2048, TextureOptions.DEFAULT);
        mBitmapTextureAtlasItems = new BitmapTextureAtlas(2048, 2048, TextureOptions.DEFAULT);
        mBitmapTextureAtlasPnj = new BitmapTextureAtlas(2048, 2048, TextureOptions.DEFAULT);
        mBitmapTextureAtlasInventaire = new BitmapTextureAtlas(2048,2048,TextureOptions.DEFAULT);
        //mBitmapTextureAtlasItemsInventaire = new BitmapTextureAtlas(2048,2048,TextureOptions.DEFAULT);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        // Préparation du Background
        mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(mBitmapTextureAtlasBackground, c, myScene.getBackground(),0, 0);

        // Préparation des ITEMS
        mItemsTextureRegion = new ArrayList<TextureRegion>();
        for(int i=0;i<lItems.size();i++)
        {
            if(lItems.get(i).isInteractif() == true)
            {
                mItemsTextureRegion.add(BitmapTextureAtlasTextureRegionFactory
                        .createFromAsset(mBitmapTextureAtlasItems, c, lItems.get(i).getSprite(), // ATTENTION IL NE FAUT PAS d'ERREURS SUR LE NOM DU FICHIER.
                                lItems.get(i).getPositionX(), lItems.get(i).getPositionY()));
            }
        }

        // Préparation des PNJ
        mPnjTextureRegion = new ArrayList<TextureRegion>();
        for(int j=0; j<lPnj.size();j++)
        {
            mPnjTextureRegion.add(BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(mBitmapTextureAtlasPnj, c, lPnj.get(j).getSprite(),
                            lPnj.get(j).getPositionX(), lPnj.get(j).getPositionY()));
        }

        // Préparation de l'interface de l'inventaire
        mInventaireTextureRegion = new ArrayList<TextureRegion>();
        mInventaireTextureRegion.add(BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(mBitmapTextureAtlasInventaire,c,"interface-inventaire.png",0,0));
        mInventaireTextureRegion.add(BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(mBitmapTextureAtlasInventaire,c,"sacoche.png",00,120));


        // CHARGEMENT des ressources
         mEngine.getTextureManager().loadTexture(mBitmapTextureAtlasBackground);
         mEngine.getTextureManager().loadTexture(mBitmapTextureAtlasItems);
         mEngine.getTextureManager().loadTexture(mBitmapTextureAtlasPnj);
         mEngine.getTextureManager().loadTexture(mBitmapTextureAtlasInventaire);
        // mEngine.getTextureManager().loadTexture(mBitmapTextureAtlasItemsInventaire);
    }

    /* ======================================================================================
     ==========                             LoadScene                              ==========
     ====================================================================================== */
    public Scene LoadScene(final Context  AppContext,final DataBaseHandler bdd)
    {
        this.AppContext = AppContext;
        mScene = new Scene();
        final Level lvlCourant = this;
        // Attachement du Background
        mScene.attachChild(new Sprite(0, 0, mBackgroundTextureRegion));

        // Attachement des items interactifs
        final ArrayList<Sprite> lSpriteItem = new ArrayList<Sprite>();
        for(int i=0; i<mItemsTextureRegion.size();i++)
        {
            final int finalI = i;

            lSpriteItem.add(new Sprite(lItems.get(finalI).getPositionX(), lItems.get(finalI).getPositionY(), mItemsTextureRegion.get(finalI)) {
                private final int id = finalI;
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
                    if (pSceneTouchEvent.isActionUp()) {
                        switch(lItems.get(id).getId())
                        {
                            case 4: // savon
                                bdd.AddInventaireItem(4);
                                mScene.detachChild(lSpriteItem.get(id));
                                mScene.unregisterTouchArea(lSpriteItem.get(id));
                                bdd.VisibiliteItem(1,4,false);
                                Inventaire.AfficherInventaire(lvlCourant);
                                mEngine.setScene(mScene);
                                Popup.AfficherPopup(lvlCourant,lItems.get(id).getDescription());
                                break;

                            case 5: // fleche prison 1
                                MyScene Myscene_coin_prison = bdd.getScene(2); // On récupère la scène 2 de la prsion.

                                lItems = bdd.getAllItems(2);
                                lPnj = bdd.getAllPnj(2);

                                Level level_coin_prison = new Level(Myscene_coin_prison,lPnj,lItems);

                                level_coin_prison.LoadRessources(AppContext,mEngine,bdd);

                                scene_coin_prison = level_coin_prison.LoadScene(AppContext, bdd);
                                mScene.reset();
                                mEngine.setScene(scene_coin_prison);
                                break;

                            case 7: // cuillere
                                bdd.AddInventaireItem(7);
                                mScene.detachChild(lSpriteItem.get(id));
                                mScene.unregisterTouchArea(lSpriteItem.get(id));
                                bdd.VisibiliteItem(2,7,false);
                                Inventaire.AfficherInventaire(lvlCourant);
                                mEngine.setScene(mScene);
                                break;

                            case 8: // sulfure
                                bdd.AddInventaireItem(8);
                                mScene.detachChild(lSpriteItem.get(id));
                                mScene.unregisterTouchArea(lSpriteItem.get(id));
                                bdd.VisibiliteItem(2,8,false);
                                Inventaire.AfficherInventaire(lvlCourant);
                                mEngine.setScene(mScene);
                                break;

                            case 6: // bambou
                                bdd.AddInventaireItem(6);
                                mScene.detachChild(lSpriteItem.get(id));
                                mScene.unregisterTouchArea(lSpriteItem.get(id));
                                bdd.VisibiliteItem(2,6,false);
                                Inventaire.AfficherInventaire(lvlCourant);
                                mEngine.setScene(mScene);
                                break;

                            case 10: // flèche coin_prison
                                MyScene Myscene_prison = bdd.getScene(1); // On récupère la scène 2 de la prsion.

                                lItems = bdd.getAllItems(1);
                                lPnj = bdd.getAllPnj(1);

                                Level level_prison = new Level(Myscene_prison,lPnj,lItems);

                                level_prison.LoadRessources(AppContext,mEngine,bdd);

                                scene_prison = level_prison.LoadScene(AppContext, bdd);
                                mScene.reset();
                                mEngine.setScene(scene_prison);
                                break;

                            case 11: // Lunette
                                bdd.AddInventaireItem(11);
                                mScene.detachChild(lSpriteItem.get(id));
                                mScene.unregisterTouchArea(lSpriteItem.get(id));
                                bdd.VisibiliteItem(1,11,false);
                                Inventaire.AfficherInventaire(lvlCourant);
                                mEngine.setScene(mScene);
                                Popup.AfficherPopup(lvlCourant,"Avec ces lunettes j'y verrai plus clair !");
                                break;


                            default:
                                Popup.AfficherPopup(lvlCourant,lItems.get(id).getDescription());
                                /*Toast toast = Toast.makeText(AppContext,lItems.get(id).getDescription(), Toast.LENGTH_SHORT);
                                toast.show();*/
                                break;
                        }
                    }
                    return true;
                };
            });
            if(lItems.get(i).getVisible())
            {
                mScene.registerTouchArea(lSpriteItem.get(i));
                mScene.attachChild(lSpriteItem.get(i));
            }
        }

        // Attachement des items non-interactifs
        final ArrayList<Rectangle> lRectangleItem = new ArrayList<Rectangle>();
        int nbElementsNonInteractifs = 0;
        for(int j=0; j<lItems.size();j++)
        {
            if(lItems.get(j).isInteractif() == false) // Créer des zones cliquables en rectangle seulement pour les items non-interactifs.
            {
                final int finalJ = j;
                final int finalNbElementsNonInteractifs = nbElementsNonInteractifs;
                lRectangleItem.add(new Rectangle(lItems.get(j).getPositionX(),lItems.get(j).getPositionY(),
                        lItems.get(j).getDimensions()[0],lItems.get(j).getDimensions()[1])
                {
                    private final int id = finalJ;
                    private final int nbElemNonInteractif = finalNbElementsNonInteractifs;
                    @Override
                    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
                    {
                        if (pSceneTouchEvent.isActionUp()) {

                            switch (lItems.get(id).getId())
                            {
                                case 200: // mur du sulfure
                                    if(bdd.CheckItemInventaire(7) && (bdd.CheckVisibiliteItem(2,8) == false)) { //si la cuillere est dans l'inventaire et que le sulfure est caché

                                        for(int k=0; k<lItems.size();k++)
                                        {
                                            if(lItems.get(k).getId() == 8){  // On affiche le sulfure et on cache le mur sulfure et on supprime la culliere de l'inventaire
                                                mScene.registerTouchArea(lSpriteItem.get(k));
                                                mScene.attachChild(lSpriteItem.get(k));
                                                mScene.detachChild(lRectangleItem.get(finalNbElementsNonInteractifs));
                                                bdd.SuppInventaireItem(7);
                                            }
                                        }
                                        bdd.VisibiliteItem(2,8,true);
                                        bdd.VisibiliteItem(2,200,false);
                                        mEngine.setScene(mScene);
                                    }
                                    else{
                                        Popup.AfficherPopup(lvlCourant,lItems.get(id).getDescription());
                                    }
                                    break;

                                case 201: // mur barre nb de jour
                                    if(!bdd.CheckVisibiliteItem(2,9)) // Si la barre n'est pas déjà affiché alors on l'affiche.
                                    {
                                        for(int k=0; k<lItems.size();k++)
                                        {
                                            if(lItems.get(k).getId() == 9){  // On affiche la barre
                                                mScene.registerTouchArea(lSpriteItem.get(k));
                                                mScene.attachChild(lSpriteItem.get(k));
                                            }
                                        }
                                        bdd.VisibiliteItem(2,9,true);
                                        mEngine.setScene(mScene);
                                    }
                                    Popup.AfficherPopup(lvlCourant,lItems.get(id).getDescription());
                                    break;

                                default:
                                    Popup.AfficherPopup(lvlCourant,"Rien à voir ici -"+ lItems.get(id).getDescription());
                                    break;
                            }
                        }
                        return true;
                    };
                });
                lRectangleItem.get(nbElementsNonInteractifs).setColor(255, 0, 0, ALPHA);
                if(lItems.get(j).getVisible())
                {
                    mScene.registerTouchArea(lRectangleItem.get(nbElementsNonInteractifs));
                    mScene.attachChild(lRectangleItem.get(nbElementsNonInteractifs));
                }
                nbElementsNonInteractifs++;
            }
        }

        // Attachement des PNJ
        ArrayList<Rectangle> lRectanglePnj = new ArrayList<Rectangle>();
        for(int p=0;p<lPnj.size();p++)
        {
            final int finalP = p;
            lRectanglePnj.add(new Rectangle(lPnj.get(p).getPositionX(),lPnj.get(p).getPositionY(),
                    lPnj.get(p).getDimensions()[0],lPnj.get(p).getDimensions()[1]){
                private final int id = finalP;
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)
                {
                    if (pSceneTouchEvent.isActionUp()) {

                        switch (lPnj.get(id).getId())
                        {

                            case 1: // prisonnier
                                if(bdd.CheckItemInventaire(12)!=true){
                                    //bdd.AddInventaireItem(12); // les clef !
                                    //Inventaire.AfficherInventaire(mEngine,AppContext,mScene,bdd,mInventaireTextureRegion);
                                    Popup.AfficherPopup(lvlCourant,"Ceci est un test de popup !!!! Je m'appel Monsieur Godart.");
                                }
                                else {
                                    Toast toast = Toast.makeText(AppContext, "Je suis le PNJ " + lPnj.get(id).getNomPnj() + " !", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;
                        }
                    }
                    return true;
                };
            });
            lRectanglePnj.get(p).setColor(0, 0, 255, ALPHA);

            if(lPnj.get(p).getVisible())
            {
                mScene.registerTouchArea(lRectanglePnj.get(p));
                mScene.attachChild(lRectanglePnj.get(p));
            }
        }

        // Attachement de l'INTERFACE
        Inventaire.AfficherInventaire(lvlCourant);

        if(myScene.getVisited()==false){
            if(myScene.getId()==1)
                Popup.AfficherPopup(lvlCourant,"Me faire choper par les poulets pour une volaille c'est incroyable." +
                        " Il faut que je trouve un moyen de me sortir d'ici au plus vite.");
                myScene.setVisited(true);


            bdd.VisitedScene(myScene.getId(),true);
        }

        return mScene;
    }
}


