package net.carriercommander.vrmodule;

import net.carriercommander.CarrierCommander;
import com.jme3.math.Vector3f;
import com.jme3.app.LostFocusBehavior;
import com.jme3.system.AppSettings;
import com.onemillionworlds.tamarin.actions.ActionType;
import com.onemillionworlds.tamarin.actions.OpenXrActionState;
import com.onemillionworlds.tamarin.actions.actionprofile.Action;
import com.onemillionworlds.tamarin.actions.actionprofile.ActionManifest;
import com.onemillionworlds.tamarin.actions.actionprofile.ActionSet;
import com.onemillionworlds.tamarin.openxr.XrAppState;
import com.onemillionworlds.tamarin.vrhands.HandSpec;
import com.onemillionworlds.tamarin.vrhands.VRHandsAppState;

/**
 * Used to launch a jme application in desktop VR environment
 *
 */
public class VrLauncher {
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.put("Renderer", AppSettings.LWJGL_OPENGL45); // OpenXR only supports relatively modern OpenGL
        settings.setTitle("CarrierCommander");
        settings.setVSync(false); // don't want to VSync to the monitor refresh rate, we want to VSync to the headset refresh rate

        XrAppState xrState = new XrAppState();
        xrState.movePlayersFeetToPosition(new Vector3f(0,0,10));
        xrState.playerLookAtPosition(new Vector3f(0,0,0));

        CarrierCommander app = new CarrierCommander(xrState, new OpenXrActionState(manifest(), ActionSets.MAIN), new VRHandsAppState(handSpec()));
        app.setLostFocusBehavior(LostFocusBehavior.Disabled);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }


    private static ActionManifest manifest(){

        //see https://github.com/oneMillionWorlds/Tamarin/wiki/Action-Based-Vr for more details on actions

        Action handPose = Action.builder()
                .actionHandle(ActionHandles.HAND_POSE)
                .translatedName("Hand Pose")
                .actionType(ActionType.POSE)
                .withSuggestAllKnownAimPoseBindings()
                .build();

        return ActionManifest.builder()
                .withActionSet(ActionSet
                                .builder()
                                .name("main")
                                .translatedName("Main Actions")
                                .priority(1)
                                .withAction(handPose)
                                //add more actions here
                                .build()
                ).build();
    }

    /**
     * The hand spec describes the openXR actions that are bound to the hand graphics
     * The hand model could also be changed here but the tamarin default is being used here
     */
    private static HandSpec handSpec(){
        return HandSpec.builder(
                ActionHandles.HAND_POSE,
                ActionHandles.HAND_POSE
        ).build();
    }

}
