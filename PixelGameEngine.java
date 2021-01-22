package com.javabinary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/*
Copyright (c) 2021, Hakimen
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software must display the following acknowledgement: This product includes software developed by the Hakimen.
4. Neither the name of the Hakimen nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY HAKIMEN ''AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
**/
/**
 * <b>Hakimen's Game Engine</b>
 *
 * @author Hakimen
 * @version 2.2
 */

/*
Example Implementation :

import java.awt.*;

public class ExampleClass {
    static class Example extends PixelGameEngine{
        public Example(int width, int height,int scaleX,int scaleY,Color bgColor) {
            super("Example", width, height, scaleX, scaleY, bgColor);
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
        Example ex = new Example(800,600,Color.BLACK);
        ex.start();
    }
}

 */
public abstract class PixelGameEngine {

    static class Sprite {

        private BufferedImage img;

        int sizeX;
        int sizeY;

        private Sprite(String path) {
            try {
                img = ImageIO.read(getClass().getResource(path));
                sizeX = img.getWidth();
                sizeY = img.getHeight();
            } catch (IOException ex) {
                Logger.getLogger(PixelGameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    public int nScrollDir;

    enum MouseState {
        NULL(0),
        BUTTON_1(1),
        BUTTON_2(2),
        BUTTON_3(3),
        BUTTON_4(4),
        BUTTON_5(5);

        int btn;

        MouseState(int i) {
            this.btn = i;
        }
    }

    private final int ScreenWidth;
    private final int ScreenHeight;
    private final int scaleX;
    private final int scaleY;

    protected boolean b_keys[] = new boolean[1024];
    protected boolean b_mouse[] = new boolean[6];

    public JFrame frame;
    private static String title;
    private static boolean isRunning;
    protected static int fps;
    private Color bgColor;
    static Graphics2D g;

    public abstract boolean OnUserCreate();

    public abstract boolean OnUserUpdate(float fElapsedTime);

    public boolean OnUserDestroy() {
        return true;
    }
    View display;

    public PixelGameEngine(String sTitle, int width, int height, int scaleX, int scaleY, Color bgColor) {
        title = sTitle;
        title = "JavaPixelGameEngine (by Hakimen) | " + title;
        this.bgColor = bgColor;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.ScreenHeight = height * this.scaleX;
        this.ScreenWidth = width * this.scaleY;
        display = new View();
        frame = new JFrame(title);
        this.frame.setSize(ScreenWidth, ScreenHeight);
        this.frame.getContentPane().add(display);
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
        return this.ScreenHeight/scaleY;
    }

    public int ScreenWidth() {
        return this.ScreenWidth/scaleX;
    }

    void Translate(int dx, int dy) {
        g.translate(dx, dy);
    }

    void Rotate(int r) {
        g.rotate(r);
    }

    /**
     * <b>Draws a rectangle</b>
     *
     * @param x The x position;
     * @param y The y position;
     * @param dx The size in the x axis;
     * @param dy The size in the y axis;
     * @param color The color used to draw the rectangle
     * @since 1.0
     *
     */
    void DrawRect(int x, int y, int dx, int dy, Color color) {
        g.setColor(color);
        g.drawRect(x, y, dx, dy);
    }

    /**
     * <b>Draws a filled in rectangle</b>
     *
     * @param x The x position;
     * @param y The y position;
     * @param dx The size in the x axis;
     * @param dy The size in the y axis;
     * @param color The color used to draw the rectangle
     * @since 1.0
     *
     */
    void FillRect(int x, int y, int dx, int dy, Color color) {
        g.setColor(color);
        g.fillRect(x, y, dx, dy);
    }

    /**
     * <b>Draws a rounded rectangle</b>
     *
     * @param x The x position;
     * @param y The y position;
     * @param dx The size in the x axis;
     * @param dy The size in the y axis;
     * @param arcWidth The Arc's Width;
     * @param arcHeight The Arc's Height;
     * @param color The color used to draw the rectangle
     * @since 2.1
     *
     */
    void DrawRoundedRect(int x, int y, int dx, int dy, int arcWidth, int arcHeight, Color color) {
        g.setColor(color);
        g.drawRoundRect(x, y, dx, dy, arcWidth, arcHeight);
    }

    /**
     * <b>Draws a rounded filled in rectangle</b>
     *
     * @param x The x position;
     * @param y The y position;
     * @param dx The size in the x axis;
     * @param dy The size in the y axis;
     * @param arcWidth The Arc's Width;
     * @param arcHeight The Arc's Height;
     * @param color The color used to draw the rectangle
     * @since 2.1
     *
     */
    void FillRoundedRect(int x, int y, int dx, int dy, int arcWidth, int arcHeight, Color color) {
        g.setColor(color);
        g.fillRoundRect(x, y, dx, dy, arcWidth, arcHeight);
    }

    /**
     * <b>Draws a Circle</b>
     *
     * @param x The x position;
     * @param y The y position;
     * @param radius The radius of the Circle
     * @param color The color used to draw the Circle
     * @since 1.0
     *
     */
    void DrawCircle(int x, int y, int radius, Color color) {
        g.setColor(color);
        g.drawOval(x, y, radius, radius);
    }

    /**
     * <b>Draws a filled in Circle</b>
     *
     * @param x The x position;
     * @param y The y position;
     * @param radius The radius of the Circle
     * @param color The color used to draw the Circle
     * @since 1.0
     *
     */
    void FillCircle(int x, int y, int radius, Color color) {
        g.setColor(color);
        g.fillOval(x, y, radius, radius);
    }

    /**
     * <b>Draws a Oval</b>
     *
     * @param x The x position;
     * @param y The y position;
     * @param dx The size of the oval in the x axis;
     * @param dy The size of the oval in the y axis;
     * @param color The color used to draw the Circle
     * @since 1.0
     *
     */
    void DrawOval(int x, int y, int dx, int dy, Color color) {
        g.setColor(color);
        g.drawOval(x, y, dx, dy);
    }

    /**
     * <b>Draws a filled in Oval</b>
     *
     * @param x The x position;
     * @param y The y position;
     * @param dx The size of the oval in the x axis;
     * @param dy The size of the oval in the y axis;
     * @param color The color used to draw the Circle
     * @since 1.0
     *
     */
    void FillOval(int x, int y, int dx, int dy, Color color) {
        g.setColor(color);
        g.fillOval(x, y, dx, dy);
    }

    /**
     * <b>Draw a string</b>
     *
     * @param x The x position
     * @param y The y position
     * @param color The Color for the String
     * @param s The String to be Drew
     * @since 1.0
     *
     */
    void DrawString(int x, int y, Color color, String s) {
        g.setColor(color);
        g.drawString(s, x, y + 10);
    }

    /**
     * <b>Draw a string</b>
     *
     * @param x The x position
     * @param y The y position
     * @param color The Color for the String
     * @param template The template string to be formatted;
     * @param data The data to fill in the template string;
     * @since 1.0
     *
     */
    void DrawFormattedString(int x, int y, Color color, String template, Object... data) {
        g.setColor(color);
        g.drawString(String.format(template, data), x, y + 10);
    }

    /**
     * <b>Draws an image</b>
     *
     * @param x The x position
     * @param y The y position
     * @param img The buffered image to draw
     * @since 1.0
     *
     */
    void DrawSprite(int x, int y, Sprite img) {
        g.drawImage(img.img, x, y, null);
    }

    /**
     * <b>Draws an part of a image</b>
     *
     * @param x The x position
     * @param y The y position
     * @param topX The top x position relative to the image
     * @param topY The top y position relative to the image
     * @param bottomX The bottom x position relative to the image
     * @param bottomY The bottom y position relative to the image
     * @param img The buffered image to draw
     * @since 1.0
     *
     */
    void DrawPartialSprite(int x, int y, int topX, int topY, int bottomX, int bottomY, Sprite img) {
        g.drawImage(img.img.getSubimage(topX, topY, bottomX, bottomY), x, y, null);
    }

    /**
     * <b>Draws a pixel<b/>
     *
     * @param x The x position
     * @param y The y position
     * @param color The color of the pixel
     * @since 1.0
     */
    void DrawPixel(int x, int y, Color color) {
        g.setColor(color);
        g.drawLine(x, y, x, y);
    }

    /**
     * <b>Draw a Line</b>
     *
     * @param startX The Starting x position
     * @param startY The Starting y position
     * @param endX The End x position
     * @param endY The End y position
     * @param color The Color of the line
     * @since 1.0
     *
     */
    void DrawLine(int startX, int startY, int endX, int endY, Color color) {
        g.setColor(color);
        g.drawLine(startX, startY, endX, endY);
    }

    /**
     * <b>Draw a wireframe model</b>
     *
     * @param modelCoords The Model to Draw
     * @param x The x position
     * @param y The y position
     * @param r The rotation of the model
     * @param s The scale of the model
     * @param color The color for the model
     * @since 1.3
     *
     */
    void DrawWireframeModel(ArrayList<int[]> modelCoords, int x, int y, float r, int s, Color color) {
        ArrayList<int[]> transformedCoords = new ArrayList<>();
        int vertices = modelCoords.size();
        //Rotate
        for (int i = 0; i < vertices; i++) {
            int key = (int) (modelCoords.get(i)[0] * Math.cos(r) - modelCoords.get(i)[1] * Math.sin(r));
            int val = (int) (modelCoords.get(i)[0] * Math.sin(r) + modelCoords.get(i)[1] * Math.cos(r));
            transformedCoords.add(new int[]{key, val});
        }
        //Scale
        for (int i = 0; i < vertices; i++) {
            int key = transformedCoords.get(i)[0] * s;
            int val = transformedCoords.get(i)[1] * s;
            transformedCoords.set(i, new int[]{key, val});
        }
        //Offset
        for (int i = 0; i < vertices; i++) {
            int key = transformedCoords.get(i)[0] + x;
            int val = transformedCoords.get(i)[1] + y;
            transformedCoords.set(i, new int[]{key, val});
        }
        for (int i = 0; i < vertices + 1; i++) {
            int j = (i + 1);
            DrawLine(transformedCoords.get(i % vertices)[0], transformedCoords.get(i % vertices)[1],
                    transformedCoords.get(j % vertices)[0], transformedCoords.get(j % vertices)[1], color);
        }
    }

    /**
     * Get the pixel color in the x,y coords off the given image
     *
     * @param img The Sprite;
     * @param x The x coord of the pixel in the image
     * @param y The y coord of the pixel in the image
     * @return The Color of The given pixel
     * @since 2.1
     */
    Color GetPixel(Sprite img, int x, int y) {
        int rgb = img.img.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return new Color(red, green, blue);
    }

    public void DrawTintedSprite(int dx,int dy,Sprite sprite,Color color) {
        BufferedImage tintedSprite = new BufferedImage(sprite.img.getWidth(), sprite.img.
                getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D graphics = tintedSprite.createGraphics();
        graphics.drawImage(sprite.img, 0, 0, null);
        graphics.dispose();
        float r = map(color.getRed(),0,255,0,1),g = map(color.getGreen(),0,255,0,1),b = map(color.getBlue(),0,255,0,1),a = map(color.getAlpha(),0,255,0,1);
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
       DrawSprite(dx,dy,new Sprite(tintedSprite));
    }
    static private final float map(float value, float istart, float istop, float ostart, float ostop) {
            return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }
    
    public void DrawPartialTintedSprite(int x, int y, int topX, int topY, int bottomX, int bottomY, Sprite img,Color color){
        DrawTintedSprite(x, y, new Sprite(img.img.getSubimage(topX, topY, bottomX, bottomY)), color);
    }
    

    static class View extends Canvas implements Runnable {

        private long scrollResetTimer;

        public View() {
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    engine.b_keys[keyEvent.getKeyCode()] = true;
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

        private void render(double delta) {
            BufferStrategy bs = this.getBufferStrategy();
            if (bs == null) {
                this.createBufferStrategy(4);
                return;
            }
            if (System.currentTimeMillis() - scrollResetTimer > 100) {
                engine.nScrollDir = 0;
            }
            engine.g = (Graphics2D) bs.getDrawGraphics();
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            g.scale(engine.scaleX, engine.scaleY);
            g.setFont(new Font("Consolas", 1, 8));
            engine.FillRect(0, 0, engine.ScreenWidth, engine.ScreenHeight, engine.bgColor);
            if (!engine.OnUserUpdate((float) delta)) {
                engine.OnUserDestroy();
                stop();
            }
            g.dispose();
            bs.show();
        }

        @Override
        public void run() {
            long lastTime = System.currentTimeMillis();
            long currentTime = System.currentTimeMillis();
            final double ns = 1000000000.0 / 60;
            double delta = 0;
            int frames = 0;

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
                render(delta);
                frames++;
                if (System.currentTimeMillis() - currentTime > 1000) {
                    currentTime += 1000;
                    fps = frames;
                    engine.frame.setTitle(title + " | FPS :" + frames);
                    frames = 0;
                }
            }
            stop();
        }
    }
}
