package chicken.butt;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Health check script for Coolify
 */
public class HealthCheck {
    public static void main(String[] args) {
        File file = new File("tmp/heartbeat");
        
        // 1. If the bot is actively running and updating the file locally, we are healthy.
        if (file.exists() && (System.currentTimeMillis() - file.lastModified() < 60000)) {
            System.exit(0);
        }
        
        // 2. The heartbeat file is stale. Let's see if Discord itself is down.
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();
                    
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://discordstatus.com/api/v2/status.json"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            // Check if Discord Status page reports an active major or critical incident
            if (body.contains("\"indicator\":\"major\"") || body.contains("\"indicator\":\"critical\"")) {
                System.out.println("Discord API is experiencing an outage. Skipping container restart.");
                System.exit(0); // Fake a healthy exit so Coolify doesn't loop reboot
            }
            
        } catch (Exception e) {
            // If the status page itself times out or fails to load, assume a massive network 
            // or Cloudflare routing issue. Safe to skip the restart to avoid loop cycles.
            System.out.println("Could not reach Discord Status API. Assuming network outage and skipping restart.");
            System.exit(0); 
        }

        // 3. Discord is completely operational, but our bot stopped ticking.
        // This confirms a local software deadlock or OOM issue. Force a restart.
        System.err.println("Discord is operational but bot heartbeat is stale. Initiating restart.");
        System.exit(1);
    }
}