# Project description

Project with automated test using java + gradle + selenium + testNG + allure to test adding a new computer by filling the form on the page 
"http://computer-database.gatling.io/computers/new" and verifying its appearance in the table with all registered
computers

# How to run test?
1. Download project using git or zip
2. Install Java 8
3. Open command line in the project's folder (containing gradle.build)
4. Execute commands: 
   
   ./gradlew -i clean build   - to build the project
   
   ./gradlew -i runTests      - to run the tests

   ./gradlew allureReport      - to generate report

   ./gradlew allureServe       - to open generated Allure report

