package testgame;

import org.joml.Vector2i;
import xyz.izaak.radon.Game;
import xyz.izaak.radon.gamesystem.FlyingCameraSystem;
import xyz.izaak.radon.gamesystem.KeyReleaseQuitSystem;
import xyz.izaak.radon.math.Points;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class TestGame {
    static final Vector2i WINDOW_SIZE = new Vector2i(1200, 800);

    public static void main(String... args) {
        // Create a new game with a 1200x800 window
        Game testGame = new Game("TestGame", WINDOW_SIZE.x, WINDOW_SIZE.y, Points.BLACK);

        // Game should exit when player presses ESC
        testGame.addGameSystem(new KeyReleaseQuitSystem(testGame.getWindow(), GLFW_KEY_ESCAPE));

        // Camera motion uses WASD (move) + LSHIFT (down) + SPACE (up) + mouse (look around)
        testGame.addGameSystem(new FlyingCameraSystem(Points.__Z));

        // Add TestGame custom world builder system which will set up demo scenes
        testGame.addGameSystem(new WorldBuilderSystem());

        // And we're off!
        testGame.run();
    }
}
