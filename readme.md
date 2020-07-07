# Inventory Application Readme

### Description
This is the implementation of Inventory Application

### Requirement
1. An Inventory has name, category, sub-category, quantity information
2. The system supported to create Inventory, update the quantity and browse the Inventory records
3. There are some validation rules when creating an inventory, eg. a sub-category "Shoe" should not in category "Food", or sub-category "Cake" should not in category "Clothes".
4. Additional features supported which not mention in above will be treated as bonus

### Technical Stack
Java 8

### Installation
Import Maven project with pom.xml file.

### API (Swagger-UI)
Go to http://localhost:8080/swagger-ui.html
 
### Test cases and code coverage
 - Unit test for controllers and services by mocking
 - 80% Code coverage 
 

### Improvements
 - Increase the code coverage to 100%
 - Add update/delete features for Category/Sub-category
 - Add delete features for Inventory
 - Add browse inventory records by category/sub-category
 - Store quantity change history
 - Security 
  