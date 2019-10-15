package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.ss.commons.Tweens;
import com.ss.core.G.AB;
import com.ss.core.G.G;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.gameLogic.scene.GGameMain;


import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class BoardGame {
    TextureAtlas cardAtlas;
    Group group;
    Group groupBtn;
    Array<Card> allCard = new Array<>();
    Array<Integer> cards;
    GGameMain game;
    Array<Array<Card>> cardsPlay;
    Array<Group> cardsGroup;
    int indexCard = 0;
    int turnGame = 0;
    int turnStart = 0;
    boolean flagDistribute = false;
    int turnDistribute = 3;
    Image btnSkip, btnEspouse, btnAddMore;
    Array<Integer> passList;
    Image hand, circle;

    public BoardGame(TextureAtlas cardAtlas, Group group, GGameMain game){
        this.cardAtlas = cardAtlas;
        this.group = group;
        this.groupBtn = new Group();
        GStage.addToLayer(GLayer.top, groupBtn);
        this.game = game;
        turnGame = turnStart;
        initPassList();
        initCards();
        initCardsGroup();
        renderCard();
        Gdx.app.log("okok","IDCard: "+Integer.toBinaryString(allCard.get(0).Key));
        Gdx.app.log("okok","IDCard: "+allCard.get(0).Key);
        distributeCardsOutSide();

//      GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(cardAtlas, group);
//      greedilyAlgorithm.renderChip(8698422635l, 470, 200);
//      greedilyAlgorithm.moveChips(600, 500, 0.4f, fastSlow, ()->{
//          Gdx.app.log("debug", "run done!");
//      });
//

    }

    private void initPassList(){
        passList = new Array<>();
        for(int i = 0; i < boardConfig.modePlay; i++){
            int a = 0;
            passList.add(a);
        }
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
        distributeCardInside();
    }

    void distributeCardInside(){
        if(indexCard >= game.positionCards.size*2){
            Tweens.setTimeout(group, 0.1f, ()->{
              // todo: complete distribute Cards, todo somethings.
//                Card card = new Card(cardAtlas, cardsGroup.get(0), 10);
//                card.moveCard(100, 100);
                moveCardsStart();
            });
            return;
        }


        Card card = new Card(cardAtlas, cardsGroup.get(turnGame%boardConfig.modePlay), allCard.get(indexCard).valueName);
        cardsPlay.get(turnGame%boardConfig.modePlay).add(card);
        allCard.get(indexCard).card.setVisible(false);
        allCard.get(indexCard).setVisibleTiledown(false);

        float detalX = indexCard >= game.positionCards.size ? allCard.get(0).card.getWidth()/3: 0;
        final int indexTemp = indexCard+1;

        G.b(card.card).r(180);
        G.b(card.tileDown).r(180);

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
            }
            else {
                cardsPlay.get(i).get(0).flipCard(1, 1);
                Vector2 positionTemp = new Vector2(cardsPlay.get(i).get(0).card.getX(), cardsPlay.get(i).get(0).card.getY());
                cardsPlay.get(i).get(0).setPosition(cardsPlay.get(i).get(1).card.getX(), cardsPlay.get(i).get(1).card.getY());
                cardsPlay.get(i).get(1).setPosition(positionTemp.x, positionTemp.y);
                swapZIndexCards(cardsPlay.get(i).get(0),cardsPlay.get(i).get(1));
            }
        }

        distributeCardPlay();
    }

    private void distributeCardPlay(){
        if(turnDistribute == 6){
            //todo: goi ham chung tien, show card -> end game
            showCardEnd();
            return;
        }
        if(passList.get(turnGame%boardConfig.modePlay) == 1){
            turnGame++;
            distributeCardPlay();
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
                bet();
                return;
            }
        }

        //todo:: kiem tra neu da up bai tang turn len
        Card card = new Card(cardAtlas, cardsGroup.get(turnGame%boardConfig.modePlay), allCard.get(indexCard).valueName);
        allCard.get(indexCard).card.setVisible(false);
        allCard.get(indexCard).setVisibleTiledown(false);

        if(turnGame%boardConfig.modePlay == 0 && turnDistribute == 5){
            card.addClickTileDown(this);
        }

        int turnDistributeTemp = turnDistribute;

        float detalX = indexCard >= game.positionCards.size ? cardsPlay.get(turnGame%boardConfig.modePlay).size*allCard.get(0).card.getWidth()/3: 0;

        G.b(card.card).r(180);
        G.b(card.tileDown).r(180);

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
            else if(turnGame%boardConfig.modePlay == 1){
                Gdx.app.log("debug", "da bat tay");
                float pX1 = cardsPlay.get(0).get(4).card.getX() + boardConfig.widthCard*0.4f/2, pY1 = game.positionCards.get(0).y;
                hand = (Image) G.c(Image.class).k("hand").wh2(0.65f, 0.65f).add(group).p(pX1, pY1).ub();
                circle = (Image) G.c(Image.class).k("circle").wh2(0.65f, 0.65f).add(group).ub();
                float pX2 = cardsPlay.get(0).get(4).card.getX() , pY2 = game.positionCards.get(0).y - circle.getHeight()/2;
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
        Gdx.app.log("debug", "turn game: " + turnGame%boardConfig.modePlay);
        if(turnGame%boardConfig.modePlay == turnStart){
            if(flagDistribute){
                Gdx.app.log("debugabc456", turnGame%boardConfig.modePlay + "");
                flagDistribute = !flagDistribute;
                distributeCardPlay();
                return;
            }
            else {
                flagDistribute = !flagDistribute;
            }
        }
        if(turnGame%boardConfig.modePlay == 0){
            Gdx.app.log("debugabc", turnGame%boardConfig.modePlay + "");

            if(passList.get(0) != 1)
                showAllbutton();
            else {
                turnGame++;
                bet();
            }
            return;
        }
        float duaration = (int) Math.floor(Math.random()*2 + 1);
        Tweens.setTimeout(group, duaration, ()->{
            Gdx.app.log("debug", "turn: " + turnGame%boardConfig.modePlay + " to ne!");
            turnGame++;
            bet();
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
        eventBtnAddMore(btnAddMore);

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
        G.b(btn).clk((e,x,y)->{
            btn.addAction(Actions.sequence(
                Actions.scaleTo(0.8f,0.8f,0.1f),
                Actions.scaleTo(1f,1f,0.1f)
            ));
            Tweens.setTimeout(groupBtn,0.2f,()->{
                new MoreBets(cardAtlas, this);
            });
        });
    }

    void eventBtnSkip(Image btn){
        G.b(btn).clk((e,x,y)->{
            btn.addAction(Actions.sequence(
                Actions.scaleTo(0.8f,0.8f,0.1f),
                Actions.scaleTo(1f,1f,0.1f)
            ));
            Tweens.setTimeout(groupBtn,0.2f,()->{
                clickBtnSkip();
            });
        });
    }

    void eventBtnEspouse(Image btn){
        G.b(btn).clk((e,x,y)->{
            btn.addAction(Actions.sequence(
                Actions.scaleTo(0.8f,0.8f,0.1f),
                Actions.scaleTo(1f,1f,0.1f)
            ));
            Tweens.setTimeout(groupBtn,0.2f,()->{
                clickBtnEspouse();
            });
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
        clickBetBtn();
    }

    private void clickBtnEspouse(){
        clickBetBtn();
    }

    private void swapZIndexCards(Card card1, Card card2){
        int ZIndex0 = card1.card.getZIndex();
        int ZIndex1 = card1.tileDown.getZIndex();
        card1.card.setZIndex(card2.card.getZIndex());
        card1.tileDown.setZIndex(card2.tileDown.getZIndex());
        card2.card.setZIndex(ZIndex0);
        card2.tileDown.setZIndex(ZIndex1);

    }
}
