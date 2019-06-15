package project.road;

import java.util.ArrayList;
import java.util.List;

public class RoadDel {

	public static final float RUMBLE_LENGTH = 3;
	public final List<RoadSegment> roadSegments = new ArrayList<>();
	
//	 private void delRoad(int enter, int hold, int leave, int curve, float y) {
//	        float startY = lastY();  //p2�� world.y
//	        float endY = startY + (y * RoadSegment.SEGMENT_LENGTH); //LENGTH(200)
//	        int n;
//	        float total = enter + hold + leave;
//	        for (n = 0; n < enter; n++) {  //?������ ����
//	            delSegment(easeIn(0, curve, n / (float) enter), easeInOut(startY, endY, n / total));  //���(������ �����ߴٰ� ������), ���� ����
//	        }
//	        for (n = 0; n < hold; n++) { //?������ �ִ� ����
//	            delSegment(curve, easeInOut(startY, endY, (enter + n) / total));
//	        }
//	        for (n = 0; n < leave; n++) { //?���� ��
//	            delSegment(easeInOut(curve, 0, n / (float) leave), easeInOut(startY, endY, (enter + hold + n) / total));
//	        }
//	    }
//	 
	
	 
	   private float lastY() {
	        return (roadSegments.size() == 0) ? 0 : roadSegments.get(roadSegments.size() - 1).getP2().world.y; //?������ �迭�� p2�� world.y�� ������
	    }
	    //�ִϸ��̼�
	    private float easeIn(float a, float b, float percent) {  //������ �����ߴٰ� ������ ����
	        return (float) (a + (b - a) * Math.pow(percent, 2));
	    }

	    private float easeOut(float a, float b, float percent) { //�������� �����ϴ� �� 
	        return (float) (a + (b - a) * (1 - Math.pow(1 - percent, 2)));
	    }

	    private float easeInOut(float a, float b, float percent) { //������ ���� �� ���Ӱ� ����, ������ �����ؼ� ����ϸ� ���ƽ
	        return (float) (a + (b - a) * ((-Math.cos(percent * Math.PI) / (double) 2) + 0.5d));
	    }

}
