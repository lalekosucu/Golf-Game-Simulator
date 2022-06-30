## The game was created by Aurélien Giuglaris Michael, Sezi Yakar, Lily McGrath, Gerben Prikanowski and Lale Koşucu Özgen.


### Starting the game

The Crazy Putting game can be run from MyLauncher.
It will show the terrain were lakes are represented by blue
and the height is shown by a shade of green.
Darker green means that the terrain is higher at that point.
The lightest green is at height 0 and the height 
of the darkest green is shown at the surface of the terrain.

At the surface of the terrain the player can also see 
the current coordinates and the number of time the ball has 
been hit.

By draging the screen the player can change the angle at 
which he/she looks at the golf course.
By scroling up or down the view will be from a lower or 
higher point.

Keys
ENTER: when this key is pressed a screen will pop up in 
which the player can enter the velocity in the x direction 
and the velocity in the y direcetion at which he/she wants 
to hit the ball.
SPACE: the bot will hit the ball as close to the target as 
it can.

------------------------------------------------------------
# Settings

In the App class the following constants can be changed:
- instance, changes the golf course on which is played
- botType, change the bot that is used when SPACE is pressed

In the PhysicsEngine the following constant can be changed:
- defaultSolver, changes the solver that is used to solve 
  first order differential equations

There a are more constant values that can be changed, 
however changing them is not recomended.

------------------------------------------------------------
# Input files

When the program is run it will use two files:
 - a file by the name "terrain" + instance + ".txt".
 - a file by the name "moves" + instance + ".txt".
In the App class the instance can be changed.

The terrain file has the following structure:
<String containing a formula for the height profile>
<kinetic friction coefficient> <static friction coefficient>
<objectType> <object variable 1> ... <object variable n>

The moves file has the following structure:
<x coordinate> <y coordinate>
<velocity in x direction> <velocity in y direction>
or
<x coordinate> <y coordinate>

------------------------------------------------------------
# Experiments

The program can also be run from the Expireiments class.
This class can be used to do the following two tests:

- Testing all the FirstOrderSolvers for the current 
  constants of the program. ("TestSolvers")

- Testing all the bots for all the terrains for the current 
  constants of the program. ("TestBots")
  
- Testing the different physics models for a number of 
  different slopes. ("TestModel")
  
Choosing which test you want to run can be done by changing 
the parameter testType. 
Give it the value of the string between brackets of the test
you want to run.
