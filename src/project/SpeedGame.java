package project;

import org.lwjgl.openal.AL;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import project.road.Finish;

public class SpeedGame extends Game{ 
	//첫 시작-Select4에서 speedGame불러옴
	
	public static final int GAME_VIEWPORT_WIDTH = 1024, GAME_VIEWPORT_HEIGHT = 768;  //화면 화질(가로,세로)
	public static String LOG = SpeedGame.class.getSimpleName();
	public static Game game;

   //@Override
    public void create() {
      this.setScreen(new GameScreen());
    }
    public void recreate() {
    	this.dispose();     
    }

    @Override
    public void pause() { 
    	super.pause(); 
    	Gdx.app.log(SpeedGame.LOG,"게임 잠시 멈춤");
    	}
    @Override
    public void resume() {
    	super.resume();
    	Gdx.app.log(SpeedGame.LOG,"게임 다시 시작"); 
    	} 
    @Override 
    public void dispose() {
    	super.dispose();
    	Gdx.gl.glDeleteBuffer(0);
    	Gdx.app.log(SpeedGame.LOG,"게임 종료");	
    	Finish f = new Finish(); 
        Select4.app.exit();   	
    
    }
    public static void dispose1() {
    	AL.destroy();
    	//Gdx.app.exit();
    	System.exit(0);    	
    }
    public static void dispose2() {
    	AL.destroy();
        Select4.app.exit();  
    	System.exit(0);    	
    }



}
