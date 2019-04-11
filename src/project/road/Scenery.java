package project.road;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Scenery {
	private static final Texture TEXTURE = new Texture("img/igloo.png");
	private static final Texture TEXTURE2 = new Texture("img/igloo.png");
	private static final Texture TEXTURE3 = new Texture("img/igloo.png");


    private float offset;  //편차
    private int z;

    public Scenery(float offset, int z) {
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

  //add 사막
    public int getHeight2() {
        return TEXTURE2.getHeight();
    }

    public int getWidth2() {
        return TEXTURE2.getWidth();
    }
    
  //add 북극
    public int getHeight3() {
        return TEXTURE3.getHeight();
    }

    public int getWidth3() {
        return TEXTURE3.getWidth();
    }
    
    
    public void updateCar(int z, float offset) {
        this.z = z;
        this.offset = offset;
    }

//    public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw) {
//        batch.draw(TEXTURE, x, y + height * (1 - percentageToDraw), width, height * percentageToDraw, 0, percentageToDraw, 1, 0);  //u,v,u2,v2
//        //u,v -위 오른쪽  u2,v2 -아래 왼쪽  uv-
//    }

    //고쳐야함!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw) {
	//if(roadNum==1)
    	batch.draw(TEXTURE, x, y + height * (1 - percentageToDraw), width, height * percentageToDraw, 0, percentageToDraw, 1, 0);  //u,v,u2,v2
        //u,v -위 오른쪽  u2,v2 -아래 왼쪽  uv-
    }
    
    //add사막
    public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw, int a) {
        batch.draw(TEXTURE2, x, y + height * (1 - percentageToDraw), width, height * percentageToDraw, 0, percentageToDraw, 1, 0);  //u,v,u2,v2
        //u,v -위 오른쪽  u2,v2 -아래 왼쪽  uv-
    }
    
    //add북극
    public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw, int a, int b) {
        batch.draw(TEXTURE3, x, y + height * (1 - percentageToDraw), width, height * percentageToDraw, 0, percentageToDraw, 1, 0);  //u,v,u2,v2
        //u,v -위 오른쪽  u2,v2 -아래 왼쪽  uv-
    }
    //고쳐야함!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    

    @Override
    public String toString() {
        return "Scenery{" +
                "offset=" + offset +
                ", z=" + z +
                '}';
    }

}
