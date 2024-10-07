package me.superischroma.spectaculation.sql;

import me.superischroma.spectaculation.Spectaculation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;

public class SQLDatabase {
    private final Spectaculation plugin;
    private static final String DATABASE_FILENAME = "database.db";
    private Connection connection;
    private final File file;

    public SQLDatabase(Spectaculation plugin) {
        this.plugin = plugin;
        this.file = new File(this.plugin.getDataFolder(), DATABASE_FILENAME);

        if (!this.file.exists()) {
            try {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
                this.plugin.saveResource(DATABASE_FILENAME, false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        if (this.connection == null || !isValidConnection()) {
            try {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.file.getAbsolutePath());
                initializeTables();
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return this.connection;
    }

    private boolean isValidConnection() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initializeTables() throws SQLException {
        if (this.connection != null) {
            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS `worlds` (`id` INTEGER, `name` TEXT);").execute();
            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER, `uuid` TEXT);").execute();
            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS `regions` (`name` TEXT, `x1` INTEGER, `y1` INTEGER, `z1` INTEGER, `x2` INTEGER, `y2` INTEGER, `z2` INTEGER, `world` INTEGER, `type` TEXT);").execute();
            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS `launchers` (`region_name` TEXT, `x` INTEGER, `y` INTEGER, `z` INTEGER);").execute();
        }
    }
}
