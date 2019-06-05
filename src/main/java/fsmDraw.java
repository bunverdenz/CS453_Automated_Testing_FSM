import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class fsmDraw extends PApplet {
	
	DirectedGraph g = new DirectedGraph();
	ArrayList<String> graphStringList;
	
	int NODE_RADIUS = 30;
	int NODE_PADDING = 20;
	
	GraphNode fromClicked;
	GraphNode toClicked;
	
	int clickCount = 0;
	String printEdge = "no link";
	int node_rad = 30;
	
	
	public void setup(){
		graphStringList = new ArrayList<String>();
		makeGraph();
	}
	
	public void draw(){
		background(255);
		
		g.draw();
		
		textSize(24);
		textAlign(CENTER, CENTER);
		fill(255, 0, 100);
		text(printEdge, width/2, 19*height/20);
	}
	
	public void mousePressed(){
		if(clickCount == 0){
			for(int i = 0; i < g.getNodes().size(); i++){
				if(g.getNodes().get(i).isOver(mouseX, mouseY)){
					g.getNodes().get(i).firstClick();
					fromClicked = g.getNodes().get(i);
					
					ArrayList<Edge> out = getOutGoingEdges(g, g.getNodes().get(i));
					
					for(Edge e: out){
						e.click();
					}
					
					clickCount++;
				}
			}
		}else if(clickCount == 1){
			for(int i = 0; i < g.getNodes().size(); i++){
				if(g.getNodes().get(i).isOver(mouseX, mouseY)){
					
					for(Edge e: g.getEdges()){
						e.unclick();
					}
					
					g.getNodes().get(i).secondClick();
					toClicked = g.getNodes().get(i);
					
					clickCount++;
					
					Edge fromEdgeList = getEdgeFromList(g, fromClicked, toClicked);
					if(fromEdgeList != null){
						fromEdgeList.click();
						printEdge = fromEdgeList.getLabel();
					}
					
				}
			}
		}else{
			for(GraphNode n: g.getNodes()){
				n.unclick();
			}
			
			for(Edge e: g.getEdges()){
				e.unclick();
			}
			
			printEdge = "no link";
			clickCount = 0;
		}
	}
	
	public void makeGraph(){
		/*
		GraphNode n1 = new GraphNode("1", NODE_RADIUS);
		GraphNode n2 = new GraphNode("2", NODE_RADIUS);
		GraphNode n3 = new GraphNode("3", NODE_RADIUS);
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addEdge(new Edge(n1,n2, "n1 -> n2"));
		g.addEdge(new Edge(n2,n3, "n2 -> n3"));
		g.addEdge(new Edge(n1,n3, "n1 -> n3"));
		*/
		
		//
		//
		/*
		graphStringList = Team6.drawStringList; 
		for(String s: graphStringList) {
			System.out.println(s);
		}
		*/
		//
		//
		
		//
		//
		//
		graphStringList.add("fn: Melodize - Home");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: requesterBtn");
		graphStringList.add("tn: Melodize - Request");
		graphStringList.add("ed: requesterBtn");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: workerBtn");
		graphStringList.add("tn: Melodize - Song List");
		graphStringList.add("ed: learnBtn");
		graphStringList.add("tn: Melodize - About");
		graphStringList.add("ed: profileTab");
		graphStringList.add("tn: Melodize - Profile");
		graphStringList.add("ed: team6_0");
		graphStringList.add("tn: Melodize - Gallery");
		graphStringList.add("ed: team6_1");
		graphStringList.add("tn: Melodize - Home");
		
		graphStringList.add("fn: loginModal");
		graphStringList.add("ed: loginBtn");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: team6_2");
		graphStringList.add("tn: Melodize - Sign Up");
		
		graphStringList.add("fn: Melodize - Sign Up");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: profileTab");
		graphStringList.add("tn: Melodize - Profile");
		graphStringList.add("ed: team6_3");
		graphStringList.add("tn: Melodize - Gallery");
		graphStringList.add("ed: team6_4");
		graphStringList.add("tn: Melodize - Home");
		
		graphStringList.add("fn: Melodize - Home");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: requesterBtn");
		graphStringList.add("tn: Melodize - Request");
		graphStringList.add("ed: requesterBtn");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: workerBtn");
		graphStringList.add("tn: Melodize - Song List");
		graphStringList.add("ed: learnBtn");
		graphStringList.add("tn: Melodize - About");
		graphStringList.add("ed: profileTab");
		graphStringList.add("tn: Melodize - Profile");
		graphStringList.add("ed: team6_5");
		graphStringList.add("tn: Melodize - Gallery");
		graphStringList.add("ed: team6_6");
		graphStringList.add("tn: Melodize - Home");
		
		graphStringList.add("fn: Melodize - Request");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: profileTab");
		graphStringList.add("tn: Melodize - Profile");
		graphStringList.add("ed: team6_7");
		graphStringList.add("tn: Melodize - Gallery");
		graphStringList.add("ed: team6_8");
		graphStringList.add("tn: Melodize - Home");
		
		graphStringList.add("fn: Melodize - Profile");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: requesterBtn");
		graphStringList.add("tn: Melodize - Request");
		graphStringList.add("ed: workerBtn");
		graphStringList.add("tn: Melodize - Song List");
		graphStringList.add("ed: profileTab");
		graphStringList.add("tn: Melodize - Profile");
		graphStringList.add("ed: team6_9");
		graphStringList.add("tn: Melodize - Gallery");
		graphStringList.add("ed: team6_10");
		graphStringList.add("tn: Melodize - Home");
		
		graphStringList.add("fn: Melodize - Song List");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: addComment");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: joinBtn");
		graphStringList.add("tn: Melodize - Composing");
		graphStringList.add("ed: joinBtn");
		graphStringList.add("tn: passwordModal");
		graphStringList.add("ed: joinBtn");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: passwordBtn");
		graphStringList.add("tn: Melodize - Composing");
		graphStringList.add("ed: profileTab");
		graphStringList.add("tn: Melodize - Profile");
		graphStringList.add("ed: team6_11");
		graphStringList.add("tn: Melodize - Gallery");
		graphStringList.add("ed: team6_12");
		graphStringList.add("tn: Melodize - Home");
		
		graphStringList.add("fn: Melodize - Composing");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: profileTab");
		graphStringList.add("tn: Melodize - Profile");
		graphStringList.add("ed: team6_13");
		graphStringList.add("tn: Melodize - Gallery");
		graphStringList.add("ed: team6_14");
		graphStringList.add("tn: Melodize - Home");
		
		graphStringList.add("fn: Melodize - Gallery");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: profileTab");
		graphStringList.add("tn: Melodize - Profile");
		graphStringList.add("ed: team6_15");
		graphStringList.add("tn: Melodize - Gallery");
		graphStringList.add("ed: team6_16");
		graphStringList.add("tn: Melodize - Home");
		
		graphStringList.add("fn: passwordModal");
		graphStringList.add("ed: passwordBtn");
		graphStringList.add("tn: Melodize - Composing");
		graphStringList.add("ed: close");
		graphStringList.add("tn: Melodize - Song List");
		
		graphStringList.add("fn: Melodize - About");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: loginModal");
		graphStringList.add("ed: loginTab");
		graphStringList.add("tn: Melodize - Home");
		graphStringList.add("ed: profileTab");
		graphStringList.add("tn: Melodize - Profile");
		graphStringList.add("ed: team6_17");
		graphStringList.add("tn: Melodize - Gallery");
		graphStringList.add("ed: team6_18");
		graphStringList.add("tn: Melodize - Home");
		//
		//
		//
		
		
		//
		//
		//
		ArrayList<GraphNode> nl = new ArrayList<GraphNode>();
		
		String from = "";
		String to = "";
		String edge = "";
		
		for(String s: graphStringList) {
			if(s.substring(0, 3).equals("fn:")) {
				nl.add(new GraphNode(s.substring(3), NODE_RADIUS));
			}
		}
		
		for(GraphNode gn: nl) {
			g.addNode(gn);
		}
		
		for(String s: graphStringList) {
			if(s.substring(0, 3).equals("fn:")) {
				from = s.substring(3);
				continue;
			}else if(s.substring(0, 3).equals("ed:")){
				edge = s.substring(3);
			}else{
				to = s.substring(3);
			}
			
			if(findGraphNode(from, nl) >= 0 && findGraphNode(to, nl) >= 0){
				g.addEdge(new Edge(nl.get(findGraphNode(from, nl)), nl.get(findGraphNode(to, nl)), edge));
			}
		}
		//
		//
		//
		
		
		for(int i = 0; i < g.getNodes().size(); i++){
			float interval = i*2*PI/(float)g.getNodes().size();
			float vl = width/2 - (2*g.getNodes().get(0).getRadii()) - 10;
			
			int[] nc = rotateCoordinate(vl, 0, interval);
			
			g.getNodes().get(i).setPosition(width/2 + nc[0], height/2 + nc[1]);
		}
	}
	
	class DirectedGraph{
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		public void addNode(GraphNode node){
			if(!nodes.contains(node)) nodes.add(node);
		}
		
		public void addEdge(Edge edge){
			if(!edges.contains(edge)) edges.add(edge);
		}
		
		public int numberOfNodes() {
			return nodes.size();
		}
		
		public int numberOfEdges() {
			return edges.size();
		}
		
		public GraphNode getNode(int index){
			return nodes.get(index);
		}
		
		public Edge getEdge(int index){
			return edges.get(index);
		}
		
		public ArrayList<GraphNode> getNodes(){
			return nodes;
		}
		
		public ArrayList<Edge> getEdges(){
			return edges;
		}
		
		public void draw() {
			for(Edge e: edges) e.draw();
			for(GraphNode n: nodes) n.draw();
		}
	}
	
	class Edge{
		GraphNode from;
		GraphNode to;
		String label;
		boolean clicked;
		
		int[] arrowhead = {0, -4, 0, 4, 7, 0};
		
		Edge(GraphNode from, GraphNode to, String label){
			this.from = from;
			this.to = to;
			this.label = label;
			clicked = false;
		}
		
		public GraphNode getFromNode(){
			return from;
		}
		
		public GraphNode getToNode(){
			return to;
		}
		
		public String getLabel(){
			return label;
		}
		
		public boolean isClicked(){
			return clicked;
		}
		
		public void click(){
			clicked = true;
		}
		
		public void unclick(){
			clicked = false;
		}
		
		public void drawArrow(int x, int y, int ox, int oy){
			strokeWeight(1);
			if(clicked) strokeWeight(5);
			
			int dx = ox - x;
			int dy = oy - y;
			float angle = getDirection(dx, dy);
			float vl = sqrt(dx*dx + dy*dy) - NODE_RADIUS*1.5f;
			int[] end = rotateCoordinate(vl, 0, angle);
			
			line(x, y, x + end[0], y + end[1]);
			drawArrowHead(x + end[0], y + end[1], angle);
		}
		
		public void drawArrowHead(int ox, int oy, float angle){
			int[] rc1 = rotateCoordinate(arrowhead[0], arrowhead[1], angle);
			int[] rc2 = rotateCoordinate(arrowhead[2], arrowhead[3], angle);
			int[] rc3 = rotateCoordinate(arrowhead[4], arrowhead[5], angle);
			int[] narrow = {ox + rc1[0], oy + rc1[1], ox + rc2[0], oy + rc2[1], ox + rc3[0], oy + rc3[1]};
			
			stroke(0);
			fill(255);
			triangle(narrow[0], narrow[1], narrow[2], narrow[3], narrow[4], narrow[5]);
		}
		
		public void draw(){
			drawArrow(from.getPositionX(), from.getPositionY(), to.getPositionX(), to.getPositionY());
		}
	}
	
	class GraphNode{
		int x;
		int y;
		int r;
		String label;
		boolean clicked1;
		boolean clicked2;
		
		GraphNode(String label, int r){
			this.r = r;
			this.label = label;
			clicked1 = false;
			clicked2 = false;
		}
		
		public boolean isFirstClicked(){
			return clicked1;
		}
		
		public boolean isSecondClicked(){
			return clicked2;
		}
		
		public void firstClick(){
			clicked1 = true;
		}
		
		public void secondClick(){
			clicked2 = true;
		}
		
		public void unclick(){
			clicked1 = false;
			clicked2 = false;
		}
		
		public String getLabel(){
			return label;
		}
		
		public boolean equals(GraphNode other){
			if(this == other) return true;
			return label.equals(other.label);
		}
		
		public void setPosition(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public void setRadii(int r){
			this.r = r;
		}
		
		public int getPositionX(){
			return x;
		}
		
		public int getPositionY(){
			return y;
		}
		
		public int getRadii(){
			return r;
		}
		
		public boolean isOver(int mx, int my){
			if(dist(x, y, mx, my) < r) return true;
			return false;
		}
		
		public void draw(){
			stroke(0);
			strokeWeight(1);
			fill(255);
			
			ellipseMode(CENTER);
			textAlign(CENTER, CENTER);
			
			if(this.clicked1) {
				stroke(255, 0, 100);
				strokeWeight(5);
			}
			
			if(this.clicked2) fill(255, 0, 100);
			
			ellipse(x, y, r*2, r*2);
			fill(50, 50, 255);
			textSize(NODE_RADIUS/2);
			text(label, x, y);
		}
	}
	
	public ArrayList<Edge> getOutGoingEdges(DirectedGraph g, GraphNode n){
		ArrayList<Edge> result = new ArrayList<Edge>();
		
		for(Edge e: g.getEdges()){
			if(e.getFromNode().equals(n)){
				result.add(e);
			}
		}
		
		return result;
	}
	
	public ArrayList<Edge> getIncomingEdges(DirectedGraph g, GraphNode n){
		ArrayList<Edge> result = new ArrayList<Edge>();
		
		for(Edge e: g.getEdges()){
			if(e.getToNode().equals(n)){
				result.add(e);
			}
		}
		
		return result;
	}
	
	public Edge getEdgeFromList(DirectedGraph g, GraphNode from, GraphNode to){
		for(Edge e: g.getEdges()){
			if(e.getFromNode().equals(from) && e.getToNode().equals(to)){
				return e;
			}
		}
		
		return null;
	}
	
	public float getDirection(double dx, double dy) {
		double d1 = 0.0f;
		double d2 = PI/2.0f;
		double d3 = PI;
		double d4 = 3.0f*PI/2.0f;
		
		double angle = 0;
		float adx = abs((float)dx);
		float ady = abs((float)dy);
		
		if(dx == 0) { angle = (dy >= 0? d2 : d4); }
		else if(dy == 0) { angle = (dx >= 0? d1 : d3); }
		else if(dx > 0 && dy > 0) { angle = d1 + atan(ady/adx); }
		else if(dx < 0 && dy > 0) { angle = d2 + atan(adx/ady); }
		else if(dx < 0 && dy < 0) { angle = d3 + atan(ady/adx); }
		else if(dx > 0 && dy < 0) { angle = d4 + atan(adx/ady); }
		
		return (float)(angle + 2*PI)%(2*PI);
	}
	
	public int[] rotateCoordinate(float x, float y, float angle){
		int[] rc = {0, 0};
		rc[0] = (int)(x*cos(angle) - y*sin(angle));
		rc[1] = (int)(x*sin(angle) + y*cos(angle));
		
		return rc;
	}
	
	public int findGraphNode(String s, ArrayList<GraphNode> nl) {
	
		for(int i = 0; i < nl.size(); i++) {
			if(nl.get(i).getLabel().equals(s)) return i;
		}
		
		return -1;
	}
	
	public void settings() {  size(600,600); }
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "fsmDraw" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}
