Hello Spring Cloud
============

A Cloud Foundry Spring application that connects to IBM Compose MySQL over SSL.

## Configure

- Add your Compose MySQL TRUSTED_CA_CERTIFICATE to manifest.yml
- Add your Compose MySQL URL and Credentials to src/main/resources/application.yml

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

