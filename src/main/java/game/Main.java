package game;

import engine.GameApplication;
import engine.GameEngine;
import engine.Window;

public class Main {
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            GameApplication gameLogic = new MyGame();
            GameEngine gameEng = new GameEngine("LWJGL Sandbox : " + gameLogic.getClass().getSimpleName(), 600, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}