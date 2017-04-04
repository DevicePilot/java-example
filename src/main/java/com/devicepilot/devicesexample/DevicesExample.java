package com.devicepilot.devicesexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DevicesExample {

    public static void main(String[] args) {
        try {
            configureJsonResClient();
            tutorialDevices();
        } catch (IOException | UnirestException e) {
            e.printStackTrace();
        }
    }

    private static void tutorialDevices() throws UnirestException, IOException {
        System.out.println("Java example of the DevicePilot tutorial.");
        System.out.println("See https://app-development.devicepilot.com/#/tutorial/devices for more information.");

        System.out.println("Please type in your API key, found at: https://app.devicepilot.com/#/settings\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String apiKey = reader.readLine();
        Unirest.setDefaultHeader("Authorization", "TOKEN " + apiKey); // Authorize using token.

        DeviceRecord record = new DeviceRecord("1", 51.503530, -0.127560, "My sensor", 22.4);
        String urn = createADeviceInDevicePilot(record);
        System.out.printf("The device you created (urn: %s) is now visible in the view tab.\nPress any key to continue.\n", urn);
        System.in.read();

        record.setTemperature(10.1);
        updateYourDevice(record);
        System.out.println("The new device now has an updated temperature; visible in the view tab.");
        System.out.println("Press any key to continue.");
        System.in.read();

        deleteADevice(urn);
        System.out.println("The created device has been deleted; visible in the view tab.");
        System.out.println("Press any key to complete.");
        System.in.read();
    }

    private static String createADeviceInDevicePilot(DeviceRecord record) throws UnirestException {
        // To create a device record in DevicePilot, make an POST to the /devices endpoint
        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://api.devicepilot.com/devices")
                .body(record)
                .asJson();

        return jsonResponse.getBody().getObject().getString("$urn");
    }

    private static void updateYourDevice(DeviceRecord record) throws UnirestException {
        // To update a device, send another POST request similar to above the /devices endpoint with your device $id in the body and the properties to update.
        Unirest.post("https://api.devicepilot.com/devices")
                .body(record)
                .asJson();
    }

    private static void deleteADevice(String urn) throws UnirestException {
        // To delete a device entirely from DevicePilot, simply call the DELETE method on the $urn.
        Unirest.delete("https://api.devicepilot.com" + /* /devices/ is included in urn */ urn)
                .asString();
    }

    private static void configureJsonResClient() {
        // Java http client configuration; not specific to DevicePilot.
        // See http://unirest.io/java.html for unirest documentation.
        Unirest.setDefaultHeader("Content-Type", "application/json");
        Unirest.setDefaultHeader("accept", "application/json");

        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
