package net.carriercommander.ui.controls.walrus;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import net.carriercommander.ui.AbstractState;

public class StateHUD extends AbstractState {
    private Node crossHairs, targetIndicator;
    private long timestamp = 0;

    @Override
    protected void initialize(Application app) {
        float size = 20f;
        float width = 4f;

        crossHairs = new Node();
        Geometry geometry = new Geometry("Quad", new Quad(size, width));
        geometry.move(size * -1.5f, 0, 0);
        crossHairs.attachChild(geometry);
        geometry = new Geometry("Quad", new Quad(size, width));
        geometry.move(size * 0.5f, 0, 0);
        crossHairs.attachChild(geometry);
        geometry = new Geometry("Quad", new Quad(width, size));
        geometry.move(0, size * -1.5f, 0);
        crossHairs.attachChild(geometry);
        geometry = new Geometry("Quad", new Quad(width, size));
        geometry.move(0, size * 0.5f, 0);
        crossHairs.attachChild(geometry);

        Material mat1 = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", new ColorRGBA(.3f, .8f, .8f, .6f));
        crossHairs.setMaterial(mat1);

        crossHairs.setCullHint(Spatial.CullHint.Never);
        crossHairs.setLocalTranslation((float) getApplication().getCamera().getWidth() / 2, (float) getApplication().getCamera().getHeight() / 2, 0);

        setEnabled(false);
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        ((SimpleApplication)getApplication()).getGuiNode().attachChild(crossHairs);
    }

    @Override
    protected void onDisable() {
        crossHairs.removeFromParent();
    }

    @Override
    public void update(float tpf) {
        if (System.currentTimeMillis() > timestamp + 100) {
//            updateGauges();
            timestamp = System.currentTimeMillis();
        }
    }

    protected void targetAcquired() {

    }

    protected void targetLost() {

    }

}
