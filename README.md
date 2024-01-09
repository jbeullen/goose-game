# Refactor Step 2 - Refactor Create Player functionality

## Goal

We want to achieve a hexagonal design where Rest API and Domain Layer are seperated.
The End Game is a Goose Game that is ported to the Sparks framework that turns it into an API.
On the other side we want to create an Adapter for Rolling the dice.
This will improve readability but also make it more extendable.

The Domain Layer should contain only queries and actions.
We expose only what is needed.
We want to start introducing immutability wherever we can.

All new code should be under test.
For the Domain Layer tests should behaviour driven, so we can always refactor the code as we see fit and
can really focus on the features themselves.

We should watch out that we don't reinvent the wheel.
Extract the existing logic when implementing the new Domain Layer, letting the IDE do the heavy lifting.
A good starting point is to take a copy of the code in App and remove all the Spark references.

## Steps done

* Introduce immutability: IDE plugins (e.g. Save Actions) + Fix existing code as we go
* Add assertj api for advanced assert support
* Update the Player class: add constructor from primitives
* Create GooseGame domain API + Exception + Impl + Tests
* Implement Domain API case by case using TDD/BDD
* Refactor App by implementing the domain API.
* Clean up dead code

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