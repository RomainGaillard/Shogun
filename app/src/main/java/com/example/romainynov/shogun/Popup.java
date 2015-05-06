package com.example.romainynov.shogun;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import java.util.ArrayList;

/**
 * Created by RomainYnov on 22/04/2015.
 */
public class Popup {

    private Popup(){
    }

    static private TextureRegion LoadPopup(Level lvl){
        // Préparation des items de l'inventaire
        BitmapTextureAtlas mBitmapTextureAtlasPopup = new BitmapTextureAtlas(2048,2048, TextureOptions.DEFAULT);
        String background = "background-popup.png";
        final TextureRegion mPopupTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlasPopup,lvl.AppContext,background, 0, 0);
        lvl.mEngine.getTextureManager().loadTexture(mBitmapTextureAtlasPopup);

        return mPopupTextureRegion;
    }


    static public void AfficherPopup(final Level lvl,String msg){
        final Scene scene_popup;
        scene_popup = lvl.mScene.getChildScene();
        scene_popup.setBackgroundEnabled(false);

        float PosX = lvl.mEngine.getCamera().getWidth()/3-150;
        float PosY = lvl.mEngine.getCamera().getMaxY()-150;

        String suite_message = "";
        // TRAITEMENT DE LA LONGUEUR DU TEXTE.
        int posX_txt = (int)PosX + 35;
        int posY_txt = (int)PosY + 20;
        if(msg.length()>85)
        {
            String msg_temp = msg.substring(0,85);
            int index_last_space = msg_temp.lastIndexOf(" ");
            String premiere_ligne = msg.substring(0,index_last_space);
            premiere_ligne += " \n";
            String deuxieme_ligne = msg.substring(index_last_space);
            if(deuxieme_ligne.length() > 85)
            {
                String deuxieme_ligne_temp = deuxieme_ligne.substring(0,85);
                index_last_space = deuxieme_ligne.lastIndexOf(" ");
                suite_message = deuxieme_ligne.substring(index_last_space);
                deuxieme_ligne = deuxieme_ligne_temp.substring(0,index_last_space);
            }
            msg = premiere_ligne+deuxieme_ligne;
        }
        else{
            posY_txt += 20;
            posX_txt -= (msg.length()-85)/2*9.5; // Centrer le txt horizontalement !
        }


        Font font;
        final BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);
        lvl.mEngine.getTextureManager().loadTexture(fontTexture);
        font = new Font(fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 24, true, Color.BLACK);
        lvl.mEngine.getFontManager().loadFont(font);

        final Text text = new Text(posX_txt,posY_txt, font,msg);
        final String Suite_Msg = suite_message;
        final Sprite background = new Sprite(PosX,PosY,LoadPopup(lvl)){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
                if (pSceneTouchEvent.isActionUp()) {
                    scene_popup.detachChild(this);
                    scene_popup.detachChild(text);
                    scene_popup.unregisterTouchArea(this);
                    if(Suite_Msg != "")
                    {
                        Popup.AfficherPopup(lvl,Suite_Msg);
                    }
                }
                return true;
            } ;
        };
        // Si plusieurs popup d'ouverte. On enleve les clic des précédentes pour les remettre dans l'ordre.
        ArrayList<Scene.ITouchArea> lArea = scene_popup.getTouchAreas();
        if(lArea.size()>1){
            int nbArea = lArea.size();
            scene_popup.registerTouchArea(background);
            for(int i=1;i<nbArea;i++)
            {
                scene_popup.registerTouchArea(lArea.get(1));
                scene_popup.unregisterTouchArea(lArea.get(1));
            }
            scene_popup.attachChild(background);
            scene_popup.attachChild(text);
        }
        else
        {
            scene_popup.registerTouchArea(background);
            scene_popup.attachChild(background);
            scene_popup.attachChild(text);
        }

        lvl.mScene.setChildScene(scene_popup);
    }
}
