public class Stop {

    private final String id;
    private final Location location;
    private final String name;

    public Stop(String id, Location loc, String name) {
        this.id = id;
        this.location = loc;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
