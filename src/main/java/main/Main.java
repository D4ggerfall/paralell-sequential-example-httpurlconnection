package main;

import http.GithubHttpFetcher;

import java.util.List;

public class Main {


    public static void main(String[] args){
        String resourceUri = "src/main/resources/input.txt";
        GithubHttpFetcher githubHttpFetcher = new GithubHttpFetcher();
        List<String> urls = githubHttpFetcher.readFromFileAndAssembleList(resourceUri);

        long before1 = System.currentTimeMillis();
        githubHttpFetcher.countCommitsSequential(urls);
        long after1 = System.currentTimeMillis();
        long result1 = after1 - before1;
        System.out.println("Sequential execution time:" + result1 + "ms");


        long before2 = System.currentTimeMillis();
        githubHttpFetcher.countCommitsCompletableFuture(urls);
        long after2 = System.currentTimeMillis();
        long result2 = after2 - before2;
        System.out.println("CompletableFuture execution time:" + result2 + "ms");

    }
}
