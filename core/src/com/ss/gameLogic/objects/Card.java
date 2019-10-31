package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.assetManager.X;
import com.ss.commons.Tweens;
import com.ss.core.G.AB;
import com.ss.core.G.G;

import static com.badlogic.gdx.math.Interpolation.*;

public class Card {
  TextureAtlas cardAtlas;
  Image card, tileDown;
  Group group;
  int Key;
  int valueName;
  BoardGame board;
  boolean isClick = false;
  boolean isDown = false;

  Card( TextureAtlas cardAtlas, Group group, int valueName){
    this.cardAtlas = cardAtlas;
    this.group = group;
    this.valueName = valueName;
    //Key = valueName;
    card = (Image)G.c(Image.class).k(valueName+"").add(this.group).w2(0.4f).h2(0.4f).a(Align.center).o(Align.center).ub();
    tileDown = (Image) G.c(Image.class).k("tiledown2").add(this.group).w2(0.4f).h2(0.4f).a(Align.center).o(Align.center).ub();


  }
  public void setVisibleTiledown(boolean isVisible){
    tileDown.setVisible(isVisible);
  }
  public void moveCard(float x,float y){
    card.addAction(AB.build(card).p(x - card.getWidth()/2,y - card.getHeight()/2).d(boardConfig.durationDistrbute).in(slowFast).done());
    tileDown.addAction(AB.build(card).p(x - card.getWidth()/2,y - card.getHeight()/2).d(boardConfig.durationDistrbute).in(slowFast).done());
  }
  public void scaleCard(float ratioX, float ratioY){
    card.addAction(Actions.scaleTo(ratioX,ratioY, boardConfig.durationDistrbute));
    tileDown.addAction(Actions.scaleTo(ratioX,ratioY, boardConfig.durationDistrbute));
  }
  public void rotateCard(int angle){
    card.addAction(Actions.rotateTo(angle, boardConfig.durationDistrbute));
    tileDown.addAction(Actions.rotateTo(angle, boardConfig.durationDistrbute));

  }
  public void setPosition(float x,float y){
    G.b(card).p(x, y);
    G.b(tileDown).p(x, y);
  }
  public void setKey(int id){
    Key=id;
  }

  public void flipCard(float x, float y){
    G.b(card).sx(0);
    tileDown.addAction(Actions.scaleTo(0,y,boardConfig.durationDistrbute));
    Tweens.setTimeout(group, boardConfig.durationDistrbute, ()->{
      card.addAction(Actions.scaleTo(x,y,boardConfig.durationDistrbute));
    });
  }

  public void flipTileDown(float x, float y){
    tileDown.setVisible(true);
    G.b(tileDown).sx(0);
    card.addAction(Actions.scaleTo(0, y, boardConfig.durationDistrbute));
    Tweens.setTimeout(group, boardConfig.durationDistrbute, ()->{
      tileDown.addAction(Actions.scaleTo(x, y, boardConfig.durationDistrbute));
    });
  }

  public void addClick(BoardGame board){
    this.board = board;
    card.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        board.setTouchCard();
        X.getSound("selectCard").play();
        G.b(card).touch(Touchable.disabled).c(Color.WHITE);
        isClick = true;
        board.isClickCard();
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  public void addClickTileDown(BoardGame board){
    this.board = board;
    G.b(tileDown).clk((e,x,y) ->{
      //sound
      X.getSound("flipCard").play();

      G.b(tileDown).touch(Touchable.disabled);
      board.cancelHandEffect();
      flipCard(1, 1);
    });
  }

  public void setColor(Color color){
    G.b(card).c(color);
  }

}
