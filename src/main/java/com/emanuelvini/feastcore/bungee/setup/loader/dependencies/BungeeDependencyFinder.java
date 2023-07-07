package com.emanuelvini.feastcore.bungee.setup.loader.dependencies;


import com.emanuelvini.feastcore.bungee.setup.MainBungee;
import com.emanuelvini.feastcore.bungee.setup.loader.util.PluginManager;
import com.emanuelvini.feastcore.common.loader.dependecies.IDependencyFinder;
import com.emanuelvini.feastcore.common.loader.dependecies.models.Dependency;
import com.emanuelvini.feastcore.common.logging.BridgeLogger;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

@AllArgsConstructor
public class BungeeDependencyFinder implements IDependencyFinder {



    private final MainBungee plugin;


    private final ArrayList<Dependency> dependencies = new ArrayList<>();
    
    private final BridgeLogger logger = MainBungee.getBridgeLogger();

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
            val plugin = ProxyServer.getInstance().getPluginManager().getPlugin(dependency.getName());
            if (plugin != null) {
                try {
                    PluginManager.unloadPlugin(plugin);

                    logger.log(String.format(
                            "§aDependência §f%s§a desabilitada com sucesso!", dependency.getName()));
                } catch (Exception e) {
                    logger.log(String.format
                            ("§cOcorreu um erro ao desabilitar a dependência §f%s§c:", dependency.getName()));
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
                logger.log(String.format(
                        "§aDependência §f%s§a baixada com sucesso!",
                        name));
            }

            try {
                val plugin = PluginManager.loadPlugin(dependencyFile);
                plugin.onLoad();
                plugin.onEnable();

                logger.log(String.format(
                        "§aDependência §f%s§ahabilitada com sucesso!",
                        name));
            } catch (Exception e) {
                logger.log(String.format
                        ("§cOcorreu um erro ao habilitar a dependência §f%s§c:", dependency.getName()));
                e.printStackTrace();
            }
        }
    }
}
