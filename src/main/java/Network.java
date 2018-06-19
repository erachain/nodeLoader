import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Network {

    static void sendGet(String url) {
        try {
            URL request = new URL(url);
            URLConnection connection = request.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
        } catch (IOException e) {
            System.out.println("An I/O error occurs: " + e.getMessage());
        }
    }


}
