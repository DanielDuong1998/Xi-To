package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.ss.core.util.GUI;

public class Card {
    TextureAtlas cardAtlas;
    Image card, tileDown;
    Group group;
    public int[] value;
    int Key ;

    Card( TextureAtlas cardAtlas, Group group, int value){
        this.cardAtlas = cardAtlas;
        this.group = group;
        this.value =new int[]{value};
        card = GUI.createImage(cardAtlas,""+this.value[0]);
        card.setWidth(card.getWidth()*0.4f);
        card.setHeight(card.getHeight()*0.4f);
        this.group.addActor(card);
        card.setAlign(Align.center);
        card.setOrigin(Align.center);
        card.setPosition(0,0);
        //////// tileDown////////
        tileDown = GUI.createImage(cardAtlas,"tiledown");
        tileDown.setWidth(tileDown.getWidth()*0.4f);
        tileDown.setHeight(tileDown.getHeight()*0.4f);
        tileDown.setPosition(0,0);
        tileDown.setOrigin(Align.center);
        tileDown.setAlign(Align.center);
        this.group.addActor(tileDown);


    }
    public void setVisibleTiledown(){
        tileDown.setVisible(false);
    }
    public void moveCard(float x,float y){
        card.addAction(Actions.moveTo(x - card.getWidth()/2, y - card.getHeight()/2, boardConfig.durationDistrbute, Interpolation.slowFast));
        tileDown.addAction(Actions.moveTo(x - tileDown.getWidth()/2, y - tileDown.getHeight()/2, boardConfig.durationDistrbute, Interpolation.slowFast));
    }
    public void scaleCard(){
        card.addAction(Actions.scaleTo(0.3f,0.3f, boardConfig.durationDistrbute));
        tileDown.addAction(Actions.scaleTo(0.3f,0.3f, boardConfig.durationDistrbute));

    }
    public void rotateCard(int angle){
        card.addAction(Actions.rotateTo(angle, boardConfig.durationDistrbute));
        tileDown.addAction(Actions.rotateTo(angle, boardConfig.durationDistrbute));

    }
    public void setPosition(float x,float y, int align){
        card.setPosition(x,y, align);
        tileDown.setPosition(x,y, align);
    }
    public void setKey(int id){
        Key=id;
    }

}
