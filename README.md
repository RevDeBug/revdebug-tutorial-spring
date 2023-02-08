# RevDeBug recording of a Java Spring Boot application demo
The following application is an example spring boot application showing the tracing and code execution recording of RevDeBug.
A prerequisite for this follow along tutorial is a working RevDeBug Server instance running, please refer to user manual at: <https://revdebug.gitbook.io/revdebug/installing-revdebug-server#setting-up-revdebug-server-instance>
## Clone the project (Linux/MacOS X machine with Docker installed recommended)

```
git clone https://github.com/RevDeBug/revdebug-tutorial-spring
cd revdebug-tutorial-spring
```

## Build the application using the included Dockerfile

```
docker build --build-arg REVDEBUG_RECORD_SERVER_ADDRESS_ARG=[revdebug_server_address] -t rdb_java_demo .
```

Where ```[revdebug_server_address]``` is your RevDeBug server address (IP address or just the hostname). If you are using TLS on your RevDeBug server, add the ```--build-arg REVDEBUG_TLS=true``` variable.

## Run the built Docker image

```
docker run -d -p 8080:8080 --name rdb_java_demo rdb_java_demo:latest
```

## Use the application to cause its error

Afterwards the application will be accessible through a web browser at: <http://localhost:8080/>

Navigate to Invoices web page.

![Demo application web page](./demo_screens_java/web-app.png)


Select the invoice that came from: Exceptional LLC, access the details of the invoice and press the "Reconcile" link.

![Demo application web page](./demo_screens_java/web-app-details.png)

It will result with an error - you may see a "blank" or an error 500 web page at this time.

# Use RevDeBug to access code execution recording

## Find the errorneous trace
Next, you may switch to a RevDeBug server web interface and navigate to the “Trace” tab, where you may filter the traces to show only errors and limit to application “InvoiceJava”. 

![Demo application web page](./demo_screens_java/trace.png)

There you’ll find the trace for the caused error and you can access the recording of code execution. Accessing the recording for the first time will ask to configure the source code repository address.

## Connect code repository (one time action)

Follow the “connect to repository” option and fill the “Repository address” to github repository location of  ```https://github.com/RevDeBug/revdebug-tutorial-spring``` and press “Save”.

![Demo application web page](./demo_screens_java/repository.png)

## Access the code execution for the error
Closing the inner window and reopening the code recording will present the recording on the source code and allow you to move around the recording timeline using the arrow buttons at top left.

![Demo application web page](./demo_screens_java/code-recording.png)

