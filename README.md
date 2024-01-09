# Refactor Step 3 - Dice Adapter

## Goal

In order to move our attention to the roll functionality, we need to be able to control the rolling of the dice.
We want to implement this as an adapter as part of our hexagonal architecture.
Here we invert the dependency and look at what the game needs.

The end game should be a Goose Game that is agnostic of the way that dice rolling is implemented.

## Steps done

* Create DiceRollerAdapter + DTO + Test
* Implement Adapter using the DiceRollerService as base and extracting parsing logic from App
* Issue found: dice rolling api has been decommissioned
* Refactor DiceRollerService to use random number generator

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