
## Demo PCF SpringBoot app with Azure KeyVault Secrets

For many Azure services OSBA or Meta Azure Service Broker provide functionality to manage Azure resource lifecycle and credentials.
For example it will create database (or connect to existing one) when service is created and provide user credentials to the application that binds to it. 

Azure KeyVault and secrets lifecycle are typically managed by different groups responsible for security in the organization.
In this case it might be sufficient to make use of cloud foundry user -provided service with specifies credentials required to retrieve secrets

### User-provided service

As per Microsoft docs [How to use the Spring Boot Starter for Azure Key Vault](https://docs.microsoft.com/en-us/java/azure/spring-framework/configure-spring-boot-starter-java-app-with-azure-key-vault?view=azure-java-stable)

- Create or get info for service principal that is granted access to secrets in the Azure KeyVault ( see doc above on steps to create Vault,grant permissions etc)
- Add secret to Vault with name `my-very-secret`
- Create `json` file with CF service credential definition as below ( saved as `akv-service.json` in this project) 
```json
{
  "clientId":"<SP clientId>",
  "clientSecret":"<SP client Secret>",
  "vaultUri":"https://<vaultname>.vault.azure.net"
}
``` 
- Create CF User Provided Service named `azure-vault-service` instance by running CF CUPS command
```shell
 cf cups azure-vault-service -p akv-service.json
```

### Spring boot AKV starter
Now that we have credentials to establish connection to AKV available in CF service, we will use Spring Boot Starter use this credentials to get secrets from Azure Key Vault

- Add Azure Key Vault starter to the project `pom.xml`
```xml
<dependency>
    <groupId>com.microsoft.azure</groupId>
    <artifactId>azure-keyvault-secrets-spring-boot-starter</artifactId>
</dependency>
``` 
- Add to the application settings in `application.yaml` to the `cloud` profile activated in CloudFoundry Service Principle and Vault details
that will be read from CF User Provided Service. 

```yml

---
spring.profiles: cloud

# running on PCF - read from VCAP service
azure:
  keyvault:
    uri: ${vcap.services.azure-vault-service.credentials.vaultUri}
    client-id: ${vcap.services.azure-vault-service.credentials.clientId}
    client-key: ${vcap.services.azure-vault-service.credentials.clientSecret}
```

Note: CF CUPS service data is available as flattened spring boot environment properties with following notation 
`vcap.services.<name of service>.credentials.<name of key>`

- Now we can read Azure KeyVault secrets as any other Spring boot configuration properties ,( as Key Vault will be bound as custom Properties Datasource)
Example in `TestController.java`, where `@Value("${my-very-secret}")` is read from Azure KeyVault.

```java
@RestController
@RequestMapping("/v1")
public class TestController {

    @Value("${my-very-secret}")
    private String secretValue;

    @Value("${my-not-secret}")
    private String nosecretValue;

    @GetMapping("/values")
    public String[] Values() {
        return  new String[] { secretValue, nosecretValue};
    }
}
```


- Compile, package and push application with binding to CF CUPS service created above, here is our `manifest.yml`

```yaml

---
applications:
- name: spring-akv
  path: target/akv-0.0.1-SNAPSHOT.jar
  buildpack: java_buildpack
  services:
   - azure-vault-service

```

```sbtshell
mvn clean package
cf push
```


### References
[Meta Azure Service Broker AKV docs](https://github.com/Azure/meta-azure-service-broker/tree/master/docs/user-provided-services)

[Azure Spring Boot Starter for AKV ](https://github.com/microsoft/azure-spring-boot/tree/master/azure-spring-boot-samples/azure-keyvault-secrets-spring-boot-sample)

[Azure OSBA](https://github.com/Azure/open-service-broker-azure)