package simulator.view;

import simulator.control.Controller;
import simulator.model.*;
import simulator.model.Event;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.awt.Image;


import javax.imageio.ImageIO;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

    private static final long serialVersionUID = 1L;

    private static final int _JRADIUS = 10;

    private static final Color _BG_COLOR = Color.WHITE;
    private static final Color _JUNCTION_COLOR = Color.BLUE;
    private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
    private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
    private static final Color _RED_LIGHT_COLOR = Color.RED;

    private RoadMap _map;

    private Image _car;
    private Image _cloud;
    private Image _sunny;
    private Image _wind;
    private Image _storm;
    private Image _rainy;


    public MapByRoadComponent(Controller ctrl){
        this.setPreferredSize(new Dimension(300, 200));
        initGUI();
        ctrl.addObserver(this);
    }

    private void initGUI() {
       _car = loadImage("car.png");
       _cloud = loadImage("cloud.png");
       _rainy = loadImage("rain.png");
       _storm = loadImage("storm.png");
       _sunny = loadImage("sun.png");
       _wind = loadImage("wind.png");


    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // clear with a background color
        g.setColor(_BG_COLOR);
        g.clearRect(0, 0, getWidth(), getHeight());

        if (_map == null || _map.getJunctions().size() == 0) {
            g.setColor(Color.red);
            g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
        } else {
            updatePrefferedSize();
            drawMap(g);
        }
    }

    private void drawMap(Graphics g) {

        drawRoads(g);
        drawVehicles(g);
        drawJunctions(g);
    }

    private void drawRoads(Graphics g) {
        for (Road r : _map.getCarreteras()) {

            // the road goes from (x1,y1) to (x2,y2)
            int x1 = r.getCruceOrigen().getCoordenadaX();
            int y1 = r.getCruceOrigen().getCoordenadaY();
            int x2 = r.getCruceDestino().getCoordenadaX();
            int y2 = r.getCruceDestino().getCoordenadaY();

            // choose a color for the arrow depending on the traffic light of the road
            Color arrowColor = _RED_LIGHT_COLOR;
            int idx = r.getCruceDestino().getIndiceSemaforo();
            if (idx != -1 && r.equals(r.getCruceDestino().getListaCarreterasEntrantes().get(idx))) {
                arrowColor = _GREEN_LIGHT_COLOR;
            }

            // choose a color for the road depending on the total contamination, the darker
            // the
            // more contaminated (wrt its co2 limit)
            int roadColorValue = 200 - (int) (200.0 * Math.min(1.0, (double) r.getContaminacionTotal() / (1.0 + (double) r.getAlarmaContaminacion())));
            Color roadColor = new Color(roadColorValue, roadColorValue, roadColorValue);

            // draw line from (x1,y1) to (x2,y2) with arrow of color arrowColor and line of
            // color roadColor. The size of the arrow is 15px length and 5 px width
            drawLineWithArrow(g, x1, y1, x2, y2, 15, 5, roadColor, arrowColor);
        }

    }

    private void drawLineWithArrow(Graphics g, int x1, int y1, int x2, int y2, int w, int h, Color lineColor, Color arrowColor) {

        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - w, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = { x2, (int) xm, (int) xn };
        int[] ypoints = { y2, (int) ym, (int) yn };

        g.setColor(lineColor);
        g.drawLine(x1, y1, x2, y2);
        g.setColor(arrowColor);
        g.fillPolygon(xpoints, ypoints, 3);
    }

    private void drawVehicles(Graphics g) {
        for (Vehicle v : _map.getVehiculos()) {
            if (v.getEstado() != VehicleStatus.ARRIVED) {

                // The calculation below compute the coordinate (vX,vY) of the vehicle on the
                // corresponding road. It is calculated relativly to the length of the road, and
                // the location on the vehicle.
                Road r = v.getCarretera();
                int x1 = r.getCruceOrigen().getCoordenadaX();
                int y1 = r.getCruceOrigen().getCoordenadaY();
                int x2 = r.getCruceDestino().getCoordenadaX();
                int y2 = r.getCruceDestino().getCoordenadaY();
                double roadLength = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                double alpha = Math.atan(((double) Math.abs(x1 - x2)) / ((double) Math.abs(y1 - y2)));
                double relLoc = roadLength * ((double) v.getLocalizacion()) / ((double) r.getLength());
                double x = Math.sin(alpha) * relLoc;
                double y = Math.cos(alpha) * relLoc;
                int xDir = x1 < x2 ? 1 : -1;
                int yDir = y1 < y2 ? 1 : -1;

                int vX = x1 + xDir * ((int) x);
                int vY = y1 + yDir * ((int) y);

                // Choose a color for the vehcile's label and background, depending on its
                // contamination class
                int vLabelColor = (int) (25.0 * (10.0 - (double) v.getGradoContaminacion()));
                g.setColor(new Color(0, vLabelColor, 0));

                // draw an image of a car (with circle as background) and it identifier
                g.fillOval(vX - 1, vY - 6, 14, 14);
                g.drawImage(_car, vX, vY - 6, 12, 12, this);
                g.drawString(v.getId(), vX, vY - 6);
            }
        }
    }

    private void drawJunctions(Graphics g) {
        for (Junction j : _map.getJunctions()) {

            // (x,y) are the coordinates of the junction
            int x = j.getCoordenadaX();
            int y = j.getCoordenadaY();

            // draw a circle with center at (x,y) with radius _JRADIUS
            g.setColor(_JUNCTION_COLOR);
            g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

            // draw the junction's identifier at (x,y)
            g.setColor(_JUNCTION_LABEL_COLOR);
            g.drawString(j.getId(), x, y);
        }
    }

    // this method is used to update the preffered and actual size of the component,
    // so when we draw outside the visible area the scrollbars show up
    private void updatePrefferedSize() {
        int maxW = 200;
        int maxH = 200;
        for (Junction j : _map.getJunctions()) {
            maxW = Math.max(maxW, j.getCoordenadaX());
            maxH = Math.max(maxH, j.getCoordenadaY());
        }
        maxW += 20;
        maxH += 20;
        setPreferredSize(new Dimension(maxW, maxH));
        setSize(new Dimension(maxW, maxH));
    }

    private Image loadImage(String img) {
        Image i = null;
        try {
            return ImageIO.read(new File("resources/icons/" + img));
        } catch (IOException e) {
        }
        return i;
    }



    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {

    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onError(String err) {

    }
}