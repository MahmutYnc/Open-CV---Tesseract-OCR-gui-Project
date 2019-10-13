package main.java.com.company;

public class Combiner {
    public GuiClass guiClass = new GuiClass();
    public Main m = new Main();

    public String path;



    public String pathReturner() {
        path = guiClass.path;
        return path;
    }
}
