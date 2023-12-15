

В файле settings.xml необходимо прописать логин (username) и пароль (password) зарегистрированного на nexus сервере пользователя   

```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
            <profiles>
                <profile>
                    <id>coral</id>
                        <repositories>
                            <repository>
                                <id>efr-repository</id>
                                <url>http://coral:18088/repository/efr/</url>
                            </repository>
                            <repository>
                                <id>archetype</id>
                                    <url>http://coral:18088/repository/efr/</url>
                                </repository>
                            </repositories>
                </profile>
            </profiles>
            <activeProfiles>
                <activeProfile>coral</activeProfile>
            </activeProfiles>
                
            <servers>
                <server>
                    <id>coral</id>
                    <username>XXX</username>
                    <password>XXXXXXXX</password>
                    <privateKey>../.ssh/id_rsa</privateKey>
                </server>
            </servers>
    </settings>

```   

 
Для публикации требуется выполнить mvn команду

```bash
    mvn release:clean release:prepare release:perform -Darguments="-DskipTests"
```

Далее в интерактивном режиме выбрать версию публикуемого артефакта.


После публикации новой версии артефакта требуется оповестить коллег (группа в мессенджере).  