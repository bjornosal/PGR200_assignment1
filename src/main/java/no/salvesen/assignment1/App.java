package no.salvesen.assignment1;

public class App
{
    public static void main(String[] args){
        App app = new App();
        app.runApp();
    }

    /**
     * Currently drops table and recreates, and fills it with information in file due to issues
     * regarding making the entire system generic, and to be valid for all classes and tables.
     * Currently using web-server to make the creation of a login for each tester redundant for now.
     * This makes the app very slow, due to having to reach the web server apparently.
     */
    private void runApp() {
        InputHandler inputHandler = new InputHandler();
        inputHandler.startMenuLoop();

    }
}
