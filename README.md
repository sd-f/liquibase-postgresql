# Usages

## Jira

https://softwaredesign.foundation/jira/projects/LPMP

## Maven

```xml

	<!-- snapshot repo -->
	<repositories>
        <repository>
            <id>sd.f-releases</id>
            <name>softwaredesign.foundation Releases</name>
            <url>https://softwaredesign.foundation/nexus/content/repositories/sd.f-releases</url>
        </repository>
        <repository>
            <id>sd.f-snapshots</id>
            <name>softwaredesign.foundation Snapshots</name>
            <url>https://softwaredesign.foundation/nexus/content/repositories/sd.f-snapshots</url>
			<snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

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
                        http://www.liquibase.org/xml/ns/dbchangelog-ext https://softwaredesign.foundation/xsd/liquibase/dbchangelog-ext.xsd">

	
	<changeSet id="example_creating_schema"
               author="Lucas Reeh">
        <ext:createSchema schemaName="my_schema"/>
    </changeSet>
	
	<!-- and many more -->
	
</databaseChangeLog>
```

See test resources directory for more examples