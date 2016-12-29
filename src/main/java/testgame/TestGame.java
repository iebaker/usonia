package testgame;

import org.joml.Vector2i;
import xyz.izaak.radon.Game;
import xyz.izaak.radon.gamesystem.FlyingCameraSystem;
import xyz.izaak.radon.gamesystem.KeyReleaseQuitSystem;
import xyz.izaak.radon.math.Points;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class TestGame {
    public static final Vector2i WINDOW_SIZE = new Vector2i(1200, 800);

    public static void main(String... args) {
        Game testGame = new Game("TestGame", WINDOW_SIZE.x, WINDOW_SIZE.y, Points.BLACK);
        testGame.addGameSystem(new KeyReleaseQuitSystem(testGame.getWindow(), GLFW_KEY_ESCAPE));
        testGame.addGameSystem(new FlyingCameraSystem(Points.__Z));
        testGame.addGameSystem(new WorldBuilderSystem());
        testGame.run();
    }
}
