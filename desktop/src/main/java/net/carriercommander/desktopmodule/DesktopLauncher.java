package net.carriercommander.desktopmodule;

import net.carriercommander.CarrierCommander;
import com.jme3.system.AppSettings;
import net.carriercommander.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;

import net.carriercommander.ui.config.StateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to launch a jme application in desktop environment
 *
 */
public class DesktopLauncher {

    private static final Logger logger = LoggerFactory.getLogger(CarrierCommander.class);

    public static void main(String[] args) {
        final CarrierCommander game = new CarrierCommander();

        final AppSettings settings = new AppSettings(true);
        try {
            settings.load(StateConfig.SETTINGS_TOKEN);
        } catch (BackingStoreException e) {
            logger.error("unable to load previous settings", e);
        }
        settings.setSettingsDialogImage("/Interface/splash-512.png");
        settings.setTitle("Carrier Commander");
        if (!isMac()) {
            settings.setIcons(loadIcons());
        }

        game.setSettings(settings);
        game.setShowSettings(!Constants.AUTOSTART); //Settings dialog not supported on mac
        game.start();
    }

    private static BufferedImage[] loadIcons() {
        ArrayList<BufferedImage> icons = new ArrayList<>();
        try {
            icons.add(ImageIO.read(CarrierCommander.class.getResource("/Icons/icon-128.png")));
            icons.add(ImageIO.read(CarrierCommander.class.getResource("/Icons/icon-32.png")));
            icons.add(ImageIO.read(CarrierCommander.class.getResource("/Icons/icon-16.png")));
        } catch (Exception e) {
            logger.warn("Error loading globe icons", e);
        }
        return icons.toArray(new BufferedImage[0]);
    }

    private static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac") ;
    }
}
