package http;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GithubHttpFetcher {

    private final String gitHubBaseUrl = "https://api.github.com/repos/";
    private HttpURLConnection connection;
    private ArrayList<Integer> resultList = new ArrayList<>();


    public GithubHttpFetcher(){

    }

    public List<String> readFromFileAndAssembleList(String uri){
        List<String> urls = new ArrayList<String>();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(uri));
            String currentLine = reader.readLine();
            while (currentLine != null){
                urls.add(gitHubBaseUrl + currentLine + "/contributors");
                currentLine = reader.readLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return urls;
    }

    public int countCommitsSequential(List<String> urls){
        int sum = 0;

        for (String currentUrl:urls){
            sum += doHttpRequestAndCountCommits(currentUrl);
        }
        connection.disconnect();
        return sum;
    }

    public int countCommitsCompletableFuture(List<String> urls){
        resultList.clear();

        for(String url:urls){
            try {
                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> doHttpRequestAndCountCommits(url));
                addToResults(future.get());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return resultList.stream().parallel().mapToInt(value -> value).sum();
    }


    //Doesn't really serve much of a purpose, here to play around with completeablefuture
    private void addToResults(Integer toAdd){
        resultList.add(toAdd);
    }


    private int doHttpRequestAndCountCommits(String url){
        int sum = 0;
        try {
            //Build a connection for url
            URL connectionUrl = new URL(url);
            connection = (HttpURLConnection) connectionUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            //Read response in opened connection
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //Parse JSON response into array and iterate through it
            JsonArray jsonArray = new JsonParser().parse(in.readLine()).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                //Parse Current field of array into JSON Object and count only contributions field (=commits)
                //Doesn't actually count all commits since in this case github limits result list
                sum += Integer.parseInt(jsonArray.get(i).getAsJsonObject().get("contributions").getAsString());
            }
            in.close();
        }
        catch (MalformedURLException e){
            throw new RuntimeException("Error with specified URL", e);
        }
        catch (IOException e){
            throw new RuntimeException("Error while reading response", e);
        }
        finally {
            connection.disconnect();
        }


        return sum;
    }


}
