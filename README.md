# Project description

Project with Automated Testing Framework to test adding a new computer by filling the form on the page 
"http://computer-database.gatling.io/computers/new" and verifying its appearance in the table with all registered
computers

# How to run tests?
1. Download project using git or zip
2. Install Java 8
3. Open command line in the project's folder (containing gradle.build)
4. Execute commands: 
   
   ./gradlew -i clean build   - for building the project
   
   ./gradlew -i runTests      - for running the tests

   ./gradlew allureReport      - for formatting report

   ./gradlew allureServe       - for introducing results

