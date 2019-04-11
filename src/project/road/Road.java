package project.road;

import com.badlogic.gdx.graphics.Camera;

public interface Road {
	void resetRoad();
    void update(float delta);
    void render(Camera camera);   //īƮ�� �����ִ� ī�޶�

    String stats();
}
