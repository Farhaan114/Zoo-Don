package com.zoo.server;

import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyHttpServer {

    public static void startServer() {
        try {
            // Create HTTP server listening on port 8088
            HttpServer server = HttpServer.create(new InetSocketAddress(8088), 0);

            // Define the /data context and attach a handler
            server.createContext("/data", new MyHandler());

            // Start the server
            server.start();
            System.out.println("Server is listening on port 8088...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handler for processing HTTP requests
    static class MyHandler implements HttpHandler {
    	@Override
        public void handle(HttpExchange exchange) throws IOException {
            // CORS headers
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // Change "*" to your allowed domain if needed
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

            // Handle preflight OPTIONS request for CORS
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1); // No Content for OPTIONS requests
                return;
            }

            String jsonResponse = "";
            String method = exchange.getRequestMethod();

            // Handle GET requests
            if ("GET".equals(method)) {
                String path = exchange.getRequestURI().getPath();
                switch (path) {
                    case "/data/all-creatures":
                        jsonResponse = fetchAllCreatures();
                        break;
                    case "/data/endangered-species":
                        jsonResponse = fetchEndangeredSpecies();
                        break;
                    case "/data/donors-info":
                        jsonResponse = fetchAllDonorsInfo();
                        break;
                    case "/data/total-donations":
                        jsonResponse = fetchTotalDonations();
                        break;
                    case "/data/number-of-donors":
                        jsonResponse = fetchNumberOfDonors();
                        break;
                    case "/data/top-donor":
                        jsonResponse = fetchTopDonorDetails();
                        break;
                    default:
                        jsonResponse = "{\"error\":\"Invalid GET request.\"}";
                }

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(responseBytes);
                }
            }
            // Handle POST requests
            else if ("POST".equals(method)) {
                String path = exchange.getRequestURI().getPath();
                String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                        .lines().reduce("", (accumulator, actual) -> accumulator + actual);

                switch (path) {
                    case "/data/insert-donation":
                        jsonResponse = insertDonation(requestBody);
                        break;
                    case "/data/insert-feedback":
                        jsonResponse = insertFeedback(requestBody);
                        break;
                    default:
                        jsonResponse = "{\"error\":\"Invalid POST request.\"}";
                }

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(responseBytes);
                }
            }
            // Unsupported request method
            else {
                jsonResponse = "{\"error\":\"Only GET and POST requests are supported.\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(405, responseBytes.length);
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(responseBytes);
                }
            }
        }



        // Fetch all creatures
        private String fetchAllCreatures() {
            return fetchDataFromDatabase("SELECT * FROM creatures LIMIT 2");
        }

        // Fetch all endangered species
        private String fetchEndangeredSpecies() {
            return fetchDataFromDatabase("SELECT name, species, description FROM creatures WHERE endangered = TRUE");
        }

        // Fetch all donors and their information
        private String fetchAllDonorsInfo() {
            return fetchDataFromDatabase("SELECT donor_name, amount, email, donation_date FROM donations");
        }

        // Total amount of donations collected
        private String fetchTotalDonations() {
            return fetchSingleValueFromDatabase("SELECT SUM(amount) AS total_donations FROM donations");
        }

        // Number of unique donors
        private String fetchNumberOfDonors() {
            return fetchSingleValueFromDatabase("SELECT COUNT(DISTINCT donor_name) AS number_of_donors FROM donations");
        }

        // Top donor details
        private String fetchTopDonorDetails() {
            return fetchDataFromDatabase("SELECT donor_name, amount, email, donation_date FROM donations ORDER BY amount DESC LIMIT 1");
        }

        // Insert a donation
        private String insertDonation(String requestBody) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);

            // Use getAsString() and getAsDouble() with null checks
            String donorName = jsonObject.has("donor_name") ? jsonObject.get("donor_name").getAsString() : null;
            Double amount = jsonObject.has("amount") ? jsonObject.get("amount").getAsDouble() : null;
            String email = jsonObject.has("email") ? jsonObject.get("email").getAsString() : null;

            // Validate inputs
            if (donorName == null || amount == null || email == null) {
                return "{\"error\":\"Missing required fields in the request.\"}";
            }

            // Construct the query
            String query = String.format("INSERT INTO donations(donor_name, amount, email) VALUES ('%s', %f, '%s')", donorName, amount, email);
            return executeUpdate(query);
        }


        // Insert feedback
        private String insertFeedback(String requestBody) {
            String[] data = requestBody.split(",");
            String donorName = data[0].split(":")[1].replace("\"", "").trim();
            String email = data[1].split(":")[1].replace("\"", "").trim();
            String feedbackText = data[2].split(":")[1].replace("\"", "").trim();
            String feedbackDate = data[3].split(":")[1].replace("\"", "").trim();

            String query = String.format("INSERT INTO feedback (donor_name, email, feedback_text, feedback_date) VALUES ('%s', '%s', '%s', '%s')",
                    donorName, email, feedbackText, feedbackDate);
            return executeUpdate(query);
        }

        // Execute select query and return results as JSON
        private String fetchDataFromDatabase(String query) {
            String jdbcUrl = "jdbc:mysql://localhost:3306/zoo_donation";
            String username = "root";
            String password = "sudopw";
            StringBuilder jsonBuilder = new StringBuilder();

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                jsonBuilder.append("["); // Start of JSON array
                boolean first = true; // Handle the comma between JSON objects
                while (resultSet.next()) {
                    if (!first) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("{");
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        jsonBuilder.append("\"").append(resultSet.getMetaData().getColumnName(i)).append("\":");
                        jsonBuilder.append("\"").append(resultSet.getString(i)).append("\"");
                        if (i < resultSet.getMetaData().getColumnCount()) {
                            jsonBuilder.append(",");
                        }
                    }
                    jsonBuilder.append("}");
                    first = false;
                }
                jsonBuilder.append("]"); // End of JSON array

                // Debug output
                System.out.println("Query executed: " + query);
                System.out.println("JSON Response: " + jsonBuilder.toString());

                // Return as a JSON object or error message
                if (jsonBuilder.length() > 2) { // 2 accounts for the [] brackets
                    return jsonBuilder.toString(); // Return the constructed JSON string
                } else {
                    return "{\"error\":\"No data found.\"}";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "{\"error\":\"Error occurred while fetching data from the database.\"}";
            }
        }



        // Execute select query and return a single value as JSON
        private String fetchSingleValueFromDatabase(String query) {
            String jdbcUrl = "jdbc:mysql://localhost:3306/zoo_donation";
            String username = "root";
            String password = "sudopw";
            StringBuilder jsonBuilder = new StringBuilder();

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                if (resultSet.next()) {
                    jsonBuilder.append("{");
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        jsonBuilder.append("\"").append(resultSet.getMetaData().getColumnName(i)).append("\":");
                        jsonBuilder.append("\"").append(resultSet.getString(i)).append("\"");
                        if (i < resultSet.getMetaData().getColumnCount()) {
                            jsonBuilder.append(",");
                        }
                    }
                    jsonBuilder.append("}");
                } else {
                    jsonBuilder.append("{\"error\":\"No data found.\"}");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "{\"error\":\"Error occurred while fetching data from the database.\"}";
            }

            return jsonBuilder.toString();
        }

        // Execute update query (for INSERT, UPDATE, DELETE) and return success message
        private String executeUpdate(String query) {
            String jdbcUrl = "jdbc:mysql://localhost:3306/zoo_donation";
            String username = "root";
            String password = "sudopw";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                return "{\"status\":\"success\"}";
            } catch (SQLException e) {
                e.printStackTrace();
                return "{\"error\":\"Error occurred while executing the update.\"}";
            }
        }
    }

    public static void main(String[] args) {
        startServer();
    }
}
