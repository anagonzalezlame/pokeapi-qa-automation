@api
Feature: Type damage relations
  As a player planning a battle team
  I want to know which types are strong or weak against each other
  So that I can choose the right Pokemon

  Scenario: Fire type strengths
    When I request the type "fire"
    Then the response status code should be 200
    And the type should deal double damage to:
      | grass |
      | ice   |
      | bug   |
      | steel |

  Scenario: Fire type weaknesses
    When I request the type "fire"
    Then the response status code should be 200
    And the type should take double damage from:
      | water  |
      | ground |
      | rock   |
