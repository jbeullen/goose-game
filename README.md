# Refactor Step 1 - Setup for Refactoring

## Goal

Write an integration test that covers a decent chunk of code.
We choose to cover all the 'Create Player' scenarios so we can start initial refactoring.
We try to re-use the existing libraries as much as possible to prevent the introduction of new frameworks or apis.


## Steps done

* Add an integration test to create the first player
* Extend the integration test to cover adding a player with existing nickname
* First bug already found and fixed
* Extend the integration test to cover adding too many players


# Goose Game API
The rule of this classic game are explained [here](https://en.wikipedia.org/wiki/Game_of_the_Goose).

### Add a player to the game
To add a new player to the game, send a POST request like this:

`POST /players`

with a JSON body like `{ "name": "Thijs", "nickname": "goose1"}`

And the response should be something like

`{"id": "95df85f8-e342-4420-8917-187d00870df5", "name": "Thijs", "nickname": "goose1"}`

Game starts when exactly four players join.
Players must have a unique nickname.

### Playing the game

The game will start as soon as four players join the game.

During your turn, you can roll the dice by sending a POST request like this:

`POST /players/95df85f8-e342-4420-8917-187d00870df5/roll`

where `95df85f8-e342-4420-8917-187d00870df5` should be your player id.

The response will contain the roll result, your new position and a message, like this one:

`{"roll":[5, 4], "position":21, "message": "Paolo moves from 12 to 21." }`