@CentralServer
@CertificationService
Feature: CS: System Settings -> Api Keys

  Background:
    Given CentralServer login page is open
    And Page is prepared to be tested
    And User xrd logs in to CentralServer with password secret
    And CentralServer Settings tab is selected
    And API Keys sub-tab is selected

  Scenario: API key creation wizard is working and key is created
    Given Create API key button is clicked
    And Create API key wizard next button status is disabled
    When Role "Security Officer" is being clicked
    Then Create API key wizard next button status is enabled

    When Create API key wizard next button is clicked
    And Create API key wizard Previous button is clicked
    And Create API key wizard next button is clicked
    And Create API key wizard Create Key button is clicked
    Then API key is created and visible

    When Create API key wizard Finish button is clicked
    Then Newly created API key is present in the list

  Scenario: API key is created and revoked
    Given Create API key button is clicked
    When Role "Registration Officer" is being clicked
    When Create API key wizard next button is clicked
    And Create API key wizard Create Key button is clicked
    Then API key is created and visible
    When Create API key wizard Finish button is clicked
    Then Newly created API key is present in the list
    When Newly created API key is revoked
    Then Newly created API key is missing in the list

  Scenario: API key is created and edited
    Given Create API key button is clicked
    When Role "Registration Officer" is being clicked
    When Create API key wizard next button is clicked
    And Create API key wizard Create Key button is clicked
    Then API key is created and visible
    When Create API key wizard Finish button is clicked
    Then Newly created API key is present in the list
    When Newly created API key is edit dialog is opened
    And Role "System Administrator" is being clicked
    And Role "Security Officer" is being clicked
    And Role "Registration Officer" is being clicked
    And Dialog Save button is clicked
    Then Newly created API key is present in the list and has roles
      | $role                | $condition |
      | System Administrator | present    |
      | Security Officer     | present    |
      | Registration Officer | missing    |




