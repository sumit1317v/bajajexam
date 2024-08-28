package demops_1;


import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Random;

public class DestinationHashGenerator {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN> <file_path>");
            return;
        }

        String prn = args[0].toLowerCase().replace(" ", "");
        String filePath = args[1];
        
        // Read and parse JSON file
        FileReader reader = new FileReader(filePath);
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
        
        // Find the first "destination" key
        String destinationValue = findDestinationValue(jsonObject);
        if (destinationValue == null) {
            System.out.println("Destination key not found");
            return;
        }

        // Generate a random 8-character string
        String randomString = generateRandomString(8);
        
        // Concatenate PRN, destination value, and random string
        String concatenated = prn + destinationValue + randomString;
        
        // Generate MD5 hash
        String hash = md5Hash(concatenated);
        
        // Output the result
        System.out.println(hash + ";" + randomString);
    }

    private static String findDestinationValue(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (key.equals("destination")) {
                return value.toString();
            } else if (value instanceof JSONObject) {
                String result = findDestinationValue((JSONObject) value);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static String md5Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}