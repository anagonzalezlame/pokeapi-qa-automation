@api
Feature: Pokemon list pagination
  As a developer
  I want to browse the Pokemon list page by page
  So that I don't have to download every Pokemon at once

  @smoke
  Scenario: The first page has no previous link
    When I request the pokemon list with limit 5 and offset 0
    Then the response status code should be 200
    And the list should contain 5 results
    And the total count should be greater than 1000
    And the first result should be "bulbasaur"
    And the "next" page link should be present
    And the "previous" page link should be empty

  Scenario: The second page continues where the first one ended
    When I request the pokemon list with limit 5 and offset 5
    Then the response status code should be 200
    And the list should contain 5 results
    And the first result should be "charizard"
    And the "previous" page link should be present
