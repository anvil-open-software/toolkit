Dynamic Arquillian Configuration
---

This project provides an arquillian extension that injects values in an arquillian container definition as it is loaded.
It covers the following use-case: arquillian tests run against a wildfly instance launched by _io.fabric8:maven-docker-plugin_
that is configured to dynamically find out available ports at run-time.

The extension requires a file _target/ports.properties_ to contain (at least) two properties named _servlet.port_ and _container.configuration.managementPort_.

Usage:
1. Have a (test) dependency on this project
1. Configure _io.fabric8:maven-docker-plugin_:
```xml
<plugin>
    <groupId>io.fabric8</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <configuration>
        <portPropertyFile>${project.build.directory}/ports.properties</portPropertyFile>
        <images>
            <image>
                ....
                <run>
                    <ports>
                        <port>servlet.port:8080</port>
                        <port>container.configuration.managementPort:9990</port>
                    </ports>
                    ....
                </run>
            </image>
        </images>
        ....
    </configuration>
    ....
</plugin>
```