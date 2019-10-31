package com.ss.gameLogic.objects;

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
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.effect.SoundEffect;
import com.ss.gameLogic.scene.GameStart;

import java.text.DecimalFormat;

public class WatchVideo {
  TextureAtlas textureAtlas;
  Group group = new Group();
  Group groupText = new Group();
  Runnable runnable;
  BitmapFont fontUi;
  GameStart scene;
  //BoardGame boardGame;

  public WatchVideo(TextureAtlas textureAtlas, GameStart scene){
    GStage.addToLayer(GLayer.top,group);
    GStage.addToLayer(GLayer.top, groupText);
    this.textureAtlas = textureAtlas;
    this.scene = scene;
    this.runnable = runnable;
    initfont();
    showFrame();

  }
  void showFrame(){
    group.setPosition(-300, 0,Align.center);
    group.setOrigin(Align.center);
    Image frame = GUI.createImage(textureAtlas,"frameWatchAds");
    frame.setPosition(0,0, Align.center);
    group.addActor(frame);
    //////// btn/////////
    Image btnWatch = GUI.createImage(textureAtlas,"btnWatch");
    btnWatch.setOrigin(Align.center);
    btnWatch.setPosition(0,150,Align.center);
    group.addActor(btnWatch);
    group.addAction(Actions.moveTo(GMain.screenWidth/2,GMain.screenHeight/2,0.5f, Interpolation.swingOut));
    eventbtn(btnWatch);

  }
  void eventbtn(Image btn){
    btn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
      super.clicked(event, x, y);
        SoundEffect.Play(SoundEffect.click);
      btn.setTouchable(Touchable.disabled);
      btn.addAction(Actions.sequence(
          Actions.scaleTo(0.8f,0.8f,0.2f),
          Actions.scaleTo(1f,1f,0.2f)
      ));
      Tweens.setTimeout(group,0.2f,()->{
        group.addAction(Actions.moveTo(1500,0,0.3f,Interpolation.linear));
        Tweens.setTimeout(group,0.3f,()->{
          group.clear();
          groupText.clear();
          showAds();

        });
      });
      }
    });


  }
  void showAds(){
    if(GMain.platform.isVideoRewardReady()) {
      GMain.platform.ShowVideoReward((boolean success) -> {
        if (success) {
          boardConfig.moneyPlayer+=500000;
          GMain.prefs.putLong("myMonney",boardConfig.moneyPlayer);
          GMain.prefs.flush();
          scene.Labelmonney.setText(FortmartPrice(boardConfig.moneyPlayer));
          scene.showAdsStart();
          //boardGame.moneyTxts.get(0).setText(FortmartPrice(boardConfig.moneyPlayer));

        }
      });
    }else {
      Label notice = new Label("Chưa có Video tặng bạn 50.000$",new Label.LabelStyle(fontUi,null));
      notice.setPosition(GMain.screenWidth/2,GMain.screenHeight/2,Align.center);
      groupText.addActor(notice);
      notice.addAction(Actions.sequence(
        Actions.moveBy(0,-50,0.5f),
        GSimpleAction.simpleAction((d, a)->{
          notice.clear();
          notice.remove();
          boardConfig.moneyPlayer+=50000;
          GMain.prefs.putLong("myMonney",boardConfig.moneyPlayer);
          GMain.prefs.flush();
          scene.Labelmonney.setText(FortmartPrice(boardConfig.moneyPlayer));
          //boardGame.moneyTxts.get(0).setText(FortmartPrice(boardConfig.moneyPlayer));
          scene.showAdsStart();
          return true;
        })
      ));

    }
  }
  private String FortmartPrice(Long Price) {

    DecimalFormat mDecimalFormat = new DecimalFormat("###,###,###,###");
    String mPrice = mDecimalFormat.format(Price);

    return mPrice;
  }
  void initfont(){
    fontUi = GAssetsManager.getBitmapFont("font_white.fnt");

  }
}
