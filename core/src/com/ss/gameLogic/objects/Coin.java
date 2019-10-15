package com.ss.gameLogic.objects;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.core.G.G;
import com.ss.core.action.exAction.GArcMoveToAction;

import static com.badlogic.gdx.math.Interpolation.linear;

public class Coin {
  Group group;
  Image coin;
  int value;

  public Coin(Group group, int value){
    this.group = group;
    this.value = value;
    coin = (Image)G.c(Image.class).k("c500").add(this.group).wh2(0.45f, 0.45f).p(0, 0).ub();
  }

  public void move(){
    coin.addAction(Actions.forever(Actions.sequence(
        GArcMoveToAction.arcMoveTo(800, 100, 600,0, 1, linear),
        GArcMoveToAction.arcMoveTo(400, 100, 600,200, 1, linear)
//        GSimpleAction.simpleAction((d, a)->{
//          return true ;
//        })
    )));
  }
}
