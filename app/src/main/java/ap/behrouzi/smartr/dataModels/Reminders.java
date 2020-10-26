package ap.behrouzi.smartr.dataModels;

public class Reminders {

    private int id;
    private String name;
    private String time;
    private String date;
    private int repeat;
    private String location;
    private String isLocational;
    private String extra2;
    private String done;

    public Reminders(String name, String time, String date, String location, String isLocational, String extra2, String done, int repeat) {
        this.name = name;
        this.time = time;
        this.location = location;
        this.isLocational = isLocational;
        this.extra2 = extra2;
        this.done = done;
        this.repeat = repeat;
        this.date = date;
    }

    public String getDone() {
        return done;
    }

    public String getExtra2() {
        return extra2;
    }

    public String getIsLocational() {
        return isLocational;
    }

    public String getLocation() {
        return location;
    }

    public int getRepeat() {
        return repeat;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
