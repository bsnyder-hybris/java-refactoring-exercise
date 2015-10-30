Java Refactoring Exercise 
=========================

**Please, before starting this exercise, read through all of the instructions carefully!**

Introduction
------------

This project is used within the hybris software hiring process to determine knowledge of Java/Spring best practices and refactoring.

The idea of this exercise is to evaluate your ability to identify poor coding practices and improve the code through the use of best practices.

The main project is a very basic user management application. We are not looking to add any supplementary features, instead we are verifying the following items:

* Your knowledge of REST
* Your knowledge of Maven
* Your knowledge of Spring
* Your ability to identify and refactor poor Java code
* Your ability to identify and fix bugs
* Your ability to apply proven design principles
* Your ability to write useful and effective tests

Feel free to modify whatever you want! :)

Prerequisites
-------------

* You must have a Github account. If you don't have one, please create one via the [Github website](http://github.com/).

* This repo uses Git for source control management (SCM). If you don't already have the git utility already installed on your machine you will need to install it. To do so, check out the [git downloads page](http://git-scm.com/downloads).

* To build this project you must use Maven 3.x. If you do not have Maven already installed on your machine you will need to install it. To do so, check out the [Maven downloads](http://maven.apache.org/download.cgi).

* For deployment you can use whatever web container/application server you prefer. We use [Tomcat 7.0.26](http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.26/bin/) to validate that the application will start up correctly.

Instructions
------------

1. Clone this repo from Github to your local machine 
1. At the project root directory, run the following command from the command-line:
    `$ mvn package`
1. Once the previous step completes successfully, the build should have run successfully and every test should be green
1. Now perform the refactoring you deem necessary by using your knowledge of Java/Spring best practices. Remember that this includes both code and tests. Also, please feel free to innovate!
1. Please make sure that your code compiles and that all tests are green when you are done
1. When you are finished please commit your code on your local machine and then [create a patch using git](http://git-scm.com/docs/git-format-patch). Please DO NOT create a pull request against this repo.
1. The final step is to send an email to your contact at hybris software to inform this person that you have completed the exercise. Please make sure to attach a copy of the patch containing your changes!

Business Requirements
---------------------

* The user's email is a unique identifier and should be handled accordingly.
* A user should have at least one role.

Tips
----

* Unit tests != integration tests
* Spring dependency is provided, feel free to use it
* Don't be afraid to import additional dependencies if you think you need them
* Remember that you will have to handle concurrent requests
* Your final architecture should be portable, extensible and easily maintainable

Good luck!

