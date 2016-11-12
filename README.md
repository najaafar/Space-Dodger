# Space-Dodger
#### CMSC 137 1st1617 CD-4L Project
###### Abuel, Edora, Esguerra, Jaafar

* The following commands are for testing. To play the game:
  1.  As the Server

    ```
    cd GameCore
    java GameServer
    ```
    * Wait for players to connect to the game. The system will prompt if 3 or more players have connected.
      * Y to start game.
      * N to wait for more players.

  2. As a Player
    * Change string host in GameCore/Main.java to server's current IP address
    ```
    cd GameCore
    javac Main.java
    java Main <username>
    ```

* There must be 3 or more players in order to start the game.
  * To change minimum players, modify the condition in waitForPlayers() in GameServer.java