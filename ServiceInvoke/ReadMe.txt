RESTInvokeDB
------------
To run the RESTInvokeDB make sure that the host on which the service is running has a Oracle XE instance running.
The instance contains EMPLOYEES table in the SYSTEM tablespace. The Employee.sql will have the SQL commands for this.
The URL to call the service is -  http://localhost:8083/RESTInvoke?lastname=MENEZES

These properties are changeable depending upon the connector.

SOAPgeoIP
---------
The command to invoke the SOAPgeoIP using CURL would be - 
curl http://localhost:8083/GeoIPSOAP -X POST -d @SOAPRequest.xml --header "Content-Type:application/xml"

The SOAPRequest.xml is part of the project and should be specified with the complete path.


XMLgeoIP
--------
The command to invoke the XMLgeoIP using CURL is -
curl http://localhost:8083/GeoIPXML -X POST -d @XMLRequest.xml --header "Content-Type:application/xml"

The XMLRequest.xml is part of the project and should be specified with the complete path.