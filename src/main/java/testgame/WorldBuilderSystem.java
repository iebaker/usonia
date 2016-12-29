package testgame;

import org.joml.Vector3f;
import org.joml.Vector3i;
import xyz.izaak.radon.Channel;
import xyz.izaak.radon.exception.RadonException;
import xyz.izaak.radon.gamesystem.GameSystem;
import xyz.izaak.radon.math.OrthonormalBasis;
import xyz.izaak.radon.math.Points;
import xyz.izaak.radon.math.field.ScalarVolume;
import xyz.izaak.radon.mesh.Mesh;
import xyz.izaak.radon.mesh.geometry.IsosurfaceGeometry;
import xyz.izaak.radon.mesh.geometry.PolarSphereGeometry;
import xyz.izaak.radon.mesh.geometry.PolarSphereOutlineGeometry;
import xyz.izaak.radon.mesh.geometry.QuadGeometry;
import xyz.izaak.radon.mesh.geometry.QuadOutlineGeometry;
import xyz.izaak.radon.mesh.material.NormalMaterial;
import xyz.izaak.radon.mesh.material.PhongMaterial;
import xyz.izaak.radon.mesh.material.SolidColorMaterial;
import xyz.izaak.radon.world.Camera;
import xyz.izaak.radon.world.DirectionalLight;
import xyz.izaak.radon.world.Entity;
import xyz.izaak.radon.world.PointLight;
import xyz.izaak.radon.world.Portal;
import xyz.izaak.radon.world.Scene;

import static testgame.TestGame.WINDOW_SIZE;

/**
 * Created by ibaker on 29/12/2016.
 */
public class WorldBuilderSystem implements GameSystem {
    private Scene currentScene;
    private Camera camera;
    private Channel<Camera> currentCameraChannel;
    private Channel<Scene> currentSceneChannel;

    @Override
    public void initialize() {
        currentCameraChannel = Channel.request(Camera.class, Channel.CURRENT_CAMERA);
        currentSceneChannel = Channel.request(Scene.class, Channel.CURRENT_SCENE);

        float aspectRatio = (float) (WINDOW_SIZE.x) / (float) WINDOW_SIZE.y;
        float fov = Points.piOver(3);
        Vector3f eye = new Vector3f(9f, 9f, 4f);
        Vector3f look = new Vector3f(-1f, -1f, 0).normalize();
        Vector3f up = Points.__Z;
        camera = Camera.builder().aspectRatio(aspectRatio).fov(fov).eye(eye).look(look).up(up).build();
        currentCameraChannel.publish(camera);

        Scene spheresScene = buildSpheresScene();
        Scene isosurfaceScene = buildIsosurfaceScene();

        spheresScene.getPortals().get(0).link(isosurfaceScene.getPortals().get(0));
        isosurfaceScene.getPortals().get(0).link(spheresScene.getPortals().get(0));

        currentSceneChannel.publish(isosurfaceScene);
        currentSceneChannel.subscribe(scene -> this.currentScene = scene);
    }

    @Override
    public void update(float seconds) {
        camera.clearBuffers();
        try {
            camera.capture(currentScene);
        } catch (RadonException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Scene buildSpheresScene() {
        Mesh floorFill = new Mesh(new QuadGeometry(), new SolidColorMaterial(Points.GRAY));
        floorFill.scale(10f, 10f, 1f);
        Mesh floorOutline = new Mesh(new QuadOutlineGeometry(), new SolidColorMaterial(Points.WHITE));
        floorOutline.scale(10f, 10f, 1f);
        Entity floor = Entity.builder().build();
        floor.addMeshes(floorFill, floorOutline);

        Mesh solidSphereFill = new Mesh(new PolarSphereGeometry(15, 15), new SolidColorMaterial(Points.RED));
        Mesh sphereWireframe = new Mesh(new PolarSphereOutlineGeometry(15, 15), new SolidColorMaterial(Points.WHITE));
        sphereWireframe.scale(1.01f);
        Entity solidSphere = Entity.builder().build();
        solidSphere.addMeshes(solidSphereFill, sphereWireframe);
        solidSphere.translate(0f, 0f, 2f);

        Mesh normalSphereFill = new Mesh(new PolarSphereGeometry(15, 15), new NormalMaterial());
        Entity normalSphere = Entity.builder().build();
        normalSphere.addMeshes(normalSphereFill);
        normalSphere.translate(0f, 3f, 2f);

        PhongMaterial pinkPlastic = new PhongMaterial(Points.BLUE, Points.BLUE, Points.WHITE, Points.BLACK, 100f);
        Mesh phongSphereFill = new Mesh(new PolarSphereGeometry(15, 15), pinkPlastic);
        Entity phongSphere = Entity.builder().build();
        phongSphere.addMeshes(phongSphereFill);
        phongSphere.translate(0f, -3f, 2f);

        DirectionalLight sun = new DirectionalLight(Points.WHITE, Points.XYZ);

        OrthonormalBasis portalBasis = new OrthonormalBasis(Points._Y_, Points.__Z);
        Portal portal = new Portal(new Vector3f(-3f, 0f, Portal.PORTAL_DIMENSIONS.y / 2), portalBasis);

        Scene spheresScene = Scene.builder().build();
        spheresScene.addEntity(floor);
        spheresScene.addEntity(solidSphere);
        spheresScene.addEntity(normalSphere);
        spheresScene.addEntity(phongSphere);
        spheresScene.addDirectionalLight(sun);
        spheresScene.addPortal(portal);

        return spheresScene;
    }

    private Scene buildIsosurfaceScene() {
        int sampleDensity = 40;
        float worldSize = 20f;
        Vector3f min = new Vector3f(-worldSize / 2f);
        Vector3i dimensions = new Vector3i(sampleDensity);
        float fidelity = worldSize / (float) sampleDensity;

        ScalarVolume function = (x, y, z) -> (float) (Math.sin(x / 2) * Math.sin(y / 2) - z);
        IsosurfaceGeometry geometry = new IsosurfaceGeometry(function, min, dimensions, 0f, fidelity);

        PhongMaterial material = new PhongMaterial(Points.WHITE, Points.WHITE, Points.WHITE, Points.BLACK, 10f);

        Mesh isosurfaceMesh = new Mesh(geometry, material);
        Entity isosurfaceEntity = Entity.builder().build();
        isosurfaceEntity.addMeshes(isosurfaceMesh);

        PointLight green = new PointLight(new Vector3f(0f, 5f, 0f), new Vector3f(-2.5f, 2.5f, 3f));
        PointLight white = new PointLight(new Vector3f(3f, 3f, 3f), new Vector3f(2.5f, -2.5f, 3f));

        OrthonormalBasis portalBasis = new OrthonormalBasis(Points.x__, Points.__Z);
        Portal portal = new Portal(new Vector3f(3f, 0f, Portal.PORTAL_DIMENSIONS.y / 2), portalBasis);

        Scene isosurfaceScene = Scene.builder().build();
        isosurfaceScene.addEntity(isosurfaceEntity);
        isosurfaceScene.addPointLight(green);
        isosurfaceScene.addPointLight(white);
        isosurfaceScene.addPortal(portal);

        return isosurfaceScene;
    }
}
