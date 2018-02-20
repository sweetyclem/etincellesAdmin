# etincellesAdmin
This website is the admin side of a community website that contains a directory of members, a google calendar integration, a blog and an about us page. This app allows the creation and update of users and posts.

To use this project, clone it then open Eclipse (or another editor) and click import > existing maven project.
You need to create an application.properties file for the project to work that needs to be located at src/main/resources/ and have this format : https://github.com/sweetyclem/etincelles/blob/master/application.properties.sample 

Then you can right cick on the project and select "run as" > "spring boot app" and your app will be launched on http://localhost:8080

To package it into a .jar, navigate to the folder in your terminal (you need to be in the foler that contains the pom.xml file) and use the command "mvn package"

To work properly, this application goes with the client one you will find here https://github.com/sweetyclem/etincelles

