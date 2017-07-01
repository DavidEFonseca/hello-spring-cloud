Hello Spring Cloud
============

A Cloud Foundry Spring application that connects to IBM Compose MySQL over SSL.

## Building the application

Use Maven to build the application:

~~~
$ mvn clean package
~~~

## Running the application on Cloud Foundry

To run the application on Cloud Foundry, first target and long into a Cloud Foundry environment, then run this command:

~~~
$ cf push
~~~

The application will be deployed using settings in the provided `manifest.yml` file. The output from the command will show the URL that has been assigned to the application. Browse to the provided URL to view information about the application.

## SSL Certificates

Compose SSL Certificates need to be loaded into the Cloud Foundry runtime on Bluemix.  This is performed automatically by adding the following to your pom.xml:

```
<repositories>
   <repository>
     <id>jitpack.io</id>
     <url>https://jitpack.io</url>
   </repository>
</repositories>

<dependency>
   <groupId>com.github.snowch</groupId>
   <artifactId>spring-boot-ssl-truststore-gen</artifactId>
   <version>master</version>
</dependency>
```
