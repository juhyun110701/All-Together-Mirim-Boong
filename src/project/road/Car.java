package project.road;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Car {
	    //다른 플레이어
	 	private static final Texture TEXTURE = new Texture("img/Car01.png");

	    private float offset;
	    private int z;
	    private final float speed;
	   
	    public float percentageToDraw=0;  //public 추가


	    public Car(float offset, int z, float speed) {
	        this.offset = offset;
	        this.z = z;
	        this.speed = speed;
	    }

	    public float getOffset() {
	        return offset;
	    }

	    public int getZ() {
	        return z;
	    }

	    public float getSpeed() {
	        return speed;
	    }

	    public int getHeight() {
	        return TEXTURE.getHeight();
	    }

	    public int getWidth() {
	        return TEXTURE.getWidth();
	    }

	    public void updateCar(int z, float offset) {
	        this.z = z;
	        this.offset = offset;
	    }

	    //Batch -> 2D 사각형 그리기
	    public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw) {  //void->float
	        batch.draw(TEXTURE, x, y + height * (1 - percentageToDraw), width, height * percentageToDraw, 0,percentageToDraw, 1,0);
	       // System.out.println("per :"+percentageToDraw);
	    }
}
