package com.emanuelvini.feastcore.common.loader.dependecies;

import lombok.SneakyThrows;

public interface IDependencyFinder {

    @SneakyThrows
    void downloadAndLoadDependencies();

    void disableAll();

    void addDependency(String name, String url);


}
