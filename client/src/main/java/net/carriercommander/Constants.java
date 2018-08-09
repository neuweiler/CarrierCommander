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

/**
 * Carrier Commander Global Constants
 *
 * @author Michael Neuweiler
 */
public interface Constants {

  public enum GameType {strategy, action, network}

  // names of game objects
  public final static String CARRIER_PLAYER = "carrier-1";
  public final static String CARRIER_ENEMY  = "carrier-2";

  public final static String WALRUS_1 = "walrus-1";
  public final static String WALRUS_2 = "walrus-2";
  public final static String WALRUS_3 = "walrus-3";
  public final static String WALRUS_4 = "walrus-4";

  public final static String MANTA_1 = "manta-1";
  public final static String MANTA_2 = "manta-2";
  public final static String MANTA_3 = "manta-3";
  public final static String MANTA_4 = "manta-4";

  // key input
  public final static String INPUT_LEFT       = "left";
  public final static String INPUT_RIGHT      = "right";
  public final static String INPUT_UP         = "up";
  public final static String INPUT_DOWN       = "down";
  public final static String INPUT_ACCELERATE = "accelerate";
  public final static String INPUT_DECELERATE = "decelerate";

  // name of menu's and controls (nifty)
  public final static String SCREEN_START = "start";
  public final static String SCREEN_HUD   = "hud";

  public final static String MENU_MAIN    = "main";
  public final static String MENU_CARRIER = "carrier";
  public final static String MENU_WEAPONS = "weapons";
  public final static String MENU_WALRUS  = "walrus";
  public final static String MENU_MANTA   = "manta";
  public final static String MENU_GAME    = "game";

  public final static String CONTROL_CONTROL   = "Control";
  public final static String CONTROL_MAP       = "Map";
  public final static String CONTROL_REPAIR    = "Repair";
  public final static String CONTROL_RESOURCES = "Resources";
  public final static String CONTROL_MESSAGES  = "Messages";
  public final static String CONTROL_LASER     = "Laser";
  public final static String CONTROL_FLARES    = "Flares";
  public final static String CONTROL_MISSILES  = "Missiles";
  public final static String CONTROL_DRONES    = "Drones";
  public final static String CONTROL_STATUS    = "Status";
  public final static String CONTROL_EQUIP     = "Equip";
  public final static String CONTROL_HANGAR    = "Hangar";

  public final static String CONTROL_EXTENSION_BUTTON   = "Button";
  public final static String CONTROL_EXTENSION_CONTROLS = "Controls";
  public final static String CONTROL_SUBCONTROLS        = "subControls";

  public final static String STYLE_SELECTED   = "selected";
  public final static String STYLE_UNSELECTED = "unselected";
}
