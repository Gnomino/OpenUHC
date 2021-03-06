package eu.gnomino.openuhc;

import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Gnomino on 4/24/15.
 */
public class NetworkListener implements Listener {
    private OpenUHC pl;
    public NetworkListener(OpenUHC plugin) {
        pl = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent e) {
        if(!pl.getGame().isJoinable()) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, pl._("game_started_kick"));
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        try {
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
            pl.getGame().addPlayer(new UHCPlayer(e.getPlayer(), true));
            if(pl.getGame().getStatus() == GameStatus.COUNTDOWN || !pl.getConfig().getBoolean("enable_auto_start"))
                e.setJoinMessage(pl._("join_message").replace("{PLAYER}", e.getPlayer().getDisplayName()));
            else
                e.setJoinMessage(pl._("join_message_waiting").replace("{PLAYER}", e.getPlayer().getDisplayName()).replace("{LEFT}", "" + (pl.getConfig().getInt("auto_start") - pl.getGame().playersNb())));
        } catch (GameStartedException e1) {
            e1.printStackTrace();
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        pl.getGame().removePlayer(e.getPlayer());
    }
}
