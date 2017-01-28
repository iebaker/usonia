package usonia;

import xyz.izaak.radon.external.xml.XmlSceneLoader;

public class Application {
    public static void main(String... args) {
        System.out.println("hey");

        XmlSceneLoader testSceneLoader = new XmlSceneLoader("testScene.xml");
        testSceneLoader.newInstance();
    }
}
