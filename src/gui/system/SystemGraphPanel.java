package gui.system;

import gui.system.figures.ProcessorConnectionLine;
import gui.system.figures.ProcessorSquare;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim Petruk  on 2/8/14.
 */
public class SystemGraphPanel extends JPanel {

    public boolean drawTempConnectionLine = false;
    public int tempLineX1;
    public int tempLineY1;
    public int tempLineX2;
    public int tempLineY2;

    private final int processorSquareRadius = 50;

    private final Color processorSquareFillColor = new Color(255, 235, 180);
    private final Color processorSquareColor = new Color(189, 68, 241);
    private final Color processorSquareTextColor = new Color(0, 0, 0);
    public static final Color connectiveLineColor = new Color(252, 103, 24);
    public static final Color connectiveLineNameColor = new Color(129, 65, 23);
    private final Color connectiveDirectLineColor = new Color(200, 0, 23);

    private List<ProcessorSquare> processorSquares = new ArrayList();
    private List<ProcessorConnectionLine> processorConnectionLines = new ArrayList<>();

    private boolean systemConnectivity = true;

    private double power = 0.0;

    public SystemGraphPanel() {
        setLayoutParameters();
    }

    private void setLayoutParameters() {
        setLayout(new BorderLayout());
    }

    public void addProcessorToDraw(int id, double weight, int x, int y) {
        ProcessorSquare processorSquare = new ProcessorSquare(id, weight, x, y, processorSquareRadius, processorSquareColor, processorSquareFillColor);
        processorSquares.add(processorSquare);
    }

    public void removeProcessorToDraw(int id) {
        ProcessorSquare processorSquare = getProcessorSquare(id);
        processorSquares.remove(processorSquare);
    }

    public void addConnectionToDraw(int processorFromId, int processorToId, double bandwidth) {
        ProcessorSquare processorFromSquare = getProcessorSquare(processorFromId);
        ProcessorSquare processorToSquare = getProcessorSquare(processorToId);
        ProcessorConnectionLine processorConnectionLine = new ProcessorConnectionLine(processorFromSquare, processorToSquare, processorSquareRadius, bandwidth);
        processorConnectionLines.add(processorConnectionLine);
    }


    public void editProcessorToDraw(int id, double power, int x, int y) {
        ProcessorSquare processorSquare = getProcessorSquare(id);
        processorSquare.setPower(power);
        processorSquare.setX(x);
        processorSquare.setY(y);
    }

    public void paint(Graphics g) {
        //draw task ovals
        drawConnections(g);
        if (drawTempConnectionLine)
            drawTempConnectionLine(g);
        drawProcessors(g);
        drawConnectivity(g);
        drawPower(g);
    }

    private void drawProcessors(Graphics g) {
        for (ProcessorSquare processorSquare : processorSquares) {
//            System.out.println("Task: id=" + taskOval.getId() + ", weight=" + taskOval.getBandwidth() + ", x=" + taskOval.getX() + ", y=" + taskOval.getY());
            g.setColor(processorSquare.getFillColor());
            g.fillOval(processorSquare.getX(), processorSquare.getY(), processorSquareRadius, processorSquareRadius);
            g.setColor(processorSquare.getColor());
            g.drawOval(processorSquare.getX(), processorSquare.getY(), processorSquareRadius, processorSquareRadius);
            g.setColor(processorSquareTextColor);
            g.drawString("" + processorSquare.getId(), processorSquare.getX() + processorSquareRadius / 2, processorSquare.getY() + processorSquareRadius / 2);
            g.drawString(" " + processorSquare.getPower(), processorSquare.getX() + (int) (1.1 * processorSquareRadius), processorSquare.getY() + processorSquareRadius / 2);
        }
    }

    private void drawConnections(Graphics g) {
        for (ProcessorConnectionLine processorConnectionLine : processorConnectionLines) {
//            System.out.println("Task connection: taskFromId=" + taskConnectionLine.getTaskFromOval().getId() +
//                    "taskToId=" + taskConnectionLine.getTaskToOval().getId());
            g.setColor(connectiveLineColor);
            g.drawLine(processorConnectionLine.getX1(), processorConnectionLine.getY1(), processorConnectionLine.getX2(), processorConnectionLine.getY2());

            g.setColor(connectiveLineNameColor);
            g.drawString(processorConnectionLine.getName(), processorConnectionLine.getNameX(), processorConnectionLine.getNameY());
        }
    }

    private void drawTempConnectionLine(Graphics g) {
        g.drawLine(tempLineX1, tempLineY1, tempLineX2, tempLineY2);
    }

    public ProcessorSquare getProcessorSquare(int id) {
        for (ProcessorSquare processorSquare : processorSquares) {
            if (processorSquare.getId() == id) {
                power = processorSquare.getPower();
                return processorSquare;
            }
        }
        return null;
    }

    public void removeAllProcessors() {
        processorSquares = new ArrayList<>();
        processorConnectionLines = new ArrayList<>();
    }

    public void removeAllConnections() {
        processorConnectionLines = new ArrayList<>();
    }

    public List<ProcessorSquare> getProcessorSquares() {
        return processorSquares;
    }

    public List<ProcessorConnectionLine> getProcessorConnectionLines() {
        return processorConnectionLines;
    }

    public void setNewCoordinate(int id, int x, int y) {
        ProcessorSquare processorSquare = getProcessorSquare(id);
        processorSquare.setX(x);
        processorSquare.setY(y);

        for (ProcessorConnectionLine processorConnectionLine : processorConnectionLines) {
            if (processorConnectionLine.getTaskFromOval().getId() == id) {
                processorConnectionLine.setX1(x + processorSquareRadius / 2);
                processorConnectionLine.setY1(y + processorSquareRadius / 2);
            }
            if (processorConnectionLine.getTaskToOval().getId() == id) {
                processorConnectionLine.setX2(x + processorSquareRadius / 2);
                processorConnectionLine.setY2(y + processorSquareRadius / 2);
            }
        }
    }

    public int getProcessorSquareRadius() {
        return processorSquareRadius;
    }

    public void setNewPower(int id, double newWeight) {
        getProcessorSquare(id).setPower(newWeight);
    }

    public double getProcessorConnectionBandwidth(int fromID, int toId) {
        return getProcessorConnectionLine(fromID, toId).getBandwidth();
    }

    public ProcessorConnectionLine getProcessorConnectionLine(int fromId, int toId) {
        for (ProcessorConnectionLine processorConnectionLine : processorConnectionLines) {
            if (processorConnectionLine.getTaskFromOval().getId() == fromId & processorConnectionLine.getTaskToOval().getId() == toId) {
                return processorConnectionLine;
            }
        }
        return null;
    }

    public void setNewPower(int fromId, int toId, double newPower) {
        getProcessorConnectionLine(fromId, toId).setBandwidth(newPower);
    }

    public void setNewBandwidth(int fromId, int toId, double newBandwidth) {
        getProcessorConnectionLine(fromId, toId).setBandwidth(newBandwidth);
    }

    private void drawConnectivity(Graphics g) {
        int x = this.getWidth();

        if (!systemConnectivity) {
            g.drawString("!!!", x - 80, 30);
        }
    }

    private void drawPower(Graphics g) {
        int x = this.getWidth();
        g.drawString("Вага = " + power, x - 80, 15);

    }

    public void setPower(double power) {
        this.power = power;
    }

    public boolean isSystemConnectivity() {
        return systemConnectivity;
    }

    public void setSystemConnectivity(boolean systemConnectivity) {
        this.systemConnectivity = systemConnectivity;
    }
}
