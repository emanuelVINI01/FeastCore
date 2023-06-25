# HigeCore
Centro de todos plugins do Hige (API)

Uso:

<code>
package com.emanuelvini.higecrates;

import com.emanuelvini.higecore.bukkit.api.plugin.HigePlugin;
import com.emanuelvini.higecrates.manager.MessageManager;
import lombok.Getter;

public class MainCrates extends HigePlugin {


    @Override
    public void setupDependencies() {
        super.setupDependencies();
        addDependency("example", "https://github.com/example/example/releases/download/v1.0/example.jar");
        
    }

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

</code>

<h1>
 Você precisa por o plugin em HigeCore/plugins.
 Os arquivos salvos ficam em HigeCore/plugins/plugin/arquivo.yml
</h1>
