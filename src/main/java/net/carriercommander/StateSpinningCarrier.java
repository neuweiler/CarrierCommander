package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import net.carriercommander.ui.AbstractState;

public class StateSpinningCarrier extends AbstractState {

	private Node node;

	private DirectionalLight light;
	private AmbientLight ambient;

	@Override
	protected void initialize(Application app) {
		node = new Node();
		Spatial carrier = app.getAssetManager().loadModel("Models/CarrierPlayer/carrier.obj");
		carrier.move(0, 3.7f, 0);
		node.attachChild(carrier);

		light = new DirectionalLight();
		light.setDirection(new Vector3f(-0.2f, -1, -0.3f).normalizeLocal());

		ambient = new AmbientLight();
		ambient.setColor(new ColorRGBA(0.25f, 0.25f, 0.25f, 1));
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		Node root = getRootNode();
		root.attachChild(node);

		getApplication().getCamera().setLocation(new Vector3f(0, 50, 150));
		getApplication().getCamera().lookAtDirection(new Vector3f(0, -.1f, -1), Vector3f.UNIT_Y);

		root.addLight(light);
		root.addLight(ambient);
	}

	@Override
	protected void onDisable() {
		node.removeFromParent();
		getRootNode().removeLight(light);
		getRootNode().removeLight(ambient);
	}

	@Override
	public void update(float tpf) {
		node.rotate(0, tpf / 5, 0);
	}
}
