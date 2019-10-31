package com.ss.gameLogic.scene;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.assetManager.X;
import com.ss.core.G.G;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
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
  GLayerGroup uiGroup;
  Group GroupNewBtn;

  BoardGame boardGame;

  Image newGameBtn;
  public Array<Vector2> positionCards;
  public Array<Vector2> positionAvts;

  public Array<Long> moneyBot;
  public Array<Image> avts;
  Image settingBtn;
  int counterShowFullScreen = 0;

  @Override
  public void dispose() {

  }

  @Override
  public void init() {

    backgroundGroup = new Group();
    uiGroup = new GLayerGroup();
    GroupNewBtn = new Group();
    GStage.addToLayer(GLayer.ui, backgroundGroup);
    GStage.addToLayer(GLayer.ui, uiGroup);
    GStage.addToLayer(GLayer.ui, GroupNewBtn);

    atlas = GAssetsManager.getTextureAtlas("atlasXiTo.atlas");
    cardsAtlas = GAssetsManager.getTextureAtlas("Cards.atlas");

    initPositionCards();
    handlingViewport();

    Image bg = (Image) G.c(Image.class).k("bg3").add(backgroundGroup).ub();
    Image panel = (Image)G.c(Image.class).k("panel").add(backgroundGroup).ub();


    G.b(panel).p(0, GMain.screenHeight - panel.getHeight());
    uiGroup.setSize(1280, 720);

    //initmoneybot
    initMoneyBots();

    //initAvt
    initAvt();

    boardGame = new BoardGame(cardsAtlas, uiGroup, this);

  }



  private void initMoneyBots(){
    moneyBot = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      long money = boardConfig.moneyBasic*100 + (long)(Math.floor(Math.random()*20 + 1)*boardConfig.moneyBasic);
      System.out.println("money: " + money);
      moneyBot.add(money);
    }
  }

  private void initAvt(){
    avts = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      if(i == 0){
        Image avt = GUI.createImage(cardsAtlas, "am");
        avt.setPosition(positionAvts.get(0).x, positionAvts.get(0).y, Align.center);
        backgroundGroup.addActor(avt);
        avts.add(avt);
      }
      else{
        Image avt = GUI.createImage(cardsAtlas, "ab" + i);
        avt.setPosition(positionAvts.get(i).x, positionAvts.get(i).y, Align.center);
        backgroundGroup.addActor(avt);
        avts.add(avt);
      }
    }
  }

  public void newGame(){
    if(newGameBtn != null){
      newGameBtn.remove();
      newGameBtn.clear();
    }
    newGameBtn = (Image)G.c(Image.class).k("newGame").add(GroupNewBtn).ub();
    G.b(newGameBtn).p(GMain.screenWidth/2 - newGameBtn.getWidth()/2, GMain.screenHeight/2 - newGameBtn.getHeight()/2).v(true).o(Align.center).ub();
    addListennerNewGameBtn(newGameBtn);
  }

  private void addListennerNewGameBtn(Image image){
    G.b(image).clk((e, x, y)->{
      G.b(image).touch(Touchable.disabled).ub();
      //sound
      X.getSound("buttonClick").play();
      image.addAction(Actions.sequence(
        Actions.scaleTo(0, 0, 0.1f, Interpolation.linear),
        GSimpleAction.simpleAction((d, a)->{
          newBoard();
          return true;
        })
      ));
    });
  }

  private void newBoard(){
    counterShowFullScreen++;
    if(counterShowFullScreen==boardConfig.timeShowFullScreen){
      counterShowFullScreen = 0;
      GMain.platform.ShowFullscreen();
    }
    boardGame.dispose();
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
    positionAvts = new Array<>();

    int numberPlayer = boardConfig.modePlay;
    float delta2 = (GMain.screenHeight*16/9)/5;
    float delta3 = (GMain.screenHeight*16/9)/15;
    float delta4 = (GMain.screenHeight*16/9)/30;
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
        Vector2 position1 = new Vector2(GMain.screenWidth/2 - delta4, GMain.screenHeight*3/4);
        Vector2 position2 = new Vector2(delta + delta2 + delta3,GMain.screenHeight/3 - deltaY);
        Vector2 position3 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight/3 - deltaY);
        positionCards.add(position1, position2, position3);

        Vector2 aP1 = new Vector2(position1.x - GMain.screenWidth/10, position1.y);
        Vector2 aP2 = new Vector2(position2.x - GMain.screenWidth/15, position2.y);
        Vector2 aP3 = new Vector2(position3.x + GMain.screenWidth/7, position3.y);
        positionAvts.add(aP1, aP2, aP3);
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
        Vector2 position1 = new Vector2(GMain.screenWidth/2 - delta4, GMain.screenHeight*3/4);
        Vector2 position2 = new Vector2(delta + delta2,GMain.screenHeight*2/3);
        Vector2 position3 = new Vector2(delta + delta2,GMain.screenHeight/3);
        Vector2 position4 = new Vector2((float)GMain.screenHeight*16/9 - delta - delta2 - delta3, GMain.screenHeight/3);
        Vector2 position5 = new Vector2((float)GMain.screenHeight*16/9 - delta - delta2 - delta3, GMain.screenHeight*2/3);
        positionCards.add(position1, position2, position3, position4);
        positionCards.add(position5);

        Vector2 aP1 = new Vector2(position1.x - GMain.screenWidth/10, position1.y);
        Vector2 aP2 = new Vector2(position2.x - GMain.screenWidth/15, position2.y);
        Vector2 aP3 = new Vector2(position3.x - GMain.screenWidth/15, position3.y);
        Vector2 aP4 = new Vector2(position4.x + GMain.screenWidth/7, position4.y);
        Vector2 aP5 = new Vector2(position5.x + GMain.screenWidth/7, position5.y);
        positionAvts.add(aP1, aP2, aP3);
        positionAvts.add(aP4, aP5);
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
