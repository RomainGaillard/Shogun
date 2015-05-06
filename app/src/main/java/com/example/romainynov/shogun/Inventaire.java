package com.example.romainynov.shogun;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Toast;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.TextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import java.util.ArrayList;

/**
 * Created by RomainYnov on 05/02/2015.
 */

/*
    La classe INVENTAIRE se base entièrement sur la BDD. (Table ITEM) Les items étant présent dans la TABLE ITEM de la BDD sont ceux qui sont dans l'inventaire.
 */

 public class Inventaire {

    private Inventaire(){
    }

    static private ArrayList<Sprite> LoadItemInventaire(final Level lvl){
        // Préparation des items de l'inventaire
        BitmapTextureAtlas mBitmapTextureAtlasItemsInventaire = new BitmapTextureAtlas(2048,2048,TextureOptions.DEFAULT);
        final ArrayList<TextureRegion> mItemsInventaireTextureRegion = new ArrayList<TextureRegion>();
        ArrayList<Item> lItemsInventaire = lvl.bdd.getAllItemInventaire();
        int posYItemInventaire = 0;
        for(int r=0;r<lItemsInventaire.size();r++)
        {
            mItemsInventaireTextureRegion.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlasItemsInventaire,lvl.AppContext, lItemsInventaire.get(r).getSpriteIcon(),0,posYItemInventaire));
            posYItemInventaire += 100;
        }

        lvl.mEngine.getTextureManager().loadTexture(mBitmapTextureAtlasItemsInventaire);

        final ArrayList<Sprite> SpriteItemInventaire = new ArrayList<Sprite>();
        int PosXItem = 165;
        for(int i=0;i<mItemsInventaireTextureRegion.size();i++)
        {
            SpriteItemInventaire.add(new Sprite(PosXItem,67,mItemsInventaireTextureRegion.get(i)));
            PosXItem+=65;
        }

        return SpriteItemInventaire;
    }

    static public void AfficherInventaire(final Level lvl){
        // Afficher la fenêtre de l'inventaire.
        /*Rectangle rectangle = new Rectangle(100,100,500,500);
        Font font;
        final BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);
        mEngine.getTextureManager().loadTexture(fontTexture);
        font = new Font(fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 24, true, Color.BLACK);
        mEngine.getFontManager().loadFont(font);
        */
        final Scene scene_inventaire;
        if(lvl.mScene.getChildScene()!=null) {
            scene_inventaire = lvl.mScene.getChildScene();
        }
        else
            scene_inventaire = new Scene();
        scene_inventaire.setBackgroundEnabled(false);

        final Sprite background = new Sprite(110,45,lvl.mInventaireTextureRegion.get(0));

        final Sprite SpriteSacoche = new Sprite(10, 10,lvl.mInventaireTextureRegion.get(1)) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
                if (pSceneTouchEvent.isActionUp()) {
                    if(lvl.EtatInventaire == false){
                        lvl.EtatInventaire = true;
                        scene_inventaire.detachChild(this);
                        ArrayList<Sprite> SpriteItemInventaire = LoadItemInventaire(lvl);
                        scene_inventaire.attachChild(background);
                        for(int j=0;j<SpriteItemInventaire.size();j++)
                        {
                            scene_inventaire.attachChild(SpriteItemInventaire.get(j));
                        }
                        scene_inventaire.attachChild(this);
                    }
                    else{
                        lvl.EtatInventaire = false;
                        scene_inventaire.detachChildren();
                        scene_inventaire.attachChild(this);
                    }
                }
                return true;
            } ;
        };
        scene_inventaire.registerTouchArea(SpriteSacoche);

        if(lvl.EtatInventaire == true)
        {
            ArrayList<Sprite> SpriteItemInventaire = LoadItemInventaire(lvl);
            scene_inventaire.attachChild(background);
            for(int j=0;j<SpriteItemInventaire.size();j++)
            {
                scene_inventaire.attachChild(SpriteItemInventaire.get(j));
            }
        }

        scene_inventaire.attachChild(SpriteSacoche);

        lvl.mScene.setChildScene(scene_inventaire);
        lvl.mEngine.setScene(lvl.mScene);
    }
}


