@ui
Feature: "Try it now!" API explorer on pokeapi.co
  As a developer who is new to PokeAPI
  I want to try requests from the home page
  So that I can see real responses before writing any code

  Background:
    Given I open the PokeAPI home page

  @smoke
  Scenario: The home page shows the API explorer
    Then the page title should be "PokéAPI"
    And I should see the "Try it now!" heading

  @smoke
  Scenario: Search a resource from the explorer
    When I search the resource "pokemon/pikachu"
    Then I should see the result message "Resource for pikachu"
    And the direct link should point to "https://pokeapi.co/api/v2/pokemon/pikachu"

  Scenario Outline: The suggested hints load their resource
    When I click the hint "<hint>"
    Then I should see the result message "Resource for <name>"

    Examples:
      | hint                      | name         |
      | type/3                    | flying       |
      | pokemon-species/aegislash | aegislash    |
      | ability/battle-armor      | battle-armor |

  @negative
  Scenario: Search a resource that does not exist
    When I search the resource "pokemon/missingno-2026"
    Then I should see the result message "Resource not found"
