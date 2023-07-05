# FeastCore
Centro de todos plugins do Feast (API)

<h2> Tecnologias usadas: </h2>

<div style="display: inline">
    <img width=128 height=128 alt="MySQL + HikariCP" src="https://avatars.githubusercontent.com/u/45949248?s=200&v=4"/> <h3><bold><a href="https://github.com/projectlombok/lombok"> Lombok </a> </bold>: Marcações como @Getter e @Setter, deixando o código mais limpo. </h3>
</div>
<div style="display: inline">
    <img width=128 height=128 alt="MySQL + HikariCP" src="https://icons-for-free.com/iconfiles/png/512/mysql+original+wordmark-1324760553527083815.png"/> <h3><bold><a href="https://github.com/projectlombok/lombok"> HikariCP </a> </bold>: Melhora a perfomance do desempenho do JDBC. </h3>
</div>

<h3>
 Você precisa por o plugin que usa a API em FeastCore/plugins.<br/>
 Os arquivos salvos ficam em FeastCore/plugins/plugin/arquivo.yml
</h3>

<h2>Exemplo:</h2>

```java
    
package com.emanuelvini.feastcrates;

import com.emanuelvini.feastcore.bukkit.api.plugin.HigePlugin;
import com.emanuelvini.feastcrates.manager.MessageManager;
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

```


