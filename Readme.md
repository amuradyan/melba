# Melba

A simple notes application.


## REST API
You can find the API docs [here](https://documenter.getpostman.com/view/749450/SVfRtnrL?version=latest)


## Build and deploy
Due to lack of time this is not implemented. What I do in such cases id create a fat jar using the shadow plugin


## Present functionality

* User creation
* Note CRUD


# Build and deploy

To build a "fat jar" do the following
> ./gradlew shadowJar

The line above will generate a jar in _build/libs_ named _melba-1.0-all.jar_. To deploy it do the following
> java -jar melba-1.0-all.jar


## Missing functionality

* Tests (REST and unit)
