package com.emanuelvini.feastcore.common.api.logging;


import com.emanuelvini.feastcore.common.api.FeastPlugin;
import com.emanuelvini.feastcore.common.logging.BridgeLogger;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Logging {

    private final BridgeLogger logger;

    private final FeastPlugin plugin;

    public void log(String message, String color) {
        logger.log(String.format("§d[%s]§f: §%s%s", plugin.getName(), color, message));
    }

}
