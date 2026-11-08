@api
Feature: Get a Pokemon from the API
  As a developer building an app on top of PokeAPI
  I want to get a Pokemon by its name or its id
  So that I can show accurate data to my users

  @smoke
  Scenario: Get a Pokemon by name
    When I request the pokemon "pikachu"
    Then the response status code should be 200
    And the response should be in JSON format
    And the response field "name" should be "pikachu"
    And the response field "id" should be the number 25
    And the pokemon should have the type "electric"

  Scenario Outline: Get a Pokemon by id returns the expected name
    When I request the pokemon "<id>"
    Then the response status code should be 200
    And the response field "name" should be "<name>"

    Examples:
      | id  | name      |
      | 1   | bulbasaur |
      | 25  | pikachu   |
      | 132 | ditto     |
      | 150 | mewtwo    |

  Scenario: Ditto has exactly two abilities
    When I request the pokemon "ditto"
    Then the response status code should be 200
    And the pokemon should have exactly these abilities:
      | limber   |
      | imposter |

  @contract
  Scenario: The Pokemon response follows the expected contract
    When I request the pokemon "bulbasaur"
    Then the response status code should be 200
    And the response body should match the "pokemon" schema

  @performance
  Scenario: The Pokemon endpoint answers in a reasonable time
    When I request the pokemon "charizard"
    Then the response status code should be 200
    And the response time should be under 3000 milliseconds
