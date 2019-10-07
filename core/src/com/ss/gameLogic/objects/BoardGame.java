package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.gameLogic.scene.GGameMain;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class BoardGame {
    TextureAtlas cardAtlas;
    Group group;
    Array<Card> allCard = new Array<>();
    Array<Integer> cards;
    GGameMain game;
    int turn =2 ,turn2 =0;


    public BoardGame(TextureAtlas cardAtlas, Group group, GGameMain game){
        this.cardAtlas = cardAtlas;
        this.group = group;
        this.game = game;
        renderCard();
        Gdx.app.log("okok","IDCard: "+Integer.toBinaryString(allCard.get(0).Key));
        Gdx.app.log("okok","IDCard: "+allCard.get(0).Key);
        distributeCardsOutSide();

    }
    void renderCard(){
        cards = CheckCard.makeCards();
        for(int i=1;i<53;i++){
                Card card = new Card(cardAtlas,group,i);
                allCard.add(card);


        }
        for (int i=0 ; i < allCard.size; i++){
            allCard.get(i).setKey(cards.get(i));
        }
        allCard.shuffle();
    }

    void distributeCardsOutSide(){
        allCard.shuffle();
        distributeCardInside(0);
    }

    void distributeCardInside(int index){
        if(index >= game.positionCards.size*2){
            Tweens.setTimeout(group, 0.1f, ()->{
                Card card = new Card(cardAtlas, group, 16);
                card.card.addAction(moveTo(500, 500, 1f, linear));
            });
            return;
        }
        float detalX = index >= game.positionCards.size ? allCard.get(0).card.getWidth(): 0;
        final int indexTemp = index+1;
        allCard.get(index).card.addAction(moveTo(game.positionCards.get(index%boardConfig.modePlay).x + detalX - allCard.get(index).card.getWidth()/2,game.positionCards.get(index%boardConfig.modePlay).y - allCard.get(index).card.getHeight()/2, 0.3f, linear));
        allCard.get(index).tileDown.addAction(sequence(
            moveTo(game.positionCards.get(index%boardConfig.modePlay).x + detalX - allCard.get(index).card.getWidth()/2,game.positionCards.get(index%boardConfig.modePlay).y - allCard.get(index).card.getHeight()/2, 0.3f, linear)
//            GSimpleAction.simpleAction((d, a)->{
//                distributeCardInside(indexTemp);
//                return true;
//            })
        ));
        Tweens.setTimeout(group, 0.2f, ()->{
            distributeCardInside(indexTemp);
        });
    }

    void distributeCards(){
        allCard.shuffle();
        for (int i = 0; i< boardConfig.modePlay*2; i++){
            if(boardConfig.modePlay==2){
                if(i%turn==0){
                    int finalI = i;
                    Tweens.setTimeout(group,0.3f*(i+1),()->{
                        //allCard.get(finalI).moveCard(boardConfig.playerX+(finalI*30), boardConfig.playerY);
                        allCard.get(finalI).moveCard(game.positionCards.get(0).x, game.positionCards.get(0).y);
                        allCard.get(finalI).setVisibleTiledown();
//                        playerCard.add(allCard.get(finalI));

                    });

                }else {
                    int finalI = i;
                    Tweens.setTimeout(group,0.3f*(i+1),()-> {
                        //allCard.get(finalI).moveCard(boardConfig.bot0X+((finalI-1)*30), boardConfig.bot0Y);
                        allCard.get(finalI).moveCard(game.positionCards.get(1).x, game.positionCards.get(1).y);
                        allCard.get(finalI).setVisibleTiledown();
//                        CardBotO.add(allCard.get(finalI));

                    });
                }
            }else if(boardConfig.modePlay==5){
                if(turn2==0){
                    turn2++;
                    int finalI = i;
                    Tweens.setTimeout(group,0.3f*(i+1),()->{
                        allCard.get(finalI).moveCard(boardConfig.playerX+(finalI*10), boardConfig.playerY);
                        allCard.get(finalI).setVisibleTiledown();
                    });

                }else if(turn2==1) {
                    int finalI = i;
                    turn2++;
                    Tweens.setTimeout(group,0.3f*(i+1),()-> {
                        allCard.get(finalI).moveCard(boardConfig.bot1X, boardConfig.bot1Y+(finalI*10));
                        allCard.get(finalI).scaleCard();
                        allCard.get(finalI).rotateCard(-90);
                        allCard.get(finalI).setVisibleTiledown();

                    });
                }else if(turn2==2){
                    int finalI = i;
                    turn2++;
                    Tweens.setTimeout(group,0.3f*(i+1),()-> {
                        allCard.get(finalI).moveCard(boardConfig.bot2X+((finalI-1)*10), boardConfig.bot2Y);
                        allCard.get(finalI).scaleCard();
                        allCard.get(finalI).setVisibleTiledown();

                    });
                }else if(turn2==3){
                    int finalI = i;
                    turn2++;
                    Tweens.setTimeout(group,0.3f*(i+1),()-> {
                        allCard.get(finalI).moveCard(boardConfig.bot3X +((finalI-1)*10), boardConfig.bot3Y);
                        allCard.get(finalI).scaleCard();
                        allCard.get(finalI).setVisibleTiledown();

                    });
                }else if(turn2==4){
                    int finalI = i;
                    turn2 =0;
                    Tweens.setTimeout(group,0.3f*(i+1),()-> {
                        allCard.get(finalI).moveCard(boardConfig.bot4X, boardConfig.bot4Y+(finalI*10));
                        allCard.get(finalI).scaleCard();
                        allCard.get(finalI).rotateCard(90);
                        allCard.get(finalI).setVisibleTiledown();

                    });
                }
            }

        }

    }

//    void test(){
//        cards = CheckCard.makeCards();
//        int[] pattern = new int[5];
//        //manual check
//        pattern[0] = CheckCard.nameMap.getKey("K Chuon", false);
//        pattern[1] = CheckCard.nameMap.getKey("K Ro", false);
//        pattern[2] = CheckCard.nameMap.getKey("K Co", false);
//        pattern[3] = CheckCard.nameMap.getKey("3 Co", false);
//        pattern[4] = CheckCard.nameMap.getKey("3 Bich", false);
//
//        for (int i : pattern) System.out.println(CheckCard.nameMap.get(i));
//        //int a = CheckCard.check(pattern);
//        //System.out.println(a);
//    }
}
