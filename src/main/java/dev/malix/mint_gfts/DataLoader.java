package dev.malix.mint_gfts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataLoader {

    private String api_url;

    public DataLoader(String api_url) {
        this.api_url = api_url;
    }

    public JSONObject getJson(String url) throws IOException, JSONException {
        InputStream api = new URL(url).openStream();
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(api, Charset.forName("UTF-8")));
            String jsonText = "";
            String line = reader.readLine();
            while(line != null) {
                jsonText = jsonText+line;
                line = reader.readLine();
            }
            JSONObject json = new JSONObject(jsonText);
            return json;

        } catch (MalformedURLException | JSONException e) {
            System.out.println("Format incompatible");
            return new JSONObject("");
        }
        finally {
            api.close();
        }
    }

    public String getUrl() throws JSONException, IOException {
        JSONObject json = getJson(api_url);
        JSONArray records = json.getJSONArray("records");
        String datasetid = records.getJSONObject(0).getString("datasetid");
        String id = records.getJSONObject(0).getJSONObject("fields").getJSONObject("fichier").getString("id");
        String res = "https://data.nantesmetropole.fr/explore/dataset/" + datasetid + "/files/" + id + "/download/";
        return res;
    }

    public void unzipData(String zipDir, String extractionDir) throws IOException {
        File destDir = new File(extractionDir);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipDir));
        ZipEntry zipEntry = zipIn.getNextEntry();

        while (zipEntry != null){
            String filePath = extractionDir+File.separator+zipEntry.getName();
            if (!zipEntry.isDirectory()){
                extractFile(zipIn, filePath);
            } else{
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipEntry = zipIn.getNextEntry();
        }
    }

    public void extractFile(ZipInputStream zipIn, String filePath) throws IOException{
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte bytes[] = new byte[4096];
        int read = 0;
        read = zipIn.read(bytes);
        while (read != -1){
            bos.write(bytes,0,read);
            read = zipIn.read(bytes);
        }
        bos.close();
        System.out.println("Done!");
    }

    public void downloadAndUnzip(String zipFilePath, String destDirectory) throws IOException, JSONException {
        URL destURL = new URL(getUrl());
        URLConnection urlConnection = destURL.openConnection();
        ReadableByteChannel zipByteChannel = Channels.newChannel(urlConnection.getInputStream());
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        try (FileOutputStream fos = new FileOutputStream(zipFilePath)) {
            fos.getChannel().transferFrom(zipByteChannel,0,Long.MAX_VALUE);
        }
        unzipData(zipFilePath,destDirectory);
    }
}
