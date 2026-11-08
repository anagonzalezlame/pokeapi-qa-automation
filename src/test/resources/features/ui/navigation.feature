@ui
Feature: Site navigation
  As a visitor of pokeapi.co
  I want to move between the main sections of the site
  So that I can find the documentation and general information

  Background:
    Given I open the PokeAPI home page

  Scenario Outline: Header links open the right section
    When I click "<link>" in the header
    Then the URL should contain "<path>"
    And the page title should be "<title>"

    Examples:
      | link    | path          | title                   |
      | About   | /about        | About - PokéAPI         |
      | API v2  | /docs/v2      | Documentation - PokéAPI |
      | GraphQL | /docs/graphql | Documentation - PokéAPI |

  Scenario: The call to action takes me to the docs
    When I click the "Check out the docs!" link
    Then the URL should contain "/docs/v2"
    And the page title should be "Documentation - PokéAPI"
