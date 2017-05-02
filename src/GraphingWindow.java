/**
 * Created by s641173 on 11/4/2016.
 */


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

import net.objecthunter.exp4j.*;    //exp4j stuff
import net.objecthunter.exp4j.function.Function;

import static java.awt.GridBagConstraints.*;
import static java.lang.System.out;


public class GraphingWindow extends JFrame {
    static HashMap<String,Color> colorMap = new HashMap<>();
    static ArrayList<Function> list = new ArrayList();
    private RPNCalc obj;
    static Scanner sc = new Scanner(System.in);
    private GraphPanel graphPanel = new GraphPanel();
    private JPanel leftPanel = new JPanel();
    private JTextArea textArea = new JTextArea();
    private JScrollPane textScrollPane = new JScrollPane(textArea);
    private JButton enterButton = new JButton("ENTER");
    private JMenuBar menuBar = new JMenuBar();

    private Color CROSSHAIR_COLOR = Color.black;    //color of crosshair and tick marks
    private Color MOUSE_CROSSHAIR_COLOR = Color.lightGray;
    private ArrayList<ExpressionWithColor> functions = new ArrayList<>();
    private double[] mouseClickedPoint = new double[2]; //array that holds the point where the user clicks on the graph

    private static DecimalFormat decimalFormat1 = new DecimalFormat("###.000");

    public static void main(String[] args) {
        populateColorMap();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GraphingWindow w = new GraphingWindow();
            }
        });
    }

    public GraphingWindow(){
        super("Graphing Calculator");
        initUI();
        addTestFunctions();
        graph();
        setVisible(true);
    }


    public void graph() {

        for (ExpressionWithColor exp : functions){
            graphPanel.graph(exp);
        }
        repaint();
    }

    public void addTestFunctions(){
        Expression exp = new ExpressionBuilder("1/x")
                .functions(list)
                .variables("x")
                .build();
        functions.add(new ExpressionWithColor(exp,Color.black));
        Expression exp1 = new ExpressionBuilder("x^3-5x+2")
                .functions(list)
                .variables("x")
                .build();
        functions.add(new ExpressionWithColor(exp1,Color.blue));
        Expression exp2 = new ExpressionBuilder("x")
                .functions(list)
                .variables("x")
                .build();
        functions.add(new ExpressionWithColor(exp2,Color.red));

    }

    public void initUI() {
        EnterListener enterListener = new EnterListener("ENTER");
        enterButton.addActionListener(enterListener);
        textArea.addKeyListener(enterListener);


        graphPanel.setXmax(10);
        graphPanel.setXmin(-10);
        graphPanel.setYmax(10);
        graphPanel.setYmin(-10);
        this.setSize(new Dimension(900, 900));
        graphPanel.setNumPoints(1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
//        this.setResizable(false);

        GridBagConstraints menuConstraints = new GridBagConstraints();
        menuConstraints.gridx = 0;
        menuConstraints.gridy = 0;
        menuConstraints.weightx = 0.1;
        menuConstraints.weighty = 0.05;
        menuConstraints.gridwidth = 3;
        menuConstraints.fill = BOTH;
        this.add(menuBar, menuConstraints);

        GridBagConstraints leftPaneConstraints = new GridBagConstraints();
        leftPaneConstraints.gridx = 0;
        leftPaneConstraints.gridy = 1;
        leftPaneConstraints.weightx = 0.7;
        leftPaneConstraints.weighty = 0.9;
        leftPaneConstraints.gridheight = 2;
        leftPaneConstraints.fill = BOTH;
        leftPanel.add(new JButton());
        this.add(leftPanel, leftPaneConstraints);

        GridBagConstraints enterButtonConstraints = new GridBagConstraints();
        enterButtonConstraints.gridx = 2;
        enterButtonConstraints.gridy = 3;
        enterButtonConstraints.weightx = 0.05;
        enterButtonConstraints.weighty = 0.3;
        enterButtonConstraints.fill = BOTH;
        this.add(enterButton, enterButtonConstraints);

        GridBagConstraints textAreaConstraints = new GridBagConstraints();
        textAreaConstraints.gridx = 1;
        textAreaConstraints.gridy = 3;
        textAreaConstraints.weightx = 0.1;
        textAreaConstraints.weighty = 0.3;
        textAreaConstraints.fill = BOTH;

        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        this.add(textScrollPane, textAreaConstraints);

        GridBagConstraints graphPanelConstraints = new GridBagConstraints();
        graphPanelConstraints.gridx = 1;
        graphPanelConstraints.gridy = 1;
        graphPanelConstraints.gridwidth = 2;
        graphPanelConstraints.gridheight = 2;
        graphPanelConstraints.weightx = 0.7;
        graphPanelConstraints.weighty = 0.7;
        graphPanelConstraints.fill = BOTH;

        graphPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                mouseClickedPoint[0] = graphPanel.frameXToCartesian(e.getX());
                mouseClickedPoint[1] = graphPanel.frameYToCartesian(e.getY());
                textArea.append("\nCrosshair at " + decimalFormat1.format(mouseClickedPoint[0]) +" , " + decimalFormat1.format(mouseClickedPoint[1]));
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


        this.add(graphPanel, graphPanelConstraints);
        graphPanel.setSize((int) (this.getWidth() * 0.85), (int) (this.getHeight() * 0.85));

    }

    class GraphPanel extends JPanel {

        public int width = this.getWidth();
        public int height = this.getHeight();
        protected double xmin, xmax, ymin, ymax, xscl, yscl;
        protected int numPoints;
        protected int thisX, nextX, thisY, nextY;
        protected ArrayList<FunctionObject> functionObjects = new ArrayList<>();

        public GraphPanel() {
            setSize(getPreferredSize());
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        public void graph(ExpressionWithColor exp) {
            Expression func = exp.getExpression();
            width = getWidth();
            height = getHeight();
            ArrayList<Point> points = new ArrayList<>();
            double step = Math.abs(xmax - xmin) / numPoints;
            int thisX;
            int nextX = cartesianToFrameX(frameXToCartesian(0));
            func.setVariable("x", frameXToCartesian(0));
            int thisY;
            int nextY = cartesianToFrameY(func.evaluate());
            for (double x = frameXToCartesian(0); x < frameXToCartesian(width) - step; x += step) {
                func.setVariable("x", clamp(x, xmin, xmax));
                thisX = nextX;
                nextX = cartesianToFrameX(clamp(x, xmin, xmax));
                thisY = nextY;
                nextY = cartesianToFrameY(clamp(func.evaluate(), ymin, ymax));
                out.println("f(" + x + ") = " + func.evaluate());
                out.println("clamp(y) = " + clamp(func.evaluate(), ymin, ymax));
                this.thisX = thisX;
                this.thisY = thisY;
                this.nextX = nextX;
                this.nextY = nextY;
                out.println("Graphing line from " + this.thisX + "," + this.thisY + " to " + this.nextX + "," + this.nextY);
                Point p = new Point(thisX, thisY, nextX, nextY);
                points.add(p);
            }
            functionObjects.add(new FunctionObject(points, exp.getColor(), func));

            repaint();

        }


        public double frameXToCartesian(int x) {    //returns cartesian coordinate from frame
            return (((x * (xmax - xmin)) / width) + xmin);
        }

        public int cartesianToFrameX(double x) {        //returns frame coordinate from cartesian

            return (int) Math.round((width / (xmax - xmin)) * (x - xmin));
        }

        public double frameYToCartesian(int y) {    //returns cartesian coordinate from frame
            return (ymax - ((y * (ymax - ymin)) / height));
        }

        public int cartesianToFrameY(double y) {        //returns frame coordinate from cartesian
            return (int) Math.round((height / (ymax - ymin)) * (ymax - y));
        }

        public double getXmin() {
            return xmin;
        }

        public void setXmin(double xmin) {
            this.xmin = xmin;
        }

        public double getXmax() {
            return xmax;
        }

        public void setXmax(double xmax) {
            this.xmax = xmax;
        }

        public double getYmin() {
            return ymin;
        }

        public void setYmin(double ymin) {
            this.ymin = ymin;
        }

        public double getYmax() {
            return ymax;
        }

        public void setYmax(double ymax) {
            this.ymax = ymax;
        }

        public double getXscl() {
            return xscl;
        }

        public void setXscl(double xscl) {
            this.xscl = xscl;
        }

        public double getYscl() {
            return yscl;
        }

        public void setYscl(double yscl) {
            this.yscl = yscl;
        }

        public int getNumPoints() {
            return numPoints;
        }

        public void setNumPoints(int numPoints) {
            this.numPoints = numPoints;
        }

        public double clamp(double val, double min, double max) {
            if (val < min) {
                return min;
            }
            if (val > max) {
                return max;
            }
            return val;
        }

        public void clear() {
            functionObjects.clear();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(CROSSHAIR_COLOR);
            //set up crosshair
            g.drawLine(cartesianToFrameX(0), 0, cartesianToFrameX(0), cartesianToFrameY(ymin));
            g.drawLine(0, cartesianToFrameY(0), cartesianToFrameX(xmax), cartesianToFrameY(0));

            //draw mouse clicked crosshair, if it exists
            g.setColor(MOUSE_CROSSHAIR_COLOR);
            g.drawLine(0,cartesianToFrameY(mouseClickedPoint[1]),cartesianToFrameX(xmax),cartesianToFrameY(mouseClickedPoint[1]));
            g.drawLine(cartesianToFrameX(mouseClickedPoint[0]),0,cartesianToFrameX(mouseClickedPoint[0]),cartesianToFrameY(ymin));

            for (FunctionObject func : functionObjects) {
                ArrayList<Point> points = func.getPoints();
                g.setColor(func.getColor());
                for (int i = 0; i < points.size(); i++) {
                    g.drawLine(points.get(i).thisX, points.get(i).thisY, points.get(i).nextX, points.get(i).nextY);
                }
            }


        }

        @Override
        public Dimension getPreferredSize() {
            if (getParent() != null) {
                return new Dimension((int) (getParent().getWidth() * 0.7), (int) (getParent().getHeight() * 0.7));
            } else {
                return new Dimension(500, 500);
            }
        }

        private class Point {
            int thisX, thisY, nextX, nextY;

            public Point(int tx, int ty, int nx, int ny) {
                thisX = tx;
                thisY = ty;
                nextX = nx;
                nextY = ny;
            }
        }

        private class FunctionObject {
            private ArrayList<Point> points;
            private Color color = Color.black;
            private Expression expression;

            public FunctionObject(ArrayList<Point> p, Expression expression) {
                points = p;
                this.expression = expression;
            }

            public FunctionObject(ArrayList<Point> p, Color color, Expression expression) {
                points = p;
                this.color = color;
                this.expression = expression;
            }

            public Color getColor() {
                return color;
            }

            public void setColor(Color color) {
                this.color = color;
            }

            public Expression getExpression() {
                return expression;
            }

            public ArrayList<Point> getPoints() {
                return points;
            }
        }




    }

    private class EnterListener extends AbstractAction implements KeyListener {

        public EnterListener(String actionCommandKey) {
            putValue(ACTION_COMMAND_KEY,actionCommandKey );
        }

        public void actionPerformed(ActionEvent evt) {
            operate();
        }

        public void keyTyped(KeyEvent evt){

        }
        public void keyPressed(KeyEvent evt){
            if(evt.getKeyCode() == KeyEvent.VK_ENTER){
                operate();
            }
        }
        public void keyReleased(KeyEvent evt){

        }
        private void operate(){
            if (textArea.getText() != null && !textArea.getText().equals("")) {
                String[] text = textArea.getText().split("\n");
                String[] command = text[text.length - 1].split(" ");

                switch (command[0]) {
                    case "setWindow":
                        graphPanel.setXmax(Integer.parseInt(command[2]));
                        graphPanel.setXmin(Integer.parseInt(command[1]));
                        graphPanel.setYmax(Integer.parseInt(command[4]));
                        graphPanel.setYmin(Integer.parseInt(command[3]));
                        graphPanel.clear();
                        graph();
                        break;
                    case "repaint":
                        graphPanel.clear();
                        graph();
                        break;
                    case "clear":
                        functions.clear();
                        graphPanel.clear();
                        graph();
                        break;
                    case "graph":
                        if(command.length < 3){
                            textArea.append("\nError with input, try again.");
                            break;
                        }

                        String func = "";
                        for(int i = 2; i < command.length; i++){
                            func+= command[i];
                        }
                        try{
                            Expression exp = new ExpressionBuilder(func)
                                    .functions(list)
                                    .variables("x")
                                    .build();
                            functions.add(new ExpressionWithColor(exp,colorMap.get(command[1])));
                        }catch (Exception ex){
                            textArea.append("\nBad function input; try again.");
                        }
                        graphPanel.clear();
                        graph();
                        break;
                    case "setNumPoints":
                        try{
                            graphPanel.setNumPoints(Integer.parseInt(command[1]));
                            graphPanel.clear();
                            graph();
                        }catch (Exception ex){
                            textArea.append("\nBad function input; try again.");
                        }
                        break;
                    case "resetCrosshair":
                        try{
                            mouseClickedPoint[0] = 0;
                            mouseClickedPoint[1] = 0;
                            repaint();
                        }catch (Exception ex){
                            textArea.append("\nBad function input; try again.");
                        }
                        break;
                    case "setCrosshair":
                        if(command.length != 3){
                            textArea.append("\nError with input, try again.");
                            break;
                        }
                        try{
                            mouseClickedPoint[0] = Double.parseDouble(command[1]);
                            mouseClickedPoint[1] = Double.parseDouble(command[2]);
                            repaint();
                        }catch (Exception ex){
                            textArea.append("\nBad function input; try again.");
                        }
                        break;

                    default:
                        textArea.append("\nUnrecognized command; try again.");
                        break;

                }
                textArea.append("\n");
            }


        }
    }
    private class ExpressionWithColor {
        private Expression expression;
        private Color color;

        public ExpressionWithColor(Expression exp, Color c) {
            color = c;
            expression = exp;
        }

        public Expression getExpression() {
            return expression;
        }

        public void setExpression(Expression expression) {
            this.expression = expression;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public boolean equals(ExpressionWithColor expressionWithColor) {
            return expressionWithColor.getExpression().equals(expression);
        }
    }

    private static void populateColorMap(){
        colorMap.put("blue",Color.blue);
        colorMap.put("red",Color.red);
        colorMap.put("orange",Color.orange);
        colorMap.put("yellow",Color.yellow);
        colorMap.put("black", Color.black);
        colorMap.put("magenta",Color.magenta);
        colorMap.put("cyan",Color.cyan);
        colorMap.put("green",Color.green);
    }


}
