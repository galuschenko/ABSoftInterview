# Project description

Project with automated test using java + gradle + selenium + testNG + allure to test adding a new computer by filling the form on the page 
"http://computer-database.gatling.io/computers/new" and verifying its appearance in the table with all registered
computers

# How to run test?
Default OS - Windows (for changing you need to configure the  file environment.properties)
1. Download project using git or zip
2. Install Java 8
3. Open command line in the project's folder (containing gradle.build)
4. Install Firefox (s
5. Execute commands: 
   
   - to build the project
  
         ./gradlew -i clean build 
 
   - to run the tests
   
         ./gradlew -i runTests   

   - to generate report
   
         ./gradlew allureReport      

   - to open generated Allure report

          ./gradlew allureServe


If you want to change gefault settings (Change OS, browser, input data - reconfigure the file /src/test/resources/config/environment.properties)