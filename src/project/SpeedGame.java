package project;

import org.lwjgl.openal.AL;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import project.road.Finish;

public class SpeedGame extends Game{ 
	//ù ����-Select4���� speedGame�ҷ���
	
	public static final int GAME_VIEWPORT_WIDTH = 1024, GAME_VIEWPORT_HEIGHT = 768;  //ȭ�� ȭ��(����,����)
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
    	Gdx.app.log(SpeedGame.LOG,"���� ��� ����");
    	}
    @Override
    public void resume() {
    	super.resume();
    	Gdx.app.log(SpeedGame.LOG,"���� �ٽ� ����"); 
    	} 
    @Override 
    public void dispose() {
    	super.dispose();
    	Gdx.gl.glDeleteBuffer(0);
    	Gdx.app.log(SpeedGame.LOG,"���� ����");	
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
