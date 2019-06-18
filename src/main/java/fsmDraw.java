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
	ArrayList<String> edgeStringList;
	ArrayList<String> pathTextList;
	ArrayList<String> targetTextList;
	ArrayList<Path> pathList;
	ArrayList<TargetPath> targetList;
	ArrayList<GraphNode> nl;
	
	int NODE_RADIUS = 30;
	int NODE_PADDING = 20;
	
	GraphNode fromClicked;
	GraphNode toClicked;
	GraphNode rootNode;
	
	int path_index = 0;
	int timer = 0;
	int clickCount = 0;
	String printEdge = "no link";
	int node_rad = 30;
	
	boolean info_input = false;
	boolean running_main = false;
	boolean running_ui = false;
	
	boolean explore_mode = true;
	boolean target_edge_mode = false;
	boolean path_toggle_mode = false;
	boolean timer_mode = false;
	
	boolean ran_func = false;
	
	boolean edit_path = false;
	boolean edit_root = true;
	boolean edit_id = false;
	boolean edit_pw = false;
	boolean edit_loginpage = false;
	
	String folder = "";
	String root = "";
	String id = "";
	String pw = "";
	String loginPage = "";
	
	public void setup(){
		info_input = true;
		edgeStringList = new ArrayList<String>();
		pathList = new ArrayList<Path>();
		pathTextList = new ArrayList<String>();
		targetTextList = new ArrayList<String>();
		targetList = new ArrayList<TargetPath>();
		
		running_main = false;
		running_ui = false;
		
	}
	
	public void draw(){
		background(255);
		
		if(info_input) {
			textSize(24);
			textAlign(CENTER, CENTER);
			
			fill(0);
			text("If no login info, leave blank", width/2, height/10);
			
			fill(0);
			if(edit_path) fill(255, 0, 100);
			text("path: ", width/5, 9*height/20);
			fill(0);
			textSize(18);
			text(folder, width*2/3, 9*height/20);
			textSize(24);
			
			fill(0);
			if(edit_root) fill(255, 0, 100);
			text("root (homepage): ", width/5, 11*height/20);
			fill(0);
			text(root, width*2/3, 11*height/20);
			
			fill(0);
			if(edit_id) fill(255, 0, 100);
			text("id: ", width/5, 13*height/20);
			fill(0);
			text(id, width*2/3, 13*height/20);
			
			fill(0);
			if(edit_pw) fill(255, 0, 100);
			text("pw: ", width/5, 15*height/20);
			fill(0);
			text(pw, width*2/3, 15*height/20);
			
			fill(0);
			if(edit_loginpage) fill(255, 0, 100);
			text("login page: ", width/5, 17*height/20);
			fill(0);
			text(loginPage, width*2/3, 17*height/20);
			
		}else if(running_main) {
			textSize(24);
			textAlign(CENTER, CENTER);
			fill(0);
			
			text("building FSM please wait", width/2, 19*height/20);
			
			
			
			if(ran_func == false) {
				ran_func = true;
				try {
					Team6.mainFunc(root, id, pw, loginPage, "");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(Team6.done) {
				running_main = false;
				makeGraph();
				running_ui = true;
			}
			
			
			
			
			//running_main = false;
			//makeGraph();
			//running_ui = true;
			
			
		}else {
			
			if(explore_mode) {
				g.draw();
				
				textSize(24);
				textAlign(CENTER, CENTER);
				fill(255, 0, 100);
				text(printEdge, width/2, 19*height/20);
			}else if(path_toggle_mode) {
				textSize(24);
				textAlign(CENTER, CENTER);
				fill(0);
				text(Double.toString(Team6.gaRES), width/5, height/20);
				fill(255, 0, 100);
				text("Optimal Paths", width*4/5, height/20);
				
				g.draw();
				
				if(timer_mode) {
					for(GraphNode n: g.getNodes()){
						n.unclick();
					}
					
					for(Edge e: g.getEdges()){
						e.unclick();
					}
					
					//GraphNode currentFrom = rootNode;
					ArrayList<String> ndl = pathList.get(clickCount).getNDList();
					ArrayList<String> edl = pathList.get(clickCount).getEDList();
					
					if(ndl.size() > 0) {
						if(millis() - timer > 500) {
							timer = millis();
							path_index++;
							if(path_index >= ndl.size()-2) path_index = 0;
						}
						
						for(Edge e: g.getEdges()){
							if(e.getToNode().getLabel().equals(ndl.get(path_index+1)) && e.getFromNode().getLabel().equals(ndl.get(path_index))){
								e.click();
								break;
							}
						}
					}
				}
			}else if(target_edge_mode) {
				textSize(24);
				textAlign(CENTER, CENTER);
				fill(255, 0, 100);
				text("Path to Target Edges", width*4/5, height/20);
				
				g.draw();
			}
		}
		
	}
	
	public void keyPressed() {
		if(keyCode == CONTROL && path_toggle_mode) {
			timer_mode = !timer_mode;
		}
		
		if(keyCode == ALT) {
			if(explore_mode) {
				clickCount = 0;
				
				for(GraphNode n: g.getNodes()){
					n.unclick();
				}
				
				for(Edge e: g.getEdges()){
					e.unclick();
				}
				
				timer = millis();
				
				printEdge = "no link";
				explore_mode = false;
				path_toggle_mode = true;
				target_edge_mode = false;
			}else if(path_toggle_mode) {
				clickCount = 0;
				
				for(GraphNode n: g.getNodes()){
					n.unclick();
				}
				
				for(Edge e: g.getEdges()){
					e.unclick();
				}
				
				printEdge = "no link";
				explore_mode = false;
				path_toggle_mode = false;
				target_edge_mode = true;
			}else if(target_edge_mode) {
				clickCount = 0;
				
				for(GraphNode n: g.getNodes()){
					n.unclick();
				}
				
				for(Edge e: g.getEdges()){
					e.unclick();
				}
				
				printEdge = "no link";
				explore_mode = true;
				path_toggle_mode = false;
				target_edge_mode = false;
			}
		}
		
		if(info_input) {
			
			if(key == CODED){
				if (keyCode == UP){
					if(edit_path) {
						edit_path = true;
					}else if(edit_root) {
						edit_root = false;
						edit_path = true;
					}else if (edit_id) {
						edit_id = false;
						edit_root = true;
					}else if (edit_pw) {
						edit_pw = false;
						edit_id = true;
					}else if (edit_loginpage) {
						edit_loginpage = false;
						edit_pw = true;
					}
				}
				if(keyCode == DOWN){
					if(edit_path){
						edit_path = false;
						edit_root = true;
					}else if (edit_root) {
						edit_root = false;
						edit_id = true;
					}else if (edit_id) {
						edit_id = false;
						edit_pw = true;
					}else if (edit_pw) {
						edit_pw = false;
						edit_loginpage = true;
					}else if(edit_loginpage) {
						edit_loginpage = true;
					}
				}
			
			}else {
				
				if(edit_path) {
					if(keyCode == BACKSPACE) {
						if(folder.length() > 0) {
							folder = folder.substring(0, folder.length()-1);
						}
					}else if(keyCode == ENTER) {
						background(255);
						textSize(24);
						textAlign(CENTER, CENTER);
						fill(255, 0, 100);
						text("building FSM please wait", width/2, 19*height/20);
						
						if(folder.length()>0) {
							Team6.resource_folder_path = folder;
						}
						
						info_input = false;
						running_main = true;
					}else if(keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT) {
						folder = folder + key;
					}
				}else if(edit_root) {
					if(keyCode == BACKSPACE) {
						if(root.length() > 0) {
							root = root.substring(0, root.length()-1);
						}
					}else if(keyCode == ENTER) {
						background(255);
						textSize(24);
						textAlign(CENTER, CENTER);
						fill(255, 0, 100);
						text("building FSM please wait", width/2, 19*height/20);
						
						if(folder.length()>0) {
							Team6.resource_folder_path = folder;
						}
						
						info_input = false;
						running_main = true;
					}else if(keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT) {
						root = root + key;
					}
				}else if(edit_id) {
					if(keyCode == BACKSPACE) {
						if(id.length() > 0) {
							id = id.substring(0, id.length()-1);
						}
					}else if(keyCode == ENTER) {
						background(255);
						textSize(24);
						textAlign(CENTER, CENTER);
						fill(255, 0, 100);
						text("building FSM please wait", width/2, 19*height/20);
						
						if(folder.length()>0) {
							Team6.resource_folder_path = folder;
						}
						
						info_input = false;
						running_main = true;
					}else if(keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT) {
						id = id + key;
					}
				}else if(edit_pw) {
					if(keyCode == BACKSPACE) {
						if(pw.length() > 0) {
							pw = pw.substring(0, pw.length()-1);
						}
					}else if(keyCode == ENTER) {
						background(255);
						textSize(24);
						textAlign(CENTER, CENTER);
						fill(255, 0, 100);
						text("building FSM please wait", width/2, 19*height/20);
						
						if(folder.length()>0) {
							Team6.resource_folder_path = folder;
						}
						
						info_input = false;
						running_main = true;
					}else if(keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT) {
						pw = pw + key;
					}
				}else if(edit_loginpage) {
					if(keyCode == BACKSPACE) {
						if(loginPage.length() > 0) {
							loginPage = loginPage.substring(0, loginPage.length()-1);
						}
					}else if(keyCode == ENTER) {
						background(255);
						textSize(24);
						textAlign(CENTER, CENTER);
						fill(255, 0, 100);
						text("building FSM please wait", width/2, 19*height/20);
						
						if(folder.length()>0) {
							Team6.resource_folder_path = folder;
						}
						
						info_input = false;
						running_main = true;
					}else if(keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT) {
						loginPage = loginPage + key;
					}
				}
			}
			
			
			
		}
	}
	
	public void mousePressed(){
		if(explore_mode) {
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
							if(fromEdgeList.isdud()) printEdge = "dud " + printEdge;
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
		}else if(path_toggle_mode) {
			
			if(clickCount >= pathList.size() - 2) clickCount = 0;
			
			path_index = 0;
			timer = millis();
			
			if(!timer_mode) {
				for(GraphNode n: g.getNodes()){
					n.unclick();
				}
				
				for(Edge e: g.getEdges()){
					e.unclick();
				}
				
				ArrayList<String> ndl = pathList.get(clickCount).getNDList();
				ArrayList<String> edl = pathList.get(clickCount).getEDList();
				for(int i = 0; i < ndl.size()-1; i++) {
					
					for(Edge e: g.getEdges()){
						if(e.getToNode().getLabel().equals(ndl.get(i+1)) && e.getFromNode().getLabel().equals(ndl.get(i))){
							e.click();
							break;
						}
					}
					
				}
			}
			
			
			
			clickCount++;
		}else if(target_edge_mode) {
			if(clickCount > targetList.size() - 2) clickCount = 0;
			
			for(GraphNode n: g.getNodes()){
				n.unclick();
			}
			
			for(Edge e: g.getEdges()){
				e.unclick();
			}
			
			while(targetList.get(clickCount).getEDT().contentEquals(" dud") && clickCount <= targetList.size()) clickCount++;
			
			
			GraphNode currentFrom = rootNode;
			
			for(String s: targetList.get(clickCount).getNewPath()) {
				if(getEdgeFromOutGoingEdges(g, currentFrom, s) != null) {
					getEdgeFromOutGoingEdges(g, currentFrom, s).click();
				}
				if(getOutGoingNode(currentFrom, s) != null) {
					currentFrom = getOutGoingNode(currentFrom, s);
				}
			}
			
			getEdgeFromList(g, nl.get(findGraphNode(targetList.get(clickCount).getTargetFrom(), nl)), nl.get(findGraphNode(targetList.get(clickCount).getTargetTo(), nl))).click();
			
			for(Edge e: g.getEdges()){
				if(e.getFromNode().equals(rootNode) && e.getLabel().equals(targetList.get(clickCount).getED())){
					e.click();
				}
			}
			
			clickCount++;
		}
		
	}
	
	public void makeGraph(){
		
		
		String line;
		BufferedReader reader = createReader(Team6.resource_folder_path + "fsmAfterText.txt");

		try {
		  while((line = reader.readLine()) != null) {
			  edgeStringList.add(line);
		  }
		} catch (IOException e) {
		  e.printStackTrace();
		}
		
		
		//
		//
		//
		nl = new ArrayList<GraphNode>();
		ArrayList<String> dupcheck = new ArrayList<String>();
		
		String from = "";
		String to = "";
		String edge = "";
		boolean dud = false;
		
		for(String s: edgeStringList) {
			if(s.length() == 0) continue;
			if(s.substring(0, 3).equals("fn:")) {
				if(!dupcheck.contains(s.substring(3))) {
					nl.add(new GraphNode(s.substring(3), NODE_RADIUS));
					dupcheck.add(s.substring(3));
				}
			}
		}
		
		rootNode = nl.get(0);
		
		for(GraphNode gn: nl) {
			g.addNode(gn);
		}
		
		for(String s: edgeStringList) {
			if(s.length() == 0) continue;
			if(s.substring(0, 3).equals("new")) continue;
			if(s.substring(0, 3).equals("///")) continue;
			if(s.substring(0, 3).equals("fn:")) {
				from = s.substring(3);
				continue;
			}else if(s.substring(0, 3).equals("ed:")){
				edge = s.substring(3);
			}else if(s.substring(0, 3).equals("edt")){
				if(s.contains("dud")) dud = true;
				else dud = false;
			}else{
				to = s.substring(3);
				if(findGraphNode(from, nl) >= 0 && findGraphNode(to, nl) >= 0){
					g.addEdge(new Edge(nl.get(findGraphNode(from, nl)), nl.get(findGraphNode(to, nl)), edge, dud));
				}
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
		
		
		
		
		
		BufferedReader pathreader = createReader(Team6.resource_folder_path + "fsmPathDrawText.txt");
		
		try {
		  while((line = pathreader.readLine()) != null) {
			  pathTextList.add(line);
		  }
		} catch (IOException e) {
		  e.printStackTrace();
		}
		
		boolean save_to_ed = false;
		ArrayList<String> ndl = new ArrayList<String>();
		ArrayList<String> edl = new ArrayList<String>();
		for(String s: pathTextList) {
			if(s.length() == 0) continue;
			if(s.length() == 6 && s.substring(0, 6).equals("fedges")) {
				save_to_ed = true;
				continue;
			}else if(s.length() == 6 && s.substring(0, 6).equals("sedges")) {
				save_to_ed = false;
				pathList.add(new Path(edl, ndl));
				ndl = new ArrayList<String>();
				edl = new ArrayList<String>();
				continue;
			}else {
				if(save_to_ed) edl.add(" " + s);
				else ndl.add(" " + s);
			}
		}
		
		System.out.println("HERE IS THE PATHLIST");
		
		int n = 0;
		
		for(Path p: pathList) {
			System.out.println();
			System.out.println(n);
			for(String s: p.getNDList()) {
				System.out.println("node: " + s);
			}
			for(String s: p.getEDList()) {
				System.out.println("edge: " + s);
			}
			n++;
		}
		
		
		
		
		
		
		
		
		
		BufferedReader targetreader = createReader(Team6.resource_folder_path + "fsmAfterText.txt");
		
		try {
		  while((line = targetreader.readLine()) != null) {
			  targetTextList.add(line);
		  }
		} catch (IOException e) {
		  e.printStackTrace();
		}
		
		String edgeT = "";
		ArrayList<String> newpath = new ArrayList<String>();
		for(String s: targetTextList) {
			if(s.length() == 0) continue;
			if(s.substring(0, 3).equals("fn:")) {
				from = s.substring(3);
				continue;
			}else if(s.substring(0, 3).equals("new")) {
				continue;
			}else if(s.substring(0, 3).equals("edt")) {
				edgeT = s.substring(4);
			}else if(s.substring(0, 3).equals("ed:")) {
				edge = s.substring(3);
			}else if(s.substring(0, 3).equals("tn:")){
				to = s.substring(3);
			}else if(s.substring(0, 3).equals("///")){
				targetList.add(new TargetPath(newpath, from, to, edge, edgeT));
				newpath = new ArrayList<String>();
			}else {
				newpath.add(s);
			}
		}
		
		System.out.println("HERE IS THE TARGETLIST");
		
		n = 0;
		
		for(TargetPath p: targetList) {
			System.out.println();
			System.out.println(n);
			System.out.println("from: " + p.getTargetFrom());
			System.out.println(" ed : " + p.getED());
			System.out.println(" edt: " + p.getEDT());
			System.out.println(" to : " + p.getTargetTo());
			for(String s: p.getNewPath()) {
				System.out.println(" np : " + s);
			}
			n++;
		}
	}
	

	class Path{
		ArrayList<String> edList;
		ArrayList<String> ndList;
		
		Path(ArrayList<String> edList, ArrayList<String> ndList){
			this.edList = edList;
			this.ndList = ndList;
		}
		
		ArrayList<String> getEDList(){
			return edList;
		}
		
		ArrayList<String> getNDList(){
			return ndList;
		}
		
	}
	
	class TargetPath{
		ArrayList<String> newPath;
		String targetFrom;
		String targetTo;
		String ed;
		String edt;
		
		TargetPath(ArrayList<String> newPath, String targetFrom, String targetTo, String ed, String edt){
			this.newPath = newPath;
			this.targetFrom = targetFrom;
			this.targetTo = targetTo;
			this.ed = ed;
			this.edt = edt;
		}
		
		ArrayList<String> getNewPath(){
			return newPath;
		}
		
		String getTargetFrom() {
			return targetFrom;
		}
		
		String getTargetTo() {
			return targetTo;
		}
		
		String getED() {
			return ed;
		}
		
		String getEDT() {
			return edt;
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
		boolean dud;
		
		int[] arrowhead = {0, -4, 0, 4, 7, 0};
		
		Edge(GraphNode from, GraphNode to, String label, boolean dud){
			this.from = from;
			this.to = to;
			this.label = label;
			clicked = false;
			this.dud = dud;
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
		
		public boolean isdud(){
			return dud;
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
			if(clicked) {
				strokeWeight(5);
			}
			
			fill(0);
			
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
	
	public Edge getEdgeFromOutGoingEdges(DirectedGraph g, GraphNode from, String edgeLabel){
		for(Edge e: g.getEdges()){
			if(e.getFromNode().equals(from) && e.getLabel().equals(edgeLabel)){
				return e;
			}
		}
		
		return null;
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
	
	public GraphNode getOutGoingNode(GraphNode node, String edgeLabel) {
		
		Edge e = getEdgeFromOutGoingEdges(g, node, edgeLabel);
		
		if(e != null) return e.getToNode();
		
		return null;
		
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
	
	public void settings() {  size(1000,1000); }
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "fsmDraw" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}
