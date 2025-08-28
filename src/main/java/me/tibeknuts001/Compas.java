package me.tibeknuts001;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class Compas implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (ManhuntSettings.getInstance().config.getBoolean("Resource_pack"))
            event.getPlayer().setResourcePack(ManhuntSettings.getInstance().data.getString("resourcepack.link"), ManhuntSettings.getInstance().data.getString("resourcepack.hash"), false, Component.text("This resource pack is to make the compass icon in the actionbar work").compact());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (ManhuntSettings.getInstance().data.getStringList("hunters").stream().anyMatch(s -> s.equalsIgnoreCase(event.getPlayer().getName()))) {
            ShowCompas(event.getPlayer());
        }
    }

    public void ShowCompas(Player player) {
        Player nearestRunner = null;
        double closestDistance = Double.MAX_VALUE;

        for (String stringCandidate : ManhuntSettings.getInstance().data.getStringList("runners")) {
            Player candidate = Bukkit.getPlayer(stringCandidate);
            if (candidate == null) continue;
            if (candidate.isDead()) continue; // skip dead entities
            if (candidate.getGameMode() != GameMode.SURVIVAL) continue;
            double distance = candidate.getLocation().distanceSquared(player.getLocation()); // use distanceSquared for performance
            if (distance < closestDistance) {
                closestDistance = distance;
                nearestRunner = candidate;
            }
        }


        if (nearestRunner != null) {
            // nearestRunner is the closest entity
            player.sendActionBar(Component.text("Tracking: " + nearestRunner.getName()).append(Component.text(getCompassIndex(player, nearestRunner, 16)).font(Key.key("compass")).append(Component.text(" Distance: " + Math.round(player.getLocation().distance(nearestRunner.getLocation()))).font(Key.key("default")))));
        }
    }

    public static String getCompassIndex(Player player, Player target, int slices) {
        Location pLoc = player.getLocation();
        Vector forward = pLoc.getDirection().clone(); // player's look direction
        forward.setY(0).normalize();

        Vector toTarget = target.getLocation().toVector().subtract(pLoc.toVector());
        toTarget.setY(0);

        // handle the case where target is exactly above/below the player (no horizontal difference)
        if (toTarget.lengthSquared() < 1e-6) {
            return "0"; // arbitrary, target is basically at player's XZ
        }
        toTarget.normalize();

        // signed angle between forward and toTarget:
        double dot = forward.dot(toTarget); // cos(theta)
        double cross = forward.getX() * toTarget.getZ() - forward.getZ() * toTarget.getX(); // sin(theta) (2D cross z)
        double angleRad = Math.atan2(cross, dot); // -pi .. +pi
        double angleDeg = Math.toDegrees(angleRad); // -180 .. +180

        // convert to 0..360 where 0 = forward
        double angle360 = (angleDeg + 360.0) % 360.0;

        // map to slice index (0..slices-1)
        double sliceSize = 360.0 / slices;
        int index = (int) Math.round(angle360 / sliceSize) % slices;
        if (index < 0) index += slices;
        return switch (index) {
            case 10 -> "A";
            case 11 -> "B";
            case 12 -> "C";
            case 13 -> "D";
            case 14 -> "E";
            case 15 -> "F";
            default -> String.valueOf(index);
        };
    }

}
