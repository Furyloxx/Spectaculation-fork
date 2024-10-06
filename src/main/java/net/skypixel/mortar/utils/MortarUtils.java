/*
 * Copyright (C) 2023 by Ruby Game Studios
 * Mortar is apart of Skyblock, and is licensed under the Creative Commons Non-Commercial 4.0 International License.
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
package net.skypixel.mortar.utils;

import lombok.SneakyThrows;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class MortarUtils {
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    @SneakyThrows
    private static void addChannel(Player player, String channel) {
        player.getClass().getMethod("addChannel", String.class).invoke(player, channel);
    }
}
