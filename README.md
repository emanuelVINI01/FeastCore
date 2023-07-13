# FeastCore
Centro de todos plugins do Feast (API)

<h2> Tecnologias usadas: </h2>

<div style="display: inline">
    <img width=128 height=128 alt="MySQL + HikariCP" src="https://avatars.githubusercontent.com/u/45949248?s=200&v=4"/> <h3><bold><a href="https://github.com/projectlombok/lombok"> Lombok </a> </bold>: Marcações como @Getter e @Setter, deixando o código mais limpo. </h3>
</div>

<div>
  <h3><a href="https://github.com/henrysaantos/sql-provider">SQLProvider</h1>
  <h3><a href="https://github.com/tr7zw/Item-NBT-API">NBTAPI</h1>
</div>

<h3>
 Você precisa por o plugin que usa a API em FeastCore/plugins.<br/>
</h3>

<h2>Exemplo:</h2>

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


