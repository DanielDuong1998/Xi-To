package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.P;
import com.ss.assetManager.X;
import com.ss.commons.Tweens;
import com.ss.core.G.AB;
import com.ss.core.G.G;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.action.exAction.GTemporalAction;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.effect.SoundEffect;
import com.ss.gameLogic.effect.effectWin;
import com.ss.gameLogic.scene.GGameMain;
import com.ss.gameLogic.scene.GameStart;

import java.text.DecimalFormat;
import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class BoardGame {
  TextureAtlas cardAtlas;
  GLayerGroup group;
  Group groupBtn;
  Group groupImageSkip;
  Group groupFinal;
  Group groupSetting;
  Group groupparticle;
  Array<Card> allCard = new Array<>();
  Array<Integer> cards;
  GGameMain game;
  Array<Array<Card>> cardsPlay;
  Array<Group> cardsGroup;
  int indexCard = 0;
  int turnGame = 0;
  int turnStart = 0;
  boolean flagDistribute = false, isHandEffectShow = false;
  int turnDistribute = 3;
  Image btnSkip, btnEspouse, btnAddMore;
  Array<Integer> passList;
  Array<Integer> allMoney1;
  Array<Integer> allMoney2;
  Array<Integer> betList;
  Array<Image> SkipImgBotArr = new Array<>();
  Image hand, circle;
  Array<Array<Image>> aniImage;
  Array<Array<Integer>> cardsInt;

  long moneyBasic = boardConfig.moneyBasic;
  long totalMoney = 0;
  GreedilyAlgorithm totalMoneyChip;

  BitmapFont font1, font2, font3;
  public Array<Label> moneyTxts;
  Array<Label> typeCards;
  Array<Image> frameMoneys;
  Array<Label> noticeLabel;
  Array<Label> moneyFinal;
  Array<Image> framesFinal;

  Image frameNotice, totalMoneyFrame;
  Array<Array<Image>> winloseImage;

  Array<Long> moneyBet;
  int indexTo = -1;
  Image settingBtn;
  Image lightTurn;

  public BoardGame(TextureAtlas cardAtlas, GLayerGroup group, GGameMain game){
    this.cardAtlas = cardAtlas;
    this.group = group;
    this.groupBtn = new Group();
    this.groupImageSkip = new Group();
    this.groupparticle = new Group();
    totalMoneyChip = new GreedilyAlgorithm(cardAtlas, group);
    GStage.addToLayer(GLayer.ui, groupparticle);
    GStage.addToLayer(GLayer.top, groupBtn);
    GStage.addToLayer(GLayer.top, groupImageSkip);
    this.game = game;
    turnGame = turnStart;
    initFrameNotice();
    initPassList();
    initBetList();
    initCards();
    initCardsInt();
    initAniImage();
    loadSkipImg();
    initCardsGroup();
    initMoneyTxt();
    initTypeCards();
    initWinloseImage();
    initMoneyBet();
    initMoneyFinal();
    initSettingBtn();
    renderCard();
    Tweens.setTimeout(group, 1f, ()->{
      X.getSound("slideFirstChips").play();
      betStart();
    });
    Tweens.setTimeout(group, 2f, ()->{
      updateFrameNotice();
      distributeCardsOutSide();
    });

//    Array<Integer> a = new Array<>();
//    Array<Integer> b = new Array<>();
//    P.makeCards();
//    a.add(P.nameMap.getKey("QB", false),
//        P.nameMap.getKey("AC", false),
//        P.nameMap.getKey("QR", false),
//        P.nameMap.getKey("7C", false)); //Aco - 8co
//    a.add(P.nameMap.getKey("JR", false));
//
//    b.add(P.nameMap.getKey("9R", false),
//        P.nameMap.getKey("10C", false),
//        P.nameMap.getKey("JB", false),
//        P.nameMap.getKey("KC", false)); //Aco - 8co
//    b.add(P.nameMap.getKey("9C", false));
//
//    System.out.println(Integer.toBinaryString(P.check(a)));
//    System.out.println(Integer.toBinaryString(P.check(b)));
//
//    if(P.compare(b, a) == -1){
//      System.out.println("a > b");
//    }
//    else {
//      System.out.println("b > a");
//    }
//
//    System.out.println("a1: " + CheckCard.nameMap.get(a.get(0)));
//    System.out.println("a2: " + CheckCard.nameMap.get(a.get(1)));
//    System.out.println("a3: " + CheckCard.nameMap.get(a.get(2)));
//    System.out.println("a4: " + CheckCard.nameMap.get(a.get(3)));
//    System.out.println("a5: " + CheckCard.nameMap.get(a.get(4)));
//    System.out.println("--------------------------------------");
//    System.out.println("b1: " + CheckCard.nameMap.get(b.get(0)));
//    System.out.println("b2: " + CheckCard.nameMap.get(b.get(1)));
//    System.out.println("b3: " + CheckCard.nameMap.get(b.get(2)));
//    System.out.println("b3: " + CheckCard.nameMap.get(b.get(3)));
//    System.out.println("b3: " + CheckCard.nameMap.get(b.get(4)));

  }

  private void initSettingBtn(){
    groupSetting = new Group();
    GStage.addToLayer(GLayer.top, groupSetting);
    settingBtn = GUI.createImage(cardAtlas, "btnSetting");
    groupSetting.addActor(settingBtn);
    settingBtn.setAlign(Align.center);
    settingBtn.setOrigin(Align.center);
    settingBtn.setPosition(GMain.screenWidth-settingBtn.getWidth()/2,settingBtn.getHeight()/2, Align.center);
    addClick(settingBtn);
  }

  private void addClick(Image image){
    image.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        image.setTouchable(Touchable.disabled);
        group.setPause(true);
        image.addAction(Actions.sequence(
          Actions.scaleBy(-0.5f, -0.5f, 0.1f, Interpolation.linear),
          Actions.scaleBy(0.5f, 0.5f, 0.1f, Interpolation.linear),
          GSimpleAction.simpleAction((d, a)->{
            new Setting(cardAtlas, image, game, group);
            return true;
          })
        ));
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  private void initMoneyFinal(){
    groupFinal = new Group();
    GStage.addToLayer(GLayer.top, groupFinal);
    moneyFinal = new Array<>();
    framesFinal = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      Image frame = GUI.createImage(cardAtlas, "frameMoney");

      groupFinal.addActor(frame);
      frame.setPosition(game.positionCards.get(i).x + frame.getWidth()/4,game.positionCards.get(i).y + frame.getHeight()/3,Align.center);
      frame.setVisible(false);
      framesFinal.add(frame);

      Label lb = new Label("1", new Label.LabelStyle(font1, Color.YELLOW));
      lb.setFontScale(0.5f);
      groupFinal.addActor(lb);
      moneyFinal.add(lb);
      lb.setPosition(game.positionCards.get(i).x,game.positionCards.get(i).y);
      lb.setVisible(false);
    }

  }

  private int checkUpCard(){
    int index = -1, dem = 0;
    for(int i = 0; i < boardConfig.modePlay; i++){
      if(passList.get(i) == 1){
        dem++;
      }
      else index = i;
    }
    if(dem == boardConfig.modePlay - 1){
      return index;
    }
    return -1;
  }



  private void initMoneyBet(){
    moneyBet = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      long bet = 0;
      moneyBet.add(bet);
    }
  }

  private void updateWinLoseMoney(int index, long win, long lose){
    for(int i = 0; i < boardConfig.modePlay; i++){
      if(allMoney1.get(i) == 0 && passList.get(i) == 0)
      moneyBet.set(i, win);
    }
  }

  private void initWinloseImage(){
    winloseImage = new Array<>();
    Array<Image> win = new Array<>();
    Array<Image> lose = new Array<>();
    winloseImage.add(win,lose);
    for(int i = 0; i < boardConfig.modePlay; i++){
      Image win1 = GUI.createImage(cardAtlas, "winVi");
      win1.setSize(win1.getWidth()*0.6f, win1.getHeight()*0.6f);
      win1.setAlign(Align.center);
      win1.setPosition(game.positionCards.get(i).x + win1.getWidth()/4,game.positionCards.get(i).y - 0.5f*win1.getHeight(), Align.center);
      group.addActor(win1);
      win1.setVisible(false);
      win.add(win1);
    }

    for(int i = 0; i < boardConfig.modePlay; i++){
      Image lose1 = GUI.createImage(cardAtlas, "loseVi");
      lose1.setSize(lose1.getWidth()*0.6f, lose1.getHeight()*0.6f);
      lose1.setAlign(Align.center);
      lose1.setPosition(game.positionCards.get(i).x + lose1.getWidth()/4,game.positionCards.get(i).y - 0.5f*lose1.getHeight(), Align.center);
      group.addActor(lose1);
      lose1.setVisible(false);
      lose.add(lose1);
    }
  }

  private void showWinloseImage(int winner){
    winloseImage.get(0).get(winner).setVisible(true);
    for(int i = 0; i < boardConfig.modePlay; i++){
      if(i != winner){
        winloseImage.get(1).get(i).setVisible(true);
      }
    }

    long moneyWinner = moneyBet.get(winner);
    long finalMoneyWinner = moneyWinner;


    for(int i = 0; i < boardConfig.modePlay; i++){
      if(i != winner){
        if(moneyBet.get(i) > moneyWinner){
          finalMoneyWinner += moneyWinner;
          System.out.println("tien du cua nguoi thu " + i + ": " + (moneyBet.get(i) - moneyWinner));
          moneyFinal.get(i).setText("-" + FortmartPrice(moneyWinner));
          framesFinal.get(i).setVisible(true);
          moneyFinal.get(i).setVisible(true);
          if(i == 0){
            counterMonneyUp(moneyTxts.get(0), boardConfig.moneyPlayer,moneyBet.get(i) - moneyWinner,1);
            boardConfig.moneyPlayer += moneyBet.get(i) - moneyWinner;
            GMain.prefs.putLong("myMonney", boardConfig.moneyPlayer);
            GMain.prefs.flush();
          }
          else {
            long money = game.moneyBot.get(i - 1);
            long moneyDelta = moneyBet.get(i) - moneyWinner;
            counterMonneyUp(moneyTxts.get(i), money, moneyDelta,1);
            game.moneyBot.set(i-1, money + moneyDelta);
          }
        }
        else {
          finalMoneyWinner += moneyBet.get(i);
          framesFinal.get(i).setVisible(true);
          moneyFinal.get(i).setVisible(true);
          moneyFinal.get(i).setText("-" + FortmartPrice(moneyBet.get(i)));
          System.out.println("tien nguoi thu " + i + ": " + moneyBet.get(i));
        }
      }
    }
    if(winner == 0){
      counterMonneyUp(moneyTxts.get(0), boardConfig.moneyPlayer, finalMoneyWinner, 1);
      boardConfig.moneyPlayer += finalMoneyWinner;
      GMain.prefs.putLong("myMonney", boardConfig.moneyPlayer);
      GMain.prefs.flush();
    }
    else {
      long money = game.moneyBot.get(winner-1);
      counterMonneyUp(moneyTxts.get(winner), money, finalMoneyWinner, 1);
      game.moneyBot.set(winner-1, money + finalMoneyWinner);
    }
    //updateMoneyTxt();
    moneyFinal.get(winner).setText("+" + FortmartPrice(finalMoneyWinner - moneyBet.get(winner)));
    framesFinal.get(winner).setVisible(true);
    moneyFinal.get(winner).setVisible(true);
    System.out.println("tien winner cuoc: " + moneyWinner);
    System.out.println("tien winner: " + finalMoneyWinner);
    Tweens.setTimeout(group, 0.3f, ()->{
      totalMoneyChip.moveChipsWin(winner, game.positionCards.get(winner).x,game.positionCards.get(winner).y,0.3f, fastSlow, ()->{
        totalMoneyChip.disposeChips();
      });
      checkMoney();
    });
  }

  private void checkMoney(){
    if(boardConfig.moneyPlayer <= boardConfig.moneyBasic){
      System.out.println("ban khong du tien de choi o ban nay");
      Tweens.setTimeout(group, 2f, ()->{
        game.setScreen(new GameStart());
      });

      return;
    }

    for(int i = 0; i < game.moneyBot.size; i++){
      if(game.moneyBot.get(i) <= boardConfig.moneyBasic){
        updateMoneyBot(i);
      }
    }

    Tweens.setTimeout(group, 1, ()->{
      endARound();
    });
  }

  private void updateMoneyBot(int index){
    long money = boardConfig.moneyBasic*100 + (long)(Math.floor(Math.random()*20 + 1)*boardConfig.moneyBasic);
    game.moneyBot.set(index, money);
    game.avts.get(index+1).addAction(sequence(
        Actions.moveBy(-2000, 0, 0.5f, fastSlow),
        Actions.moveBy(2000, 0, 0.5f, fastSlow),
        GSimpleAction.simpleAction((d, a)->{
          updateMoneyTxt();
          return true;
        })
    ));
  }

  private void betStart(){
    float w = GMain.screenWidth, h = GMain.screenHeight;
    System.out.println("so nguoi choi: " + boardConfig.modePlay);
    for(int i = 0; i < boardConfig.modePlay; i++){
      if(i == 0){
        counterMonneyDown(moneyTxts.get(i), boardConfig.moneyPlayer, boardConfig.moneyBasic,1);
        boardConfig.moneyPlayer -= boardConfig.moneyBasic;
        GMain.prefs.putLong("myMonney", boardConfig.moneyPlayer);
        GMain.prefs.flush();
      }
      else {
        counterMonneyDown(moneyTxts.get(i), game.moneyBot.get(i-1), boardConfig.moneyBasic, 1);
        game.moneyBot.set(i-1, game.moneyBot.get(i-1) - boardConfig.moneyBasic);
      }

      final int itemp = i;
      GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(cardAtlas, group);
      greedilyAlgorithm.renderChip(boardConfig.moneyBasic, game.positionCards.get(i).x, game.positionCards.get(i).y, false);
      greedilyAlgorithm.moveChips(i,w/2 - w/30, h/4 + h/10, 0.3f, fastSlow, ()->{
        greedilyAlgorithm.disposeChips();
        totalMoney+=moneyBasic;
        moneyBet.set(itemp, moneyBasic);
        System.out.println("tien nguoi thu " + itemp + ": " + moneyBet.get(itemp));
        totalMoneyChip.renderChip(totalMoney,w/2 - w/8, h/4, true);
      });
    }
  }

  private void initFrameNotice(){
    font3 = GAssetsManager.getBitmapFont("fontVn.fnt");
    frameNotice = GUI.createImage(cardAtlas, "frameNotice");
    totalMoneyFrame = GUI.createImage(cardAtlas, "totalMoneyFrame");

    frameNotice.setSize(frameNotice.getWidth()*0.9f, frameNotice.getHeight()*0.9f);
    totalMoneyFrame.setSize(totalMoneyFrame.getWidth()*1.2f, totalMoneyFrame.getHeight()*1.2f);

    totalMoneyFrame.setPosition(GMain.screenWidth/2, GMain.screenHeight/2, Align.center);
    group.addActor(frameNotice);
    group.addActor(totalMoneyFrame);

    noticeLabel = new Array<>();
    Label lb1 = new Label(FortmartPrice(boardConfig.moneyBasic), new Label.LabelStyle(font3, null));
    Label lb2 = new Label("" + boardConfig.modePlay, new Label.LabelStyle(font3, null));
    Label lb3 = new Label(FortmartPrice(moneyBasic), new Label.LabelStyle(font3, null));
    Label lb4 = new Label("" + totalMoney, new Label.LabelStyle(font3, null));
    lb1.setPosition(frameNotice.getWidth()*3/7, frameNotice.getHeight()*1/30);
    lb2.setPosition(frameNotice.getWidth()*3/7, frameNotice.getHeight()*4/13);
    lb3.setPosition(frameNotice.getWidth()*3/7, frameNotice.getHeight()*8/14);
    lb4.setPosition(GMain.screenWidth/2, GMain.screenHeight/2 - 5, Align.center);
    lb1.setFontScale(0.6f);
    lb3.setFontScale(0.6f);
    lb2.setFontScale(0.6f);
    lb4.setFontScale(0.6f);
    lb4.setAlignment(Align.center);
    lb4.setOrigin(Align.center);

    group.addActor(lb1);
    group.addActor(lb2);
    group.addActor(lb3);
    group.addActor(lb4);
    noticeLabel.add(lb1, lb2, lb3, lb4);

  }

  private void updateFrameNotice(){

    noticeLabel.get(2).setText(FortmartPrice(moneyBasic));
    noticeLabel.get(3).setText(FortmartPrice(totalMoney));
  }

  private void showEffectWin(){
    for(int i = 0; i < cardsInt.size; i++){
      if(passList.get(i) == 0){
        float pX = game.positionAvts.get(i).x, pY = game.positionAvts.get(i).y;
        float h = GMain.screenHeight;
        effectWin effectWinTemp = new effectWin((P.check(cardsInt.get(i))>>13), pX, pY);
        group.addActor(effectWinTemp);
      }
    }
  }

  private void initTypeCards(){
    typeCards = new Array<>();
    font2 = GAssetsManager.getBitmapFont("fontVn1.fnt");
    for(int i = 0; i < boardConfig.modePlay; i++){
      Label typeCard = new Label("", new Label.LabelStyle(font2, null));
      typeCard.setAlignment(Align.center);
      group.addActor(typeCard);
      typeCards.add(typeCard);
    }

    float h = GMain.screenHeight;
    for(int i = 0; i < boardConfig.modePlay; i++){
      typeCards.get(i).setPosition(game.positionCards.get(i).x,game.positionCards.get(i).y - h/8 , Align.center);
      typeCards.get(i).setVisible(false);
    }
  }

  private void showTypeCards(int sizeCard){
    boolean isFiveCard = sizeCard == 6 ? true : false;
    //makeCardsInt(isFiveCard);
    for(int i = 0; i < cardsInt.size; i++){
      if(passList.get(i) == 0){
        typeCards.get(i).setText(P.m.get(P.check(cardsInt.get(i)) >> 13));
        typeCards.get(i).setVisible(!isFiveCard);
      }
    }
  }

  private void hiddenTypeCards(){
    for(int i = 0; i < boardConfig.modePlay; i++){
      typeCards.get(i).setVisible(false);
    }
  }

  private void initMoneyTxt(){
    frameMoneys = new Array<>();
    font1 = GAssetsManager.getBitmapFont("gold.fnt");
    moneyTxts = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      Image frameMoney = GUI.createImage(cardAtlas, "frameMoney");
      frameMoney.setAlign(Align.center);
      group.addActor(frameMoney);
      frameMoneys.add(frameMoney);

      Label moneyTxt = new Label("", new Label.LabelStyle(font1, null));
      moneyTxt.setAlignment(Align.center);
      group.addActor(moneyTxt);
      moneyTxts.add(moneyTxt);
    }

    for(int i = 0; i < boardConfig.modePlay; i++){
      if(i == 0){
        float h = GMain.screenHeight;
        frameMoneys.get(0).setVisible(false);
        moneyTxts.get(0).setText(FortmartPrice(boardConfig.moneyPlayer));
        moneyTxts.get(0).setPosition(game.positionCards.get(0).x, game.positionCards.get(0).y + h/6);
      }
      else {
        float h = GMain.screenHeight;
        frameMoneys.get(i).setPosition(game.positionAvts.get(i).x,game.positionAvts.get(i).y + h/12, Align.center);

        moneyTxts.get(i).setText(FortmartPrice(game.moneyBot.get(i-1)));
        moneyTxts.get(i).setFontScale(0.4f);
        moneyTxts.get(i).setOrigin(Align.center);
        moneyTxts.get(i).setPosition(game.positionAvts.get(i).x,game.positionAvts.get(i).y + h/12);
      }
    }
  }

  private void updateMoneyTxt(){
    String moneyStr;
    for(int i = 0; i < boardConfig.modePlay; i++){
      long money = i == 0 ? boardConfig.moneyPlayer : game.moneyBot.get(i-1);
      moneyStr = FortmartPrice(money);
      moneyTxts.get(i).setText(moneyStr);
    }
  }

  private void initAniImage(){
    aniImage = new Array<>();
    for(int i = 0; i  < boardConfig.modePlay; i++){
      Array<Image> img = new Array<>();
      aniImage.add(img);
      for(int j = 0; j < 3; j++){
        String str = j == 0 ? "noticeTheo" : j == 1 ? "noticeTo" : "noticeXalang";
        Image image = GUI.createImage(cardAtlas, str);
        img.add(image);
        image.setVisible(false);
      }
    }
  }


  private void initPassList(){
    passList = new Array<>();
    allMoney1 = new Array<>();
    allMoney2 = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      int a = 0, b = 0, c = 0;
      passList.add(a);
      allMoney1.add(b);
      allMoney2.add(c);
    }
  }

  private void initBetList(){
    betList = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      int a = -1;
      betList.add(a);
    }
  }

  private void initCardsInt(){
    cardsInt = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      Array<Integer> playerCardsInt = new Array<>();
      cardsInt.add(playerCardsInt);
    }
  }

  private void resetBetList(){
    for(int a : betList){
      a = -1;
    }
  }

  void renderCard(){
    cards = CheckCard.makeCards();
    for(int i=boardConfig.NumCard + 1;i<53;i++){
      Card card = new Card(cardAtlas,group,i);
      card.setVisibleTiledown(false);
      card.card.setVisible(false);

      allCard.add(card);
    }
    for (int i=0; i < allCard.size; i++){
      allCard.get(i).setKey(cards.get(i+boardConfig.NumCard));
    }
    allCard.shuffle();
  }

  void distributeCardsOutSide(){
    allCard.shuffle();
    distributeCardInside();
  }

  void distributeCardInside(){
    if(indexCard >= game.positionCards.size*2){
      Tweens.setTimeout(group, 0.1f, ()->{
        // todo: complete distribute Cards, todo somethings.
        moveCardsStart();
      });
      return;
    }


    Card card = new Card(cardAtlas, cardsGroup.get(turnGame%boardConfig.modePlay), allCard.get(indexCard).valueName);
    card.setKey(allCard.get(indexCard).Key);
    cardsPlay.get(turnGame%boardConfig.modePlay).add(card);
    allCard.get(indexCard).card.setVisible(false);
    allCard.get(indexCard).setVisibleTiledown(false);

    float detalX = indexCard >= game.positionCards.size ? allCard.get(0).card.getWidth()/3: 0;
    final int indexTemp = indexCard+1;

    G.b(card.card).r(180).p(GMain.screenWidth/2, GMain.screenHeight/4);
    G.b(card.tileDown).r(180).p(GMain.screenWidth/2, GMain.screenHeight/4);

    //sound throw cards
    //X.getSound("throwCard").play();
    SoundEffect.Play(SoundEffect.throwCard);

    card.card.addAction(parallel(
        moveTo(game.positionCards.get(turnGame%boardConfig.modePlay).x + detalX - allCard.get(indexCard).card.getWidth()/2,game.positionCards.get(turnGame%boardConfig.modePlay).y - allCard.get(indexCard).card.getHeight()/2, 0.3f, linear),
        rotateTo(0,0.3f, linear)
    ));
    card.tileDown.addAction(parallel(
        moveTo(game.positionCards.get(turnGame%boardConfig.modePlay).x + detalX - allCard.get(indexCard).card.getWidth()/2,game.positionCards.get(turnGame%boardConfig.modePlay).y - allCard.get(indexCard).card.getHeight()/2, 0.3f, linear),
        rotateTo(0,0.3f, linear)
    ));
    Tweens.setTimeout(group, 0.2f, ()->{
      indexCard++;
      turnGame++;
      distributeCardInside();
    });
  }

  private void initCards(){
    cardsPlay = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      Array<Card> cards = new Array<>();
      cardsPlay.add(cards);
    }
  }

  private void chooseCards(){
    moveCardsStart();
  }

  private void moveCardsStart(){
    int count = 0;
    float rationScale = 1.7f;

    //sound
    //X.getSound("takeCard").play();
    SoundEffect.Play(SoundEffect.takeCard);
    for(Card card : cardsPlay.get(0)){
      //todo: duaration boardConfig.durationDistrbute
      card.moveCard(GMain.screenWidth/2 + (count-0.5f)*boardConfig.widthCard*(0.4f+0.4f*0.7f),GMain.screenHeight/2);
      card.scaleCard(rationScale,rationScale);
      count++;
    }
    Tweens.setTimeout(group, boardConfig.durationDistrbute + 0.2f, ()->{
      showCards(rationScale);
    });
  }

  private void showCards(float ratioScale){
    for(Card card : cardsPlay.get(0)){
      card.flipCard(ratioScale, ratioScale);
      card.setColor(Color.GRAY);
      card.addClick(this);
    }

  }

  public void isClickCard(){
    Gdx.app.log("debug", "clicked!!!");

    if(cardsPlay.get(0).get(0).isClick){
      cardsPlay.get(0).get(1).isDown = true;
    }
    else cardsPlay.get(0).get(0).isDown = true;

    for(Card card : cardsPlay.get(0)){
      G.b(card.card).touch(Touchable.disabled);
      float pX = card.isClick ? game.positionCards.get(0).x + boardConfig.widthCard*0.4f/3 : game.positionCards.get(0).x;
      //todo: swap ZIndex
      if(card.isClick && cardsPlay.get(0).indexOf(card, true) == 0){
        int ZIndex1 = cardsPlay.get(0).get(0).card.getZIndex();
        int ZIndex2 = cardsPlay.get(0).get(0).card.getZIndex();

        cardsPlay.get(0).get(0).card.setZIndex(cardsPlay.get(0).get(1).card.getZIndex());
        cardsPlay.get(0).get(1).card.setZIndex(ZIndex1);

        cardsPlay.get(0).get(0).tileDown.setZIndex(cardsPlay.get(0).get(1).tileDown.getZIndex());
        cardsPlay.get(0).get(1).tileDown.setZIndex(ZIndex2);
      }
      card.setVisibleTiledown(false);
      card.moveCard(pX, game.positionCards.get(0).y);
      card.scaleCard(1, 1);
    }

    Tweens.setTimeout(group, 0.3f, ()->{
      showCardBots();
    });
  }


  private void showCardBots(){
    for(int i = 1; i < boardConfig.modePlay; i++){
      int percent = (int)Math.floor(Math.random()*70 + 1);
      if(cardsPlay.get(i).get(0).Key > cardsPlay.get(i).get(1).Key){
        cardsPlay.get(i).get(1).flipCard(1, 1);
        cardsPlay.get(i).get(0).isDown = true;
      }
      else {
        cardsPlay.get(i).get(0).flipCard(1, 1);
        cardsPlay.get(i).get(1).isDown = true;
        Vector2 positionTemp = new Vector2(cardsPlay.get(i).get(0).card.getX(), cardsPlay.get(i).get(0).card.getY());
        cardsPlay.get(i).get(0).setPosition(cardsPlay.get(i).get(1).card.getX(), cardsPlay.get(i).get(1).card.getY());
        cardsPlay.get(i).get(1).setPosition(positionTemp.x, positionTemp.y);
        swapZIndexCards(cardsPlay.get(i).get(0),cardsPlay.get(i).get(1));
      }
    }


    distributeCardPlay();
  }

  private void distributeCardPlay(){
    hiddenImgs();
    moneyBasic = boardConfig.moneyBasic;
    if(turnDistribute == 6){
      //todo: goi ham chung tien, show card -> end game
      showCardEnd();
      boolean isFiveCard = turnDistribute == 6 ? true : false;
      makeCardsInt(isFiveCard);
      int winner = findMax();
      if(winner == 0){
        //X.getSound("win").play();
        SoundEffect.Play(SoundEffect.win);
      }
      else {
        //X.getSound("lose").play();
        SoundEffect.Play(SoundEffect.lose);
      }
      Gdx.app.log("debug", "endfind: " + winner);
      return;
    }

    if(turnGame%boardConfig.modePlay == turnStart){
      if(!flagDistribute){
        flagDistribute = !flagDistribute;
      }
      else{
        flagDistribute = !flagDistribute;
        turnDistribute++;
        //todo: goi ham to
        boolean isFiveCard = turnDistribute == 6 ? true : false;
        makeCardsInt(isFiveCard);
        Gdx.app.log("debug", "find: " + findMax());
        if(turnDistribute == 6){
          for(int index : passList){
            if(index == 0){
              turnStart = passList.indexOf(index, true);
              break;
            }
          }
        }
        else {
          turnStart = findMax();
        }
        turnGame = turnStart;

        showTypeCards(turnDistribute);
        Tweens.setTimeout(group, 1f, ()->{
          bet();
        });
        return;
      }
    }

    int a = passList.get(turnGame%boardConfig.modePlay);
    if(a == 1){
      turnGame++;
      distributeCardPlay();
      return;
    }

    //todo:: kiem tra neu da up bai tang turn len
    Card card = new Card(cardAtlas, cardsGroup.get(turnGame%boardConfig.modePlay), allCard.get(indexCard).valueName);
    card.setKey(allCard.get(indexCard).Key);
    allCard.get(indexCard).card.setVisible(false);
    allCard.get(indexCard).setVisibleTiledown(false);

    if(turnGame%boardConfig.modePlay == 0 && turnDistribute == 5){
      card.addClickTileDown(this);
    }

    int turnDistributeTemp = turnDistribute;

    float detalX = indexCard >= game.positionCards.size ? cardsPlay.get(turnGame%boardConfig.modePlay).size*allCard.get(0).card.getWidth()/3: 0;

    G.b(card.card).r(180).p(GMain.screenWidth/2, GMain.screenHeight/4);
    G.b(card.tileDown).r(180).p(GMain.screenWidth/2, GMain.screenHeight/4);


    //sound

//    X.getSound("throwCard").play();
    SoundEffect.Play(SoundEffect.throwCard);
    card.card.addAction(parallel(
        moveTo(game.positionCards.get(turnGame%boardConfig.modePlay).x + detalX - allCard.get(indexCard).card.getWidth()/2,game.positionCards.get(turnGame%boardConfig.modePlay).y - allCard.get(indexCard).card.getHeight()/2, 0.3f, linear),
        rotateTo(0,0.3f, linear)
    ));
    card.tileDown.addAction(parallel(
        moveTo(game.positionCards.get(turnGame%boardConfig.modePlay).x + detalX - allCard.get(indexCard).card.getWidth()/2,game.positionCards.get(turnGame%boardConfig.modePlay).y - allCard.get(indexCard).card.getHeight()/2, 0.3f, linear),
        rotateTo(0,0.3f, linear)
    ));

    Tweens.setTimeout(group, 0.3f, ()->{
      if(turnDistributeTemp < 5)
        card.flipCard(1, 1);
      else if(cardsPlay.get(0).size == 5 && passList.get(0) == 0 && !isHandEffectShow){
        float delta = boardConfig.widthCard*0.4f*4/5;
        isHandEffectShow = true;
        Gdx.app.log("debug", "da bat tay");
        float pX1 = game.positionCards.get(0).x + delta + 5*delta/8, pY1 = game.positionCards.get(0).y;
        hand = (Image) G.c(Image.class).k("hand").wh2(0.65f, 0.65f).add(group).p(pX1, pY1).ub();
        circle = (Image) G.c(Image.class).k("circle").wh2(0.65f, 0.65f).add(group).ub();
        float pX2 = game.positionCards.get(0).x + delta, pY2 = game.positionCards.get(0).y - circle.getHeight()/2;
        G.b(circle).p(pX2, pY2);
        handEffect();
      }
    });

    cardsPlay.get(turnGame%boardConfig.modePlay).add(card);
    Tweens.setTimeout(group, 0.2f, ()->{
      indexCard++;
      turnGame++;
      distributeCardPlay();
    });
  }

  public void cancelHandEffect(){
    hand.remove();
    hand.clear();
    circle.remove();
    circle.clear();
  }

  private void handEffect(){
    G.b(hand).o(Align.center).r(80).s(1.3f);
    G.b(circle).o(Align.center).s(0);
    hand.addAction(AB.build(hand).r(0).d(0.3f).s(1, 1f).in(linear).done(() -> {
      circle.addAction(AB.build(circle).d(0.3f).s(1.3f, 1.3f).done(()->{
        G.b(circle).s(0);
      }));
      Tweens.setTimeout(group, 0.3f, ()->{
        hand.addAction((AB.build(hand).r(80).d(0.3f).s(1.3f, 1.3f).in(linear).done(()->{
          circleEffect(circle, hand);
        })));

      });
    }));

  }

  private void circleEffect(Image image1, Image image2){
    image2.addAction(AB.build(image2).r(0).d(0.3f).s(1, 1).in(linear).done(()->{
      circle.addAction(AB.build(circle).d(0.3f).s(1.3f, 1.3f).done(()->{
        G.b(image1).s(0);
      }));
      Tweens.setTimeout(group, 0.3f, ()->{
        hand.addAction((AB.build(hand).r(80).d(0.3f).s(1.3f, 1.3f).in(linear).done(()->{
          circleEffect(image1, image2);
        })));
      });
    }));
  }

  private void showCardEnd(){

    if(hand != null){
      cancelHandEffect();
    }

    for(Array<Card> cards : cardsPlay){
      if(passList.get(cardsPlay.indexOf(cards, true)) == 0){
        cards.get(4).flipCard(1, 1);
        if(cardsPlay.indexOf(cards, true) == 0){
          cards.get(1).setColor(Color.WHITE);
          cards.get(0).setColor(Color.WHITE);
        }
        else {
          cards.get(1).flipCard(1, 1);
          cards.get(0).flipCard(1, 1);
        }
      }
    }

    showWinloseImage(findMax());
    showEffectWin();

  }

  private void initCardsGroup(){
    cardsGroup = new Array<>();
    for(int i = 0; i < boardConfig.modePlay; i++){
      Group groupTemp = new Group();
      group.addActor(groupTemp);
      cardsGroup.add(groupTemp);
    }
  }

  private void bet(){
    updateMoneyTxt();
    updateFrameNotice();
    Gdx.app.log("debug", "turn game: " + turnGame%boardConfig.modePlay);
    if(turnGame%boardConfig.modePlay == turnStart){
      if(flagDistribute){
        flagDistribute = !flagDistribute;
        indexTo = -1;
        distributeCardPlay();
        return;
      }
      else {
        flagDistribute = !flagDistribute;
      }
    }

    int a = passList.get(turnGame%boardConfig.modePlay);
    int b = allMoney1.get(turnGame%boardConfig.modePlay);
    int c = allMoney2.get(turnGame%boardConfig.modePlay);
    if(a == 1 || b == 1 || c == 1){
      turnGame++;
      bet();
      return;
    }

    if(lightTurn != null)
      lightTurn.remove();
    int index = turnGame%boardConfig.modePlay;
    onTurn(index, game.positionAvts.get(index).x, game.positionAvts.get(index).y, 40);
    if(turnGame%boardConfig.modePlay == 0){
      if(passList.get(0) != 1)
        showAllbutton();
      else {
        turnGame++;
        bet();
      }
      return;
    }
    botBet();
  }

  private void botBet(){
    float duaration = (int) Math.floor(Math.random()*2 + 1);
    int bet1 = getBet();
    Tweens.setTimeout(group, duaration, ()->{
      hiddenTypeCards();
      if(bet1 == 0){ //todo: bo bai
        hiddenImg();
        passList.set(turnGame%boardConfig.modePlay, 1);
        betList.set(turnGame%boardConfig.modePlay, 0);
        for(Card card : cardsPlay.get(turnGame%boardConfig.modePlay)){
          card.flipTileDown(1, 1);
        }
        //str = "bo bai";
        float w = SkipImgBotArr.get(turnGame%boardConfig.modePlay).getWidth(), h = SkipImgBotArr.get(turnGame%boardConfig.modePlay).getHeight();
        //sound
        //X.getSound("skip").play();
        SoundEffect.Play(SoundEffect.skip);

        aniSkip(SkipImgBotArr.get(turnGame%boardConfig.modePlay), game.positionCards.get(turnGame%boardConfig.modePlay).x + w/2, game.positionCards.get(turnGame%boardConfig.modePlay).y + h);
        turnGame++;

        Tweens.setTimeout(group, 0.3f, ()->{
          bet();
        });
        return;
      }
      else if(bet1 == 1){ //todo: theo
        betList.set(turnGame%boardConfig.modePlay, 1);
        //todo: goi ham move chip
        float pX = game.positionCards.get(turnGame%boardConfig.modePlay).x, pY = game.positionCards.get(turnGame%boardConfig.modePlay).y;
        aniImage(aniImage.get(turnGame%boardConfig.modePlay).get(0), pX + 30, pY - 90);
        //sound
//        X.getSound("slideChips").play();
        SoundEffect.Play(SoundEffect.slideChips);
        float w = GMain.screenWidth, h = GMain.screenHeight;

        if(indexTo != -1){
          moneyBasic = moneyBet.get(indexTo) - moneyBet.get(turnGame%boardConfig.modePlay);
          updateFrameNotice();
        }

        Label money = new Label("", new Label.LabelStyle(font1, Color.RED));
        money.setText(FortmartPrice(moneyBasic));
        money.setFontScale(0.5f);
        groupFinal.addActor(money);
        money.setPosition(game.positionAvts.get(turnGame%boardConfig.modePlay).x - 50,game.positionAvts.get(turnGame%boardConfig.modePlay).y, Align.center);
        money.addAction(sequence(
            moveBy(0, -50, 0.2f, linear),
            delay(0.3f),
            GSimpleAction.simpleAction((d, a)->{
              money.remove();
              money.clear();
              return true;
            })
        ));

        GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(cardAtlas, group);
        greedilyAlgorithm.renderChip(moneyBasic, game.positionCards.get(turnGame%boardConfig.modePlay).x,game.positionCards.get(turnGame%boardConfig.modePlay).y, false );
        greedilyAlgorithm.moveChips(turnGame%boardConfig.modePlay,w/2 - w/30, h/4 + h/10, 0.3f, fastSlow, ()->{
          greedilyAlgorithm.disposeChips();
          totalMoney+=moneyBasic;
          long moneyB = moneyBet.get(turnGame%boardConfig.modePlay);
          moneyBet.set(turnGame%boardConfig.modePlay, moneyB + moneyBasic);


          totalMoneyChip.renderChip(totalMoney,w/2 - w/8, h/4, true);
          long moneyTemp = game.moneyBot.get(turnGame%boardConfig.modePlay - 1);
          int index = turnGame%boardConfig.modePlay;
          counterMonneyDown(moneyTxts.get(index),moneyTemp, moneyBasic, 1);
          game.moneyBot.set(turnGame%boardConfig.modePlay - 1, moneyTemp - moneyBasic);
          turnGame++;
          updateFrameNotice();
          //updateMoneyTxt();
          Tweens.setTimeout(group, 0.3f, ()->{
            bet();
          });
        });
      }
      else if(bet1 == 2){ //todo: to
        indexTo = turnGame%boardConfig.modePlay;
        int ratio = (int) Math.floor(Math.random()*10 + 1);
        float ratio1 = (float)ratio/10;
        long moneyMore = (long)(ratio1*moneyBasic + moneyBasic);
        moneyBasic = moneyMore;
        if(moneyMore > game.moneyBot.get(turnGame%boardConfig.modePlay)){
//          X.getSound("slideChips").play();
          SoundEffect.Play(SoundEffect.slideChips);
          allMoney1();
          return;
        }

        betList.set(turnGame%boardConfig.modePlay, 2);
        hiddenImgs();
        turnStart = turnGame%boardConfig.modePlay;
        //todo: goi ham move chip
        //str = "To";
        float pX = game.positionCards.get(turnGame%boardConfig.modePlay).x, pY = game.positionCards.get(turnGame%boardConfig.modePlay).y;
        aniImage(aniImage.get(turnGame%boardConfig.modePlay).get(1), pX + 30, pY - 90);
        System.out.println("More money: " + moneyMore);
        //sound
        SoundEffect.Play(SoundEffect.slideChips);

//        X.getSound("slideChips").play();

        Label money = new Label("", new Label.LabelStyle(font1, Color.RED));
        money.setText(FortmartPrice(moneyMore));
        money.setFontScale(0.5f);
        groupFinal.addActor(money);
        money.setPosition(game.positionAvts.get(turnGame%boardConfig.modePlay).x - 50,game.positionAvts.get(turnGame%boardConfig.modePlay).y, Align.center);
        money.addAction(sequence(
            moveBy(0, -50, 0.2f, linear),
            delay(0.3f),
            GSimpleAction.simpleAction((d, a)->{
              money.remove();
              money.clear();
              return true;
            })
        ));

        float w = GMain.screenWidth, h = GMain.screenHeight;
        GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(cardAtlas, group);
        greedilyAlgorithm.renderChip(moneyMore, game.positionCards.get(turnGame%boardConfig.modePlay).x,game.positionCards.get(turnGame%boardConfig.modePlay).y, false );
        greedilyAlgorithm.moveChips(turnGame%boardConfig.modePlay, GMain.screenWidth/2 - w/30, GMain.screenHeight/4 + h/10, 0.3f, fastSlow, ()->{
          greedilyAlgorithm.disposeChips();
          totalMoney+=moneyMore;
          //update money bet
          long moneyB = moneyBet.get(turnGame%boardConfig.modePlay);
          moneyBet.set(turnGame%boardConfig.modePlay, moneyB + moneyMore);

          totalMoneyChip.renderChip(totalMoney,GMain.screenWidth/2 - w/8, GMain.screenHeight/4, true);
          long moneyTemp = game.moneyBot.get(turnGame%boardConfig.modePlay - 1);
          int index = turnGame%boardConfig.modePlay;
          counterMonneyDown(moneyTxts.get(index), moneyTemp, moneyMore, 1);
          game.moneyBot.set(turnGame%boardConfig.modePlay - 1, moneyTemp - moneyMore);

          turnGame++;

          updateFrameNotice();
          Tweens.setTimeout(group, 0.3f, ()->{
            bet();
          });
        });
      }

      else if(bet1 == 3) { //todo: to tat tay khi thieu tien
        System.out.println("tat tay khi thieu tien");
//        X.getSound("slideChips").play();
        SoundEffect.Play(SoundEffect.slideChips);

        long moneyB = moneyBet.get(turnGame%boardConfig.modePlay);
        moneyBet.set(turnGame%boardConfig.modePlay, moneyB + game.moneyBot.get(turnGame%boardConfig.modePlay - 1));
        allMoney1();
      }
      else { //todo: to tat tay khi du tien
        System.out.println("tat tay khi du tien");
//        X.getSound("slideChips").play();
        SoundEffect.Play(SoundEffect.slideChips);

        long moneyB = moneyBet.get(turnGame%boardConfig.modePlay);
        moneyBet.set(turnGame%boardConfig.modePlay, moneyB + game.moneyBot.get(turnGame%boardConfig.modePlay - 1));
        allMoney2();
      }
    });
  }

  private void allMoney1(){
    int index = turnGame%boardConfig.modePlay == 0 ? 0 : (turnGame%boardConfig.modePlay -1);
    float pX = game.positionCards.get(turnGame%boardConfig.modePlay).x, pY = game.positionCards.get(turnGame%boardConfig.modePlay).y;
    aniImage(aniImage.get(turnGame%boardConfig.modePlay).get(2), pX + 30, pY - 90);

    long moneyTemp = turnGame%boardConfig.modePlay == 0 ? boardConfig.moneyPlayer : game.moneyBot.get(turnGame%boardConfig.modePlay - 1);

    Label money = new Label("", new Label.LabelStyle(font1, Color.RED));
    money.setText(FortmartPrice(moneyTemp));
    money.setFontScale(0.5f);
    groupFinal.addActor(money);
    money.setPosition(game.positionAvts.get(turnGame%boardConfig.modePlay).x - 50,game.positionAvts.get(turnGame%boardConfig.modePlay).y, Align.center);
    money.addAction(sequence(
        moveBy(0, -50, 0.2f, linear),
        delay(0.3f),
        GSimpleAction.simpleAction((d, a)->{
          money.remove();
          money.clear();
          return true;
        })
    ));

    float w = GMain.screenWidth, h = GMain.screenHeight;
    GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(cardAtlas, group);
    greedilyAlgorithm.renderChip(moneyBasic, game.positionCards.get(turnGame%boardConfig.modePlay).x,game.positionCards.get(turnGame%boardConfig.modePlay).y, false );
    greedilyAlgorithm.moveChips(turnGame%boardConfig.modePlay,w/2 - w/30, h/4 + h/10, 0.3f, fastSlow, ()->{
      greedilyAlgorithm.disposeChips();
      if(turnGame%boardConfig.modePlay == 0){
        totalMoney += boardConfig.moneyPlayer;
        long moneyB = moneyBet.get(0);
        int index1 = turnGame%boardConfig.modePlay;
        counterMonneyDown(moneyTxts.get(index1), boardConfig.moneyPlayer, boardConfig.moneyPlayer,1);
        moneyBet.set(0, moneyB + boardConfig.moneyPlayer);

        boardConfig.moneyPlayer = 0;
        GMain.prefs.putLong("myMonney", boardConfig.moneyPlayer);
        GMain.prefs.flush();
      }
      else {
        int index1 = turnGame%boardConfig.modePlay;
        counterMonneyDown(moneyTxts.get(index1), game.moneyBot.get(index1 - 1), game.moneyBot.get(index1 - 1),1);
        game.moneyBot.set(index, 0l);
        totalMoney+=game.moneyBot.get(turnGame%boardConfig.modePlay - 1);
      }
      totalMoneyChip.renderChip(totalMoney,w/2 - w/8, h/4, true);
      allMoney1.set(turnGame%boardConfig.modePlay, 1);

      turnGame++;
      updateFrameNotice();
      //updateMoneyTxt();
      Tweens.setTimeout(group, 0.3f, ()->{
        bet();
      });
    });
  }

  private void allMoney2(){
    indexTo = turnGame%boardConfig.modePlay;
    int index = turnGame%boardConfig.modePlay == 0 ? 0 : (int)(turnGame%boardConfig.modePlay -1);
    float pX = game.positionCards.get(turnGame%boardConfig.modePlay).x, pY = game.positionCards.get(turnGame%boardConfig.modePlay).y;
    aniImage(aniImage.get(turnGame%boardConfig.modePlay).get(2), pX + 30, pY - 90);
    float w = GMain.screenWidth, h = GMain.screenHeight;
    turnStart = turnGame%boardConfig.modePlay;

    long moneyTemp = turnGame%boardConfig.modePlay == 0 ? boardConfig.moneyPlayer : game.moneyBot.get(turnGame%boardConfig.modePlay - 1);
    Label money = new Label("", new Label.LabelStyle(font1, Color.RED));
    money.setText(FortmartPrice(moneyTemp));
    money.setFontScale(0.5f);
    groupFinal.addActor(money);
    money.setPosition(game.positionAvts.get(turnGame%boardConfig.modePlay).x - 50,game.positionAvts.get(turnGame%boardConfig.modePlay).y, Align.center);
    money.addAction(sequence(
        moveBy(0, -50, 0.2f, linear),
        delay(0.3f),
        GSimpleAction.simpleAction((d, a)->{
          money.remove();
          money.clear();
          return true;
        })
    ));

    GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(cardAtlas, group);
    greedilyAlgorithm.renderChip(moneyBasic, game.positionCards.get(turnGame%boardConfig.modePlay).x,game.positionCards.get(turnGame%boardConfig.modePlay).y, false );
    greedilyAlgorithm.moveChips(turnGame%boardConfig.modePlay,w/2 - w/30, h/4 + h/10, 0.3f, fastSlow, ()->{
      greedilyAlgorithm.disposeChips();
      if(turnGame%boardConfig.modePlay == 0){
        totalMoney += boardConfig.moneyPlayer;
        moneyBasic += boardConfig.moneyPlayer;
        long moneyB = moneyBet.get(0);
        moneyBet.set(0, moneyB + boardConfig.moneyPlayer);
        int index1 = turnGame%boardConfig.modePlay;
        counterMonneyDown(moneyTxts.get(index1), boardConfig.moneyPlayer, boardConfig.moneyPlayer,1);
        boardConfig.moneyPlayer = 0;
        GMain.prefs.putLong("myMonney", boardConfig.moneyPlayer);
        GMain.prefs.flush();
      }
      else {
        totalMoney+=game.moneyBot.get(turnGame%boardConfig.modePlay - 1);
        moneyBasic += game.moneyBot.get(index);
        int index1 = turnGame%boardConfig.modePlay;
        counterMonneyDown(moneyTxts.get(index1),game.moneyBot.get(index1-1), game.moneyBot.get(index1-1),1);
        game.moneyBot.set(index, 0l);
      }

      totalMoneyChip.renderChip(totalMoney,w/2 - w/8, h/4, true);
      allMoney2.set(turnGame%boardConfig.modePlay, 1);

      turnGame++;
      updateFrameNotice();
      //updateMoneyTxt();
      Tweens.setTimeout(group, 0.3f, ()->{
        bet();
      });
    });
  }


  private int getTypeCard(Array<Card> cards){
    int a[] = new int[5];
    for(Card card : cards){
      a[cards.indexOf(card, true)] = card.Key;
    }
    int type = CheckCard.check5(a);
    return type;
  }

  private void makeCardsInt(boolean isFiveCard){

    for(Array<Integer> cardsN : cardsInt){
      if(cardsN.size > 0){
        cardsN.clear();
      }
    }

    for(int i = 0; i < boardConfig.modePlay; i++){
       if(passList.get(i) == 0){
         for(Card card : cardsPlay.get(i)){
           if(isFiveCard || (!card.isDown && !isFiveCard)){
             cardsInt.get(i).add(card.Key);
           }
         }
       }
    }

  }

  private int findMax(){
    int max = -1;
    for(int i : passList){
      if(i == 0){
        max = passList.indexOf(i, true);
        break;
      }
    }

    for(int i = max + 1; i < cardsInt.size; i++){
      if(passList.get(i) == 0){
        if(max != i){
          if(P.compare(cardsInt.get(max), cardsInt.get(i)) == -1){
            max = i;
          }
        }
      }
    }
    return max;
  }

  private int getBet(){
    int index = 0;
    int theo = 0, to = 0, tatay = 0;
    int itemp = P.check(cardsInt.get(turnGame%boardConfig.modePlay))>>13;
    int size = cardsPlay.get(turnGame%boardConfig.modePlay).size;
    switch (itemp){
      case 0: { //todo: mau thau
        if(turnGame%boardConfig.modePlay == turnStart){
          theo = 100;
          break;
        }
        if(size == 3){
          theo = 90;
          to = 10;
          tatay = 0;
        }
        else if(size == 4){
          theo = 80;
          to = 5;
          tatay = 0;
        }
        else {
          theo = 65;
          to = 0;
          tatay = 0;
        }
        break;
      }
      case 1: { //todo: doi
        if(turnGame%boardConfig.modePlay == turnStart){
          theo = 80;
          to = 20;
          tatay = 0;
          break;
        }

        if(size == 3){
          theo = 70;
          to = 10;
          tatay = 0;
        }
        else if(size == 4){
          theo = 65;
          to = 10;
          tatay = 0;
        }
        else {
          theo = 65;
          to = 10;
          tatay = 3;
        }
        break;
      }
      case 2: { //todo: thu
        theo = 70;
        to = 25;
        tatay = 5;
        break;
      }
      case 3: { //todo: xam

        if(size == 4){
          theo = 60;
          to = 28;
          tatay = 12;
        }
        else {
          theo = 60;
          to = 25;
          tatay = 15;
        }
        break;
      }
      case 4: { //todo: sanh
        theo = 50;
        to = 35;
        tatay = 15;
        break;
      }
      case 5: { //todo: thung
        theo = 50;
        to = 32;
        tatay = 18;
        break;
      }
      case 6: { //todo: cu
        theo = 40;
        to = 40;
        tatay = 20;
        break;

      }
      case 7: { //todo: tu quy
        theo = 30;
        to = 40;
        tatay = 30;
        break;
      }
      case 9: { //todo: thung pha sanh
        theo = 20;
        to = 30;
        tatay = 50;
        break;
      }
      default: break;
    }
    int percent2 = (int)Math.floor(Math.random()*100 + 1);
    if(percent2<= theo + to + tatay){
      if(percent2 > theo + to ){ //todo: to tat tay
        index = 4;
      }
      else if(percent2 > theo ) { // to
        index = 2;
      }
      else{ //todo: theo
        index = 1;
      }
      if(game.moneyBot.get(turnGame%boardConfig.modePlay-1) < moneyBasic){
        index = 3; //todo: tat tay khi thieu tien
      }
    }
    else index = 0;

    return index;
  }



  void showAllbutton(){
    groupBtn.setPosition(0,GMain.screenHeight/2);
    groupBtn.addAction(Actions.moveTo(0,0,0.3f, Interpolation.swingOut));
    ////////// btn skip///////
    btnSkip = (Image) G.c(Image.class).k("btnSkip").add(groupBtn).o(Align.center).ub();
    float pX = btnSkip.getWidth()+10 - btnSkip.getWidth()/2, pY = GMain.screenHeight-btnSkip.getHeight()/2-20 - btnSkip.getHeight()/2;
    G.b(btnSkip).p(pX, pY);
    eventBtnSkip(btnSkip);

    //////// btn  Espouse /////////
    btnEspouse = (Image)G.c(Image.class).k("btnEspouse").add(groupBtn).o(Align.center).ub();
    pX = GMain.screenWidth-btnEspouse.getWidth()*2+50 - btnEspouse.getWidth()/2; pY = GMain.screenHeight-btnEspouse.getHeight()/2-20 - btnEspouse.getHeight()/2;
    G.b(btnEspouse).p(pX, pY);
    eventBtnEspouse(btnEspouse);
//
//        /////////// btn add more //////////
    btnAddMore = (Image)G.c(Image.class).k("btnAddMore").add(groupBtn).o(Align.center).ub();
    pX = GMain.screenWidth-btnAddMore.getWidth()/2-30 - btnAddMore.getWidth()/2; pY = GMain.screenHeight-btnAddMore.getHeight()/2-20 - btnAddMore.getHeight()/2;
    G.b(btnAddMore).p(pX, pY);
    if(boardConfig.moneyPlayer < moneyBasic){
      btnAddMore.setColor(Color.GRAY);
      btnAddMore.setTouchable(Touchable.disabled);
    }
    else {
      eventBtnAddMore(btnAddMore);
    }
  }

  private void hiddenGroupBtn(){
    groupBtn.addAction(sequence(
        moveTo(0,GMain.screenHeight/2, 0.3f, linear),
        GSimpleAction.simpleAction((d,a)->{
          groupBtn.clearChildren();
          bet();
          return true;
        })
        )
    );
  }

  void eventBtnAddMore(Image btn){
    btn.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
       setTouchBtn(Touchable.disabled);
        btn.addAction(Actions.sequence(
            Actions.scaleTo(0.8f,0.8f,0.1f),
            Actions.scaleTo(1f,1f,0.1f),
            delay(0.2f),
            GSimpleAction.simpleAction((d, a)->{
              newMoreBets();
              return true;
            })
        ));
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  private void newMoreBets(){
    new MoreBets(cardAtlas, this);
  }

  private void setTouchBtn(Touchable touchable){
    btnSkip.setTouchable(touchable);
    btnEspouse.setTouchable(touchable);
    btnAddMore.setTouchable(touchable);
  }

  void eventBtnSkip(Image btn){
    btn.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setTouchBtn(Touchable.disabled);
        //sound
        btn.addAction(Actions.sequence(
            Actions.scaleTo(0.8f,0.8f,0.1f),
            Actions.scaleTo(1f,1f,0.1f)
        ));
        Tweens.setTimeout(groupBtn,0.2f,()->{
          clickBtnSkip();
        });
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  void eventBtnEspouse(Image btn){
    btn.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setTouchBtn(Touchable.disabled);
        //sound
//        X.getSound("theo").play();
        SoundEffect.Play(SoundEffect.theo);

        btn.addAction(Actions.sequence(
            Actions.scaleTo(0.8f,0.8f,0.1f),
            Actions.scaleTo(1f,1f,0.1f)
        ));
        Tweens.setTimeout(groupBtn,0.2f,()->{
          clickBtnEspouse();
        });
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  public void clickBetBtn(){
    turnGame++;
    hiddenGroupBtn();
  }

  private void clickBtnSkip(){
    passList.set(0, 1);
    for(Card card : cardsPlay.get(0)){
      card.flipTileDown(1, 1);
    }
    float w = SkipImgBotArr.get(0).getWidth(), h = SkipImgBotArr.get(0).getHeight();
    //sound
//    X.getSound("skip").play();
    SoundEffect.Play(SoundEffect.skip);
    aniSkip(SkipImgBotArr.get(0), game.positionCards.get(0).x + w/2, game.positionCards.get(0).y + h);
    clickBetBtn();
  }

  private void clickBtnEspouse(){

    if(boardConfig.moneyPlayer < moneyBasic){
      groupBtn.addAction(sequence(
        moveTo(0,GMain.screenHeight/2, 0.3f, linear),
          GSimpleAction.simpleAction((d,a)->{
            groupBtn.clearChildren();
            return true;
          })
      ));
      allMoney1();
      return;
    }

    float w = GMain.screenWidth, h = GMain.screenHeight;
    float pX = game.positionCards.get(0).x, pY = game.positionCards.get(0).y;

    //sound
//    X.getSound("theo").play();
    SoundEffect.Play(SoundEffect.theo);
    aniImage(aniImage.get(0).get(0), pX + 30, pY - 90);

    if(indexTo != -1){
      moneyBasic = moneyBet.get(indexTo) - moneyBet.get(0);
    }
    Label money = new Label("", new Label.LabelStyle(font1, Color.RED));
    money.setText(FortmartPrice(moneyBasic));
    money.setFontScale(0.5f);
    groupFinal.addActor(money);
    money.setPosition(game.positionAvts.get(turnGame%boardConfig.modePlay).x - 50,game.positionAvts.get(turnGame%boardConfig.modePlay).y, Align.center);
    money.addAction(sequence(
      moveBy(0, -50, 0.2f, linear),
      delay(0.3f),
      GSimpleAction.simpleAction((d, a)->{
        money.remove();
        money.clear();
        return true;
      })
    ));

    //sound
//    X.getSound("slideChips").play();
    SoundEffect.Play(SoundEffect.slideChips);
    GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(cardAtlas, group);
    greedilyAlgorithm.renderChip(moneyBasic,game.positionCards.get(turnGame%boardConfig.modePlay).x,game.positionCards.get(turnGame%boardConfig.modePlay).y, false  );
    greedilyAlgorithm.moveChips(0, GMain.screenWidth/2 - w/30, GMain.screenHeight/4 + h/10, 0.3f, fastSlow, ()->{
      greedilyAlgorithm.disposeChips();

      totalMoney+=moneyBasic;
      totalMoneyChip.renderChip(totalMoney,GMain.screenWidth/2 - w/8, GMain.screenHeight/4, true);
      counterMonneyDown(moneyTxts.get(0), boardConfig.moneyPlayer, moneyBasic, 1);
      boardConfig.moneyPlayer -= moneyBasic;

      GMain.prefs.putLong("myMonney", boardConfig.moneyPlayer);
      GMain.prefs.flush();
      if(boardConfig.moneyPlayer == 0){
        allMoney1.set(0, 1);
      }

      long moneyB = moneyBet.get(0);
      System.out.println("tien cua 0: " + moneyB);
      moneyBet.set(0, moneyB + moneyBasic);
      System.out.println("tien sau khi cong: " + moneyBet.get(0));
      updateMoneyTxt();
      updateFrameNotice();
      clickBetBtn();
    });
  }

  public void clickDoneBtnMoreMoney(long money){
    if(money == boardConfig.moneyPlayer){
//      X.getSound("tothem").play();
      SoundEffect.Play(SoundEffect.tothem);
      allMoney2();
      groupBtn.addAction(sequence(
        moveTo(0,GMain.screenHeight/2, 0.3f, linear),
          GSimpleAction.simpleAction((d,a)->{
            groupBtn.clearChildren();
            return true;
          })
      )
      );
      return;
    }
    indexTo = 0;
    hiddenImgs();
    float w = GMain.screenWidth, h = GMain.screenHeight;
    turnStart = 0;
    float pX = game.positionCards.get(0).x, pY = game.positionCards.get(0).y;

    Label money1 = new Label("", new Label.LabelStyle(font1, Color.RED));
    money1.setText(FortmartPrice(money));
    money1.setFontScale(0.5f);
    groupFinal.addActor(money1);
    money1.setPosition(game.positionAvts.get(turnGame%boardConfig.modePlay).x - 50,game.positionAvts.get(turnGame%boardConfig.modePlay).y, Align.center);
    money1.addAction(sequence(
        moveBy(0, -50, 0.2f, linear),
        delay(0.3f),
        GSimpleAction.simpleAction((d, a)->{
          money1.remove();
          money1.clear();
          return true;
        })
    ));

    //sound
//    X.getSound("tothem").play();
    SoundEffect.Play(SoundEffect.tothem);
    aniImage(aniImage.get(0).get(1), pX + 30, pY - 90);
    GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(cardAtlas, group);
    greedilyAlgorithm.renderChip(money,game.positionCards.get(turnGame%boardConfig.modePlay).x,game.positionCards.get(turnGame%boardConfig.modePlay).y, false);
    greedilyAlgorithm.moveChips(0, GMain.screenWidth/2 - w/30, GMain.screenHeight/4 + h/10, 0.3f, fastSlow, ()->{
      greedilyAlgorithm.disposeChips();
      moneyBasic = money;
      totalMoney+=money;
      counterMonneyDown(moneyTxts.get(0), boardConfig.moneyPlayer, money, 1);
      boardConfig.moneyPlayer -= money;

      GMain.prefs.putLong("myMonney", boardConfig.moneyPlayer);
      GMain.prefs.flush();

      long moneyB = moneyBet.get(0);
      moneyBet.set(0, moneyB + money);

      totalMoneyChip.renderChip(totalMoney,GMain.screenWidth/2 - w/8, GMain.screenHeight/4, true);

      updateFrameNotice();
      //updateMoneyTxt();
      clickBetBtn();
    });
  }

  private void swapZIndexCards(Card card1, Card card2){
    int ZIndex0 = card1.card.getZIndex();
    int ZIndex1 = card1.tileDown.getZIndex();
    card1.card.setZIndex(card2.card.getZIndex());
    card1.tileDown.setZIndex(card2.tileDown.getZIndex());
    card2.card.setZIndex(ZIndex0);
    card2.tileDown.setZIndex(ZIndex1);

  }

  private void loadSkipImg(){
    for(int i=0;i<boardConfig.modePlay;i++){
      Image skipImg = GUI.createImage(cardAtlas,"skip");
      groupImageSkip.addActor(skipImg);
      skipImg.setVisible(false);
      SkipImgBotArr.add(skipImg);
    }
  }

  void aniSkip(Image object,float x, float y){
    object.setVisible(true);
    object.setScale(7);
    object.setOrigin(Align.center);
    object.setPosition(x-30,y-80,Align.center);
    object.addAction(Actions.scaleTo(1,1,0.3f,bounceOut));
  }

  private void aniImage(Image image, float x, float y){
    hiddenImg();
    image.setVisible(true);
    image.setScale(0.7f, 0.7f);
    groupImageSkip.addActor(image);
    image.setOrigin(Align.center);
    image.setPosition(x, y, Align.center);
    image.addAction(Actions.scaleTo(1, 1, 0.3f, bounceOut));

  }

  private void hiddenImg(){
    for(Image img : aniImage.get(turnGame%boardConfig.modePlay)){
      img.setVisible(false);
    }
  }

  private void hiddenImgs(){
    for(Array<Image> imgs : aniImage){
      for(Image img : imgs){
        img.setVisible(false);
      }
    }
  }

  private void endARound(){
    game.newGame();
  }

  public void dispose(){
    for(Group gr : cardsGroup){
      gr.clear();
    }

    for(Actor at : group.getChildren()){
      at.remove();
      at.clearListeners();
      at.clear();
    }
    group.clearChildren();

    for(Actor at : groupImageSkip.getChildren()){
      at.remove();
      at.clearListeners();
      at.clear();
    }
    groupImageSkip.clearChildren();

    for(Actor at : groupBtn.getChildren()){
      at.remove();
      at.clearListeners();
      at.clear();
    }
    groupBtn.clearChildren();
    groupFinal.clear();
    groupSetting.clear();
    groupparticle.clear();
  }

  private String FortmartPrice(Long Price) {

    DecimalFormat mDecimalFormat = new DecimalFormat("###,###,###,###");
    String mPrice = mDecimalFormat.format(Price);

    return mPrice;
  }

  public void setTouchCard(){
    for(Card card : cardsPlay.get(0)){
      card.card.setTouchable(Touchable.disabled);
    }
  }

  void counterMonneyDown(Label object, long monney,long targetMonney, float duaration){

    group.addAction(
        GTemporalAction.add(duaration, (percent, actor) -> {
          object.setText(""+FortmartPrice((long)(monney - targetMonney*percent)));
        }));

  }
  void counterMonneyUp(Label object, long monney,long targetMonney, float duaration){

    group.addAction(
        GTemporalAction.add(duaration, (percent, actor) -> {
          object.setText(""+FortmartPrice((long)(monney + targetMonney*percent)));
        }));

  }

  void onTurn(int type, float x, float y,float duration){
    lightTurn = GUI.createImage(cardAtlas,"lightTurn");
    if(boardConfig.modePlay==5) {
      if (type == 1 || type == 2) {
        lightTurn.setWidth(lightTurn.getWidth() * 0.7f);
        lightTurn.setHeight(lightTurn.getHeight() * 0.7f);
        x = x + 35;
        y = y + 30;
      } else if (type == 3 || type == 4) {
        lightTurn.setWidth(lightTurn.getWidth() * 0.7f);
        lightTurn.setHeight(lightTurn.getHeight() * 0.7f);
        x = x + 35;
        y = y + 30;
      } else {

        x = x + 48;
        y = y + 50;
      }
    }else {

      if(type==1){
        lightTurn.setWidth(lightTurn.getWidth() * 0.7f);
        lightTurn.setHeight(lightTurn.getHeight() * 0.7f);
        x = x + 35;
        y = y + 30;
      }else if(type==2){
        lightTurn.setWidth(lightTurn.getWidth() * 0.7f);
        lightTurn.setHeight(lightTurn.getHeight() * 0.7f);
        x= x + 35;
        y= y + 30;
      }else {
        x = x + 48;
        y = y + 50;
      }
    }

    lightTurn.setPosition(x,y,Align.center);
    groupparticle.addActor(lightTurn);
    lightTurn.addAction(Actions.rotateTo(10000,duration));

  }

}
