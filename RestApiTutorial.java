import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

public class RestApiTutorial {
   static String  API_KEY ="5ecd1456a40e446d93bbe135500fb491";
    public static void main(String[] args) throws Exception {
        Transcript transcript = new Transcript();
        transcript.setAudio_url("https://bit.ly/3yxKEIY");
        Gson gson = new Gson();
        String jsonRequest=gson.toJson(transcript);
        System.out.println(jsonRequest);
        HttpRequest postRequest= HttpRequest.newBuilder().uri(new URI("https://api.assemblyai.com/v2/transcript")).header("Authorization",API_KEY).POST(HttpRequest.BodyPublishers.ofString(jsonRequest)).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> postResponse= httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
       System.out.println(postResponse.body());
       transcript= gson.fromJson(postResponse.body(),Transcript.class);
       System.out.println(transcript.getId());
       HttpRequest getRequest= HttpRequest.newBuilder().uri(new URI("https://api.assemblyai.com/v2/transcript/"+ transcript.getId())).header("Authorization",API_KEY).GET().build();
        while(true) {
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);
            System.out.println(transcript.getStatus());
            if(transcript.getStatus().equals("completed") || transcript.getStatus().equals("error"))
            {
                break;
            }
            Thread.sleep(1000);
        }
        System.out.println(transcript.getText());
    }
}
