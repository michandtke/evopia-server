package de.mwa.evopiaserver;

import com.sksamuel.hoplite.ConfigException;
import de.mwa.evopiaserver.db.kotlin.DatabaseUtil;
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.ConfigProvider;
import de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.WebServer;

public class EvopiaServerApplication {
    public static void main(String[] args) {
        try {
            var config = new ConfigProvider().config();
            var database = new DatabaseUtil(config).db();
            WebServer.INSTANCE.start(database).start(true);
        } catch (ConfigException e) {
            System.out.println("Could not load config: " + e.getMessage());
        }
    }
}
