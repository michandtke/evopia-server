package de.mwa.evopiaserver;

import de.mwa.evopiaserver.db.kotlin.DatabaseUtil;
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.ConfigProvider;
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.WebServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class EvopiaServerApplication {
    public static void main(String[] args) {
        var config = new ConfigProvider().config();
        var database = new DatabaseUtil(config).db();
        WebServer.INSTANCE.start(database).start(true);
    }
}
