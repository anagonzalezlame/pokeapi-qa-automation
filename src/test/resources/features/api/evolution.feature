@api
Feature: Evolution chains
  As a developer
  I want to follow the link from a species to its evolution chain
  So that I can show how a Pokemon evolves

  Scenario Outline: Follow a species to its evolution chain
    Given I request the species "<species>"
    When I follow the species evolution chain
    Then the response status code should be 200
    And the evolution chain should be "<chain>"

    Examples:
      | species    | chain                                |
      | charmander | charmander > charmeleon > charizard  |
      | squirtle   | squirtle > wartortle > blastoise     |
      | pichu      | pichu > pikachu > raichu             |
