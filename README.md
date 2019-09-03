
## Demo PCF SpringBoot app with Azure KeyVault Secrets

For many Azure services OSBA or Meta Azure Service Broker provide functionality to manage Azure resource lifecycle and credentials.
For example it will create database (or connect to existing one) when service is created and provide user credentials to the application that binds to it. 

Azure KeyVault and secrets lifecycle are typically managed by different groups responsible for security in the organization.
In this case it might be sufficient 

 
 
### User-provided service


### Spring boot AKV starter


### References
[Meta Azure Service Broker AKV docs](https://github.com/Azure/meta-azure-service-broker/tree/master/docs/user-provided-services)

[Azure Spring Boot Starter for AKV ](https://github.com/microsoft/azure-spring-boot/tree/master/azure-spring-boot-samples/azure-keyvault-secrets-spring-boot-sample)

[Azure OSBA](https://github.com/Azure/open-service-broker-azure)