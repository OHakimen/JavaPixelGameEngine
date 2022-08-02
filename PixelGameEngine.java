package com.hakimen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import javax.imageio.ImageIO;

/*
Copyright (c) 2021, Hakimen
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 3. All advertising materials mentioning features or use of this software
 must display the following acknowledgement: This product includes software
 developed by the Hakimen.
 4. Neither the name of the Hakimen nor the names of its contributors may be
 used to endorse or promote products derived from this software without specific
 prior written permission.

 THIS SOFTWARE IS PROVIDED BY HAKIMEN ''AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL HAKIMEN BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
**/

/**
 * <b>Hakimen's Game Engine</b>
 *
 * @author Hakimen
 * @portfrom olc::PixelGameEngine (by Javidx9)
 * @version 2.3
 */

/*
Example Implementation :
import java.awt.*;
public class ExampleClass {
    static class Example extends PixelGameEngine{
        public Example(String title,int width, int height,int scaleX,int scaleY) {
            super("Example", width, height, scaleX, scaleY);
        }
        @Override
        public boolean OnUserCreate() {
            return true;
        }

        @Override
        public boolean OnUserUpdate(float fElapsedTime) {
            return true;
        }
    }
    public static void main(String[] args) {
        Example ex = new Example("Example Title",800,600,2,2);
        ex.start();
    }
}
 */
public abstract class PixelGameEngine {

    public static class Sprite {
        public BufferedImage img;

        int sizeX;
        int sizeY;

        private Sprite(String path) {
            try {
                img = ImageIO.read(getClass().getResource(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            sizeX = img.getWidth();
            sizeY = img.getHeight();
        }

        protected Sprite(BufferedImage img) {
            this.img = img;
        }

        public static Sprite load(String path) {
            return new Sprite(path);
        }
    }
    private int mouseX;
    private int mouseY;
    private int nScrollDir;

    private final int ScreenWidth;
    private final int ScreenHeight;
    private final int scaleX;
    private final int scaleY;

    private boolean b_keys[] = new boolean[1024];
    private boolean b_mouse[] = new boolean[6];
    private char lastKey = ' ';
    public JFrame frame;
    private static String title;
    private static boolean isRunning;
    protected static int fps;
    static Graphics2D g;
    protected static int maxFPS;
    private Font engineFont = new Font("Consolas",1,8);

    public int GetMouseScroll(){
        return nScrollDir;
    }

    public char GetLastCharacter() {
        return lastKey;
    }

    public boolean GetKey(int i){
        return b_keys[i];
    }

    public boolean GetMouse(int i){
        return b_mouse[i];
    }

    public abstract boolean OnUserCreate();
    public abstract boolean OnUserUpdate(float fElapsedTime);
    public boolean OnUserDestroy() {
        return true;
    }

    View display;

    public PixelGameEngine(String sTitle,int maxFPS, int width, int height, int scaleX, int scaleY) {
        this.maxFPS = maxFPS;
        title = sTitle;
        title = "JavaPixelGameEngine (by Hakimen) " + title;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.ScreenHeight = (height) * this.scaleY;
        this.ScreenWidth = (width) * this.scaleX;
        display = new View();
        frame = new JFrame(title);
        frame.setLayout(new CardLayout());
        frame.add(display,"Hee",0);
        this.frame.setSize(ScreenWidth, ScreenHeight);
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(3);
        this.frame.setVisible(true);
        display.engine = this;
    }
    public PixelGameEngine(String sTitle, int width, int height, int scaleX, int scaleY) {
        this.maxFPS = Integer.MAX_VALUE;
        title = sTitle;
        title = "JavaPixelGameEngine (by Hakimen) " + title;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.ScreenHeight = (height) * this.scaleY;
        this.ScreenWidth = (width) * this.scaleX;
        display = new View();
        frame = new JFrame(title);
        frame.setLayout(new CardLayout());
        frame.add(display,"Hee",0);
        this.frame.setSize(ScreenWidth, ScreenHeight);
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(3);
        this.frame.setVisible(true);
        display.engine = this;
    }

    public void start() {
        display.start();
    }

    public int MouseX() {
        return mouseX;
    }

    public int MouseY() {
        return mouseY;
    }

    public int ScreenHeight() {
        return this.ScreenHeight / scaleY;
    }

    public int ScreenWidth() {
        return this.ScreenWidth / scaleX;
    }

    public void Clear(Color color) {
        FillRect(0, 0, ScreenWidth(), ScreenHeight(), color);
    }

    public void ClipArea(Shape s){
        g.clip(s);
    }

    public void Translate(int dx, int dy) {
        g.translate(dx, dy);
    }

    public void Rotate(int r) {
        g.rotate(r);
    }

    public void AffineTransform(AffineTransform at){
        g.transform(at);
    }

    public void SetDefaultFont(Font f){
        engineFont = f;
    }

    public void SetFont(Font f){
        g.setFont(f);
    }

    public void Shear(int x,int y){
        g.shear(x,y);
    }

    /**
     * <b>Draws a rectangle</b>
     *
     * @param x     The x position;
     * @param y     The y position;
     * @param dx    The size in the x axis;
     * @param dy    The size in the y axis;
     * @param color The color used to draw the rectangle
     * @since 1.0
     */
    public void DrawRect(int x, int y, int dx, int dy, Color color) {
        g.setColor(color);
        g.drawRect(x, y, dx, dy);
    }
    public void DrawRect(float x,float y,float dx,float dy,Color color) {
        g.setColor(color);
        g.drawRect((int)x, (int)y, (int)dx, (int)dy);
    }

    /**
     * <b>Draws a filled in rectangle</b>
     *
     * @param x     The x position;
     * @param y     The y position;
     * @param dx    The size in the x axis;
     * @param dy    The size in the y axis;
     * @param color The color used to draw the rectangle
     * @since 1.0
     */
    public void FillRect(int x, int y, int dx, int dy, Color color) {
        g.setColor(color);
        g.fillRect(x, y, dx, dy);
    }
    public void FillRect(float x,float y,float dx,float dy,Color color) {
        g.setColor(color);
        g.fillRect((int)x, (int)y, (int)dx, (int)dy);
    }

    /**
     * <b>Draws a rounded rectangle</b>
     *
     * @param x         The x position;
     * @param y         The y position;
     * @param dx        The size in the x axis;
     * @param dy        The size in the y axis;
     * @param arcWidth  The Arc's Width;
     * @param arcHeight The Arc's Height;
     * @param color     The color used to draw the rectangle
     * @since 2.1
     */
    public void DrawRoundedRect(int x, int y, int dx, int dy, int arcWidth, int arcHeight, Color color) {
        g.setColor(color);
        g.drawRoundRect(x, y, dx, dy, arcWidth, arcHeight);
    }
    public void DrawRoundedRect(float x, float y, float dx, float dy, float arcWidth, float arcHeight, Color color) {
        g.setColor(color);
        g.drawRoundRect((int)x, (int)y, (int)dx, (int)dy, (int)arcWidth, (int)arcHeight);
    }

    /**
     * <b>Draws a rounded filled in rectangle</b>
     *
     * @param x         The x position;
     * @param y         The y position;
     * @param dx        The size in the x axis;
     * @param dy        The size in the y axis;
     * @param arcWidth  The Arc's Width;
     * @param arcHeight The Arc's Height;
     * @param color     The color used to draw the rectangle
     * @since 2.1
     */
    public void FillRoundedRect(int x, int y, int dx, int dy, int arcWidth, int arcHeight, Color color) {
        g.setColor(color);
        g.fillRoundRect(x, y, dx, dy, arcWidth, arcHeight);
    }
    public void FillRoundedRect(float x, float y, float dx, float dy, float arcWidth, float arcHeight, Color color) {
        g.setColor(color);
        g.fillRoundRect((int)x, (int)y, (int)dx, (int)dy, (int)arcWidth, (int)arcHeight);
    }

    /**
     * <b>Draws a Circle</b>
     *
     * @param x      The x position;
     * @param y      The y position;
     * @param radius The radius of the Circle
     * @param color  The color used to draw the Circle
     * @since 1.0
     */
    public void DrawCircle(int x, int y, int radius, Color color) {
        g.setColor(color);
        g.drawOval(x, y, radius, radius);
    }

    public void DrawCircle(float x, float y, float radius, Color color) {
        g.setColor(color);
        g.drawOval((int)x, (int)y, (int)radius, (int)radius);
    }

    /**
     * <b>Draws a filled in Circle</b>
     *
     * @param x      The x position;
     * @param y      The y position;
     * @param radius The radius of the Circle
     * @param color  The color used to draw the Circle
     * @since 1.0
     */
    public void FillCircle(int x, int y, int radius, Color color) {
        g.setColor(color);
        g.fillOval(x, y, radius, radius);
    }
    public void FillCircle(float x, float y, float radius, Color color) {
        g.setColor(color);
        g.fillOval((int)x, (int)y, (int)radius, (int)radius);
    }

    /**
     * <b>Draws a Oval</b>
     *
     * @param x     The x position;
     * @param y     The y position;
     * @param dx    The size of the oval in the x axis;
     * @param dy    The size of the oval in the y axis;
     * @param color The color used to draw the Circle
     * @since 1.0
     */
    public void DrawOval(int x, int y, int dx, int dy, Color color) {
        g.setColor(color);
        g.drawOval(x, y, dx, dy);
    }
    public void DrawOval(float x, float y, float dx, float dy, Color color) {
        g.setColor(color);
        g.drawOval((int)x, (int)y, (int)dx, (int)dy);
    }

    /**
     * <b>Draws a filled in Oval</b>
     *
     * @param x     The x position;
     * @param y     The y position;
     * @param dx    The size of the oval in the x axis;
     * @param dy    The size of the oval in the y axis;
     * @param color The color used to draw the Circle
     * @since 1.0
     */
    public void FillOval(int x, int y, int dx, int dy, Color color) {
        g.setColor(color);
        g.fillOval(x, y, dx, dy);
    }
    public void FillOval(float x, float y, float dx, float dy, Color color) {
        g.setColor(color);
        g.fillOval((int)x, (int)y, (int)dx, (int)dy);
    }

    /**
     * <b>Draw a string</b>
     *
     * @param x     The x position
     * @param y     The y position
     * @param color The Color for the String
     * @param s     The String to be Drew
     * @since 1.0
     */
    public void DrawString(int x, int y, Color color, Object s) {
        g.setColor(color);
        g.drawString(s.toString(), x, y + 10);
    }

    /**
     * <b>Draw a string</b>
     *
     * @param x        The x position
     * @param y        The y position
     * @param color    The Color for the String
     * @param template The template string to be formatted;
     * @param data     The data to fill in the template string;
     * @since 1.0
     */
    public void DrawFormattedString(int x, int y, Color color, String template, Object... data) {
        g.setColor(color);
        g.drawString(String.format(template, data), x, y + 10);
    }

    /**
     * <b>Draws an image</b>
     *
     * @param x   The x position
     * @param y   The y position
     * @param img The buffered image to draw
     * @since 1.0
     */
    public void DrawSprite(int x, int y, Sprite img) {
        g.drawImage(img.img,x,y,null);
    }
    public void DrawSprite(float x, float y, Sprite img) {
        g.drawImage(img.img,(int)x,(int)y,null);
    }
    public void DrawSprite(int x, int y, int width,int height,Sprite img) {
        g.drawImage(img.img,x,y,width,height,null);
    }
    public void DrawSprite(float x, float y,float width,float height, Sprite img) {
        g.drawImage(img.img,(int)x,(int)y,(int)width,(int)height,null);
    }

    /**
     * <b>Gets an part of a image</b>
     *
     * @param topX    The top x position relative to the image
     * @param topY    The top y position relative to the image
     * @param bottomX The bottom x position relative to the image
     * @param bottomY The bottom y position relative to the image
     * @param img     The buffered image to draw
     * @since 1.0
     */
    public Sprite GetPartialSprite(int topX, int topY, int bottomX, int bottomY, Sprite img) {
        return new Sprite(img.img.getSubimage(topX, topY, bottomX, bottomY));
    }

    /**
     * <b>Draws a pixel<b/>
     *
     * @param x     The x position
     * @param y     The y position
     * @param color The color of the pixel
     * @since 1.0
     */
    public void DrawPixel(int x, int y, Color color) {
        g.setColor(color);
        g.drawLine(x, y, x, y);
    }
    public void DrawPixel(float x, float y, Color color) {
        g.setColor(color);
        g.drawLine((int)x, (int)y, (int)x, (int)y);
    }

    /**
     * <b>Draw a Line</b>
     *
     * @param startX The Starting x position
     * @param startY The Starting y position
     * @param endX   The End x position
     * @param endY   The End y position
     * @param color  The Color of the line
     * @since 1.0
     */
    public void DrawLine(int startX, int startY, int endX, int endY, Color color) {
        g.setColor(color);
        g.drawLine(startX, startY, endX, endY);
    }
    public void DrawLine(float startX, float startY, float endX, float endY, Color color) {
        g.setColor(color);
        g.drawLine((int)startX, (int)startY, (int)endX, (int)endY);
    }


    /**
     * <b>Draw a wireframe model</b>
     *
     * @param modelCoords The Model to Draw
     * @param x           The x position
     * @param y           The y position
     * @param r           The rotation of the model
     * @param s           The scale of the model
     * @param color       The color for the model
     * @since 1.3
     */
    public void DrawWireframeModel(ArrayList<float[]> modelCoords, int x, int y, float r, int s, Color color) {
        ArrayList<float[]> transformedCoords = new ArrayList<>();
        int vertices = modelCoords.size();
        //Rotate
        for (int i = 0; i < vertices; i++) {
            float key = (float) (modelCoords.get(i)[0] * Math.cos(r) - modelCoords.get(i)[1] * Math.sin(r));
            float val = (float) (modelCoords.get(i)[0] * Math.sin(r) + modelCoords.get(i)[1] * Math.cos(r));
            transformedCoords.add(new float[]{key, val});
        }
        //Scale
        for (int i = 0; i < vertices; i++) {
            float key = transformedCoords.get(i)[0] * s;
            float val = transformedCoords.get(i)[1] * s;
            transformedCoords.set(i, new float[]{key, val});
        }
        //Offset
        for (int i = 0; i < vertices; i++) {
            float key = transformedCoords.get(i)[0] + x;
            float val = transformedCoords.get(i)[1] + y;
            transformedCoords.set(i, new float[]{key, val});
        }
        for (int i = 0; i < vertices + 1; i++) {
            int j = (i + 1);
            DrawLine((int) transformedCoords.get(i % vertices)[0], (int) transformedCoords.get(i % vertices)[1],
                    (int) transformedCoords.get(j % vertices)[0], (int) transformedCoords.get(j % vertices)[1], color);
        }
    }

    public void DrawArc(int x,int y,int sizeX,int sizeY,int angleStart, int angleEnd,Color c,Stroke s){
        g.setColor(c);
        g.setStroke(s);
        g.drawArc(x,y,sizeX,sizeY,angleStart,angleEnd);
        g.setStroke(new BasicStroke());
    }
    public void DrawArc(float x,float y,float sizeX,float sizeY,float angleStart, float angleEnd,Color c,Stroke s){
        g.setColor(c);
        g.setStroke(s);
        g.drawArc((int)x,(int)y,(int)sizeX,(int)sizeY,(int)angleStart,(int)angleEnd);
        g.setStroke(new BasicStroke());
    }

    public void FillArc(int x,int y,int sizeX,int sizeY,int angleStart, int angleEnd,Color c,Stroke s){
        g.setColor(c);
        g.setStroke(s);
        g.fillArc(x,y,sizeX,sizeY,angleStart,angleEnd);
        g.setStroke(new BasicStroke());
    }
    public void FillArc(float x,float y,float sizeX,float sizeY,float angleStart, float angleEnd,Color c,Stroke s){
        g.setColor(c);
        g.setStroke(s);
        g.fillArc((int)x,(int)y,(int)sizeX,(int)sizeY,(int)angleStart,(int)angleEnd);
        g.setStroke(new BasicStroke());
    }

    public void DrawPolygon(Polygon poly, Color c) {
        g.setColor(c);
        g.drawPolygon(poly);
    }
    public void FillPolygon(Polygon poly, Color c) {
        g.setColor(c);
        g.fillPolygon(poly);
    }

    public void DrawTriangle(Color c,int p1x,int p1y,int p2x,int p2y,int p3x,int p3y){
        Polygon p = new Polygon();
        p.addPoint(p1x,p1y);
        p.addPoint(p3x,p3y);
        p.addPoint(p2x,p2y);
        DrawPolygon(p,c);
    }
    public void FillTriangle(Color c,int p1x,int p1y,int p2x,int p2y,int p3x,int p3y){
        Polygon p = new Polygon();
        p.addPoint(p1x,p1y);
        p.addPoint(p3x,p3y);
        p.addPoint(p2x,p2y);
        FillPolygon(p,c);
    }

    public void DrawTriangle(Color c,float p1x,float p1y,float p2x,float p2y,float p3x,float p3y){
        Polygon p = new Polygon();
        p.addPoint((int)p1x,(int)p1y);
        p.addPoint((int)p3x,(int)p3y);
        p.addPoint((int)p2x,(int)p2y);
        DrawPolygon(p,c);
    }
    public void FillTriangle(Color c,float p1x,float p1y,float p2x,float p2y,float p3x,float p3y){
        Polygon p = new Polygon();
        p.addPoint((int)p1x,(int)p1y);
        p.addPoint((int)p3x,(int)p3y);
        p.addPoint((int)p2x,(int)p2y);
        FillPolygon(p,c);
    }

    public void SetHints(RenderingHints.Key key,Object value){
        g.setRenderingHint(key,value);
    }

    /**
     * Get the pixel color in the x,y coords off the given image
     *
     * @param img The Sprite;
     * @param x   The x coord of the pixel in the image
     * @param y   The y coord of the pixel in the image
     * @return The Color of The given pixel
     * @since 2.1
     */




    public Color GetPixel(Sprite img, int x, int y) {
        int rgb = img.img.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return new Color(red, green, blue);
    }

    public Sprite GetTintedSprite(Sprite sprite, Color color) {
        BufferedImage tintedSprite = new BufferedImage(sprite.img.getWidth(), sprite.img.
                getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D graphics = tintedSprite.createGraphics();
        graphics.drawImage(sprite.img, 0, 0, null);
        graphics.dispose();
        float r = map(color.getRed(), 0, 255, 0, 1), g = map(color.getGreen(), 0, 255, 0, 1), b = map(color.getBlue(), 0, 255, 0, 1), a = map(color.getAlpha(), 0, 255, 0, 1);
        for (int i = 0; i < tintedSprite.getWidth(); i++) {
            for (int j = 0; j < tintedSprite.getHeight(); j++) {
                int ax = tintedSprite.getColorModel().getAlpha(tintedSprite.getRaster().
                        getDataElements(i, j, null));
                int rx = tintedSprite.getColorModel().getRed(tintedSprite.getRaster().
                        getDataElements(i, j, null));
                int gx = tintedSprite.getColorModel().getGreen(tintedSprite.getRaster().
                        getDataElements(i, j, null));
                int bx = tintedSprite.getColorModel().getBlue(tintedSprite.getRaster().
                        getDataElements(i, j, null));
                rx *= r;
                gx *= g;
                bx *= b;
                ax *= a;
                tintedSprite.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx));
            }
        }
        return new Sprite(tintedSprite);
    }

    static public final float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    public Sprite GetTintedPartialSprite(int topX, int topY, int bottomX, int bottomY, Sprite img, Color color) {
        return GetTintedSprite(new Sprite(img.img.getSubimage(topX,topY,bottomX,bottomY)),color);
    }


    static class View extends Canvas implements Runnable {

        private long scrollResetTimer;

        public View() {
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    engine.b_keys[keyEvent.getKeyCode()] = true;
                    engine.lastKey = keyEvent.getKeyChar();
                }
                public void keyReleased(KeyEvent keyEvent) {
                    engine.b_keys[keyEvent.getKeyCode()] = false;
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    engine.b_mouse[mouseEvent.getButton()] = true;
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                    engine.b_mouse[mouseEvent.getButton()] = false;
                }
            });
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent mouseEvent) {
                    engine.mouseX = mouseEvent.getX() / engine.scaleX;
                    engine.mouseY = mouseEvent.getY() / engine.scaleY;
                }

                @Override
                public void mouseDragged(MouseEvent mouseEvent) {
                    engine.mouseX = mouseEvent.getX() / engine.scaleX;
                    engine.mouseY = mouseEvent.getY() / engine.scaleY;
                }
            });
            addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                    engine.nScrollDir = mouseWheelEvent.getWheelRotation();
                    scrollResetTimer = System.currentTimeMillis();
                }
            });
        }

        private Thread thread;
        PixelGameEngine engine;

        public synchronized void start() {
            isRunning = true;
            this.thread = new Thread(this, "Render");
            this.thread.start();
        }

        public synchronized void stop() {
            isRunning = false;
            try {
                this.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        private void render(float delta){
            BufferStrategy bs = this.getBufferStrategy();
            if (bs == null) {
                this.createBufferStrategy(4);
                return;
            }
            if (System.currentTimeMillis() - scrollResetTimer > 100) {
                engine.nScrollDir = 0;
            }

            engine.g = (Graphics2D) bs.getDrawGraphics();
            g.scale(engine.scaleX, engine.scaleY);
            g.setFont(engine.engineFont);
            if (!engine.OnUserUpdate((float)delta)) {
                engine.OnUserDestroy();
                stop();
            }

            g.dispose();
            bs.show();
        }

        @Override
        public void run() {
            long lastTime = System.nanoTime();
            long currentTime = System.currentTimeMillis();
            final double ns = 1000000000.0 / 60f;
            double delta = 0;
            int frames = 0;

            long renderLastTime=System.nanoTime();
            double amtOfRenders = engine.maxFPS;//MAX FPS
            double renderNs=1000000000/amtOfRenders;
            double renderDelta = 0;

            if (!engine.OnUserCreate()) {
                stop();
            }
            while (isRunning) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while (delta >= 1) {
                    delta--;
                }
                now = System.nanoTime();
                renderDelta += (now - renderLastTime) / renderNs;
                renderLastTime = now;
                while(isRunning && renderDelta >= 1){
                    render((float)delta);
                    frames++;
                    renderDelta--;
                    if (System.currentTimeMillis() - currentTime > 1000) {
                        currentTime += 1000;
                        fps = frames;
                        engine.frame.setTitle(title + " | FPS :" + frames);
                        frames = 0;
                    }
                }

            }
            stop();
        }
    }
}