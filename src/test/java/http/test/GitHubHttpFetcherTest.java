package http.test;

import http.GithubHttpFetcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GitHubHttpFetcherTest {

    private static  GithubHttpFetcher githubHttpFetcher;
    private final String resourceUri = "src/main/resources/input.txt";

    @BeforeAll
    public static void setup(){
        githubHttpFetcher = new GithubHttpFetcher();
    }

    @Test
    public void testFileReading(){

        List<String> result = githubHttpFetcher.readFromFileAndAssembleList(resourceUri);
        System.out.println(result.get(1));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(4,result.size());
    }

    @Test
    public void testHttpRequestSequential(){
        int result = githubHttpFetcher.countCommitsSequential(githubHttpFetcher.readFromFileAndAssembleList(resourceUri));
        //Spring repository is huge so a simple check gives some idea of whether or not the implementation works
        Assertions.assertTrue(result > 10000);

    }

    @Test
    public void testHttpRequestCompleteableFuture() {
        long result = githubHttpFetcher.countCommitsParalellStream(githubHttpFetcher.readFromFileAndAssembleList(resourceUri));
        System.out.println(result);
        Assertions.assertTrue(true);
    }
}
