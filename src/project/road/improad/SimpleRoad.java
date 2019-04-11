package project.road.improad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import project.GameScreen;
import project.Player;
import project.Select3;
import project.SpeedGame;
import project.road.Car;
import project.road.Finish;
import project.road.Item;
import project.road.Item2;
import project.road.Item3;
import project.road.Point;
import project.road.Road;
import project.road.RoadBuilder;
import project.road.RoadSegment;
import project.road.Scenery;

public class SimpleRoad implements Road{
	    public static final float WIDTH = SpeedGame.GAME_VIEWPORT_WIDTH;  //화면 가로
	    public static final float HEIGHT = SpeedGame.GAME_VIEWPORT_HEIGHT;  //화면 세로

	    private static final float ROAD_WIDTH = 2000;  //도로 가로
	    private static final float CAMERA_HEIGHT = 1000;  //카메라 높이
	    private static final float FIELD_OF_VIEW = 100;   //FOV
	    private static final int DRAW_DISTANCE = 500;   //그리는 거리
	    private static final float CENTRIFUGAL = 0.2F;  //중심으로부터 멀어지는
	    private static final float SPRITE_SCALE = 0.3F * (1 / 320F); //320 is the players car width   0.0009375

	    private static final Texture BACKGROUND_SKY = new Texture("img/ground.png");
	    private static final Texture BACKGROUND_SNOW = new Texture("img/trees.png");
	    private static final Texture BACKGROUND_HILLS = new Texture("img/hill5.png");

	    private static final float ROAD_SCALE_FACTOR = WIDTH / 2 * (SPRITE_SCALE * ROAD_WIDTH);  //960
	    private static final float CAMERA_DEPTH = (float) (1 / Math.tan((FIELD_OF_VIEW / 2) * Math.PI / 180));
	    private static final float PLAYER_Z = CAMERA_HEIGHT * CAMERA_DEPTH;

	    private PolygonSpriteBatch polygonSpriteBatch = new PolygonSpriteBatch();

	    private float maxSpeed = 0;
	    private float speed = 0;

	    private final List<Car> cars = new ArrayList<>();
	    private List<RoadSegment> roadSegments;
	    private List<Item> items = new ArrayList<>();
	    private int trackLength;
	    private int position = 0;

	    private Player player = new Player();
	    
	    Point p=new Point();
	    private final StringBuilder stats = new StringBuilder();  //화면에 뜨는 좌표 정보
	    boolean finish = false;
	    public int score=0;
	    public int allscore=0;
	    
	   //Thread t1= new Thread(new Runnable())
	   
	    public void resetRoad() {
	    	player.setPlayer();
	    	//맵 add한대로 맵이 구성
	        RoadBuilder roadBuilder = new RoadBuilder()
	        		.addStart(RoadBuilder.Length.VSHORT)  //결승선
	                .addDip(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addHill(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addHill(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addDip(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addStraight(RoadBuilder.Length.MEDIUM)
	                .addRightCurve(RoadBuilder.Length.LONG, RoadBuilder.Curve.LIGHT, RoadBuilder.Hills.NONE)
	                .addLeftCurve(RoadBuilder.Length.LONG, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.NONE);

	        roadSegments = roadBuilder.build(); //roadSegments list를 읽어올 수만 있음
	        trackLength = roadSegments.size() * RoadSegment.SEGMENT_LENGTH; //배열 사이즈*200(RoadSegment.SEGMENT_LENGTH)

	        resetCars(); //다른 플레이어 구성
	        resetScenery(); //나무들 구성
	        resetItem();  //아이템생성(버섯)
	        resetItem2(); //아이템생성(등껍질)
	        resetItem3(); //아이템생성(별)
	    }
        //다른 플레이어
	    public void resetCars() {
	        Car car;  //플레이어 정보를 가져옴
	        RoadSegment segment;
	        float offset;
	        int z;
	        float speed;
	        for (int n = 0; n < 7; n++) { 
	            offset = (float) (Math.random() * MathUtils.random(-1F, 1F));  //MathUtils.random:start (포함)과 end (포함하지 않음) 사이의 난수를 반환
	            z = (int) (Math.floor(roadSegments.size()) * RoadSegment.SEGMENT_LENGTH); 
	            speed = 1000F + (float) (MathUtils.random(1000, 5000)); //다른 플레이어 속도를 다 다르게 하기 위해서
	            car = new Car(offset, z, speed);
	            segment = findSegment(car.getZ());  //RoadSegment 배열 중 랜덤으로 한 값 불러옴
	            segment.addCar(car); //RoadSegment에 있는 car list에 추가함
	            cars.add(car); //SimpleRoad에 있는 car list에 추가함
	            
	        }
	    }
	    //나무들 생성
	    public void resetScenery() { 
	        Scenery scenery;
	        float offset;
	        for (int n = 0; n < roadSegments.size(); n+=20) {  //도로 한줄마다 나무 생성
	            if (MathUtils.randomBoolean(0.5F)) {  //0에서 1 사이의 임의의 값이 지정된 값보다 작은 경우 true를 반환합니다.
	                RoadSegment roadSegment = roadSegments.get(n);
	                //offset : Boolean랜덤을 돌려서 음수->왼쪽, 양수->오른쪽  / 숫자가 커지면 도로에서 점점 멀어짐
	                offset = MathUtils.randomBoolean() ? -1F - (float) (Math.random() * 1F) : 1F + (float) (Math.random() * 1F);
	                scenery = new Scenery(offset, 0); //나무 생성
	                roadSegment.addScenery(scenery);  //나무 RoadSegment에 넣기
	            }
	        }
	    }
	    
	    //add-item(아이템 생성)
	    public void resetItem() {
	    	Item item;
	    	float offset;
	    	for(int n=80; n<roadSegments.size();n+=80) {
	    		if(MathUtils.randomBoolean(1F)) {
	    			RoadSegment roadSegment2 = roadSegments.get(n);
	    			//도로 안쪽으로 배치
	    			offset = MathUtils.randomBoolean() ? 0.5F - (float) (Math.random() * 1F) : -0.5F + (float) (Math.random() * 1F);
	    			item = new Item(offset,0);
	    			roadSegment2.addItem(item);	
	    		}
	    	}
	    }
	    
	    public void resetItem2() {
	    	Item2 item;
	    	float offset;
	    	for(int n=80; n<roadSegments.size();n+=100) {
	    		if(MathUtils.randomBoolean(1F)) {
	    			RoadSegment roadSegment2 = roadSegments.get(n);
	    			offset = MathUtils.randomBoolean() ? 0.5F - (float) (Math.random() * 1F) : -0.5F + (float) (Math.random() * 1F);
	    			item = new Item2(offset,0);
	    			roadSegment2.addItem2(item);	
	    		}
	    	}
	    }
	    public void resetItem3() {
	    	Item3 item;
	    	float offset;
	    	for(int n=80; n<roadSegments.size();n+=220) {
	    		if(MathUtils.randomBoolean(1F)) {
	    			RoadSegment roadSegment2 = roadSegments.get(n);
	    			offset = MathUtils.randomBoolean() ? 0.5F - (float) (Math.random() * 1F) : -0.5F + (float) (Math.random() * 1F);
	    			item = new Item3(offset,0);
	    			roadSegment2.addItem3(item);	
	    		}
	    	}
	    }

	    public RoadSegment findSegment(int z) {
	        return roadSegments.get(MathUtils.floor(z / RoadSegment.SEGMENT_LENGTH) % roadSegments.size()); //랜덤값 z / RoadSegment.SEGMENT_LENGTH) % roadSegments.size()
	        //z / RoadSegment.SEGMENT_LENGTH -> IndexOutOfBoundsException
	    }

	    

	   //@Override
	    public String stats() {
	        return stats.toString();
	    }
 
	    //플레이어
	    public void update(float delta) {	 //GameScreen에서 render를 통해 계속 불러짐   	
	        stats.setLength(0);
	        maxSpeed = RoadSegment.SEGMENT_LENGTH / delta;
	        float accel = maxSpeed / 5;             // 가속도 - tuned until it 'felt' right
	        float braking = -maxSpeed;               // 멈출 때 감속도 deceleration rate when braking
	        float decel = -maxSpeed / 5;             // 자연적 감속도, 가속과 제동(멈추는 거)도 안함'natural' deceleration rate when neither accelerating, nor braking
	        float offRoadDecel = -maxSpeed / 2;             // 도로를 벗어났을 때 감속은 중간에 있다. off road deceleration is somewhere in between
	        float offRoadLimit = maxSpeed / 4;          //오프로드 감속도가 더 이상 적용되지 않을 때 제한합니다 (예 : 길에서 벗어난 경우에도 항상 이 속도로 이동할 수 있음)
	        //limit when off road deceleration no longer applies (e.g. you can always go at least this speed even when off road)

	        float playerX = player.getX();
	        float playerW = player.getWidth() * SPRITE_SCALE;	        

	        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {  //방향키 위
	            speed = accelerate(speed, accel, delta);  //speed+(accel*delta)
	        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {  //방향키 아래
	            speed = accelerate(speed, braking, delta);
	        } else {
	            speed = Math.max(0,accelerate(speed, decel, delta)); //아무것도 안 눌렀을 때
	        }

	        position = Math.round(increase(position, delta * speed, trackLength));  //반올림
	        RoadSegment playerSegment = findSegment(Math.round(position + PLAYER_Z));	        
	        updateCars(delta, playerSegment, playerW);

	        if (speed > 0) {
	            float speedPercent = speed / maxSpeed;
	            float dx = delta * speedPercent * 2; // at top speed, should be able to cross from left to right (-1 to 1) in 1 second
	            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) { //왼쪽
	                playerX = playerX - dx;
	            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) { //오른쪽
	                playerX = playerX + dx;
	            }

	            playerX = playerX - (dx * speedPercent * playerSegment.getCurve() * CENTRIFUGAL);  //중심으로부터 멀어지는
	            //curve : easeIn, easeInOut함수로 나온 curve값을 가져옴

	            //도로 밖에 나갔을 때 속도가 감소하는 것
	            if (((playerX < -1) || (playerX > 1)) && (speed > offRoadLimit)) {
	                speed = accelerate(speed, offRoadDecel, delta);  //가속(speed+(offRoadDecel*delta))
	                
	                for (int n = 0; n < playerSegment.getScenery().size(); n++) {
	                    Scenery scenery = playerSegment.getScenery().get(n);
	                    float sceneryW = scenery.getWidth() * SPRITE_SCALE;
	                    float sceneryX = scenery.getOffset() + sceneryW / 2 * (scenery.getOffset() > 0 ? 1 : -1); //나무가 오른쪽,왼쪽
	                    if (overlap(playerX, playerW, sceneryX, sceneryW)) {
	                        speed = maxSpeed / 100;
	                        position = Math.round(increase(playerSegment.getP1().world.z, -PLAYER_Z, trackLength)); // 스프라이트 앞에 멈추다(세그먼트 앞)stop in front of sprite (at front of segment)
	                        break;
	                    }
	                }//for
	            }//if

	            //다른 플레이어와 충돌시
	            for (int n = 0; n < playerSegment.getCars().size(); n++) {
	                Car car = playerSegment.getCars().get(n);
	                float carW = car.getWidth() * SPRITE_SCALE;
	                if (speed > car.getSpeed()) {
	                    if (overlap(playerX, playerW, car.getOffset(), carW, 0.8F)) {
	                        speed = car.getSpeed() * (car.getSpeed() / speed);
	                        position = Math.round(increase(car.getZ(), -PLAYER_Z, trackLength));
	                        break;
	                    }
	                }
	            }
	            
	          //add-item(아이템 먹었을 때)
	            for (int n = 0; n < playerSegment.getItems().size(); n++) {	            	
	                Item item = playerSegment.getItems().get(n);
	                float itemW = item.getWidth() * SPRITE_SCALE;	  	                
	                    if (overlap(playerX, playerW, item.getOffset(), itemW, 0.8F)) {
	                        playerSegment.getItems().remove(n);
	                        score +=10;
	                        GameScreen.score.setText(score+"jeom");
	                        break;
	                    }	                
	            }
	            for (int n = 0; n < playerSegment.getItem2s().size(); n++) {	            	
	                Item2 item2 = playerSegment.getItem2s().get(n);
	                float item2W = item2.getWidth() * SPRITE_SCALE;	  	                
	                    if (overlap(playerX, playerW, item2.getOffset(), item2W, 0.8F)) {
	                    	playerSegment.getItem2s().remove(n);	  
	                    	GameScreen.i +=3;
	                    	speed =(float)3000.6133;                 
	                        break;
	                    }	                
	            }	           
	            for (int n = 0; n < playerSegment.getItem3s().size(); n++) {	            	
	                Item3 item3 = playerSegment.getItem3s().get(n);
	                float item3W = item3.getWidth() * SPRITE_SCALE;	  	                
	                    if (overlap(playerX, playerW, item3.getOffset(), item3W, 0.8F)) {
	                    	playerSegment.getItem3s().remove(n);
	                    	speed = (float) 19090.6133;
	                        break;
	                    }
	            }

	            playerX = MathUtils.clamp(playerX, -2, 2);  //playerX를 -2(최소값)과 2(최대값)로 고정시킴
	            speed = MathUtils.clamp(speed, 0, maxSpeed);
	            player.setPosition(playerX, 0);  //player위치 이동
	        } else {  //speed가 0보다 작으면 
	            speed = 0;
	        }
        
	    }//update
	    
	    //다른플레이어 위치바꾸기
	    private void updateCars(float delta, RoadSegment playerSegment, float playerW) {
	        Car car;
	        RoadSegment oldSegment;
	        RoadSegment newSegment;
	        for (int n = 0; n < cars.size(); n++) {
	            car = cars.get(n);
		      //System.out.println(car.getZ());
	            oldSegment = findSegment(car.getZ()); //car.getZ값 가져오기
	            float offset = car.getOffset() + updateCarOffset(car, oldSegment, playerSegment, playerW);
	            int z = Math.round(increase(car.getZ(), delta * car.getSpeed(), trackLength));
	            car.updateCar(z, offset);
	            newSegment = findSegment(car.getZ());
	            if (oldSegment != newSegment) {
	                oldSegment.getCars().remove(car);
	                newSegment.getCars().add(car);
	            }
	        }
	    }//for


	    private float updateCarOffset(Car car, RoadSegment carSegment, RoadSegment playerSegment, float playerW) {
	    	//car, oldSegment, playerSegment, playerW
	        float dir;
	        RoadSegment segment;
	        Car otherCar;
	        float otherCarW;
	        int lookahead = 20;
	        float carW = car.getWidth() * SPRITE_SCALE;
	        float playerX = player.getX();

	        // optimization, dont bother steering around other cars when 'out of sight' of the player
	        //최적화, 다른 자동차 주변에서 조종하는 것을 괴롭히지 않는다.
	        if ((carSegment.getIndex() - playerSegment.getIndex()) > DRAW_DISTANCE)  //그리는 거리(200)보다 크면
	            return 0;

	        for (int i = 1; i < lookahead; i++) {
	            segment = roadSegments.get((carSegment.getIndex() + i) % roadSegments.size()); 
	            if ((segment == playerSegment) && (car.getSpeed() > speed) && (overlap(playerX, playerW, car.getOffset(), carW, 1.2F))) {
	                if (playerX > 0.5)
	                    dir = -1;
	                else if (playerX < -0.5)
	                    dir = 1;
	                else
	                    dir = (car.getOffset() > playerX) ? 1 : -1;
	                return dir * 1 / i * (car.getSpeed() - speed) / maxSpeed; // the closer the cars (smaller i) and the greated the speed ratio, the larger the offset
	                //자동차가 작을수록 (i가 작을수록) 속도 비율이 높을수록 오프셋이 커집니다
	            }

	            for (int j = 0; j < segment.getCars().size(); j++) {
	                otherCar = segment.getCars().get(j);
	                otherCarW = otherCar.getWidth() * SPRITE_SCALE;
	                if ((car.getSpeed() > otherCar.getSpeed()) && overlap(car.getOffset(), carW, otherCar.getOffset(), otherCarW, 1.2F)) {
	                    if (otherCar.getOffset() > 0.5)
	                        dir = -1;
	                    else if (otherCar.getOffset() < -0.5)
	                        dir = 1;
	                    else
	                        dir = (car.getOffset() > otherCar.getOffset()) ? 1 : -1;
	                    return dir * 1 / i * (car.getSpeed() - otherCar.getSpeed()) / maxSpeed;
	                }
	            }
	            
	        }

	        // if no cars ahead, but I have somehow ended up off road, then steer back on
	        //어떤 차가 아니라도 전에 나는 어떻게 든 길에서 올라가고있게 끝났다, 그 다음 되돌아 간다.
	        if (car.getOffset() < -0.9)
	            return 0.1F;
	        else if (car.getOffset() > 0.9)
	            return -0.1F;
	        else
	            return 0;
	    }

	    //render->세우다
	    public void render(Camera camera) { 
	        polygonSpriteBatch.setProjectionMatrix(camera.combined);  //이 Batch가 사용할 투영 행렬을 설정합니다.(.combined->Matrix4)
	        polygonSpriteBatch.begin();  //드로잉 용 배치를 설정
	        polygonSpriteBatch.draw(BACKGROUND_HILLS, 0, 0, BACKGROUND_HILLS.getWidth(), HEIGHT);

	        RoadSegment playerSegment = findSegment(Math.round(position + PLAYER_Z));
	        RoadSegment baseSegment = findSegment(position);
	        
	        //결승선에 도달했을 때
	        int a =playerSegment.getIndex();	       
	        GameScreen.time = GameScreen.i;    	
	        	if((roadSegments.size()-1) == a) {	  	        	
		     			System.out.println(GameScreen.time+"초");
		     			allscore = score/10;	
		     			GameScreen.time -= allscore;
		     			System.out.println("총 시간  "+GameScreen.time+"초");
		     			
		     			SpeedGame g = new SpeedGame();
		     			g.recreate();
	        	
	        }     	

	        float playerPercent = percentRemaining(position + PLAYER_Z, RoadSegment.SEGMENT_LENGTH);  //(n % total) / total
	        float playerY = interpolate(playerSegment.getP1().world.y, playerSegment.getP2().world.y, playerPercent); //a + (b - a) * percent

	        float maxY = 0;
	        float x = 0;
	        float basePercent = (position % RoadSegment.SEGMENT_LENGTH) / (float) RoadSegment.SEGMENT_LENGTH;
	        float dx = -(baseSegment.getCurve() * basePercent);

	        for (int n = 0; n < Math.min(DRAW_DISTANCE, roadSegments.size()); n++) {
	            RoadSegment segment = roadSegments.get((baseSegment.getIndex() + n) % roadSegments.size());
	            segment.setClip(maxY);
	            boolean segmentLooped = segment.getIndex() < baseSegment.getIndex();
	            project(segment.getP1(), player.getX() * ROAD_WIDTH - x, playerY + CAMERA_HEIGHT, position - (segmentLooped ? trackLength : 0), CAMERA_DEPTH, WIDTH, HEIGHT, ROAD_WIDTH);
	            project(segment.getP2(), player.getX() * ROAD_WIDTH - x - dx, playerY + CAMERA_HEIGHT, position - (segmentLooped ? trackLength : 0), CAMERA_DEPTH, WIDTH, HEIGHT, ROAD_WIDTH);

	            x = x + dx;  //?dx->속도
	            dx = dx + segment.getCurve();

	            if ( segment.getP1().camera.z <= CAMERA_DEPTH // Behind us
	                            || (segment.getP2().screen.y <= segment.getP1().screen.y)  // back face cull
	                            || (segment.getP2().screen.y < maxY)) { // clip by (already rendered) segment
	                continue;
	            }
	            segment.draw(polygonSpriteBatch);
	            maxY = segment.getP1().screen.y;
	        }

	        // back to front painters algorithm
	        for (int n = (Math.min(DRAW_DISTANCE, roadSegments.size()) - 1); n > 0; n--) {

	            RoadSegment segment = roadSegments.get((baseSegment.getIndex() + n) % roadSegments.size());
	            // render roadside sprites
	            for (int i = 0; i < segment.getScenery().size(); i++) {
	                Scenery scenery = segment.getScenery().get(i);
	                float spriteScale = segment.getP1().screen.scale;
	                float spriteX = segment.getP1().screen.x + (spriteScale * scenery.getOffset() * ROAD_WIDTH * WIDTH / 2);
	                float spriteY = segment.getP1().screen.y;
	                renderScenery(scenery, spriteScale, spriteX, spriteY, segment.getClip()); //segment.getClip()->0 이면 도로 뒤로 나무들이 보임
	            }

	            for (int i = 0; i < segment.getCars().size(); i++) {
	                Car car = segment.getCars().get(i);
	                float carPercent = percentRemaining(car.getZ(), RoadSegment.SEGMENT_LENGTH);
	                float spriteScale = interpolate(segment.getP1().screen.scale, segment.getP2().screen.scale, carPercent);
	                float spriteX = interpolate(segment.getP1().screen.x, segment.getP2().screen.x, carPercent) + (spriteScale * car.getOffset() * ROAD_WIDTH * WIDTH / 2);
	                float spriteY = interpolate(segment.getP1().screen.y, segment.getP2().screen.y, carPercent);
	                renderCar(car, spriteScale, spriteX, spriteY, segment.getClip());
	            }
	            
	            //add-item
	            for(int i=0; i<segment.getItems().size();i++) {
	            	Item item = segment.getItems().get(i);
	                float spriteScale = segment.getP1().screen.scale;
	                float spriteX = segment.getP1().screen.x + (spriteScale * item.getOffset() * ROAD_WIDTH * WIDTH / 2);
	                float spriteY = segment.getP1().screen.y;
	                renderItem(item, spriteScale, spriteX, spriteY, segment.getClip()); 	         	            	
	            }
	            
	            for(int i=0; i<segment.getItem2s().size();i++) {
	            	Item2 item = segment.getItem2s().get(i);
	                float spriteScale = segment.getP1().screen.scale;
	                float spriteX = segment.getP1().screen.x + (spriteScale * item.getOffset() * ROAD_WIDTH * WIDTH / 2);
	                float spriteY = segment.getP1().screen.y;
	                renderItem2(item, spriteScale, spriteX, spriteY, segment.getClip()); 	         	            	
	            }
	            for(int i=0; i<segment.getItem3s().size();i++) {
	            	Item3 item = segment.getItem3s().get(i);
	                float spriteScale = segment.getP1().screen.scale;
	                float spriteX = segment.getP1().screen.x + (spriteScale * item.getOffset() * ROAD_WIDTH * WIDTH / 2);
	                float spriteY = segment.getP1().screen.y;
	                renderItem3(item, spriteScale, spriteX, spriteY, segment.getClip()); 	         	            	
	            }
	            

	            if (segment == playerSegment) {
	                renderPlayer(speed / maxSpeed, CAMERA_DEPTH/PLAYER_Z, WIDTH/2); // CAMERA_DEPTH / PLAYER_Z
	                //1)  2)scale위치  3)player화면 상 위치
	            }
	        }


	        polygonSpriteBatch.end();
	    }//render

		//플레이어 세우기
	    private void renderPlayer(float speedPercent, float scale, float destX) {
	        float bounce = (float) (1.5* Math.random() * speedPercent) * MathUtils.random(-1, 1); //1.5 * Math.random() * speedPercent
	        float destW = (player.getWidth() * scale) * ROAD_SCALE_FACTOR;  //대충 Road_SCALE--960
	        float destH = (player.getHeight() * scale) * ROAD_SCALE_FACTOR;
	        destX = destX + (destW * -0.5F);
	        player.draw(polygonSpriteBatch, destX, bounce, destW, destH);
	    }
	    
	    //나무 세우기
	    private void renderScenery(Scenery scenery, float scale, float destX, float destY, float clipY) {
	        float destW = (scenery.getWidth() * scale) * ROAD_SCALE_FACTOR;
	        float destH = (scenery.getHeight() * scale) * ROAD_SCALE_FACTOR;
	        destX = destX + (destW * (scenery.getOffset() < 0 ? -1 : 0));
	        float clipH = Math.max(0, destY + destH - clipY);
	        float amountToChop = clipH / destH;
	        float percentageToDraw = MathUtils.clamp(amountToChop, 0, 1);

	        scenery.draw(polygonSpriteBatch, destX, destY, destW, destH, percentageToDraw);
	    }
	    
	    //자동차 세우기
	    private void renderCar(Car car, float scale, float destX, float destY, float clipY) {
	        float destW = (car.getWidth() * scale) * ROAD_SCALE_FACTOR;
	        float destH = (car.getHeight() * scale) * ROAD_SCALE_FACTOR;
	        destX = destX + (destW * -0.5F);
	        float clipH = Math.max(0, destY + destH - clipY);
	        float amountToChop = clipH / destH;
	        float percentageToDraw = MathUtils.clamp(amountToChop, 0, 1);

	        car.draw(polygonSpriteBatch, destX, destY, destW, destH, percentageToDraw);
	    }
	    
	    //add-item(아이템 세우기)
	    private void renderItem(Item item, float scale, float destX, float destY, float clipY) {
	        float destW = (item.getWidth() * scale) * ROAD_SCALE_FACTOR;
	        float destH = (item.getHeight() * scale) * ROAD_SCALE_FACTOR;
	        destX = destX + (destW * (item.getOffset() < 0 ? -1 : 0));
	        float clipH = Math.max(0, destY + destH - clipY);
	        float amountToChop = clipH / destH;
	        float percentageToDraw = MathUtils.clamp(amountToChop, 0, 1);

	        item.draw(polygonSpriteBatch, destX, destY, destW, destH, percentageToDraw);
	    }
	    private void renderItem2(Item2 item, float scale, float destX, float destY, float clipY) {
	        float destW = (item.getWidth() * scale) * ROAD_SCALE_FACTOR;
	        float destH = (item.getHeight() * scale) * ROAD_SCALE_FACTOR;
	        destX = destX + (destW * (item.getOffset() < 0 ? -1 : 0));
	        float clipH = Math.max(0, destY + destH - clipY);
	        float amountToChop = clipH / destH;
	        float percentageToDraw = MathUtils.clamp(amountToChop, 0, 1);

	        item.draw(polygonSpriteBatch, destX, destY, destW, destH, percentageToDraw);
	    }
	    private void renderItem3(Item3 item, float scale, float destX, float destY, float clipY) {
	        float destW = (item.getWidth() * scale) * ROAD_SCALE_FACTOR;
	        float destH = (item.getHeight() * scale) * ROAD_SCALE_FACTOR;
	        destX = destX + (destW * (item.getOffset() < 0 ? -1 : 0));
	        float clipH = Math.max(0, destY + destH - clipY);
	        float amountToChop = clipH / destH;
	        float percentageToDraw = MathUtils.clamp(amountToChop, 0, 1);

	        item.draw(polygonSpriteBatch, destX, destY, destW, destH, percentageToDraw);
	    }
	    
	    //좌표 변경
	    private void project(Point p, float cameraX, float cameraY, float cameraZ, float cameraDepth, float width, float height, float roadWidth) {
	        //translate
	    	p.camera.x = p.world.x - cameraX;
	        p.camera.y = p.world.y - cameraY;
	        p.camera.z = p.world.z - cameraZ;
	        p.screen.scale = cameraDepth / p.camera.z;
	        p.screen.x = Math.round((width / 2) + (p.screen.scale * p.camera.x * width / 2));
	        p.screen.y = Math.round((height / 2) - (p.screen.scale * p.camera.y * -height / 2));
	        p.screen.z = Math.round((p.screen.scale * roadWidth * width / 2));  //w
	       this.p=p;
	    }


	    private float increase(float start, float increment, int max) {
	        float result = start + increment;
	        while (result >= max) {
	            result -= max;
	        }
	        while (result < 0) {
	            result += max;
	        }
	        return result;
	    }

	    private float percentRemaining(float n, float total) {
	        return (n % total) / total;
	    }

	    private float interpolate(float a, float b, float percent) {
	        return a + (b - a) * percent;
	    }

	    private float accelerate(float v, float accel, float dt) {
	        return v + (accel * dt);
	    }

	    private boolean overlap(float x1, float w1, float x2, float w2) {
	        return overlap(x1, w1, x2, w2, 1);
	    }

	    private boolean overlap(float x1, float w1, float x2, float w2, float percent) { 
	    	//playerX, playerW, sceneryX, sceneryW
	        float half = percent / 2;
	        float min1 = x1 - (w1 * half);
	        float max1 = x1 + (w1 * half);
	        float min2 = x2 - (w2 * half);
	        float max2 = x2 + (w2 * half);
	        //min1,max1-player  min2,max2-scenery
	        return !((max1 < min2) || (min1 > max2));  //!없으면 다른플레이어와 충돌이 안됨
	    }
}
