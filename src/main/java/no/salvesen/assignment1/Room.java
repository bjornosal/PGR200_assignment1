package no.salvesen.assignment1;


public abstract class Room {
    private String room_name;
    private String campus;
    private String[] facilities;

    public Room(String room_name, String campus, String[] facilities) {
        this.room_name = room_name;
        this.campus = campus;
        this.facilities = facilities;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String[] getFacilities() {
        return facilities;
    }

    public void setFacilities(String[] facilities) {
        this.facilities = facilities;
    }
}
