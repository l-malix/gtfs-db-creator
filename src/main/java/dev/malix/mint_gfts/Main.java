package dev.malix.mint_gfts;

import org.json.JSONException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {

        DataLoader dataLoader = new DataLoader("https://data.nantesmetropole.fr/api/records/1.0/search/?dataset=244400404_tan-arrets-horaires-circuits&q=");
        System.out.println(dataLoader.getUrl());
        String zipFilePath = "data/gtfs-tan.zip";
        String destDir = "data";
        dataLoader.downloadAndUnzip(zipFilePath, destDir);


        Reader rd = new Reader("data", "postgres", "lmalix", "jdbc:postgresql://localhost/mint_tan_gtfs");
        rd.readCalendar();
        rd.readCalendarDates();
        rd.readStop();
        rd.readRoutes();
        rd.readTrips();
        rd.readStopTimes();

    }
}
