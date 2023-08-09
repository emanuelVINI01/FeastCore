# FeastCore
Core of all Feast plugins (API)

<h2> Tecnologies used: </h2>


<h3><bold><a href="https://github.com/projectlombok/lombok"> Lombok </a> </bold>: Annotations as @Getter e @Setter, deixando o código mais limpo. </h3>


<div>
  <h3><a href="https://github.com/henrysaantos/sql-provider">SQLProvider</h1>
  <h3><a href="https://github.com/tr7zw/Item-NBT-API">NBTAPI</h1>
</div>

<h3>
 You only need put plugin in: FeastCore/plugins.<br/>
</h3>

<h2>Example:</h2>

```java
    
package com.emanuelvini.feastcrates;

import com.emanuelvini.feastcore.bukkit.api.plugin.BukkitFeastPlugin;
import lombok.Getter;

public class MainFeast extends BukkitFeastPlugin {


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

```


