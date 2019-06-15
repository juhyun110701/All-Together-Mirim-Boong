package project.road;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import project.SpeedGame;

//import project.road.improad.FullRoad;


public class RoadSegment {
	private static final float LANES = 4;  //���� ���ٿ� ���� ĭ ��
    public static final int SEGMENT_LENGTH = 200;  //ũ�Ⱑ Ŭ���� ���� ��ĭ�� Ŀ��
    
    private static final TextureRegion DARK_ROAD = new TextureRegion(new Texture("img/dark_road.jpg"));
    private static final TextureRegion LIGHT_ROAD = new TextureRegion(new Texture("img/light_road.jpg"));  //���ٿ� ��ĭ�� �ִµ� �� ĭ
    private static final TextureRegion RED_RUMBLE = new TextureRegion(new Texture("img/red_rumble.jpg"));  //white_rumble.jpg ���� ���� �� �� ��
    private static final TextureRegion WHITE_RUMBLE = new TextureRegion(new Texture("img/white_rumble.jpg")); //dark_road  ���� ���� �� ��
    private static final TextureRegion WHITE_LINE = new TextureRegion(new Texture("img/white_lines.jpg"));
    private static final TextureRegion DARK_GROUND = new TextureRegion(new Texture("img/brown.jpg"));
    private static final TextureRegion LIGHT_GROUND = new TextureRegion(new Texture("img/lightbrown.jpg"));
    private static final TextureRegion START = new TextureRegion(new Texture("img/finish.png"));
    private static final TextureRegion LIGHT_GARDEN = new TextureRegion(new Texture("img/beige.jpg"));
    private static final TextureRegion DARK_GARDEN = new TextureRegion(new Texture("img/gray.jpg"));
    private static final TextureRegion LIGHT_AUDI = new TextureRegion(new Texture("img/lightred.jpg"));
    private static final TextureRegion DARK_AUDI = new TextureRegion(new Texture("img/red.jpg"));

    private final int index;
    private final Point p1;
    private final Point p2;
    private final float curve;
    private final TextureRegion grassTexture;  //Ǯ�׸���	
    private final TextureRegion roadTexture;   //���α׸���
    private final TextureRegion curbTexture;  //���� ���� �� ����
    private final boolean hasLines;  //?���׾� ǥ��

    private final List<Car> cars = new ArrayList<>();
    private final List<Scenery> sceneries = new ArrayList<>();  //������
    private final List<Item> items = new ArrayList<>(); //����
    private final List<Item2> item2s = new ArrayList<>();
    private final List<Item3> item3s = new ArrayList<>();
    private float clip = 0;
    
    
    public static RoadSegment Start(int index, Point p1, Point p2, float curve) {
        return new RoadSegment(index, p1, p2, curve, DARK_GROUND, START, RED_RUMBLE, false);
    }
    public static RoadSegment Start2(int index, Point p1, Point p2, float curve) {
        return new RoadSegment(index, p1, p2, curve, DARK_GARDEN, START, RED_RUMBLE, false);
    }
    public static RoadSegment Start3(int index, Point p1, Point p2, float curve) {
        return new RoadSegment(index, p1, p2, curve, DARK_AUDI, START, RED_RUMBLE, false);
    }
    
    
    //add ���
    public static RoadSegment createLightRoadSegment(int index, Point p1, Point p2, float curve) {  //�迭������, p1, p2, curve
        return new RoadSegment(index, p1, p2, curve, LIGHT_GROUND, LIGHT_ROAD, WHITE_RUMBLE, true);  //RoadSegment ������
    }
    public static RoadSegment createDarkRoadSegment(int index, Point p1, Point p2, float curve) {
        return new RoadSegment(index, p1, p2, curve, DARK_GROUND, DARK_ROAD, RED_RUMBLE, false);
    }


  //add ����
    public static RoadSegment createLightRoadSegment2(int index, Point p1, Point p2, float curve) {
        return new RoadSegment(index, p1, p2, curve, LIGHT_GARDEN, LIGHT_ROAD, WHITE_RUMBLE, true);
    }

    public static RoadSegment createDarkRoadSegment2(int index, Point p1, Point p2, float curve) {
        return new RoadSegment(index, p1, p2, curve, DARK_GARDEN, DARK_ROAD, RED_RUMBLE, false);
    }
    
    //add ����
    public static RoadSegment createLightRoadSegment3(int index, Point p1, Point p2, float curve) {
        return new RoadSegment(index, p1, p2, curve, LIGHT_AUDI, LIGHT_ROAD, WHITE_RUMBLE, true);
    }

    public static RoadSegment createDarkRoadSegment3(int index, Point p1, Point p2, float curve) {
        return new RoadSegment(index, p1, p2, curve, DARK_AUDI, DARK_ROAD, RED_RUMBLE, false);
    }

    private RoadSegment(int index, Point p1, Point p2, float curve,
                        TextureRegion grassTexture,
                        TextureRegion roadTexture,
                        TextureRegion curbTexture,
                        boolean hasLines) {
        this.index = index;
        this.p1 = p1;
        this.p2 = p2;
        this.curve = curve;
        this.grassTexture = grassTexture;
        this.roadTexture = roadTexture;
        this.curbTexture = curbTexture;
        this.hasLines = hasLines;
    }

    public int getIndex() {
        return index;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public float getCurve() {
        return curve;
    }

    public void addCar(Car car) {
        this.cars.add(car);
    }

    public List<Car> getCars() {  //�ٸ� �÷��̾�
        return cars;
    }

    public void addScenery(Scenery scenery) {  //���� Ŭ������ �ҷ��� Scenery List�� �߰�
        this.sceneries.add(scenery);
    }

    public List<Scenery> getScenery() {  //List�� ����� �������� ������
        return sceneries;
    }
    
    //add-Item
    public void addItem(Item item) {
    	this.items.add(item);
    }
    public List<Item> getItems(){
    	return items;
    }
    
    public void addItem2(Item2 item2) {
    	this.item2s.add(item2);
    }
    public List<Item2> getItem2s(){
    	return item2s;
    }
    public void addItem3(Item3 item3) {
    	this.item3s.add(item3);
    }
    public List<Item3> getItem3s(){
    	return item3s;
    }

    public float getClip() {  //????
        return clip;
    }

    public void setClip(float clip) {
        this.clip = clip;
    }

    public void draw(PolygonSpriteBatch polygonSpriteBatch) {  // �ؽ�ó (����)�� �����ϴ� 2D �������� �׸��� �� ���

        Point.Screen screen1 = p1.screen;
        Point.Screen screen2 = p2.screen;

        float x1 = screen1.x;
        float y1 = screen1.y;
        float z1 = screen1.z;  //w

        float x2 = screen2.x;
        float y2 = screen2.y;
        float z2 = screen2.z;  //w

        float lanew1, lanew2, lanex1, lanex2, lane;

        float r1 = rumbleWidth(z1, LANES);
        float r2 = rumbleWidth(z2, LANES);

        float l1 = laneMarkerWidth(z1, LANES);  //z��ġ, 4
        float l2 = laneMarkerWidth(z2, LANES);

        //draw(TextureRegion region, float x, float y, float width, float height)
        //���� �ϴ� �𼭸��� x, y���ִ� �簢���� �׷��� ������ ���� ���̸� �����ϵ��� ������ ���Դϴ�.
        polygonSpriteBatch.draw(grassTexture, 0, screen2.y, SpeedGame.GAME_VIEWPORT_WIDTH, screen1.y - screen2.y);

        polygon(polygonSpriteBatch, x1 - z1 - r1, y1, x1 - z1, y1, x2 - z2, y2, x2 - z2 - r2, y2, curbTexture); //���� ���� �� ��
        polygon(polygonSpriteBatch, x1 + z1 + r1, y1, x1 + z1, y1, x2 + z2, y2, x2 + z2 + r2, y2, curbTexture); //���� ���� �� ��
        polygon(polygonSpriteBatch, x1 - z1, y1, x1 + z1, y1, x2 + z2, y2, x2 - z2, y2, roadTexture);


        if (hasLines) {  //���� �����϶� ture
            lanew1 = z1 * 2 / LANES;  //lanes=4
            lanew2 = z2 * 2 / LANES;
            lanex1 = x1 - z1 + lanew1;
            lanex2 = x2 - z2 + lanew2;
            for (lane = 1; lane < LANES; lanex1 += lanew1, lanex2 += lanew2, lane++) {
                polygon(polygonSpriteBatch, lanex1 - l1 / 2, y1, lanex1 + l1 / 2, y1, lanex2 + l2 / 2, y2, lanex2 - l2 / 2, y2, WHITE_LINE);
            }
        }
    }

    private void polygon(PolygonSpriteBatch polygonSpriteBatch,
                         float x1, float y1,
                         float x2, float y2,
                         float x3, float y3,
                         float x4, float y4,
                         TextureRegion textureRegion) {

        float dx2 = x2 - x1;
        float dy2 = y2 - y1;

        float dx3 = x3 - x1;
        float dy3 = y3 - y1;

        float dx4 = x4 - x1;
        float dy4 = y4 - y1;

        PolygonRegion polygonRegion = new PolygonRegion(textureRegion, new float[]{
        		        0, 0,
                        dx2, dy2,
                        dx3, dy3,
                        0, 0,
                        dx4, dy4,
                        dx3, dy3
                }, new short[]{0, 1, 2, 3, 4, 5}  //float[] vertices, short[] triangles
        );  //�������� �ٰ��� ��ǥ�� �ﰢ�� ȭ�Ͽ� PolygonRegion�� �ۼ��ϰ��̸� ������� uvs�� ����մϴ�

        //���� �Ʒ� �𼭸��� x, y���ִ� ������ �ʺ�� ���̰��ִ� �ٰ��� ������ �׸��ϴ�.
        polygonSpriteBatch.draw(polygonRegion, x1, y1);
    }  //polygon

    private float rumbleWidth(float projectedRoadWidth, float lanes) {  //screen.z�� lanes
        return projectedRoadWidth / Math.max(32, 2 * lanes);  //ū �� ��ȯ
    }

    private float laneMarkerWidth(float projectedRoadWidth, float lanes) {
        return projectedRoadWidth / Math.max(32, 8 * lanes);//8
    }


}//RoadSegument
