# Refactor Step 3 - Roll Functionality

## Goal

We want to tackle the roll functionality of the application.

In order to make sure we don't break the existing application, we start by extending the integration test.
A simulation of a full game is done to cover all scenarios.

Again we will separate the Spark Code and Domain Logic, by extracting that what we need from App, letting the IDE
the heavy lifting.
We identify the different scenarios, write a BDD test and implement the behaviour using the extracted code as a starting
point.

The integration test can be used to verify the Spark API still works as designed.

## Steps done

* Factor out the players list in App by accessing the list from GooseGame ( Forgot to this in step 2)
* Extend the integration test to simulate a full game including error scenarios
* Separate the Spark API code from the game logic using IDE shortcuts

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