package no.salvesen.assignment1;

/**
 * Created by Bjørn Olav Salvesen on 04.09.2017.
 */
public class Subject {
    private int id;
    private String name;
    private String duration;
    private String teaching_form;
    private int attending_students;

    public Subject(int id, String name, String duration, String teaching_form, int attending_students) {
        this.id = id;

        this.name = name;
        this.duration = duration;
        this.teaching_form = teaching_form;
        this.attending_students = attending_students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTeaching_form() {
        return teaching_form;
    }

    public void setTeaching_form(String teaching_form) {
        this.teaching_form = teaching_form;
    }

    public int getAttending_students() {
        return attending_students;
    }

    public void setAttending_students(int attending_students) {
        this.attending_students = attending_students;
    }


}
