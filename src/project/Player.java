package project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Player {
	private static Texture CAR_STRAIGHT = new Texture("img/Car02.png"); //player_straight.png
//	private static final Texture MOUSE_SHORT = new Texture("img/mouse_short.png");
//	private static final Texture MOUSE_LONG = new Texture("img/mouse_long.png");
	private static final Texture MOUSE_SHORT = new Texture("img/chair_short.png");
	private static final Texture MOUSE_LONG = new Texture("img/chair_long.png");
	private static final Texture CHAIR_SHORT = new Texture("img/chair_short.png");
	private static final Texture CHAIR_LONG = new Texture("img/chair_long.png");
	private static final Texture CART_SHORT = new Texture("img/cart_short.png");
	private static final Texture CART_LONG = new Texture("img/cart_long.png");
	
	
    private float x = 0;
    private float y = 0;
    
    public void setPlayer() {
    	if(Select.ch==1&&Select2.cart==1) { //장발-마우스
    		this.CAR_STRAIGHT = MOUSE_LONG;
    	}else if(Select.ch==1&&Select2.cart==2) { //장발-의자
    		this.CAR_STRAIGHT = CHAIR_LONG;
    	}else if(Select.ch==1&&Select2.cart==3) { //장발-수레
    		this.CAR_STRAIGHT = CART_LONG;
    	}else if(Select.ch==2&&Select2.cart==1) { //단발-마우스
    		this.CAR_STRAIGHT = MOUSE_SHORT;
    	}else if(Select.ch==2&&Select2.cart==2) { //단발-의자
    		this.CAR_STRAIGHT = CHAIR_SHORT;
    	}else if(Select.ch==2&&Select2.cart==3) { //단발-수레
    		this.CAR_STRAIGHT = CART_SHORT;
    	}else {}
    		
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return CAR_STRAIGHT.getWidth();
    }

    public int getHeight() {
        return CAR_STRAIGHT.getHeight();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Batch batch, float x, float y, float width, float height) {  //5개
        draw(batch, x, y, width, height, 1);
    }

    public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw) {  //6개
    	//Batch:2D직사각형을 그리는 데 사용
    	//왼쪽 하단 모서리가 x, y에있는 사각형을 그려서 지정된 폭과 높이를 포함하도록 영역을 늘입니다.
        batch.draw(CAR_STRAIGHT, x, y, width, height);
    }
}
