# Usages

## Jira

https://softwaredesign.foundation/jira/projects/LPMP

## Maven

```xml
    <!-- ... -->
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
            <version>3.4-SNAPSHOT</version>
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
                        <version>3.4-SNAPSHOT</version>
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
                        https://softwaredesign.foundation/xsd/liquibase https://softwaredesign.foundation/xsd/liquibase/dbchangelog-ext.xsd">

    <changeSet id="test1"
               author="Lucas Reeh">
        <ext:createRole roleName="my_role" password="my_password"></ext:createRole>
    </changeSet>
	
	<changeSet id="test2"
               author="Lucas Reeh">
        <ext:createSchema schemaName="my_schema"></ext:createSchema>
    </changeSet>
	
	<changeSet id="test3"
               author="Lucas Reeh">
        <ext:dropRole roleName="my_role"></ext:dropRole>
    </changeSet>

	<changeSet id="test"
               author="Lucas Reeh">
        <ext:dropSchema schemaName="my_schema"></ext:dropSchema>
    </changeSet>
	
</databaseChangeLog>
```