package net.carriercommander.ui.hud.carrier;

import com.jme3.app.Application;
import com.simsilica.lemur.Container;
import net.carriercommander.objects.resources.*;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateResources extends WindowState {
	private StateResourceEditor resourceEditor = null;

	@Override
	protected void initialize(Application app) {
		window = new Window(100, 400);

		Container row1 = window.addChild(new Container());
		row1.addChild(new ImageButton("/Interface/hud/carrier/resources/assassinMissile.png", this, "assassinMissile"), 1);
		row1.addChild(new ImageButton("/Interface/hud/carrier/resources/harbingerMissile.png", this, "harbingerMissile"), 2);
		row1.addChild(new ImageButton("/Interface/hud/carrier/resources/hammerHeadMissile.png", this, "hammerHeadMissile"), 3);
		row1.addChild(new ImageButton("/Interface/hud/carrier/resources/fragmentationBomb.png", this, "fragmentationBomb"), 4);
		row1.addChild(new ImageButton("/Interface/hud/carrier/resources/quasarLaser.png", this, "quasarLaser"), 5);

		Container row2 = window.addChild(new Container());
		row2.addChild(new ImageButton("/Interface/hud/carrier/resources/pulseLaser.png", this, "pulseLaser"), 1);
		row2.addChild(new ImageButton("/Interface/hud/carrier/resources/defenseDrone.png", this, "defenseDrone"), 2);
		row2.addChild(new ImageButton("/Interface/hud/carrier/resources/commPod.png", this, "commPod"), 3);
		row2.addChild(new ImageButton("/Interface/hud/carrier/resources/virusBomb.png", this, "virusBomb"), 4);
		row2.addChild(new ImageButton("/Interface/hud/carrier/resources/commandCenter.png", this, "commandCenter"), 5);

		Container row3 = window.addChild(new Container());
		row3.addChild(new ImageButton("/Interface/hud/carrier/resources/decoyFlare.png", this, "decoyFlare"), 1);
		row3.addChild(new ImageButton("/Interface/hud/carrier/resources/reconDrone.png", this, "reconDrone"), 2);
		row3.addChild(new ImageButton("/Interface/hud/carrier/resources/fuelPod.png", this, "fuelPod"), 3);
		row3.addChild(new ImageButton("/Interface/hud/carrier/resources/manta.png", this, "manta"), 4);
		row3.addChild(new ImageButton("/Interface/hud/carrier/resources/walrus.png", this, "walrus"), 5);

		resourceEditor = new StateResourceEditor();
		getStateManager().attach(resourceEditor);
		resourceEditor.setEnabled(false);
	}

	@Override
	protected void cleanup(Application app) {
	}

	private void assassinMissile() {
		resourceEditor.setResourceItem(new AssassinMissile());
	}

	private void harbingerMissile() {
		resourceEditor.setResourceItem(new HarbingerMissile());
	}

	private void hammerHeadMissile() {
		resourceEditor.setResourceItem(new HammerHeadMissile());
	}

	private void fragmentationBomb() {
		resourceEditor.setResourceItem(new FragmenationBomb());
	}

	private void quasarLaser() {
		resourceEditor.setResourceItem(new QuasarLaser());
	}

	private void pulseLaser() {
		resourceEditor.setResourceItem(new PulseLaser());
	}

	private void defenseDrone() {
		resourceEditor.setResourceItem(new DefenseDrone());
	}

	private void commPod() {
		resourceEditor.setResourceItem(new CommPod());
	}

	private void virusBomb() {
		resourceEditor.setResourceItem(new VirusBomb());
	}

	private void commandCenter() {
		resourceEditor.setResourceItem(new CommandCenter());
	}

	private void decoyFlare() {
		resourceEditor.setResourceItem(new DecoyFlare());
	}

	private void reconDrone() {
		resourceEditor.setResourceItem(new ReconDrone());
	}

	private void fuelPod() {
		resourceEditor.setResourceItem(new FuelPod());
	}

	private void manta() {
		resourceEditor.setResourceItem(new Manta());
	}

	private void walrus() {
		resourceEditor.setResourceItem(new Walrus());
	}
}
