#Github benchmark

### Concept
- Read line by line from a file with a list of repositories
- Use HttpUrlConnection API to make simple HTTP call
- Use GSON to parse JSON result and filter for needed field (this implementation counts 
contributions to a repo of a user, it isn't 100% accurate since github
limits the amount of results)
- Execute same HTTP call with a normal loop and once with a CompletableFuture
- Compare Results via System time in ms

### How to run
- Run main method Main.java


Unforunately I couldn't test wheter or not the program runs outside of
the IDE since I couldn't build this project because tests fail
because I was blocked by the GitHub API for too many requests :(