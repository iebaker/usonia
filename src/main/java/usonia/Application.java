package usonia;

import xyz.izaak.radon.Game;
import xyz.izaak.radon.gamesystem.KeyReleaseQuitSystem;
import xyz.izaak.radon.math.Points;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class Application {
    public static void main(String... args) {
        Game game = new Game("Usonia", 800, 800, Points.BLACK);
        game.addGameSystem(new KeyReleaseQuitSystem(game.getWindow(), GLFW_KEY_ESCAPE));
        game.addGameSystem(new SceneLoaderSystem());
        game.run();
    }
}
