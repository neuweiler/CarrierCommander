/*
 * Copyright (c) 2015, Michael Neuweiler
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of CarrierCommander nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package net.carriercommander;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import net.carriercommander.Constants.GameType;
import net.carriercommander.network.Utils;
import net.carriercommander.ui.menu.StateMainMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.prefs.BackingStoreException;

/**
 * Carrier Commander Main Class
 *
 * @author Michael Neuweiler
 */
public class CarrierCommander extends SimpleApplication {
	private static final Logger logger = LoggerFactory.getLogger(CarrierCommander.class);

	public static void main(String... args) {
		Utils.initSerializers();
		CarrierCommander app = new CarrierCommander();
		AppSettings settings = new AppSettings(true);

		settings.setFrameRate(60);
		settings.setResolution(1280, 720);
		settings.setFullscreen(false);
		settings.setVSync(true);
		try {
			settings.load("Carrier Commander");
		} catch (BackingStoreException e) {
			logger.error("unable to load previous settings", e);
		}
		settings.setSettingsDialogImage("/Interface/splash-512.png");
		settings.setTitle("Carrier Commander");

		        /*
        try {
            BufferedImage[] icons = new BufferedImage[] {
                    ImageIO.read( TreeEditor.class.getResource( "/-icon-128.png" ) ),
                    ImageIO.read( TreeEditor.class.getResource( "/-icon-32.png" ) ),
                    ImageIO.read( TreeEditor.class.getResource( "/-icon-16.png" ) )
                };
            settings.setIcons(icons);
        } catch( IOException e ) {
            log.warn( "Error loading globe icons", e );
        }*/

		app.setShowSettings(!Constants.AUTOSTART);
		app.setSettings(settings);
		app.start();
	}

	public CarrierCommander() {
		super(new StateMainMenu(), new StateSpinningCarrier());
	}

	@Override
	public void simpleInitApp() {
		setDisplayFps(Constants.DEBUG);
		setDisplayStatView(Constants.DEBUG);
//		flyCam.setEnabled(false); // Disable the default flyby cam
		createUI();

		if (Constants.AUTOSTART) {
//			startGame(GameType.action.toString());
		}
	}

	private void createUI() {
		GuiGlobals.initialize(this);
		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);
	}

}
