package com.ss.gameLogic.objects;

import com.ss.GMain;

public class boardConfig {
    public static float ratioX = GMain.screenWidth/GMain.screenHeight;
    public static float ratioY = GMain.screenHeight/GMain.screenWidth;
    public static float bot0X=GMain.screenWidth/2-(169/2)-10;
    public static float bot0Y=0;
    public static float bot1X=GMain.screenWidth-200;
    public static float bot1Y=GMain.screenHeight/2-140;
    public static float bot2X=GMain.screenWidth/2+169;
    public static float bot2Y=0;
    public static float bot3X=GMain.screenWidth/2-169*2;
    public static float bot3Y=0;
    public static float bot4X=100;
    public static float bot4Y=GMain.screenHeight/2-180;
    public static float playerX=GMain.screenWidth/2- (169/2)-10;
    public static float playerY=GMain.screenHeight-231;
    public static int widthCard = 169;
    public static int heightCard = 231;
    public static int modePlay = 6;
    public static float durationDistrbute = 0.2f;


}
