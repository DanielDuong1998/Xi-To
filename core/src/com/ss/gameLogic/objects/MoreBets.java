package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.G.G;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;

public class MoreBets {
    TextureAtlas uiAtlas;
    BoardGame boardGame;
    BitmapFont font;
    Group group = new Group();
    float xmin, xmax, y;


    MoreBets(TextureAtlas uiAtlas, BoardGame boardGame){
        GStage.addToLayer(GLayer.top,group);
        this.uiAtlas = uiAtlas;
        this.boardGame = boardGame;
        initfont();
        showbgMoreBets();

    }
    void showbgMoreBets(){
        group.setPosition(GMain.screenWidth/2,GMain.screenHeight/2,Align.center);
        group.setOrigin(Align.center);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GMain.screenWidth/2,-GMain.screenHeight/2, GMain.screenWidth*2, GMain.screenHeight*2);
        blackOverlay.setColor(0,0,0,0.8f);
        group.addActor(blackOverlay);
        loadSlider();


    }
    void loadSlider(){
        /////// bar//////
        Image sliderBar = (Image) G.c(Image.class).k("slideBar").add(group).ub();
        G.b(sliderBar).p(sliderBar.getX()-sliderBar.getWidth()/2, sliderBar.getY() - sliderBar.getHeight()/2);

        /////// coins ///////
        Image sliderCoins = (Image)G.c(Image.class).k("slideCoin").add(group).ub();
        float pX = -sliderBar.getWidth()/2+sliderCoins.getWidth()/2 - sliderCoins.getWidth()/2, pY = -sliderCoins.getHeight()/2;
        G.b(sliderCoins).p(pX, pY);

        xmin = -sliderBar.getWidth()/2+sliderCoins.getWidth()/2;
        xmax = 220;
        /////// btn Done //////
        Image btnDone = (Image)G.c(Image.class).k("btnDone").add(group).o(Align.center).ub();
        pX = 0 - btnDone.getWidth()/2; pY = 200 - btnDone.getHeight()/2;
        G.b(btnDone).p(pX, pY);

        //////// event btn ////////
        eventBtnDone(btnDone);
        eventBtnCoins(sliderCoins);

    }
    void eventBtnCoins(Image btn){
        btn.addListener(new DragListener(){
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                super.drag(event, x, y, pointer);
                float detal = x - btn.getWidth()/2;
                if(btn.getX() + detal < xmin - btn.getWidth()/2){
                    btn.setX(xmin-btn.getWidth()/2);
                }
                else if(btn.getX() + detal > xmax){
                    btn.setX(xmax);
                }
                else btn.moveBy(x-btn.getWidth()/2, 0);
                Gdx.app.log("debug", "x: " + btn.getX());
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                super.dragStop(event, x, y, pointer);
                float divindend = 8;
                float part = 490/divindend;
                int div = (int) ((btn.getX() + 270)/part);
                Gdx.app.log("debug","div: " + div + " part: " + part);
                if(btn.getX() == -270){
                    btn.setX(xmin-btn.getWidth()/2);
                }
                else if(btn.getX() == 220|| div*490/divindend - 270 > 220)
                    btn.setX(220);
                else btn.setX(div*490/divindend - 270);
            }
        });
    }
    void eventBtnDone(Image btnDone){
        G.b(btnDone).clk((e,x,y)->{
            btnDone.addAction(Actions.sequence(
                Actions.scaleTo(0.8f,0.8f,0.2f),
                Actions.scaleTo(1f,1f,0.2f)
            ));
            Tweens.setTimeout(group,0.4f,()->{
                boardGame.clickBetBtn();
                group.clear();
            });
        });

//
    }
    void initfont(){
        font = GAssetsManager.getBitmapFont("gold.fnt");

    }
}
