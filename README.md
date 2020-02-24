# customer-statement-validator

This service will validate customer statement records and will generate the reports of failed ones.

Prerequisite : The machine should have 'Java8' and Maven 3.6.0 or grater

Step 1 : Build Code -> mvn clean install

Step 2 : Boot the application using the command -> mvn spring-boot:run

Step 3 : Access the swagger UI using http://localhost:8110/swagger-ui.html

Step 4 : Expand 'Statement Upload Controller', click on the 'POST' end point bar and finally click on 'Try it out' 

Step 5 : Then upload the sample files placed at folder -> src/test/resources using 'Choose File' from various folders like csv,text,xml and pdf

Step 6 : Click 'Execute' button

Step 7 : Check the 'Server Response' portion to check the report of failed record details. 

