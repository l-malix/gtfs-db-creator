package dev.malix.mint_gfts;

public class Main {

    public static void main(String[] args) {
        Reader rd = new Reader("data", "postgres", "lmalix", "jdbc:postgresql://localhost/mint_tan_gtfs");
        rd.readCalendar();
        rd.readCalendarDates();
        rd.readStop();
        rd.readRoutes();
        rd.readTrips();
        rd.readStopTimes();
    }
}
