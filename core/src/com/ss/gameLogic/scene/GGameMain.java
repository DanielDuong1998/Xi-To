package com.ss.gameLogic.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.objects.BoardGame;
import com.ss.gameLogic.objects.boardConfig;
import com.ss.gameLogic.objects.Static;


public class GGameMain extends GScreen {
  TextureAtlas atlas;
  TextureAtlas cardsAtlas;
  Group backgroundGroup;
  Group uiGroup;
  BoardGame boardGame;
  public Array<Vector2> positionCards;


  @Override
  public void dispose() {

  }

  @Override
  public void init() {
    backgroundGroup = new Group();
    uiGroup = new Group();
    GStage.addToLayer(GLayer.ui, backgroundGroup);
    GStage.addToLayer(GLayer.ui, uiGroup);
    atlas = GAssetsManager.getTextureAtlas("atlasXiTo.atlas");
    cardsAtlas = GAssetsManager.getTextureAtlas("Cards.atlas");
    initPositionCards();
    handlingViewport();

    Image bg = GUI.createImage(atlas, "bg2");
    Image panel = GUI.createImage(atlas, "panel");
    //Image table = GUI.createImage(atlas, "table");
    Gdx.app.log("debug", "x-y" + Static.ratioX + "-" + Static.ratioY);
    bg.setSize(bg.getWidth()* Static.ratioX, bg.getHeight()*Static.ratioY);
    //panel.setSize(bg.getWidth()* Static.ratioX, bg.getHeight()*Static.ratioY);
    //table.setSize(table.getWidth()*Static.ratioXOfficial, table.getHeight()*Static.ratioYOfficial);
    panel.setPosition(0, GMain.screenHeight - panel.getHeight());
    uiGroup.setSize(1280, 720);
    backgroundGroup.addActor(bg);
    backgroundGroup.addActor(panel);
    //uiGroup.addActor(table);
    //table.setPosition(GMain.screenWidth/2, GMain.screenHeight/2, Align.center);
    //uiGroup.setScale(Static.ratioXOfficial, Static.ratioYOfficial);
    //uiGroup.setPosition((GMain.screenWidth - 1280*Static.ratioXOfficial)/2, (GMain.screenHeight - 1280*Static.ratioYOfficial)/2);
    //uiGroup.setPosition((GMain.screenWidth - 1280*Static.ratioXOfficial)/2, (GMain.screenHeight - 1280*Static.ratioYOfficial)/2);
    Gdx.app.log("debug", "w-h: " + uiGroup.getWidth() + "-" + uiGroup.getHeight() + " rx-y: " + Static.ratioXOfficial + "-" + Static.ratioYOfficial + " w-h gmain: " + GMain.screenWidth + "-" + GMain.screenHeight);
    Gdx.app.log("w-h", "" + (GMain.screenWidth - 1280*Static.ratioXOfficial)/2 + "-" + (GMain.screenHeight - 1280*Static.ratioYOfficial)/2);
    Gdx.app.log("debug", "x-y" + uiGroup.getX() + "-" + uiGroup.getY());
    boardGame = new BoardGame(cardsAtlas, uiGroup, this);

  }

  @Override
  public void run() {

  }

  private void handlingViewport(){
    if(GMain.screenWidth >= (float)16*GMain.screenHeight/9){
      Static.ratioYOfficial = (float) GMain.screenHeight/720;
      Static.ratioXOfficial = (float) (16*GMain.screenHeight/9)/1280;
    }
    else {
      Static.ratioXOfficial = Static.ratioX;
      Static.ratioYOfficial = Static.ratioY;
    }
  }

  private void initPositionCards(){
    positionCards = new Array<>();
    int numberPlayer = boardConfig.modePlay;
    float delta2 = (GMain.screenHeight*16/9)/5;
    float delta3 = (GMain.screenHeight*16/9)/15;
    float deltaY = GMain.screenHeight/20;
    float delta = (GMain.screenWidth - (float)GMain.screenHeight*16/9)/2;
    switch (numberPlayer){
      case 2: {
        Vector2 position1 = new Vector2(delta + delta2 + delta3, GMain.screenHeight*3/4);
        Vector2 position2 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight/3 - deltaY);
        positionCards.add(position1, position2);
        break;
      }
      case 3: {
        Vector2 position1 = new Vector2(GMain.screenWidth/2, GMain.screenHeight*3/4);
        Vector2 position2 = new Vector2(delta + delta2 + delta3,GMain.screenHeight/3 - deltaY);
        Vector2 position3 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight/3 - deltaY);
        positionCards.add(position1, position2, position3);
        break;
      }
      case 4: {
        Vector2 position1 = new Vector2(delta + delta2 + delta3, GMain.screenHeight*3/4);
        Vector2 position2 = new Vector2(delta + delta2 + delta3,GMain.screenHeight/3 - deltaY);
        Vector2 position3 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight/3 - deltaY);
        Vector2 position4 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight*3/4);
        positionCards.add(position1, position2, position3, position4);
        break;
      }
      case 5: {
        Vector2 position1 = new Vector2(GMain.screenWidth/2, GMain.screenHeight*3/4);
        Vector2 position2 = new Vector2(delta + delta2,GMain.screenHeight*2/3);
        Vector2 position3 = new Vector2(delta + delta2,GMain.screenHeight/3);
        Vector2 position4 = new Vector2((float)GMain.screenHeight*16/9 - delta - delta2, GMain.screenHeight/3);
        Vector2 position5 = new Vector2((float)GMain.screenHeight*16/9 - delta - delta2, GMain.screenHeight*2/3);
        positionCards.add(position1, position2, position3, position4);
        positionCards.add(position5);
        break;
      }
      case 6: {
        Vector2 position1 = new Vector2(delta + delta2 + delta3, GMain.screenHeight*3/4);
        Vector2 position2 = new Vector2(delta + delta2 - delta3,GMain.screenHeight/2);
        Vector2 position3 = new Vector2(delta + delta2 + delta3,GMain.screenHeight/3 - deltaY);
        Vector2 position4 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight/3 - deltaY);
        Vector2 position5 = new Vector2((float)GMain.screenHeight*16/9 + delta - delta2, GMain.screenHeight/2);
        Vector2 position6 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight*3/4);
        positionCards.add(position1, position2, position3, position4);
        positionCards.add(position5, position6);
        break;
      }
      default: break;
    }
  }
}
