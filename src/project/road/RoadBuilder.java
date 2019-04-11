package project.road;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.Select3;

public class RoadBuilder {
	//도로 만들기
	
	public static final float RUMBLE_LENGTH = 3;
	public final List<RoadSegment> roadSegments = new ArrayList<>();
	//roadSegements.add()
	
	//addRightCurve, addHill등... -> addRoad(사이즈를 넘겨줌) -> addSegment(커브사이즈와 y좌표)
	
	public enum Length {  //길이
        NONE(0),
        SHORT(50),   //25
        MEDIUM(50),   //50
        LONG(100),  //숫자가 높을수록 커브를 많이 돔(맵이 길어보임)
		VSHORT(8);

        private int size;

        Length(int size) {
            this.size = size;
        }
    }

    public enum Curve {  //오른쪽,왼쪽으로 꺾을 때

        NONE(0),
        LIGHT(2),  //커브가 굉장히 빠르게 돔(조심, 난이도 상)-30
        MEDIUM(4),
        TIGHT(6);   //커브가 굉장히 빠르게 돔(조심, 난이도 상)-30

        private int size;

        Curve(int size) {
            this.size = size;
        }
    }

    public enum Hills {

        NONE(0),
        LOW(20),
        MEDIUM(40),
        HIGH(60);  //언덕이 굉장히 높아짐(숫자가 높을 수록)

        private int size;

        Hills(int size) {
            this.size = size;
        }
    }

    public RoadBuilder addRightCurve(Length length, Curve curve, Hills hills) {  //오른쪽커브
        addRoad(length.size, length.size, length.size, curve.size, hills.size);
        return this;
    }

    public RoadBuilder addLeftCurve(Length length, Curve curve, Hills hills) {  //왼쪽커브
        addRoad(length.size, length.size, length.size, -curve.size, hills.size);
        return this;
    }

    public RoadBuilder addHill(Length length, Hills hills) {  //언덕
        addRoad(length.size, length.size, length.size, 0, hills.size);
        return this;
    }

    public RoadBuilder addDip(Length length, Hills hills) { //밑으로 내려갈때
        addRoad(length.size, length.size, length.size, 0, -hills.size);
        return this;
    }

    public RoadBuilder addStraight(Length length) {  //직전
        addRoad(length.size, 0, 0, 0, 0);
        return this;
    }
    
    public RoadBuilder addStart(Length length) {
    	addRoad(length.size,0,0,0,0,1);
		return this;
    }

    public List<RoadSegment> build() {
        return Collections.unmodifiableList(roadSegments);  //읽을 수만 있음
    }

  //add 사막
    public RoadBuilder addRightCurve2(Length length, Curve curve, Hills hills) {  //오른쪽커브
        addRoad2(length.size, length.size, length.size, curve.size, hills.size);
        return this;
    }

    public RoadBuilder addLeftCurve2(Length length, Curve curve, Hills hills) {  //왼쪽커브
        addRoad2(length.size, length.size, length.size, -curve.size, hills.size);
        return this;
    }

    public RoadBuilder addHill2(Length length, Hills hills) {  //언덕
        addRoad2(length.size, length.size, length.size, 0, hills.size);
        return this;
    }

    public RoadBuilder addDip2(Length length, Hills hills) { //밑으로 내려갈때
        addRoad2(length.size, length.size, length.size, 0, -hills.size);
        return this;
    }

    public RoadBuilder addStraight2(Length length) {  //직전
        addRoad2(length.size, 0, 0, 0, 0);
        return this;
    }

//    public List<RoadSegment> build2() {
//        return Collections.unmodifiableList(roadSegments);  //읽을 수만 있음
//    }

    //add북극
    public RoadBuilder addRightCurve3(Length length, Curve curve, Hills hills) {  //오른쪽커브
        addRoad3(length.size, length.size, length.size, curve.size, hills.size);
        return this;
    }

    public RoadBuilder addLeftCurve3(Length length, Curve curve, Hills hills) {  //왼쪽커브
        addRoad3(length.size, length.size, length.size, -curve.size, hills.size);
        return this;
    }

    public RoadBuilder addHill3(Length length, Hills hills) {  //언덕
        addRoad3(length.size, length.size, length.size, 0, hills.size);
        return this;
    }

    public RoadBuilder addDip3(Length length, Hills hills) { //밑으로 내려갈때
        addRoad3(length.size, length.size, length.size, 0, -hills.size);
        return this;
    }

    public RoadBuilder addStraight3(Length length) {  //직전
        addRoad3(length.size, 0, 0, 0, 0);
        return this;
    }

    public List<RoadSegment> build3() {
        return Collections.unmodifiableList(roadSegments);  //읽을 수만 있음
    }

    private void addRoad(int enter, int hold, int leave, int curve, float y) {
        float startY = lastY();  //p2의 world.y
        float endY = startY + (y * RoadSegment.SEGMENT_LENGTH); //LENGTH(200)
        int n;
        float total = enter + hold + leave;
        for (n = 0; n < enter; n++) {  //?누르는 순간
            addSegment(easeIn(0, curve, n / (float) enter), easeInOut(startY, endY, n / total));  //출발(느리게 시작했다가 빨라짐), 도착 느낌
        }
        for (n = 0; n < hold; n++) { //?누르고 있는 순간
            addSegment(curve, easeInOut(startY, endY, (enter + n) / total));
        }
        for (n = 0; n < leave; n++) { //?뗐을 때
            addSegment(easeInOut(curve, 0, n / (float) leave), easeInOut(startY, endY, (enter + hold + n) / total));
        }
    }
    
    private void delSegment() {
		 int i = roadSegments.size();
		 while(i<0) {
	        int index = roadSegments.size();
	        roadSegments.remove(index);
	        i--;
		 }
	   }
   
    //add 사막
    private void addRoad2(int enter, int hold, int leave, int curve, float y) {
        float startY = lastY();  //p2의 world.y
        float endY = startY + (y * RoadSegment.SEGMENT_LENGTH); //LENGTH(200)
        int n;
        float total = enter + hold + leave;
        for (n = 0; n < enter; n++) {  //?누르는 순간
            addSegment2(easeIn(0, curve, n / (float) enter), easeInOut(startY, endY, n / total));  //출발(느리게 시작했다가 빨라짐), 도착 느낌
        }
        for (n = 0; n < hold; n++) { //?누르고 있는 순간
            addSegment2(curve, easeInOut(startY, endY, (enter + n) / total));
        }
        for (n = 0; n < leave; n++) { //?뗐을 때
            addSegment2(easeInOut(curve, 0, n / (float) leave), easeInOut(startY, endY, (enter + hold + n) / total));
        }
    }
    
    //add북극
    private void addRoad3(int enter, int hold, int leave, int curve, float y) {
        float startY = lastY();  //p2의 world.y
        float endY = startY + (y * RoadSegment.SEGMENT_LENGTH); //LENGTH(200)
        int n;
        float total = enter + hold + leave;
        for (n = 0; n < enter; n++) {  //?누르는 순간
            addSegment3(easeIn(0, curve, n / (float) enter), easeInOut(startY, endY, n / total));  //출발(느리게 시작했다가 빨라짐), 도착 느낌
        }
        for (n = 0; n < hold; n++) { //?누르고 있는 순간
            addSegment3(curve, easeInOut(startY, endY, (enter + n) / total));
        }
        for (n = 0; n < leave; n++) { //?뗐을 때
            addSegment3(easeInOut(curve, 0, n / (float) leave), easeInOut(startY, endY, (enter + hold + n) / total));
        }
    }

    private void addSegment(float curve, float y) {
        int index = roadSegments.size();
        Point p1 = new Point();
        p1.world.z = index * RoadSegment.SEGMENT_LENGTH;  //배열사이즈 * 200
        p1.world.y = lastY();  //마지막 배열의 p2의 world.y의 값
        Point p2 = new Point();
        p2.world.z = (index + 1) * RoadSegment.SEGMENT_LENGTH;
        p2.world.y = y;
        roadSegments.add(  //floor(값내림)
                Math.floor(index / RUMBLE_LENGTH) % 2 == 1 ?
                        RoadSegment.createDarkRoadSegment(index, p1, p2, curve) :  //도로의 어두운 부분
                        RoadSegment.createLightRoadSegment(index, p1, p2, curve)   //도로의 밝은 부분
        );
    }
    
    
    //add 사막
    private void addSegment2(float curve, float y) {
        int index = roadSegments.size();
        Point p1 = new Point();
        p1.world.z = index * RoadSegment.SEGMENT_LENGTH;
        p1.world.y = lastY();
        Point p2 = new Point();
        p2.world.z = (index + 1) * RoadSegment.SEGMENT_LENGTH;
        p2.world.y = y;
        roadSegments.add(
                Math.floor(index / RUMBLE_LENGTH) % 2 == 1 ?
                        RoadSegment.createDarkRoadSegment2(index, p1, p2, curve) :
                        RoadSegment.createLightRoadSegment2(index, p1, p2, curve)
        );
    }
    
  //add 북극
    private void addSegment3(float curve, float y) {
        int index = roadSegments.size();
        Point p1 = new Point();
        p1.world.z = index * RoadSegment.SEGMENT_LENGTH;
        p1.world.y = lastY();
        Point p2 = new Point();
        p2.world.z = (index + 1) * RoadSegment.SEGMENT_LENGTH;
        p2.world.y = y;
        roadSegments.add(
                Math.floor(index / RUMBLE_LENGTH) % 2 == 1 ?
                        RoadSegment.createDarkRoadSegment3(index, p1, p2, curve) :
                        RoadSegment.createLightRoadSegment3(index, p1, p2, curve)
        );
    }
    
    //add-결승선
    private void addRoad(int enter, int hold, int leave, int curve, float y,int a) {
    	float startY = lastY();  //p2의 world.y
        float endY = startY + (y * RoadSegment.SEGMENT_LENGTH); //LENGTH(200)
        int n;
        float total = enter + hold + leave;
        for (n = 0; n < enter; n++) {  //?누르는 순간
            addSegment(easeIn(0, curve, n / (float) enter), easeInOut(startY, endY, n / total),1);  //출발(느리게 시작했다가 빨라짐), 도착 느낌
        }
        for (n = 0; n < hold; n++) { //?누르고 있는 순간
            addSegment(curve, easeInOut(startY, endY, (enter + n) / total),1);
        }
        for (n = 0; n < leave; n++) { //?뗐을 때
            addSegment(easeInOut(curve, 0, n / (float) leave), easeInOut(startY, endY, (enter + hold + n) / total),1);
        }
        
    }

  
    
    //add-결승선
    private void addSegment(float curve, float y,int a) {
        int index = roadSegments.size();
        Point p1 = new Point();
        p1.world.z = index * RoadSegment.SEGMENT_LENGTH;  //배열사이즈 * 200
        p1.world.y = lastY();  //마지막 배열의 p2의 world.y의 값
        Point p2 = new Point();
        p2.world.z = (index + 1) * RoadSegment.SEGMENT_LENGTH;
        p2.world.y = y;
      //floor(값내림)
        if(Select3.map == 1) roadSegments.add(RoadSegment.Start(index,p1,p2,curve));  //밀림
        else if(Select3.map == 4) roadSegments.add(RoadSegment.Start2(index,p1,p2,curve));  //사막
        else if(Select3.map == 2) roadSegments.add(RoadSegment.Start3(index,p1,p2,curve));  //북극
        else roadSegments.add(RoadSegment.Start(index,p1,p2,curve));
    }

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
