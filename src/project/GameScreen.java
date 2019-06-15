package project;

import org.lwjgl.opengl.XRandR.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import project.road.Finish;
import project.road.Road;
//import project.road.improad.FullRoad;
import project.road.improad.HardRoad;
import project.road.improad.SimpleRoad;
import project.road.improad.WorstRoad;


//�⺻
public class GameScreen extends ScreenAdapter{  //ScreenAdapter
	//�ι�°
	
	//FitViewport-�������� ������ ��� �� ��ǥ�� �����ϰ� �������� ��ǥ�� ȭ�� ��ǥ ���̿��� ��ȯ�ϴ� �� ���Ǵ� ī�޶� �����մϴ�.
	private final Stage stage = new Stage(new FitViewport(SpeedGame.GAME_VIEWPORT_WIDTH, SpeedGame.GAME_VIEWPORT_HEIGHT));
	private final Stage stage1 = new Stage(new FitViewport(SpeedGame.GAME_VIEWPORT_WIDTH, SpeedGame.GAME_VIEWPORT_HEIGHT));
    private final BitmapFont bitmapFont = new BitmapFont();
    private final static BitmapFont bitmapFont1 = new BitmapFont();
    //���� ���� �� �� �ٲ����ִ� �ؽ�Ʈ ���̺�, actor�ȿ� ���ԵǾ� ����
    private final Label stats = new Label("", new Label.LabelStyle(bitmapFont, Color.WHITE));
    private Road road; 
      
    //add
    public Label counts = new Label("second",new Label.LabelStyle(bitmapFont,Color.WHITE));
    public Label uname = new Label("",new Label.LabelStyle(bitmapFont,Color.WHITE));
    public static Label score = new Label("",new Label.LabelStyle(bitmapFont1,Color.WHITE));
    public static int i=0,time=0,mapcount=0;
    
    @Override 
    public void show() {   //ó������ ������ -> render
        super.show();

        if(Select3.map ==1) {  //���
        	mapcount =1;
        	road = new SimpleRoad();        	
        }else if(Select3.map == 2) { //����
        	mapcount =3;
        	road = new WorstRoad();
        }else if(Select3.map ==4) {  //����
        	mapcount =2;
        	road = new HardRoad();
        }
        
        
        stats.setFontScale(2F);
       // stage.addActor(stats);  //�Է� �̺�Ʈ�� �޾Ƽ� actor�� ����(stats�� �Է��̺�Ʈ)
        //add
        counts.setFontScale(2F);
        stage.addActor(counts);
        //add-�ʼ���
        class ThreadCount extends Thread{
        	public void run(){
        		
        		while(true) {
        		 try{
        		        Thread.sleep(1000);
        		        //System.out.println(i);
        		        counts.setText(i+" time");
        		        i++;
        		      }catch(Exception e) {
        		        System.out.println(e.getMessage());
        		      }

        		}//while
        	}//run
        }//class
        ThreadCount th = new ThreadCount();		
		th.start();	
		
		uname.setFontScale(2F);
		uname.setText(Select.Username+" Nim");
		stage.addActor(uname);
		
		score.setFontScale(2F);
		//score.setText("");
		stage.addActor(score);
		
		Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("img/pause.png")));
		ImageButton pauseButton = new ImageButton(drawable);
		pauseButton.setPosition(10,SpeedGame.GAME_VIEWPORT_HEIGHT-80);
		pauseButton.setSize(70, 70);
		stage.addActor(pauseButton);
		pauseButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				Gdx.input.setInputProcessor(stage);
				System.out.println("�Ͻ����� ��ư Ŭ��!");
				//Select4.app.exit();
				DesktopLauncher.reply=1;
				DesktopLauncher gw = new DesktopLauncher();
			}
		});
		 
		Gdx.input.setInputProcessor(stage);
        road.resetRoad();
    }

    @Override
    public void render(float delta) { //�������� ��
        super.render(delta);
//        if(Finish.appCheck ==1) {
//        	Gdx . gl . glClearColor (135/255f, 206/255f, 235/255f, 1);
//            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//            road.update(delta);
//            road.render(stage.getCamera());
//            stats.setText(Gdx.graphics.getFramesPerSecond() + road.stats());
//            stats.setPosition(0, SpeedGame.GAME_VIEWPORT_HEIGHT-200);  // - stats.getTextBounds().height
//            //add
//            counts.setPosition(900,SpeedGame.GAME_VIEWPORT_HEIGHT-50);
//            uname.setPosition(200, SpeedGame.GAME_VIEWPORT_HEIGHT-50);
//            score.setPosition(500, SpeedGame.GAME_VIEWPORT_HEIGHT-50);
//
//            stage1.act(delta);  //Actor.act(float)���������� �� ���Ϳ��� �޼��带 ȣ��
//            stage1.draw();
//        }
//        else{
        	GL20 gl = Gdx.graphics.getGL20();
        	 gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        	 road.update(delta);
             road.render(stage.getCamera());
             stats.setText(Gdx.graphics.getFramesPerSecond() + road.stats());
             stats.setPosition(0, SpeedGame.GAME_VIEWPORT_HEIGHT-200);  // - stats.getTextBounds().height
             //add
             counts.setPosition(900,SpeedGame.GAME_VIEWPORT_HEIGHT-50);
             uname.setPosition(200, SpeedGame.GAME_VIEWPORT_HEIGHT-50);
             score.setPosition(500, SpeedGame.GAME_VIEWPORT_HEIGHT-50);

             stage.act(delta);  //Actor.act(float)���������� �� ���Ϳ��� �޼��带 ȣ��
             stage.draw();
        
       

        

        
        
        
     
    }
    


}


