package usonia;

import xyz.izaak.radon.external.xml.XmlSceneLoader;
import xyz.izaak.radon.scene.Scene;

public class Application {
    public static void main(String... args) {
        System.out.println("hey");

        XmlSceneLoader testSceneLoader = new XmlSceneLoader("testScene.xml");
        Scene test = testSceneLoader.newInstance();

        System.out.println(test.getNode("root"));
    }
}
