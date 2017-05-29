package game;

import engine.GameApplication;
import engine.GameEngine;
import engine.Window;

public class Main {
    public static void main(String[] args) {
        try {
            GameApplication myGame = new MyGame();
            GameEngine engine = new GameEngine("LWJGL Sandbox : " + myGame.getClass().getSimpleName(), 600, 480, myGame);
            engine.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}