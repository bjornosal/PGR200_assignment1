# PGR200 - Assignment 1

## Introduction

Assignment 1 for the class PGR200 at Westerdals Oslo ACT. 

For this assignment we were tasked to set up a database connection through JDBC. 
A user should be able to query the database for at least subjects. 

We were free to expand on the assignment, with the possible goal being for a user 
to generate the perfect schedule for a school based on a set of rules that we decide upon.

My solution for the assignment gives the user the possibility of connecting to the database
using either a default set of properties, or setting his/her own properties based on input. 

Afterwards, the user will be able to query the database for information regarding one of three tables.
The user will also be able to use his own information based on a file, and the user can 
change where the application will look for the file. 

As the solution is dynamic in regards to the amount of columns a table can have and what the values are,
there is a specific format that the file has to follow, or it will be unable to parse the file correctly. 

## Starting the application
To get started using the application, make sure that you have your local server started. 
There is no web alternative for the application as this is being written. 

After your local server is started, make sure that you have your files in place, using the format 
below. 

Once that is in order, just run the App class, and you follow the instructions from the program.


### File format requirements

Description of file:
* First line: tableName;columnCount;primaryKeys;foreignKeys;ForeignKeyReferences(Table(column))
* Second line: column names separated by ';' 
* Third line: MySQL values separated by ';', end with column name of primary key 
* Fourth line: Display Names  separated with ';'
* Fifth and further lines: Insertion values 

See example below: 

* First Line: Subject;5;1;0
* Second line: code;name;attending_students;teaching_form;duration
* Third line: varchar(6);VARCHAR(75) UNIQUE NOT NULL;INT(6);VARCHAR(50) NOT NULL;DECIMAL(11);code
* Fourth line: Code;Name;Attending Students;Teaching Form;Duration
* Fifth line: PGR200;Avansert Javaprogrammering;65;sequential;4.0
* Sixth line: PGR100;Objektorientert Programmering;215;sequential;4.0
* Seventh line: PRO100;Kreativt Webprosjekt;215;sequential;4.0
* Eight line: PRO101;Webprosjekt;199;sequential;4.0

To make file formatting easier, write the file in Excel and save as .csv. 
This will handle everything related to separation of fields. 

Using the example below, the table will be as follows:


| code          | name                          | attending_students  | teaching_form | duration |
| ------------- |-------------------------------| -------------------:|---------------|--------- |
| PGR200        | Avansert Javaprogrammering    |                  65 |    sequential |      4.0 |         
| PGR100        | Objektorientert Programmering |                 215 |    sequential |      4.0 |           
| PRO100        | Kreativt Webprosjekt          |                 215 |    sequential |      4.0 |          
| PRO101        | Webprosjekt                   |                 199 |    sequential |      4.0 |          


### Generate Javadoc Documentation
To generate a complete javadoc documentation for the application, run the Maven plugin "javadoc"
either through your IDE or run the command *__mvn javadoc:javadoc javadoc:aggregate__*


### Generate Code Coverage Report

To generate a coverage report for the application; run the following maven command: 
*__mvn clean test jacoco:prepare-agent jacoco:report.__* This should generate a file under target/site/jacoco named index.html.
Open the file in any browser and go through the coverage report. 

#### Testing
Prior to testing, check the testDatabaseLogin.properties file. 
Make sure that the information is correct to be able to test.