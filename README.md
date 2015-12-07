# Liquibase-Postgresql Maven-Plugin [![Build Status](https://softwaredesign.foundation/jenkins/buildStatus/icon?job=liquibase-postgresql-maven-plugin-build)](https://softwaredesign.foundation/jenkins/job/liquibase-postgresql-maven-plugin-build)

## Jira

https://softwaredesign.foundation/jira/projects/LPMP

## Maven

```xml

	<!-- sd.f repo -->
	<repositories>
        <repository>
            <id>sd.f-releases</id>
            <name>softwaredesign.foundation Releases</name>
            <url>https://softwaredesign.foundation/nexus/content/repositories/sd.f-releases</url>
        </repository>
    </repositories>
    <!-- sd.f plugin repo -->
    <pluginRepositories>
        <pluginRepository>
            <id>sd.f-releases</id>
            <name>softwaredesign.foundation Releases Plugins</name>
            <url>${comuni.nexus.repo.url}/sd.f-releases</url>
        </pluginRepository>
    </pluginRepositories>

    <!-- dependencies needed -->
	<dependencies>
        <dependency>
            <groupId>postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
		<dependency>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.liquibase.ext</groupId>
            <artifactId>liquibase-postgresql</artifactId>
            <version>3.4</version>
        </dependency>
		<!-- ... -->
	</dependencies>
	
	<!-- ... -->
	
	<build>
	    <!-- ... -->
		<plugins>
            <plugin>
                <groupId>org.liquibase</groupId>
                <artifactId>liquibase-maven-plugin</artifactId>
                <configuration>
                    <!-- ... -->
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>org.liquibase.ext</groupId>
                        <artifactId>liquibase-postgresql</artifactId>
                        <version>3.4</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
	</build>
	
	<!-- ... -->
```

## changelog.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.4.xsd
                        http://www.liquibase.org/xml/ns/dbchangelog-ext https://softwaredesign.foundation/xsd/liquibase/dbchangelog-ext.xsd">

	
	<changeSet id="example_creating_schema"
               author="Lucas Reeh">
        <ext:createSchema schemaName="my_schema"/>
    </changeSet>
	
	<!-- and many more -->
	
</databaseChangeLog>
```

## Features

See https://github.com/sd-f/liquibase-postgresql/releases

## Info

Fork from https://github.com/liquibase/liquibase-postgresql