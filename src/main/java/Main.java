import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        insertZooIntoDatabase();

        enclosuresJsonReader();

//      Animal JSON reader
        List<Animal> animalList = animalJsonReader();;


//        Enclosures JSON reader
        List<Enclosure> enclosureList = enclosuresJsonReader();
        assert enclosureList != null;
        for (Enclosure enclosure : enclosureList) {
            assert animalList != null;
            for (Animal animal : animalList) {
//                System.out.println("Species: " + animal.getSpecies());

            }
        }


    }

    private static List<Animal> animalJsonReader() {
        String apiUrlAnimals = "http://localhost:8080/api/json/animals";  // Replace with the API endpoint URL

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(apiUrlAnimals);

        try {
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                ObjectMapper objectMapper = new ObjectMapper();
                AnimalList animalList = objectMapper.readValue(EntityUtils.toString(entity), AnimalList.class);
                return animalList.getAnimals();
            } else {
                System.out.println("Request failed with status code: " + statusCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Enclosure> enclosuresJsonReader() {
        String apiUrlEnclosures = "http://localhost:8080/api/json/enclosures";  // Replace with the API endpoint URL

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(apiUrlEnclosures);

        try {
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                ObjectMapper objectMapper = new ObjectMapper();
                EnclosuresList enclosuresList = objectMapper.readValue(EntityUtils.toString(entity), EnclosuresList.class);
                return enclosuresList.getEnclosures();
            } else {
                System.out.println("Request failed with status code: " + statusCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void insertZooIntoDatabase() {
        String url = "jdbc:mysql://localhost/zoo_management";
        String username = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");

            try {
                String sql = "INSERT INTO `zoo`(`animal`, `species`, `amount`, `enclosure`) VALUES (?, ?, ?, ?)";

                PreparedStatement statement = connection.prepareStatement(sql);

                // Set the values for the placeholders
                statement.setString(1, "Monkey");
                statement.setString(2, "Test");
                statement.setString(3, "2");
                statement.setString(4, "Enc2");

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Insertion successful!");
                } else {
                    System.out.println("Insertion failed!");
                }

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            connection.close();
            System.out.println("Disconnected from the database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
