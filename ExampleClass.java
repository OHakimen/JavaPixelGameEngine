import java.awt.*;

public class ExampleClass {
    static class Example extends PixelGameEngine{
        public Example(int width, int height, Color bgColor) {
            super("Example", width, height, bgColor);
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
