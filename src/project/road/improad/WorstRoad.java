package project.road.improad;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import project.GameScreen;
import project.Player;
import project.SpeedGame;
import project.road.Car;
import project.road.Item;
import project.road.Item2;
import project.road.Item3;
import project.road.Point;
import project.road.Road;
import project.road.RoadBuilder;
import project.road.RoadSegment;
import project.road.Scenery;

public class WorstRoad implements Road {
		int roadNum=1;
		
	    public static final float WIDTH = SpeedGame.GAME_VIEWPORT_WIDTH;  //화면 가로
	    public static final float HEIGHT = SpeedGame.GAME_VIEWPORT_HEIGHT;  //화면 세로

	    private static final float ROAD_WIDTH = 2000;  //도로 가로
	    private static final float CAMERA_HEIGHT = 1000;  //카메라 높이
	    private static final float FIELD_OF_VIEW = 100;   //FOV
	    private static final int DRAW_DISTANCE = 500;   //그리는 거리
	    private static final float CENTRIFUGAL = 0.2F;  //중심으로부터 멀어지는
	    private static final float SPRITE_SCALE = 0.3F * (1 / 320F); //320 is the players car width   0.0009375

	    private static final Texture BACKGROUND_SKY = new Texture("img/arctic_sky2.png");
	    private static final Texture BACKGROUND_SNOW = new Texture("img/snow.png");
	    //private static final Texture BACKGROUND_IGLOO = new Texture("igloo.png");

	    private static final float ROAD_SCALE_FACTOR = WIDTH / 2 * (SPRITE_SCALE * ROAD_WIDTH);  //960
	    private static final float CAMERA_DEPTH = (float) (1 / Math.tan((FIELD_OF_VIEW / 2) * Math.PI / 180));
	    private static final float PLAYER_Z = CAMERA_HEIGHT * CAMERA_DEPTH;

	    private PolygonSpriteBatch polygonSpriteBatch = new PolygonSpriteBatch();

	    private float maxSpeed = 0;
	    private float speed = 0;

	    private final List<Car> cars = new ArrayList<>();
	    private List<RoadSegment> roadSegments;
	    private int trackLength;
	    private int position = 0;

	    private Player player = new Player();
	    
	    Point p=new Point();
	    public int score=0;
	    public int allscore=0;

	    public void resetRoad() {
	    	player.setPlayer();
	    	//맵 add한대로 맵이 구성
	        RoadBuilder roadBuilder = new RoadBuilder()
	        		.addStart(RoadBuilder.Length.VSHORT)
	                .addDip3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addRightCurve3(RoadBuilder.Length.LONG, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.MEDIUM)
	                .addHill3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.MEDIUM)
	                .addDip3(RoadBuilder.Length.MEDIUM,RoadBuilder.Hills.HIGH)
	                .addLeftCurve3(RoadBuilder.Length.LONG, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.LOW)
	                .addHill3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addDip3(RoadBuilder.Length.LONG, RoadBuilder.Hills.HIGH)
	                .addDip3(RoadBuilder.Length.LONG, RoadBuilder.Hills.HIGH)
	                .addLeftCurve3(RoadBuilder.Length.MEDIUM, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.LOW)
	                .addDip3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addDip3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addRightCurve3(RoadBuilder.Length.SHORT, RoadBuilder.Curve.MEDIUM, RoadBuilder.Hills.NONE)
	                .addLeftCurve3(RoadBuilder.Length.SHORT, RoadBuilder.Curve.MEDIUM, RoadBuilder.Hills.HIGH)
	                .addHill3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addDip3(RoadBuilder.Length.LONG, RoadBuilder.Hills.HIGH)
	                .addHill3(RoadBuilder.Length.MEDIUM, RoadBuilder.Hills.MEDIUM)
	                .addHill3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addLeftCurve3(RoadBuilder.Length.MEDIUM, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.MEDIUM)
	                .addDip3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addRightCurve3(RoadBuilder.Length.LONG, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.LOW)
	                .addDip3(RoadBuilder.Length.LONG, RoadBuilder.Hills.HIGH)
	                .addLeftCurve3(RoadBuilder.Length.SHORT, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.NONE)
	                .addRightCurve3(RoadBuilder.Length.MEDIUM, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.HIGH)
	                .addDip3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addRightCurve3(RoadBuilder.Length.LONG, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.NONE)
	                .addStraight3(RoadBuilder.Length.SHORT)
	                .addDip3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.MEDIUM)
	                .addDip3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addLeftCurve3(RoadBuilder.Length.SHORT, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.MEDIUM)
	                .addRightCurve3(RoadBuilder.Length.LONG, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.HIGH)
	                .addLeftCurve3(RoadBuilder.Length.MEDIUM, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.LOW)
	                .addDip3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addHill3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
	                .addStraight3(RoadBuilder.Length.SHORT)
	                .addHill3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)	                
	                .addDip3(RoadBuilder.Length.LONG, RoadBuilder.Hills.HIGH)
	                .addStraight3(RoadBuilder.Length.LONG)
	                .addHill3(RoadBuilder.Length.LONG, RoadBuilder.Hills.LOW)
	                .addHill3(RoadBuilder.Length.SHORT, RoadBuilder.Hills.MEDIUM);


	        roadSegments = roadBuilder.build();
	        trackLength = roadSegments.size() * RoadSegment.SEGMENT_LENGTH;

	        resetCars();
	        resetScenery();
	        resetItem();  //아이템생성
	        resetItem2(); resetItem3();
	    }
        //다른 플레이어
	    public void resetCars() {
	        Car car;
	        RoadSegment segment;
	        float offset;
	        int z;
	        float speed;
	        for (int n = 0; n < 7; n++) {
	            offset = (float) (Math.random() * MathUtils.random(-1F, 1F));
	            z = (int) (Math.floor(roadSegments.size()) * RoadSegment.SEGMENT_LENGTH);
	            speed = 1000F + (float) (MathUtils.random(3000, 11000));
	            car = new Car(offset, z, speed);
	            segment = findSegment(car.getZ());
	            segment.addCar(car);
	            cars.add(car);
	            //stats.append(car.percentageToDraw);
	        }
	    }

	    public void resetScenery() { 
	        Scenery scenery;
	        float offset;
	        for (int n = 0; n < roadSegments.size(); n+=15) {
	            if (MathUtils.randomBoolean(0.5F)) {
	                RoadSegment roadSegment = roadSegments.get(n);
	                //offset : Boolean랜덤을 돌려서 음수->왼쪽, 양수->오른쪽  / 숫자가 커지면 도로에서 점점 멀어짐
	                offset = MathUtils.randomBoolean() ? -1F - (float) (Math.random() * 1F) : 1F + (float) (Math.random() * 1F);
	                scenery = new Scenery(offset, 0); //나무 생성
	                roadSegment.addScenery(scenery);
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
	        return roadSegments.get(MathUtils.floor(z / RoadSegment.SEGMENT_LENGTH) % roadSegments.size());
	    }

	    private final StringBuilder stats = new StringBuilder();  //화면에 뜨는 좌표 정보

	   //@Override
	    public String stats() {
	        return stats.toString();
	    }
 
	    //플레이어
	    public void update(float delta) {
	        stats.setLength(0);

	        maxSpeed = RoadSegment.SEGMENT_LENGTH / delta;

	        float accel = maxSpeed / 5;             // acceleration rate - tuned until it 'felt' right
	        float braking = -maxSpeed;               // deceleration rate when braking
	        float decel = -maxSpeed / 5;             // 'natural' deceleration rate when neither accelerating, nor braking
	        float offRoadDecel = -maxSpeed / 2;             // off road deceleration is somewhere in between
	        float offRoadLimit = maxSpeed / 4;          // limit when off road deceleration no longer applies (e.g. you can always go at least this speed even when off road)

	        float playerX = player.getX();
	        float playerW = player.getWidth() * SPRITE_SCALE;

	        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
	            speed = accelerate(speed, accel, delta);
	        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
	            speed = accelerate(speed, braking, delta);
	        } else {
	            speed = Math.max(0,accelerate(speed, decel, delta));
	        }

	        position = Math.round(increase(position, delta * speed, trackLength));
	        RoadSegment playerSegment = findSegment(Math.round(position + PLAYER_Z));

	        updateCars(delta, playerSegment, playerW);

	        if (speed > 0) {

	            float speedPercent = speed / maxSpeed;
	            float dx = delta * speedPercent * 2; // at top speed, should be able to cross from left to right (-1 to 1) in 1 second
	            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
	                playerX = playerX - dx;
	            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
	                playerX = playerX + dx;
	            }

	            playerX = playerX - (dx * speedPercent * playerSegment.getCurve() * CENTRIFUGAL);

	            if (((playerX < -1) || (playerX > 1)) && (speed > offRoadLimit)) {
	                speed = accelerate(speed, offRoadDecel, delta);
	                for (int n = 0; n < playerSegment.getScenery().size(); n++) {
	                    Scenery scenery = playerSegment.getScenery().get(n);
	                    float sceneryW = scenery.getWidth() * SPRITE_SCALE;
	                    float sceneryX = scenery.getOffset() + sceneryW / 2 * (scenery.getOffset() > 0 ? 1 : -1);
	                    if (overlap(playerX, playerW, sceneryX, sceneryW)) {
	                        speed = maxSpeed / 100;
	                        position = Math.round(increase(playerSegment.getP1().world.z, -PLAYER_Z, trackLength)); // stop in front of sprite (at front of segment)
	                        break;
	                    }
	                }
	            }

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
	                    	speed = (float) 19000.6133;	                    	
	                        break;
	                    }
	            }


	            playerX = MathUtils.clamp(playerX, -2, 2);
	            speed = MathUtils.clamp(speed, 0, maxSpeed);
	            player.setPosition(playerX, 0);
	        } else {
	            speed = 0;
	        }
	        
	    }
	    //다른플레이어 위치바꾸기
	    private void updateCars(float delta, RoadSegment playerSegment, float playerW) {
	        Car car;
	        RoadSegment oldSegment;
	        RoadSegment newSegment;
	        for (int n = 0; n < cars.size(); n++) {
	            car = cars.get(n);
	            oldSegment = findSegment(car.getZ());
	            float offset = car.getOffset() + updateCarOffset(car, oldSegment, playerSegment, playerW);
	            int z = Math.round(increase(car.getZ(), delta * car.getSpeed(), trackLength));
	            car.updateCar(z, offset);
	            newSegment = findSegment(car.getZ());
	            if (oldSegment != newSegment) {
	                oldSegment.getCars().remove(car);
	                newSegment.getCars().add(car);
	            }
	        }
	    }


	    private float updateCarOffset(Car car, RoadSegment carSegment, RoadSegment playerSegment, float playerW) {

	        float dir;
	        RoadSegment segment;
	        Car otherCar;
	        float otherCarW;
	        int lookahead = 20;
	        float carW = car.getWidth() * SPRITE_SCALE;
	        float playerX = player.getX();

	        // optimization, dont bother steering around other cars when 'out of sight' of the player
	        if ((carSegment.getIndex() - playerSegment.getIndex()) > DRAW_DISTANCE)
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
	        if (car.getOffset() < -0.9)
	            return 0.1F;
	        else if (car.getOffset() > 0.9)
	            return -0.1F;
	        else
	            return 0;
	    }

	    public void render(Camera camera) {
	        polygonSpriteBatch.setProjectionMatrix(camera.combined);
	        polygonSpriteBatch.begin();

	        polygonSpriteBatch.draw(BACKGROUND_SKY, 0, 0, BACKGROUND_SKY.getWidth(), HEIGHT);
	        polygonSpriteBatch.draw(BACKGROUND_SNOW ,0, 0, BACKGROUND_SNOW.getWidth(), 700);
	        //polygonSpriteBatch.draw(BACKGROUND_IGLOO ,0, 0, BACKGROUND_IGLOO.getWidth(), HEIGHT);

	        RoadSegment playerSegment = findSegment(Math.round(position + PLAYER_Z));
	        RoadSegment baseSegment = findSegment(position);
	        
	        //결승선에 도달했을 때
	        int a =playerSegment.getIndex();
	        GameScreen.time = GameScreen.i;
	        boolean check = false;	        	
	        	if((roadSegments.size()-1) == a) {	   
	        		if(!check) {
	        			check=true;
		     			System.out.println(GameScreen.time+"초");
		     			allscore = score/10;	
		     			GameScreen.time -= allscore;
		     			System.out.println("총 시간  "+GameScreen.time+"초");
		     			SpeedGame g = new SpeedGame();
		     			g.recreate();
		     			System.out.println("게임종료");
	        	}
	        }

	        float playerPercent = percentRemaining(position + PLAYER_Z, RoadSegment.SEGMENT_LENGTH);
	        float playerY = interpolate(playerSegment.getP1().world.y, playerSegment.getP2().world.y, playerPercent);

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

	            x = x + dx;
	            dx = dx + segment.getCurve();

	            if (
	                    segment.getP1().camera.z <= CAMERA_DEPTH // Behind us
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
//	            render roadside sprites
	            for (int i = 0; i < segment.getScenery().size(); i++) {
	                Scenery scenery = segment.getScenery().get(i);
	                float spriteScale = segment.getP1().screen.scale;
	                float spriteX = segment.getP1().screen.x + (spriteScale * scenery.getOffset() * ROAD_WIDTH * WIDTH / 2);
	                float spriteY = segment.getP1().screen.y;
	                renderScenery(scenery, spriteScale, spriteX, spriteY, segment.getClip());
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
	                renderPlayer(speed / maxSpeed, CAMERA_DEPTH / PLAYER_Z, WIDTH / 2);
	            }
	        }


	        polygonSpriteBatch.end();
	    }

	    private void renderPlayer(float speedPercent, float scale, float destX) {

	        float bounce = (float) (1.5 * Math.random() * speedPercent) * MathUtils.random(-1, 1);

	        float destW = (player.getWidth() * scale) * ROAD_SCALE_FACTOR;
	        float destH = (player.getHeight() * scale) * ROAD_SCALE_FACTOR;

	        destX = destX + (destW * -0.5F);

	        player.draw(polygonSpriteBatch, destX, bounce, destW, destH);
	    }

	    private void renderScenery(Scenery scenery, float scale, float destX, float destY, float clipY) {


	        float destW = (scenery.getWidth() * scale) * ROAD_SCALE_FACTOR;
	        float destH = (scenery.getHeight() * scale) * ROAD_SCALE_FACTOR;

	        destX = destX + (destW * (scenery.getOffset() < 0 ? -1 : 0));

	        float clipH = Math.max(0, destY + destH - clipY);
	        float amountToChop = clipH / destH;
	        float percentageToDraw = MathUtils.clamp(amountToChop, 0, 1);

	        scenery.draw(polygonSpriteBatch, destX, destY, destW, destH, percentageToDraw,1,1);
	    }

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

	    private void project(Point p, float cameraX, float cameraY, float cameraZ, float cameraDepth, float width, float height, float roadWidth) {
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
	        float half = percent / 2;
	        float min1 = x1 - (w1 * half);
	        float max1 = x1 + (w1 * half);
	        float min2 = x2 - (w2 * half);
	        float max2 = x2 + (w2 * half);
	        return !((max1 < min2) || (min1 > max2));
	    }
}
