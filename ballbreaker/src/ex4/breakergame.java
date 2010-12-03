package ex4;
import java.util.Timer;

import jgame.*;
import jgame.platform.*;

public class breakergame extends JGEngine {
	double count = 0;
	public static void main(String [] args) {
		new breakergame(new JGPoint(640,480));
	}

	/** Application constructor. */
	public breakergame(JGPoint size) { initEngine(size.x,size.y); }

	/** Applet constructor. */
	public breakergame() { initEngineApplet(); }

	public void initCanvas() {
		
		setCanvasSettings(20,15,16,16,JGColor.black,new JGColor(255,246,199),null);
	}
	public void doFrameInGame() {
		moveObjects();
	
		if (getKey(' ')) {
			// ensure the key has to be pressed again to register
			clearKey(' ');
			// return to the Title state
			setGameState("Title");
		}

	}
	public void genBrick(int ival,int xval,int yval){
		for (int i=ival; i<xval; i+=20){
			new MyBrick(i,yval);
		}
	}
	public void initGame() {
		setFrameRate(35,2);
		defineMedia("config.tbl");
		setBGImage("mybackground");
		
		setGameState("Title");

		
	}
	
	public void startTitle() {
		// we need to remove all game objects when we go from in-game to the
		// title screen
		removeObjects(null,0);
	}

	public void paintFrameTitle() {
		drawString("Ball Breaker", pfWidth()/2, 80, 0);
		drawString("Press SPACE to go to Start The Game",pfWidth()/2,90,0);
		drawString("Press Left and right Arrows to move the bar in an accelerated manner ",pfWidth()/2,100,0);
		drawString("Press DOWN arrow to stop the Bar ie to make speed 0 ",pfWidth()/2,110,0);
		drawString("Happy Gaming ",pfWidth()/2,120,0);
		drawString("If the ball misses the bar you will be Welcomed back to this page ",pfWidth()/2,150,0);
		drawString("Happy Gaming ",pfWidth()/2,170,0);
		
		
	}

	public void doFrameTitle() {
		if (getKey(' ')) {
			// ensure the key has to be pressed again to register
			clearKey(' ');
			// Set both StartGame and InGame states simultaneously.
			// When setting a state, the state becomes active only at the
			// beginning of the next frame.
			setGameState("StartGame");
			addGameState("InGame");
			// set a timer to remove the StartGame state after a few seconds,
			// so only the InGame state remains.
			new JGTimer(
				50, // number of frames to tick until alarm
				true, // true means one-shot, false means run again
				      // after triggering alarm
				"StartGame" // remove timer as soon as the StartGame state
				            // is left by some other circumstance.
				            // In particular, if the game ends before
				            // the timer runs out, we don't want the timer to
				            // erroneously trigger its alarm at the next
				            // StartGame.
			) {
				// the alarm method is called when the timer ticks to zero
				public void alarm() {
					removeGameState("StartGame");
				}
			};
		}
	}
	public void paintFrameStartGame() {
		drawString("Enjoy the Game.",pfWidth()/2,90,0);
	}

	/** Called once when game goes into the InGame state. */
	public void startInGame() {
		// when the game starts, we create a game object
			genBrick(10, 301, 0);
			genBrick(30, 261, 20);
			genBrick(50, 241, 40);
			genBrick(70, 221, 60);
	
		new MyBar();
		new MyBall();
	}


	

	public void doFrame() {
		moveObjects();	
		//checkcollision b/w different objects
		//1->brick 3->ball 4->bar/bat
		checkCollision(1,3);
		checkCollision(3,1);		
		checkCollision(3,4);
	
	}

	/** Our user-defined object, which now bounces off other object and tiles.*/
	class MyBrick extends JGObject {

		MyBrick (double xcord,double ycord) {
			super("mybrick",true, // name				
				xcord,
				ycord,
				1, // collision ID
				"brickie"// name of sprite or animation to use.
			);
			xspeed = 0;
			yspeed = 0;
		}

		/** Update the object. This method is called by moveObjects. */
		public void move() {
			if (xspeed < 0) setGraphic("brickie"); else setGraphic("brickie");
			
		}

	

		/** Handle collision with other objects. Called by checkCollision. */
		public void hit(JGObject obj) {
			//obj.dbgPrint(Double.toString(obj.colid));
			if(obj.colid == 1.0){
				obj.remove();
			}
			
		} 

	} 

	class MyBall extends JGObject {

		/** Constructor. */
		MyBall () {
			// Initialise game object by calling an appropriate constructor
			// in the JGObject class.
			super(
				"myball",// name by which the object is known
				true,//true means add a unique ID number after the object name.
				     //If we don't do this, this object will replace any object
				     //with the same name.
				random(0,pfWidth()-10),  // X position
				150,	//Y position
				3, // the object's collision ID (used to determine which classes
				   // of objects should collide with each other)
				"ballie"// name of sprite or animation to use (null is none)
			);
			// Give the object an initial speed in a random direction.
			xspeed = -1.5;
			yspeed = -1.5;
			xdir = 1;
		}

		/** Update the object. This method is called by moveObjects. */
		public void move() {			
			// bounce off the borders of the screen.
			if (x > pfWidth()-8 ) xspeed = -xspeed;
			if (x < 8 ) xspeed = -xspeed;
			if (y > pfHeight()-8 && yspeed>0) yspeed = -yspeed;
			if (y <            8 && yspeed<0) yspeed = -yspeed;
			if (y>200){
				remove();
				setGameState("Title");
				}			
		}

		public void hit(JGObject obj) {		
			
			if (checkCollision(1,-3*xspeed,-3*yspeed)==0) {
				// reverse direction
				xspeed = -xspeed;
				yspeed = -yspeed;
				obj.remove();
			}	
				
		} 
	} 
	
	class MyBar extends JGObject {

		/** Constructor. */
		MyBar () {
			// Initialise game object by calling an appropriate constructor
			// in the JGObject class.
			super(
				"mybar",// name by which the object is known
				true,//true means add a unique ID number after the object name.
				     //If we don't do this, this object will replace any object
				     //with the same name.
				15,  // X position
				200, // Y position
				4, // the object's collision ID (used to determine which classes
				   // of objects should collide with each other)
				"barie"// name of sprite or animation to use (null is none)
			);
			// Give the object an initial speed in a random direction.
			xspeed = 1;
			yspeed = 0;
		}

		/** Update the object. This method is called by moveObjects. */
		public void move() {
		
			if (x > pfWidth()-30 && xspeed>0) {
				
				x = pfWidth()-20;
			}
			if (x <8 && xspeed<0) xspeed = 0;
			
			
			if (getKey(KeyLeft)) {
				xspeed = (xspeed > 0) ?(-1):(xspeed-0.25);
				count = count + 1;				
			}
			if (getKey(KeyRight)) xspeed = (xspeed < 0) ?(1):xspeed+0.25;
			if (getKey(KeyDown)) xspeed = 0;			
		}

		
		public void hit(JGObject obj) {
			
			
			if (checkCollision(3,60*xspeed,60*yspeed)==0) {
				//ie when wen the ball hits d bar
				// reverse direction
				obj.xspeed = -obj.xspeed;
				obj.yspeed = -obj.yspeed;
				obj.xdir   = -obj.xdir;		
				
			}
						
		} 

		

	} /* end class MyObject */
	

}