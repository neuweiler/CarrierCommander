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

import com.jme3.math.Vector3f;

/**
 * Carrier Commander Global Constants
 *
 * @author Michael Neuweiler
 */
public interface Constants {

	boolean DEBUG = false;
	boolean AUTOSTART = false;

	// network settings
	int DEFAULT_PORT = 72224;
	String DEFAULT_HOST = "localhost";

	// names of game objects
	String CARRIER_PLAYER = "carrier-1";
	String CARRIER_ENEMY = "carrier-2";
	String WALRUS_1 = "walrus-1";
	String WALRUS_2 = "walrus-2";
	String WALRUS_3 = "walrus-3";
	String WALRUS_4 = "walrus-4";
	String MANTA_1 = "manta-1";
	String MANTA_2 = "manta-2";
	String MANTA_3 = "manta-3";
	String MANTA_4 = "manta-4";
	String MISSILE = "missile";

	// input mappings
	String INPUT_LEFT = "left";
	String INPUT_RIGHT = "right";
	String INPUT_UP = "up";
	String INPUT_DOWN = "down";
	String INPUT_ACCELERATE = "accelerate";
	String INPUT_DECELERATE = "decelerate";
	String INPUT_FIRE = "fire";
	String INPUT_GRAB_MOUSE = "grab";
	String INPUT_MOUSE_X = "mouse+x";
	String INPUT_MOUSE_Y = "mouse+y";
	String INPUT_MOUSE_X_NEGATIVE = "mouse-x";
	String INPUT_MOUSE_Y_NEGATIVE = "mouse-y";

	// name of menu's and controls (nifty)
	String SCREEN_START = "start";
	String SCREEN_HUD = "hud";
	String MENU_MAIN = "main";
	String MENU_CARRIER = "carrier";
	String MENU_WEAPONS = "weapons";
	String MENU_WALRUS = "walrus";
	String MENU_MANTA = "manta";
	String MENU_GAME = "game";
	String CONTROL_CONTROL = "Control";
	String CONTROL_MAP = "Map";
	String CONTROL_REPAIR = "Repair";
	String CONTROL_RESOURCES = "Resources";
	String CONTROL_MESSAGES = "Messages";
	String CONTROL_LASER = "Laser";
	String CONTROL_FLARES = "Flares";
	String CONTROL_MISSILES = "Missiles";
	String CONTROL_DRONES = "Drones";
	String CONTROL_STATUS = "Status";
	String CONTROL_EQUIP = "Equip";
	String CONTROL_HANGAR = "Hangar";
	String CONTROL_EXTENSION_BUTTON = "Button";
	String CONTROL_EXTENSION_CONTROLS = "Controls";
	String CONTROL_SUBCONTROLS = "subControls";
	String STYLE_SELECTED = "selected";
	String STYLE_UNSELECTED = "unselected";

	int MAP_SCENE_FACTOR = 100; // by how much the map coordinates are multiplied for the scene graph

	Vector3f LIGHT_DIRECTION = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);

	enum GameType {strategy, action, network}
}
