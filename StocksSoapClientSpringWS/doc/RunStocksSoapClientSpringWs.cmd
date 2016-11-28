@ECHO OFF
setlocal
title Stock Ticker SOAP Client using Spring Web Services

rem StocksSoapClientSpringWs is designed to select stock symbols from the database
rem and iterate through the result set, submitting requests to the backend SOAP
rem service (https://DELL-I5:8002/Stockticker/)



rem Change these to reflect your environment

set JAVA_HOME=<PATH TO YOUR JDK>
set KEYSTORE=<PATH TO YOUR JKS FILE>
set KEYSTORE_PASSWD=<YOUR KEYSTORE PASSWORD>
set TRUSTSTORE=%JAVA_HOME%\jre\lib\security\cacerts
set TRUSTSTORE_PASSWD=<YOUR TRUSTSTORE PASSWORD>
set SCRIPT_OPTIONS=-Djavax.net.ssl.keyStore=%KEYSTORE% -Djavax.net.ssl.keyStorePassword=%KEYSTORE_PASSWD% -Djavax.net.ssl.password=%KEYSTORE_PASSWD% -Djavax.net.ssl.trustStore=%TRUSTSTORE% -Dweblogic.security.SSL.trustedCAKeyStore=%TRUSTSTORE%  -Djavax.net.ssl.trustStore.password=%TRUSTSTORE_PASSWD%


echo "Starting RunStocksSoapClientSpringWs.cmd ..."

echo SCRIPT_OPTIONS=%SCRIPT_OPTIONS%
echo JAVA_HOME=%JAVA_HOME%

%JAVA_HOME%\bin\java -jar %SCRIPT_OPTIONS% StocksSoapClientSpringWs.jar

endlocal
