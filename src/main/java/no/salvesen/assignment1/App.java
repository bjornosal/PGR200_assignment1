package no.salvesen.assignment1;

public class App
{
    public static void main(String[] args){
        App app = new App();
        app.runApp();
    }

    /**
     *
     */
    private void runApp() {
        InputHandler inputHandler = new InputHandler();
        inputHandler.startMenuLoop();
    }
}
