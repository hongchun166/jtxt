
package com.linkb.jstx.database;

import com.linkb.jstx.model.Config;

import java.util.HashMap;
import java.util.List;

public class ConfigRepository extends BaseRepository<Config, String> {

    private static ConfigRepository manager = new ConfigRepository();

    public static HashMap<String, String> queryConfigs() {

        List<Config> list = manager.innerQueryAll();
        HashMap<String, String> map = new HashMap<>(list.size());

        for (Config c : list) {
            map.put(c.key, c.value);

        }
        return map;
    }

    public static void add(String key, String value) {
        Config config = new Config();
        config.key = key;
        config.value = value;
        manager.createOrUpdate(config);
    }

    public static String queryValue(String key) {

        Config config = manager.innerQueryById(key);
        return config == null ? null : config.value;
    }

    public static void delete(String key) {
        manager.innerDeleteById(key);
    }
}
