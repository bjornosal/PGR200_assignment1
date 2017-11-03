package no.salvesen.assignment1;

public class Menu {


    private String[] tableChoices;
    private String[] searchChoices;
    private String[] mainMenuChoices;
    private String[] propertiesMenuChoices;

    public Menu() {
        setTableChoices();
        setSearchChoices();
        setMainMenuChoices();
        setPropertiesMenu();
    }

    public String mainMenu() {
        return menuHeader() + menuChoices(getMainMenuChoices(), false);
    }
    public String tableMenu() {
        return menuHeader() + menuChoices(getTableChoices(), true);
    }
    public String searchMenu() {
        return  menuHeader() + menuChoices(getSearchChoices(), true);
    }
    public String propertiesMenu() {return menuHeader() + menuChoices(getPropertiesMenuChoices(), false);}




    private String menuHeader() {
        String menuSeparator = "----------------------------------------------";
        return menuSeparator +
                "\nEnter the number before action and press enter\n" +
                menuSeparator +
                "\n";
    }

    private String menuChoices(String[] choices, boolean addEnd) {

        StringBuilder menuChoices = new StringBuilder();

        for(int i = 0; i < choices.length; i++) {

            menuChoices.append(i + 1).append(": ").append(choices[i]);
            if(i < choices.length - 1) {
                menuChoices.append("\n");
            }
            if(i == choices.length - 1 && addEnd) {
                menuChoices.append("\n");
                menuChoices.append(i + 2).append(": Return to main menu\n");
                menuChoices.append(i + 3).append(": Quit");
            }
        }

        return menuChoices.toString();
    }


    private void setMainMenuChoices() {
        mainMenuChoices = new String[2];
        mainMenuChoices[0] = "Get information from table.";
        mainMenuChoices[1] = "Add information to table.";
    }

    //menuchoices for all searches that a user can do.
    //TODO after adding foreign key, add an option regarding subject's that lecturer's can lecture, or which rooms that subjects can have depending on type?
    private void setSearchChoices() {
        searchChoices = new String[6];
        searchChoices[0] = "Get information on a subject";
        searchChoices[1] = "Get information on all subjects";
        searchChoices[2] = "Get information on a lecturer";
        searchChoices[3] = "Get information on all lecturers";
        searchChoices[4] = "Get information on a room";
        searchChoices[5] = "Get information on all rooms";
    }

    //Choices that makes a change on tables
    private void setTableChoices() {
        tableChoices = new String[5];
        tableChoices[0] = "Add new filepath for \'subject\' table information";
        tableChoices[1] = "Add new filepath for \'room\' table information";
        tableChoices[2] = "Add new filepath for \'lecturer\' table information";
        tableChoices[3] = "Use existing files in files folder";
        tableChoices[4] = "Fill a table with information from file.";
    }

    private void setPropertiesMenu() {
        propertiesMenuChoices = new String[3];
        propertiesMenuChoices[0] = "Use default database properties";
        propertiesMenuChoices[1] = "Use properties set by user";
        propertiesMenuChoices[2] = "Set new database properties";
    }

    private String[] getTableChoices() {
        return tableChoices;
    }

    private String[] getSearchChoices() {
        return searchChoices;
    }

    private String[] getMainMenuChoices() {
        return mainMenuChoices;
    }

    private String[] getPropertiesMenuChoices() {
        return propertiesMenuChoices;
    }
}
