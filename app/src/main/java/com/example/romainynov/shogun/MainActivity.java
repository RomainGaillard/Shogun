package com.example.romainynov.shogun;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;

public class MainActivity extends BaseGameActivity {
    private Camera mCamera;
    private Engine mEngine;
    private Level lvlPrison;
    private DataBaseHandler bdd;

    // TEMPORAIRE:
    private Scene mMainScene;
    private TextureRegion mPlayerTextureRegion;
    private BitmapTextureAtlas mBitmapTextureAtlas;
    private Sprite player;

    @Override
    public Engine onLoadEngine() {
        final Display display = getWindowManager().getDefaultDisplay();
        int cameraWidth = display.getWidth();
        int cameraHeight = display.getHeight();

        mCamera = new Camera(0, 0, cameraWidth, cameraHeight);
        bdd = new DataBaseHandler(this);
        bdd.Initialiser_Bdd();
        lvlPrison = Lvl_Definition(1);

        mEngine = new Engine(new EngineOptions(true, EngineOptions.ScreenOrientation.LANDSCAPE,
                new RatioResolutionPolicy(cameraWidth, cameraHeight), mCamera));
        return mEngine;
    }

    @Override
    public void onLoadResources() { lvlPrison.LoadRessources(this,mEngine,bdd);}

    @Override
    public Scene onLoadScene() {
        mEngine.registerUpdateHandler(new FPSLogger());
        return lvlPrison.LoadScene(getApplicationContext(), bdd);
    }

    @Override
    public void onLoadComplete() {

    }

    /* ======================================================================================
     ==========                 Définition des Levels                              ==========
     ====================================================================================== */

    private Level Lvl_Definition(int idScene)
    {
        MyScene myScene = bdd.getScene(idScene);

        ArrayList<Item> lItem = bdd.getAllItems(idScene);
        ArrayList<Pnj> lPnj = bdd.getAllPnj(idScene);

        return (new Level(myScene,lPnj,lItem));
    }
}


