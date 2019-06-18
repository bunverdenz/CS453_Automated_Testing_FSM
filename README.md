CS453_Automated_Testing_FSM
This project use Jsoup library to extract HTML events and create FSM in order to test the GUI according to Distinguishing Sequence method with Java

To run the code via console, run Team6.java. To run the code via GUI, run fsmDraw.java.

Disclaimer: when inputting your hompage URL, make sure to end your path with "/".

What you will see as a result of running this program:

FSM with nodes as states (web pages or modals) and edges (buttons or other clickable events) that has been dynamically tested.
A path to each edge from the home node--if there is no path to the edge, it is labeled dud.
Optimal population of edges that result in the best coverage of all paths.
Navigating the UI:

In the first screen, input
The local folder path to store files into
The root homepage URL
Login Information: ID
Login Information: Password
Login Information: login page
While "building FSM, please wait" is displayed, do nothing
When the FSM is done building, it will take you to the explore mode
In the explore mode, you can click on any 2 nodes and it will display the edge between them (bolded), and the edge label
Press ALT to go to the optimal path coverage mode. Here the paths for optimal edge coverage is displayed. Click on the screen to toggle through the paths, and press CTRL to see the paths displayed one edge at a time via a timer.
Press ALT, to go to the target edge path mode. Here the path from the home page to the target edge is shown. Click on the screen to toggle through each path.
Testing of the tool done by running,

https://melodize.github.io/
