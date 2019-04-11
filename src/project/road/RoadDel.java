package project.road;

import java.util.ArrayList;
import java.util.List;

public class RoadDel {

	public static final float RUMBLE_LENGTH = 3;
	public final List<RoadSegment> roadSegments = new ArrayList<>();
	
//	 private void delRoad(int enter, int hold, int leave, int curve, float y) {
//	        float startY = lastY();  //p2의 world.y
//	        float endY = startY + (y * RoadSegment.SEGMENT_LENGTH); //LENGTH(200)
//	        int n;
//	        float total = enter + hold + leave;
//	        for (n = 0; n < enter; n++) {  //?누르는 순간
//	            delSegment(easeIn(0, curve, n / (float) enter), easeInOut(startY, endY, n / total));  //출발(느리게 시작했다가 빨라짐), 도착 느낌
//	        }
//	        for (n = 0; n < hold; n++) { //?누르고 있는 순간
//	            delSegment(curve, easeInOut(startY, endY, (enter + n) / total));
//	        }
//	        for (n = 0; n < leave; n++) { //?뗐을 때
//	            delSegment(easeInOut(curve, 0, n / (float) leave), easeInOut(startY, endY, (enter + hold + n) / total));
//	        }
//	    }
//	 
	
	 
	   private float lastY() {
	        return (roadSegments.size() == 0) ? 0 : roadSegments.get(roadSegments.size() - 1).getP2().world.y; //?마지막 배열의 p2의 world.y를 리턴함
	    }
	    //애니메이션
	    private float easeIn(float a, float b, float percent) {  //느리게 시작했다가 빠르게 끝남
	        return (float) (a + (b - a) * Math.pow(percent, 2));
	    }

	    private float easeOut(float a, float b, float percent) { //마지막에 감속하는 거 
	        return (float) (a + (b - a) * (1 - Math.pow(1 - percent, 2)));
	    }

	    private float easeInOut(float a, float b, float percent) { //차량의 가속 및 감속과 유사, 적절히 조합해서 사용하면 드라마틱
	        return (float) (a + (b - a) * ((-Math.cos(percent * Math.PI) / (double) 2) + 0.5d));
	    }

}
