# Connect4

## Summary
This is an implementation of the [Connect Four game](https://en.wikipedia.org/wiki/Connect_Four).\
It can be run interactively via the command-line, or used programmatically.

## Build & Run
To build and run the game, call:

```
mvn package
java -cp target/com.rakadjiev.connect4-1.0-SNAPSHOT.jar com.rakadjiev.connect4.impl.Connect4
```

The only dependency is JUnit for the tests.
Java 8 or higher is required.

## Play
The game consists of a board and players, who take turns in inserting discs into columns of the board. Each player has a specific disc color they play with.\
The discs are inserted from the top of the board, and fall to the first empty space in the column.\
The game ends if one of the players connects the required number of their own discs, or if the board fills up.\
In the standard version, there are 2 players, the board has 7 columns and 6 rows. and 4 connected discs are required to win. 