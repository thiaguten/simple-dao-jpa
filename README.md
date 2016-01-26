# SimpleDAO-JPA

[![Build Status](https://travis-ci.org/thiaguten/simple-dao-jpa.svg)](https://travis-ci.org/thiaguten/simple-dao-jpa)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/br.com.thiaguten.persistence/simple-dao-jpa/badge.svg)](https://maven-badges.herokuapp.com/maven-central/br.com.thiaguten.persistence/simple-dao-jpa)

SimpleDAO JPA Service Provider Interface - SPI Implementation.

In test package was implemented a demo example using the SimpleDAO-JPA API. A demonstration where transactions are manually controlled using (beginTransaction, commitTransaction, rollbackTransaction, etc) and another using the SpringFramework using (@Transacional).

Requires JDK 1.6 or higher

## Latest release

SimpleDAO-JPA bundle is available from [Maven Central](http://search.maven.org/).

To add a dependency using Maven, use the following:

```xml
<dependency>
    <groupId>br.com.thiaguten.persistence</groupId>
    <artifactId>simple-dao-jpa</artifactId>
    <version>1.0.0</version>
</dependency>
```

To add a dependency using Gradle:

```
dependencies {
    compile 'br.com.thiaguten.persistence:simple-dao-jpa:1.0.0'
}
```

