package dev.malix.mint_gfts;

public class Main {

    public static void main(String[] args) {
        Reader rd = new Reader();
        rd.readCalendar("data/calendar.txt");
        rd.readCalendarDates("data/calendar_dates.txt");
        rd.readStop("data/stops.txt");
        rd.readRoutes("data/routes.txt");
        rd.readTrips("data/trips.txt");
        rd.readStopTimes("data/stop_times.txt");
    }
}
