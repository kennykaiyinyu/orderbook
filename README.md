orderbook
=======

- To bulid the program, 
  - open a command prompt.
  - `CD` to the project's root.
  - Run the following 
  ```
  ./gradlew build
  ```
- To create a intellij project, 
  - still, in the command prompt after building
  - Run the following 
  ```
  ./gradlew idea
  ```

Future Improvements
======
- More test cases coverage as time goes on;
- There should be a queue of executions retrievable from the order book.
- The 'synchronized' methods could be replaced by re-entrant read/write locks for better performance under concurrency.
