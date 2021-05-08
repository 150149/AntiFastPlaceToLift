package ciyuanwutuobang;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AntiFastPlaceToLift extends JavaPlugin implements Listener {

    private String quitname;

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]防止速搭插件已开启");
        Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]作者：150149  QQ：1802796278");
        quitname="";
    }


    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]防止速搭插件已关闭");
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = false)

    public void onplace(BlockPlaceEvent event){
        //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]检测到放置方块");
        if(event.isCancelled()==true){
            //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]检测到放置方块被取消");
            if(event.getPlayer().isFlying()==false){
                if (event.getPlayer().getLocation().getY()>=event.getBlock().getY() ){
                    //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]检测到放置方块在脚下");
                    if(event.getPlayer().getLocation().getBlockX()==event.getBlock().getX() && event.getPlayer().getLocation().getBlockZ()==event.getBlock().getZ()){

                        //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]检测到速搭");


                        event.getPlayer().getLocation().setY(Double.valueOf(event.getBlock().getY()-1));

                        event.getPlayer().teleport(event.getPlayer().getLocation().add(0,-1,0));
                        event.getPlayer().sendMessage("§8[ §c反作弊 §8]§7禁止速搭来翻墙！");

                    }



                }
            }
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        quitname = event.getPlayer().getPlayerListName();
    }


    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent event){
        if (event.isBlockInHand()){
            if (event.isCancelled()==true){
                //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]检测到交互被取消");
                if (event.getPlayer().isFlying()==false) {
                    if (event.getPlayer().getLocation().getY()-event.getClickedBlock().getY()>=0.5){
                        //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]检测到交互过高");
                        if (event.getPlayer().getLocation().getBlockX()==event.getClickedBlock().getX() && event.getPlayer().getLocation().getBlockZ()==event.getClickedBlock().getZ()){
                            //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]检测到交互在脚下,方块为"+event.getPlayer().getLocation().add(0,-1,0).getBlock().getType().toString());
                            if (true){

                                //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]检测到交互速搭");
                                event.getPlayer().getLocation().setY(event.getClickedBlock().getY()+1);
                                event.getPlayer().teleport(event.getPlayer().getLocation());
                                event.getPlayer().sendMessage("§8[ §c反作弊 §8]§7禁止速搭来翻墙！");
                            }

                        }
                    }
                }
            }
        }

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        class Aplayer{
            BukkitTask time;
            Player player;
            Location lastloc;
            Block lastblo;
        }

        Aplayer a = new Aplayer();
        a.player = event.getPlayer();
        a.lastloc=event.getPlayer().getLocation();
        a.lastblo=event.getPlayer().getLocation().add(0,-1,0).getBlock();
        //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]开始监测");

        a.time=new BukkitRunnable() {

            @Override
            public void run() {

                if (event.getPlayer().getLocation().getY()-a.lastloc.getY()>1.4){
                    if (event.getPlayer().getLocation().getX()-a.lastloc.getX()<=1 || a.lastloc.getX()-event.getPlayer().getLocation().getX()>=-1){
                        if(event.getPlayer().getLocation().getZ()-a.lastloc.getZ()<=1 || a.lastloc.getBlockZ()-event.getPlayer().getLocation().getZ()>=-1){
                            if (event.getPlayer().getLocation().getBlock().getType().toString()!="LADDER"){
                                if (event.getPlayer().isFlying()==false) {
                                    if (event.getPlayer().getLocation().add(0,-1,0).getBlock().getType().toString()=="AIR") {
                                        if (!event.getPlayer().hasPermission("essentials.fly") && !(event.getPlayer().getGameMode().toString()=="CREATIVE")  && !event.getPlayer().isOp()) {
                                            //Bukkit.getConsoleSender().sendMessage("[AntiFastPlaceToLift]检测到"+event.getPlayer().getGameMode().toString());
                                            event.getPlayer().teleport(a.lastloc);
                                            event.getPlayer().sendMessage("§8[ §c反作弊 §8]§7禁止高速向上！");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (event.getPlayer().getLocation().add(0,-1,0).getBlock().getType().toString()!="AIR") a.lastloc=event.getPlayer().getLocation();
                a.lastblo=event.getPlayer().getLocation().add(0,-1,0).getBlock();

                if (quitname.equals(a.player.getPlayerListName())) {
                    quitname = "";
                    this.cancel();
                }
            }

        }.runTaskTimer(this, 20,5);
    }

}
