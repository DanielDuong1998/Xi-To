package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.platform.ToggleHandler;
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.commons._ToggleButton;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.transitions.GTransitionSlice;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.effect.SoundEffect;
import com.ss.gameLogic.scene.GGameMain;
import com.ss.gameLogic.scene.GameStart;

public class Setting implements ToggleHandler {
  Group group = new Group();
  TextureAtlas uiAtlas;
  Image btnSetting;
  GGameMain gamePlay;
  GameStart gameStart;
  GLayerGroup groupG;
  public Setting(TextureAtlas uiAtlas, Image btnSetting, GGameMain gamePlay, GLayerGroup groupG){
    this.gamePlay = gamePlay;
    this.btnSetting = btnSetting;
    this.uiAtlas = uiAtlas;
    this.groupG = groupG;
    GStage.addToLayer(GLayer.top,group);
    showFrameSetting();
  }
  void showFrameSetting(){
    SoundEffect.Play(SoundEffect.PannelOut);
    group.setPosition(GMain.screenWidth+200,0,Align.center);
    Image frameSetting = GUI.createImage(uiAtlas,"frameSetting");
    frameSetting.setPosition(0,0, Align.center);
    group.addActor(frameSetting);
    group.addAction(Actions.moveTo(GMain.screenWidth/2,GMain.screenHeight/2,0.3f, Interpolation.swingOut));
    ////// btn close//////
    Image btnClose = GUI.createImage(uiAtlas,"btnDone");
    btnClose.setPosition(-btnClose.getWidth(),160,Align.center);
    group.addActor(btnClose);
    eventbtnClose(btnClose);
    //////// btn Exit /////
    Image btnExit = GUI.createImage(uiAtlas,"btnExit");
    btnExit.setPosition(btnExit.getWidth(),160,Align.center);
    group.addActor(btnExit);
    eventbtnExit(btnExit);
    ////////// btn TurnOn /////////
    Image btnTurnOnSound = GUI.createImage(uiAtlas,"turnOn");
    btnTurnOnSound.setPosition(150,-100,Align.center);
    group.addActor(btnTurnOnSound);
    ///////// btn TurnOff ///////
    Image btnTurnOffSound = GUI.createImage(uiAtlas,"turnOff");
    btnTurnOffSound.setPosition(150,-100,Align.center);
    group.addActor(btnTurnOffSound);
    if(SoundEffect.mute==false){
      btnTurnOffSound.setVisible(false);
    }else {
      btnTurnOnSound.setVisible(false);
    }
    new _ToggleButton(btnTurnOnSound,btnTurnOffSound,"sound",this);
    ////////////////// music///////////////////
    ////////// btn TurnOn /////////
    Image btnTurnOnMusic = GUI.createImage(uiAtlas,"turnOn");
    btnTurnOnMusic.setPosition(150,30,Align.center);
    group.addActor(btnTurnOnMusic);
    ///////// btn TurnOff ///////
    Image btnTurnOffMusic = GUI.createImage(uiAtlas,"turnOff");
    btnTurnOffMusic.setPosition(150,30,Align.center);
    group.addActor(btnTurnOffMusic);
    if(SoundEffect.music==false){
      btnTurnOffMusic.setVisible(false);
    }else {
      btnTurnOnMusic.setVisible(false);
    }
    new _ToggleButton(btnTurnOnMusic,btnTurnOffMusic,"music",this);



  }
  void eventbtnClose(Image btn){
    btn.setOrigin(Align.center);
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
        SoundEffect.Play(SoundEffect.PannelIn);
        group.addAction(Actions.sequence(
          Actions.moveTo(-400,0,0.3f,Interpolation.swingOut),
          GSimpleAction.simpleAction((d,a)->{
            btnSetting.setTouchable(Touchable.enabled);
            group.clear();
            groupG.setPause(false);
            return true;
          })
        ));

      });
      }
    });
  }
  void eventbtnExit(Image btn){
    btn.setOrigin(Align.center);
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
        GMain.prefs.putLong("mymonney",boardConfig.moneyPlayer);
        GMain.prefs.flush();
        gameStart.MyMonney = GMain.prefs.getLong("mymonney");
        gamePlay.dispose();
        gamePlay.setScreen(new GameStart(), GTransitionSlice.init(0.5f,2,1,Interpolation.swingOut));

      });
      }
    });
  }

  @Override
  public void activeHandler(String str) {
    if(str=="sound"){
      SoundEffect.mute = true;
    }
    if(str=="music"){
      SoundEffect.Stopmusic();
    }

  }
  @Override
  public void deactiveHandler(String str) {
    if(str=="sound"){
      SoundEffect.mute = false;
    }
    if(str=="music"){
      SoundEffect.Playmusic();
    }
  }
}
