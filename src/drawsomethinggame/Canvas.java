package drawsomethinggame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import javax.swing.JOptionPane;

import javax.swing.JPanel;

/**
 * @author Prof. Dr. Plinio Vilela - plinio@ft.unicamp.br
 * 
 */
public class Canvas extends JPanel implements Observer, MouseMotionListener, MouseListener {
    private SocketSender ss;
    private String myIPAddress;
    private String serverIP;
    private int mX = 0;
    private int mY = 0;
    private boolean draw = false;
    private boolean clear = false;
    private boolean erase = false;
    private SocketReceiver sr;
    List <Integer> xPoints = new ArrayList<Integer>();
    List <Integer> yPoints = new ArrayList<Integer>();

    public Canvas() {
        super();
        clear = true;
        repaint();
    }
    
    public int[] convertIntegers(List<Integer> integers){
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++){
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
    
    public void connect(String myName, int myPort, String serverIP, int serverPort) {
        this.serverIP = serverIP;
        sr = new SocketReceiver(myPort);
        sr.addObserver(this);
        try {
            myIPAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        ss = new SocketSender(serverIP,serverPort);
        try {
            ss.sendMessage("register", myName + " " + myIPAddress + " " + myPort);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Server: " + serverIP + " not available");
        }
        addMouseListener(this);
        addMouseMotionListener(this);        
    }

    public void sendMessage(String tag, String value) {
        try {
            ss.sendMessage(tag, value);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Server: " + serverIP + " not available");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Scanner in = new Scanner((String) arg);
        String tag = in.nextLine();

        if (tag.equals("p")) {
            draw = !erase;
            mX = Integer.parseInt(in.next());
            mY = Integer.parseInt(in.next());
            if(draw){
                xPoints.add(mX);
                yPoints.add(mY);
            }
            repaint();
        } else {
            draw = false;
            if(tag.equals("clear")){
                in.nextLine();
                clear = true;
                xPoints = new ArrayList<Integer>();
                yPoints = new ArrayList<Integer>();
                repaint();
            }else{
                if(tag.equals("toogleErase")){
                    in.nextLine();
                    erase = !erase;
                }
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        if(clear){
            g.setColor(this.getBackground());
            super.paintComponent(g);
            clear = false;
        }
        if (draw) {
            //g.fillOval(mX-2, mY-2, 5, 5);
            int[] xVet = convertIntegers(xPoints);
            int[] yVet = convertIntegers(yPoints);
            for(int i=0; i<xVet.length; i++){
                g.fillOval(xVet[i]-2, yVet[i]-2, 5, 5);
            }
        }else{
            // Com essa nova versao armazenando os pontos para desenhar o erase nao vai mais funcionar adequadamente.
            if(erase){
                g.setColor(this.getBackground());
                g.fillOval(mX-10, mY-10, 20, 20);
                g.setColor(Color.BLACK);
            }
        }
    }

    public void clear(){
        sendMessage("clear","null");
    }

    public void toogleErase(){
        sendMessage("toogleErase","null");
    }

    public void addObserver(Observer o) {
        sr.addObserver(o);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        sendMessage("p", (e.getX() + " " + e.getY()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        sendMessage("p", (e.getX() + " " + e.getY()));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
