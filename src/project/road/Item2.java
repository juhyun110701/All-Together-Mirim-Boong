package project.road;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Item2 {
	private static final Texture TEXTURE = new Texture("img/item2.png");

    private float offset;  //편차
    private int z;

    public Item2(float offset, int z) {
        this.offset = offset;
        this.z = z;
    }

    public float getOffset() {
        return offset;
    }

    public int getZ() {
        return z;
    }

    public int getHeight() {
        return TEXTURE.getHeight();
    }

    public int getWidth() {
        return TEXTURE.getWidth();
    }
    
    public void updateItem2(int z, float offset) {
        this.z = z;
        this.offset = offset;
    }

    public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw) {
        batch.draw(TEXTURE, x, y + height * (1 - percentageToDraw), width, height * percentageToDraw, 0, percentageToDraw, 1, 0);  //u,v,u2,v2
        //u,v -위 오른쪽  u2,v2 -아래 왼쪽  uv-
    }
	
}
