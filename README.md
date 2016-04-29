Introduction
------------
A simple clone of first stage of original Nintendo NES Super Mario with a slight touch of Dragon Ball Z anime.

Controls
--------
    A/Left: Left
    D/Right: Right
    Space: Jump
    Shift (hold): Sprint
    R: Shoot Fireballs (should have adequate powerup)

Standalone Java Package
----------------------

Working standalone java package can be downloaded **[here][1]**.

(*Note: Java 8 or higher required*).

-------------------------------------------------------

Changing Resolution
-------------------
Game resolution can be changed from file `size_config.dat` (if file doesn't exist, run game once and it will be generated), which can be opened and editted with notepad. First value is the WIDTH and second is the HEIGHT (separated with a colon `:`). Any value between 600 to 1920 for WIDTH and 300 to 1080 for HEIGHT may be used (image scaaling limitations). Any out of bounds or invalid values will default to 1000x600 resolution.

Screenshots
-----------

[![Powerup][2]][2]

[![Saiyan Mode][3]][3]

[![Ending Flag Pole Sequence][4]][4]

Developing from this Base
------------------------
This game was created in a fairly modular way. So adding/removing things is quite easy and the underlying game engine can be practically used as initial base for many 2D games. Proceed by reading `Javadoc` to understand classes and methods.

Credits
----------
Jaskaranbir


  [1]: https://github.com/Jaskaranbir/Super-Mario/tree/master/dist/SuperMario.jar
  [2]: http://i.stack.imgur.com/ku96E.png
  [3]: http://i.stack.imgur.com/9pcvb.png
  [4]: http://i.stack.imgur.com/JnGzx.png