package project.road;

import com.badlogic.gdx.graphics.Camera;

public interface Road {
	void resetRoad();
    void update(float delta);
    void render(Camera camera);   //카트를 보여주는 카메라

    String stats();
}
