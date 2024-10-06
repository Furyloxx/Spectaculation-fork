/*
 * Copyright (C) 2023 by Ruby Game Studios
 * Mortar is apart of Skyblock, which is licensed under the Creative Commons Non-Commercial 4.0 International License.
 *
 * You may not use this software for commercial use, however you are free
 * to modify, copy, redistribute, or build upon our codebase. You must give
 * appropriate credit, provide a link to the license, and indicate
 * if changes were made.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, visit https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */
package net.skypixel.mortar.command;

import lombok.NonNull;

import net.skypixel.mortar.utils.MortarUtils;

import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CommandRegistry {
    private JavaPlugin plugin;
    private Map<CommandParameters, Object> COMMANDS;

    public static CommandRegistry configure(JavaPlugin plugin) {
        CommandRegistry registry = new CommandRegistry();
        registry.plugin = plugin;
        registry.COMMANDS = new HashMap<>();

        return registry;
    }
    private void checkPlugin() {
        if (plugin == null) {
            throw new UnsupportedOperationException("The Command Registry attempted to register/run a command, yet was not configured!");
        }
    }

    @SuppressWarnings({"deprecation"})
    public void register(@NonNull Class<?> clazz) throws InstantiationException, IllegalAccessException {
        checkPlugin();

        if (!clazz.isAnnotationPresent(CommandParameters.class)) {
            throw new UnsupportedOperationException("The Command Registry attempted to register a command (" + clazz.getName() + "), yet it was not annotated with CommandParameters!");
        }

        Object command = clazz.newInstance();
        CommandParameters parameters = clazz.getAnnotation(CommandParameters.class);

        for (Method method : clazz.getDeclaredMethods()) {
            if (!(method.isAnnotationPresent(CommandExecutor.class))) continue;
            if (!(method.isAccessible())) method.setAccessible(true);

            plugin.getCommand(parameters.name()).setExecutor((sender, spigot, name, arguments) -> {
                SenderType senderType = parameters.senderType();
                String permission = parameters.permission();

                if (senderType != SenderType.SERVER && senderType != SenderType.BOTH) {
                    if (!sender.hasPermission(permission) && !Objects.equals(permission, "null")) {
                        Player player = (Player) sender;

                        player.sendMessage(MortarUtils.color("&cYou do not have permissions to use this command!"));
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);

                        return false;
                    }
                }

                if (senderType == SenderType.BOTH) {
                    if (sender instanceof Player) {
                        if (!sender.hasPermission(permission)) {
                            Player player = (Player) sender;

                            player.sendMessage(MortarUtils.color("&cYou do not have permissions to use this command!"));
                            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);

                            return false;
                        }
                    }
                }

                switch (senderType) {
                    case PLAYER:
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(MortarUtils.color("&cYou must be a player to be able to use this command!"));
                            return false;
                        }
                        break;
                    case SERVER:
                        if (!(sender instanceof ConsoleCommandSender)) {
                            Player player = (Player) sender;

                            player.sendMessage(MortarUtils.color("&cThis command can only be used by the console!"));
                            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10, 1);

                            return false;
                        }
                }


                try {
                    method.invoke(command, sender, arguments);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

                return true;
            });
        }

        COMMANDS.put(parameters, command);
    }

    public Optional<Object> search(@NonNull String name) {
        return COMMANDS.entrySet().stream()
                .filter(entry -> entry.getKey().name().equalsIgnoreCase(name))
                .map(Map.Entry::getValue)
                .findFirst();
    }
}
