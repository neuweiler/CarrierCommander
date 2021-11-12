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
import net.carriercommander.network.model.PlayerData;

/**
 * Carrier Commander Global Constants
 *
 * @author Michael Neuweiler
 */
public interface Constants {

	boolean DEBUG = false;
	boolean AUTOSTART = false;

	// network settings
	int DEFAULT_PORT = 54321;
	String DEFAULT_HOST = "localhost";
	String GAME_NAME = "CarrierCommand";
	int GAME_VERSION = 1;

	// names of game objects
	String CARRIER = "carrier";
	String WALRUS = "walrus";
	String MANTA = "manta";
	String MISSILE = "missile";
	String PROJECTILE = "projectile";

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

	int MAP_SCENE_FACTOR = 100; // by how much the map coordinates are multiplied for the scene graph

	Vector3f LIGHT_DIRECTION = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);

	int NUM_WALRUS = 4;
	int NUM_MANTA = 4;

	enum GameType {strategy, action, network}
}
