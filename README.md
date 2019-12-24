# How to compile and run the code:
## Environment:
terminal
## procedure:
- First, open folder `./src`
- Second, open a terminal there
- Third, compile `Server.java`, using the command `javac Server.java`
- Fourth, compile `Client.java`, using the command `javac Client.java`
- Fifth, run the `Server.class`, using command `java Server`
- Finally, open two new terminal there and run `Client.class` on each of them, using command `java Client`

## Notices:

1. Server communicates with clients solely by socket connection, they do not share information via joint files. Actually, you can put them in different addresses, the program will still work. But for simplicity, I just put them under the same folder. 
(All files except `Client.java` in `./src` is for Server use. `Client.java` is independent.)
2. Server can continuously support rounds of game. You do not need to terminate it every time one round of game ends. However, Client will automatically close when there is a result. Start Client again each time!

# Supported information(optional):
## protocol:
### Server ->Client:
`M|string`  message that indicates the death of the game, client only needs to slice the ..... part
`E|int`  message that indicates indicates the result after each move
`U|int|int|int`  message that instructs client to change the chess

### Client ->Server:
`int|int`  tell which position was the chess placed
`Name input.` a string that indicates the player has input his name