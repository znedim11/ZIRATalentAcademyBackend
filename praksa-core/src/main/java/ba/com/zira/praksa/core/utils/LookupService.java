package ba.com.zira.praksa.core.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.PlatformDAO;

@Component
public class LookupService {

    PlatformDAO platformDAO;
    GameDAO gameDAO;

    public LookupService(PlatformDAO platformDAO, GameDAO gameDAO) {
        this.platformDAO = platformDAO;
        this.gameDAO = gameDAO;
    }

    public static String get(final Long key, final Map<Long, String> lookup) {
        if (key != null) {
            return lookup.get(key) == null ? key.toString() : lookup.get(key);
        }
        return null;
    }

    public static String getForString(final String key, final Map<String, String> lookup) {
        if (key != null) {
            return lookup.get(key) == null ? key : lookup.get(key);
        }
        return null;
    }

    public <E> void lookupPlatformName(final List<E> values, final Function<E, Long> getter, final BiConsumer<E, String> setter) {
        // @formatter:off
	List<Long> ids = values.parallelStream().map(getter).distinct().collect(Collectors.toList());
	// @formatter:on
        if (!(ids == null || ids.isEmpty())) {
            Map<Long, String> lookup = new ConcurrentHashMap<>(platformDAO.getPlatformNames(ids));
            values.parallelStream().forEach(r -> setter.accept(r, get(getter.apply(r), lookup)));
        }
    }

    public <E> void lookupGameName(final List<E> values, final Function<E, Long> getter, final BiConsumer<E, String> setter) {
        // @formatter:off
        List<Long> ids = values.parallelStream().map(getter).distinct().collect(Collectors.toList());
        // @formatter:on
        if (!(ids == null || ids.isEmpty())) {
            Map<Long, String> lookup = new ConcurrentHashMap<>(gameDAO.getGameName(ids));
            values.parallelStream().forEach(r -> setter.accept(r, get(getter.apply(r), lookup)));
        }
    }

}
