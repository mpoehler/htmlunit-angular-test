htmlunit-angular-test
=====================

simple maven driven project to test angular apps with HtmlUnit

Currently there are just two testcases that demonstrate that angular 1.2.26 is working with HtmlUnit 2.16-SNAPSHOT and jQuery 1.8.3. And that it doesn't work with jquery 1.11.1.

mvn verify
----------

Use of "mvn verify" will start a local jetty and execute the two JUnit Testcases AngularApp1Test and AngularApp2Test. You will see the error in the output.
 
local testing
-------------

Import the maven project in your favorite IDE and start the local jetty from the commandline with "mvn jetty:run". 
Than you can execute the testcases from your IDE.
