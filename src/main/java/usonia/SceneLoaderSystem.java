package usonia;

import xyz.izaak.radon.external.xml.XmlSceneLoader;
import xyz.izaak.radon.gamesystem.GameSystem;
import xyz.izaak.radon.scene.Scene;

/**
 * Created by ibaker on 28/01/2017.
 */
public class SceneLoaderSystem implements GameSystem {
    @Override
    public void initialize() {
        XmlSceneLoader testSceneLoader = new XmlSceneLoader("testScene.xml");
        Scene test = testSceneLoader.newInstance();
        System.out.println(test.getNode("root:entities:sphere"));
    }
}
