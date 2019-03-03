package http.test;

import http.GithubHttpFetcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3,result.size());
    }

    @Test
    public void testHttpRequestSequential(){
        int result = githubHttpFetcher.countCommitsSequential(githubHttpFetcher.readFromFileAndAssembleList(resourceUri));
        //Spring repository is huge so a simple check gives some idea of whether or not the implementation works
        Assertions.assertTrue(result > 10000);

    }

    @Test
    public void testHttpRequestCompleteableFuture() {
        int result = githubHttpFetcher.countCommitsCompletableFuture(githubHttpFetcher.readFromFileAndAssembleList(resourceUri));
        Assertions.assertTrue(result > 10000);
    }

    @Test
    public void testExecutionsYieldSameResult(){
        int res1 = githubHttpFetcher.countCommitsSequential(githubHttpFetcher.readFromFileAndAssembleList(resourceUri));
        int res2 = githubHttpFetcher.countCommitsCompletableFuture(githubHttpFetcher.readFromFileAndAssembleList(resourceUri));
        Assertions.assertEquals(res1,res2);
    }

}
