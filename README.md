# Xceptance REST Test (XRT)

## Deprecation Notice
This project has been retired. A new and cleaner as well as more performant approach can be found here: https://github.com/Xceptance/rest-load-test-suite

## What is XRT?
XRT is a REST API testing tool based on Xceptance Load Test (XLT). It can be used for functional tests as well as for load testing REST APIs. 
 
XRT ships without UI. All tests are implemented in Java as JUnit tests. This allows a good integration into any CI process.
 
## Usage

The main functionality of XRT is located in one class: **com.xceptance.xrt.RESTCall**. The simplest configuration you can do is as follows:
 
```
    // Call the provided URL with HTTP method GET
    new RESTCall( "www.anyserver.com/rest/api/my_resource" ).get();
```
 
**RESTCall** comes with a lot of setters to configure your REST call. If you provide an URL (see example above) XRT analyzes the URL and you can manipulate the URL via setters:
 
```
    // Provide URL
    RESTCall call = new RESTCall( "www.anyserver.com/rest/api/my_resource" );
    
    // Add a query parameter, port, and HTTP method and change host name
    call.addQueryParam( "param", "value" ).setPort( 8080 ).setHostName( "www.differentserver.com" ).setHttpMethod( HttpMethod.POST );
    
    // Do the REST call
    call.process();
```

The REST call above calls the following URL with HTTP method **POST**:

```
    www.differentserver.com:8080/rest/api/my_resource?param=value
```

As you can see in the examples, every setter method of **RESTCall** returns the updated instance of the current **RESTCall** object. This allows fluent programming and is a concise way of configuring the REST call.

## Compile

XRT is a Maven project and can be compiled with it. The usual steps are:

```
   user@machine:~/dev$ git clone git@github.com:Xceptance/XRT-Xceptance-REST-Test.git
   user@machine:~/dev$ mvn install
```

The commands above will pull the latest version of XRT and Maven will install the ready JAR into your .m2 repository. From there you can use XRT as usual in any other maven project. Of course you can also import the JAR directly into your Java application.

## Documentation

XRT also provides java doc and some other documentation dealing with concepts that are not mentioned in this README file, e.g. templating and default validation. You can generate it via Maven as follows:

```
   user@machine:~/dev$ git clone git@github.com:Xceptance/XRT-Xceptance-REST-Test.git
   user@machine:~/dev$ mvn site
```
