# Space-Dodger
#### CMSC 137 1st1617 CD-4L Project
###### Abuel, Edora, Esguerra, Jaafar

* The following commands are for testing. To play the game:
  1.  As the Server

    * For the Game:
    ```
    cd GameCore
	javac GameServer.java 
    java GameServer 
    ```
    * For the Chat:
    ```
    cd GameCore 
  javac ChatServer.java 
  java ChatServer
    ```
    
    * Wait for players to connect to the game. The system will prompt if at least 2 or more players have connected.
      * Y to start game.
      * N to wait for more players.

  2. As a Player
    
    ```
    cd GameCore
    javac Main.java
    java Main <username> <server ip address>
    ```

* There must be at least 2 or more players in order to start the game.
  * To change minimum players, modify the condition in waitForPlayers() in GameServer.java