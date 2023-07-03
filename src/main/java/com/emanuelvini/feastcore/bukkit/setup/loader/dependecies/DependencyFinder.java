package com.emanuelvini.feastcore.bukkit.setup.loader.dependecies;

import com.emanuelvini.feastcore.bukkit.setup.MainBukkit;
import com.emanuelvini.feastcore.common.loader.dependecies.models.Dependency;
import com.emanuelvini.feastcore.common.loader.dependecies.IDependencyFinder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

@AllArgsConstructor
public class DependencyFinder implements IDependencyFinder {


    private final MainBukkit plugin;


    private final ArrayList<Dependency> dependencies = new ArrayList<>();

    @Override
    public void addDependency(String name, String url) {

        val dependency = new Dependency(name, url);

        if (!dependencies.contains(dependency)) {
            dependencies.add(dependency);
        }
    }

    @Override
    public void disableAll () {
       for (val dependency : dependencies) {
           val plugin = Bukkit.getPluginManager().getPlugin(dependency.getName());
           if (plugin != null) {
               try {
                   Bukkit.getPluginManager().disablePlugin(plugin);

                   Bukkit.getConsoleSender().
                           sendMessage(String.format(
                                   "§9[FeastCore] §aDependência §f%s§a desabilitada com sucesso!", dependency.getName())
                           );
               } catch (Exception e) {
                   Bukkit.getConsoleSender().
                           sendMessage(String.format
                                   ("§9[FeastCore] §cOcorreu um erro ao desabilitar a dependência §f%s§c:", dependency.getName())
                           );
                   e.printStackTrace();
               }
           }
       }
       dependencies.clear();
    }

    @Override
    @SneakyThrows
    public void downloadAndLoadDependencies() {
        val dependenciesDirectory = new File(plugin.getDataFolder(), "dependencies");
        if (!dependenciesDirectory.exists()) dependenciesDirectory.mkdirs();
        for (val dependency : dependencies) {
            val url = dependency.getUrl();
            val name = dependency.getName();
            val dependencyFile = new File(dependenciesDirectory,
                    name + ".jar"
            );
            if (!dependencyFile.exists()) {
                val download = new URL(url);
                ReadableByteChannel rbc = Channels.newChannel(
                        download.openStream()
                );
                FileOutputStream fos = new FileOutputStream(dependencyFile);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                Bukkit.getConsoleSender().
                        sendMessage(String.format(
                                "§9[FeastCore] §aDependência §f%s§a baixada com sucesso!",
                                name));
            }
            val plugin = Bukkit.getPluginManager().loadPlugin(dependencyFile);
            if (!plugin.isEnabled()) {
                Bukkit.getPluginManager().enablePlugin(plugin);
            }
            Bukkit.getConsoleSender().
                    sendMessage(String.format(
                            "§9[FeastCore] §aDependência §f%s§a carregada com sucesso!",
                            name));
        }
    }

}
