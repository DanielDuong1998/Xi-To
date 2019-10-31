package com.ss.gameLogic.effect;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.ss.core.util.GAssetsManager;

import java.util.HashMap;

/* renamed from: com.ss.effect.SoundEffect */
public class SoundEffect {
  public static HashMap<String, Sound> a = new HashMap<>();

  public static int MAX_COMMON = 15;
    public static Music bgSound = null;
    public static Sound[] commons = null;
    public static boolean music = false;
    public static boolean mute = false;
    public static int buttonClick = 0;
    public static int slideChips = 1;
    public  static int slideFirstChip = 2;
    public  static int throwCard = 3;
    public  static int flipCard = 4;
    public  static int takeCard = 5;
    public  static int click = 6;
    public  static int PannelIn = 7;
    public  static int PannelOut = 8;
    public  static int dingdong = 9;
    public  static int win = 10;
    public  static int lose = 11;
    public  static int skip = 12;
    public  static int theo = 13;
    public  static int tothem = 14;
//    public  static int ganar = 4;
//    public  static int toggle = 5;
//    public  static int Unlock = 6;
//    public  static int laugh1 = 7;
//    public  static int laugh2 = 8;
//    public  static int laugh3 = 9;
//    public  static int rotateCards = 10;
//    public  static int throwCard = 11;
//    public  static int changeDir = 12;
//    public  static int nope = 13;
//    public  static int uno = 14;
//    public  static int win = 15;
//    public  static int lose = 16;


    public static Audio bg = null;


    public static void initSound() {
      commons = new Sound[MAX_COMMON];
        commons[buttonClick] = GAssetsManager.getSound("buttonClick.mp3");
        commons[slideChips] = GAssetsManager.getSound("slideChips.mp3");
        commons[slideFirstChip] = GAssetsManager.getSound("slideFirstChips.mp3");
        commons[throwCard] = GAssetsManager.getSound("throwCard.mp3");
        commons[flipCard] = GAssetsManager.getSound("flipCard.mp3");
        commons[takeCard] = GAssetsManager.getSound("takeCard.mp3");
        commons[click] = GAssetsManager.getSound("buttonClick.mp3");
        commons[PannelIn] = GAssetsManager.getSound("Panel-in.mp3");
        commons[PannelOut] = GAssetsManager.getSound("Panel-out.mp3");
        commons[dingdong] = GAssetsManager.getSound("dingdong.mp3");
        commons[win] = GAssetsManager.getSound("win.mp3");
        commons[lose] = GAssetsManager.getSound("lose.mp3");
        commons[skip] = GAssetsManager.getSound("skip.mp3");
        commons[theo] = GAssetsManager.getSound("theo.mp3");
        commons[tothem] = GAssetsManager.getSound("tothem.mp3");

//        commons[ganar] = GAssetsManager.getSound("ganarmusica.mp3");
//        commons[toggle] = GAssetsManager.getSound("Toggle.mp3");
//        //commons[Unlock] = GAssetsManager.getSound("Unlock.mp3");
//        commons[laugh1] = GAssetsManager.getSound("phep2.mp3");
//        commons[laugh2] = GAssetsManager.getSound("phep4.mp3");
//        commons[laugh3] = GAssetsManager.getSound("phepn.mp3");
//        commons[rotateCards] = GAssetsManager.getSound("rotateCards.mp3");
//        commons[throwCard] = GAssetsManager.getSound("throwCard.mp3");
//        commons[changeDir] = GAssetsManager.getSound("changeDir.mp3");
//        commons[nope] = GAssetsManager.getSound("nope.mp3");
//        commons[uno] = GAssetsManager.getSound("uno.mp3");
//        commons[win] = GAssetsManager.getSound("Win.mp3");
//        commons[lose] = GAssetsManager.getSound("Lose.mp3");

      a.put("buttonClick", commons[buttonClick]);
      a.put("slideChips", commons[slideChips]);
      a.put("slideFirstChip", commons[slideFirstChip]);
      a.put("throwCard", commons[throwCard]);
      a.put("flipCard", commons[flipCard]);
      a.put("takeCard", commons[takeCard]);


      bgSound = GAssetsManager.getMusic("bgmusic.mp3");

    }

    public static void Play(int name) {
        if (!mute) {
            commons[name].play();
          //a.get(name).play();
        }
    }

    public static void Playmusic() {
        music = false;
        bgSound.play();
        bgSound.setLooping(true);
        bgSound.setVolume(5f);
    }

    public static void Stopmusic() {
        music = true;
        bgSound.pause();
    }
}
