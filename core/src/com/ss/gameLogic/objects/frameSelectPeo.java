package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.gameLogic.effect.SoundEffect;
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.transitions.GTransitionFade;
import com.ss.core.transitions.GTransitionSlice;
import com.ss.core.transitions.GTransitionSlide;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.scene.GGameMain;
import com.ss.gameLogic.scene.GameStart;

import java.text.DecimalFormat;

public class frameSelectPeo  {
  TextureAtlas ui;
  Group group = new Group();
  Image btn1,btn2,btn3,btn4;
  BitmapFont font;
  int type;
  Label Labelpeople;
  int index=5;
  Image btnLeft,btnRight;
  GameStart gameStart;
  long mucCuoc=0;
  Image btnReturn;


  public frameSelectPeo(TextureAtlas ui, Image btn1, Image btn2, Image btn3, Image btn4, BitmapFont font, int type, GameStart gameStart, Image btnReturn){
    this.btn1 = btn1;
    this.btn2 = btn2;
    this.btn3 = btn3;
    this.btn4 = btn4;
    this.font = font;
    this.type = type;
    this.gameStart = gameStart;
    this.btnReturn = btnReturn;
    GStage.addToLayer(GLayer.ui,group);
    this.ui = ui;
    showFrame();

  }
  void showFrame(){
    btnReturn.setTouchable(Touchable.disabled);
    group.setPosition(GMain.screenWidth/2,GMain.screenHeight/2,Align.center);
    group.setOrigin(Align.center);
    group.setScale(0);
    Image frame = GUI.createImage(ui,"frSelectPeo");
    frame.setPosition(0,0,Align.center);
    group.addActor(frame);
    group.addAction(Actions.scaleTo(1,1,0.3f, Interpolation.swingOut));
    ////////// btn ok//////
    Image btnOk = GUI.createImage(ui,"btnOk");
    btnOk.setOrigin(Align.center);
    btnOk.setPosition(btnOk.getWidth()/2,150, Align.center);
    group.addActor(btnOk);
    evtBtnOk(btnOk);
    ////////// btn done//////
    Image btnDone = GUI.createImage(ui,"btnHuy");
    btnDone.setOrigin(Align.center);
    btnDone.setPosition(-btnDone.getWidth()/2,150, Align.center);
    group.addActor(btnDone);
    eventBtnDone(btnDone);
    loadInFo();
    SelectPeople();
  }
  void eventBtnDone(Image btn){
    btn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
      super.clicked(event, x, y);
      SoundEffect.Play(SoundEffect.click);
      btn.setTouchable(Touchable.disabled);
      btn.addAction(Actions.sequence(
          Actions.scaleTo(0.8f,0.8f,0.1f),
          Actions.scaleTo(1f,1f,0.1f)
      ));
      Tweens.setTimeout(group,0.2f,()->{
        group.addAction(Actions.sequence(
          Actions.scaleTo(0,0,0.2f),
          GSimpleAction.simpleAction((d,a)->{
            btn1.setTouchable(Touchable.enabled);
            btn2.setTouchable(Touchable.enabled);
            btn3.setTouchable(Touchable.enabled);
            btn4.setTouchable(Touchable.enabled);
            group.clear();
            btnReturn.setTouchable(Touchable.enabled);
            return true;
          })
        ));
      });

      }
    });
  }
  void loadInFo(){
    if(type==1)
      mucCuoc = 100000;
    else if (type==2)
      mucCuoc = 500000;
    else if(type==3)
      mucCuoc = 1000000;
    else if(type==4){
      mucCuoc = 5000000;
    }
    Label LabelmucCuoc= new Label("$"+FortmartPrice(mucCuoc),new Label.LabelStyle(font, Color.GOLD));
    LabelmucCuoc.setPosition(0,-80,Align.center);
    LabelmucCuoc.setAlignment(Align.center);
    group.addActor(LabelmucCuoc);
  }
  void SelectPeople(){
    btnLeft = GUI.createImage(ui,"btnLeft");
    btnLeft.setOrigin(Align.center);
    btnLeft.setPosition(-100,60,Align.center);
    group.addActor(btnLeft);
    btnRight = GUI.createImage(ui,"btnRight");
    btnRight.setOrigin(Align.center);
    btnRight.setPosition(100,60,Align.center);
    group.addActor(btnRight);
    btnRight.setVisible(false);
    ////// label //////
    Labelpeople = new Label(""+index, new Label.LabelStyle(font,Color.GOLD));
    Labelpeople.setPosition(0,50,Align.center);
    Labelpeople.setAlignment(Align.center);
    group.addActor(Labelpeople);
    ///////// event////
    evtBtnLeft(btnLeft);
    evtBtnRight(btnRight);
  }
  void evtBtnLeft(Image btn){
    btn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        //SoundEffect.Play(SoundEffect.click);

        btn.addAction(Actions.sequence(
            Actions.scaleTo(0.8f,0.8f,0.1f),
            Actions.scaleTo(1f,1f,0.1f)
        ));
        Tweens.setTimeout(group,0.2f,()->{
          btn.setVisible(false);
          btnRight.setVisible(true);
          index = 3;
          Labelpeople.setText(""+index);
        });

      }
    });


  }
  void evtBtnRight(Image btn){
    btn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        //SoundEffect.Play(SoundEffect.click);

        btn.addAction(Actions.sequence(
            Actions.scaleTo(0.8f,0.8f,0.1f),
            Actions.scaleTo(1f,1f,0.1f)
        ));
        Tweens.setTimeout(group,0.2f,()->{
          btn.setVisible(false);
          btnLeft.setVisible(true);
          index = 5;
          Labelpeople.setText(""+index);
        });

      }
    });
  }
  void evtBtnOk(Image btn){
    btn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        //SoundEffect.Play(SoundEffect.click);
        btn.setTouchable(Touchable.disabled);
        btn.addAction(Actions.sequence(
            Actions.scaleTo(0.8f,0.8f,0.1f),
            Actions.scaleTo(1f,1f,0.1f)
        ));
        Tweens.setTimeout(group,0.2f,()->{
          btn.setTouchable(Touchable.enabled);
          boardConfig.modePlay=index;
          boardConfig.moneyBasic = mucCuoc;
          if(boardConfig.moneyPlayer>mucCuoc){
            gameStart.dispose();
            gameStart.setScreen(new GGameMain(), GTransitionFade.init(0.5f));
          }else {
            Label notice = new Label("không đủ tiền cược",new Label.LabelStyle(font,null));
            notice.setPosition(btn.getX(),btn.getY(),Align.center);
            group.addActor(notice);
            notice.addAction(Actions.sequence(
                Actions.moveBy(0,-50,0.2f),
                GSimpleAction.simpleAction((d,a)->{
                  notice.clear();
                  notice.remove();
                  return true;
                })
            ));
          }
        });

      }
    });
  }
  private String FortmartPrice(Long Price) {

    DecimalFormat mDecimalFormat = new DecimalFormat("###,###,###,###");
    String mPrice = mDecimalFormat.format(Price);

    return mPrice;
  }
}
