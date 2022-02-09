# CarrierCommander
A java based open source re-make of a fantastic 80's game...

## Purpose
1) Keep up the original spirit of the game: It's a futuristic strategy game and machines are fighting against each other - humans are controlling remotely
2) Use state-of-the-art graphics based on OpenGL
3) Make the program run on major platforms (Linux, Mac, Windows, Android) by using Java and the JMonkey3 platform
4) Have fun and learn a lot

## Gameplay
A group of 63 islands rich in resources were found on the sea. 
Two fully automated aircraft carriers were launched to colonize the islands and collect their
resources. But an enemy hacker group hijacked one of the carriers and
starts to colonize the islands for their sinister purposes.
You have to take over manual control of the second carrier which unfortunately
is slower, less powerful and with a shorter range than the enemy one.
You have to use all your wits and either cut the enemy off from its
supply net and let it run out of fuel or you have to confront it in direct
battle - but chances of your survival are slim.

Instead of one enemy controlled by the CPU you may also play against
one or more human players in a networked game.

## Controls
#### Keyboard
* Throttle: PgUp/Down
* Rudder: left/right arrow
* Attitude (Manta): up/down arrow
#### Mouse
* Grab/ungrab mouse: right-click in empty area
* Throttle: scroll wheel
* Rudder: move left/right
* Attitude: move up/down
### Interface
On the left you'll find the main controls where you switch between
carrier, weapons, walrus and manta. Depending on this selection the
contents of the controls on the right change (and also your viewpoint).
If you click on the controls on the right, the ones on the bottom of
the screen change to give you the correct context.

## Development
This is a maven project. You can build it with maven or import it into Eclipse, IntelliJ,
etc. Maven will download the necessary jmonkey jars automatically - no need to install anything related
to jmonkey beforehand. 

As long as it's under heavy development, no binaries will be published.

### Status
#### Complete:
* water with ground waves :)
* realistic floating of carrier and walrus on water
* moving walrus (land + sea), manta and carrier
* collision with objects
#### Functional:
* all islands are generated (with same heightmap)
* multiplayer (very basic with local server, multiple players can connect)
* ship radar (all same colors)
* shooting missiles and particle cannon (from Manta)
#### Not implemented yet:
* damage
* maps
* AI
* buildings and their placement
* equipment of walrus/manta
* supply chain
* Various layouts/heightmaps for islands
* ...

### Contribution
Anyone who could contribute is very welcome! It's a fun but time consuming hobby of mine 
and progress would be much faster with more than one contributor.

One precondition: I want to understand what's being changed in PR's

## Screenshots
![Carrier Bridge](../master/misc/screenshots/carrier-1.png)
![Carrier Docking Bay](../master/misc/screenshots/carrier-2.png)
![Manta Walrus and Carrier](../master/misc/screenshots/manta-1.png)
![Manta over Island](../master/misc/screenshots/manta-2.png)
![Manta landing](../master/misc/screenshots/manta-3.png)
![Walrus on water](../master/misc/screenshots/walrus-1.png)
![Walrus on land](../master/misc/screenshots/walrus-2.png)
![Walrus shore](../master/misc/screenshots/walrus-3.png)

## License
OpenBSD

## References
- carrier model: http://tf3dm.com/3d-model/admiral-kuznetsov-class-carrier-19210.html
- walrus model: http://tf3dm.com/3d-model/btr80-34552.html
- textures, terrains, effects and manta model: jmonkey3 demos
