@api @negative
Feature: Requests for Pokemon that do not exist
  As a developer
  I want the API to answer clearly when a Pokemon does not exist
  So that my app can show a proper "not found" message

  Scenario Outline: Requesting a Pokemon that does not exist returns 404
    When I request the pokemon "<value>"
    Then the response status code should be 404

    Examples:
      | value          | case                 |
      | missingno-2026 | unknown name         |
      | 99999          | id out of range      |
      | -1             | negative id          |
