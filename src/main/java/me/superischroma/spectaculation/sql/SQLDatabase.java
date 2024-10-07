package me.superischroma.spectaculation.sql;

import me.superischroma.spectaculation.Spectaculation;

import java.lang.Object;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.IOException;
import java.io.File;
import java.sql.Connection;

public class SQLDatabase
{
    private static final Spectaculation plugin;
    private static final String DATABASE_FILENAME = "database.db";
    private Connection connection;
    private final File file;
    
    public SQLDatabase() {
        final File file = new File(SQLDatabase.plugin.getDataFolder(), "database.db");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                SQLDatabase.plugin.saveResource("database.db", false);
            }
            catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
        this.file = file;
    }
    
    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            final Connection connection = DriverManager.getConnection("jdbc:sqlite:" + this.file.getAbsolutePath());
            if (connection != null) {
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `worlds` (\n\t`id` SMALLINT,\n\t`name` TEXT\n);").execute();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `users` (\n\t`id` INT,\n\t`uuid` TINYTEXT\n);").execute();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `regions` (\n\t`name` TINYTEXT,\n\t`x1` INT,\n\t`y1` INT,\n\t`z1` INT,\n\t`x2` INT,\n\t`y2` INT,\n\t`z2` INT,\n\t`world` SMALLINT,\n\t`type` TINYTEXT\n);").execute();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `launchers` (\n\t`region_name` TINYTEXT,\n\t`x` INT,\n\t`y` INT,\n\t`z` INT\n);").execute();
                return connection;
            }
        }
        catch (final SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    static {
        plugin = Spectaculation.getPlugin();
    }
}
