package no.salvesen.assignment1;

public class App
{
    public static void main(String[] args){
        App app = new App();
        app.runApp();
    }

    /**
     * Starts loop of the console app which let's a user provide input to get
     * information from any table.
     */
    private void runApp() {
        InputHandler inputHandler = new InputHandler();
        inputHandler.startMenuLoop();
    }
}
