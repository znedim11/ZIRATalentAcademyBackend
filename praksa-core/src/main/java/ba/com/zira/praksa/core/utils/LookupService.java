package ba.com.zira.praksa.core.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.praksa.api.model.users.UserCodeDisplay;
import ba.com.zira.praksa.core.clients.UAAFeignClient;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.MediaStoreDAO;
import ba.com.zira.praksa.dao.PlatformDAO;

@Component
public class LookupService {

    PlatformDAO platformDAO;
    GameDAO gameDAO;
    UAAFeignClient uaaFeignClient;
    MediaStoreDAO mediaStoreDAO;

    @Value("${image.default.url:none}")
    String defaultImageUrl;

    public LookupService(PlatformDAO platformDAO, GameDAO gameDAO, UAAFeignClient uaaFeignClient, MediaStoreDAO mediaStoreDAO) {
        this.platformDAO = platformDAO;
        this.gameDAO = gameDAO;
        this.uaaFeignClient = uaaFeignClient;
        this.mediaStoreDAO = mediaStoreDAO;
    }

    public static String get(final Long key, final Map<Long, String> lookup) {
        if (key != null) {
            return lookup.get(key) == null ? null : lookup.get(key);
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

    public <E> void lookupReviewerName(final List<E> values, final Function<E, String> getter, final BiConsumer<E, String> setter)
            throws ApiException {
        // @formatter:off
        List<String> ids = values.parallelStream().map(getter).distinct().collect(Collectors.toList());
        // @formatter:on
        if (!(ids == null || ids.isEmpty())) {
            List<UserCodeDisplay> users = uaaFeignClient.getUserDetails().getPayload().stream().filter(isInList(ids))
                    .collect(Collectors.toList());
            Map<String, String> lookup = users.stream()
                    .collect(Collectors.toMap(UserCodeDisplay::getUsercode, UserCodeDisplay::getDisplayname, (u1, u2) -> {
                        return u1;
                    }));
            values.parallelStream().forEach(r -> setter.accept(r, getForString(getter.apply(r), lookup)));
        }
    }

    private Predicate<? super UserCodeDisplay> isInList(List<String> ids) {
        return u -> ids.contains(u.getUsercode());
    }

    public <E> void lookupCoverImage(final List<E> values, final Function<E, Long> getter, String objectType,
            final BiConsumer<E, String> setter, final Function<E, String> getterForImage) throws ApiException {
        // @formatter:off
        List<Long> ids = values.parallelStream().map(getter).distinct().collect(Collectors.toList());
        // @formatter:on
        if (!(ids == null || ids.isEmpty())) {
            Map<Long, String> lookup = mediaStoreDAO.getUrlsForList(ids, objectType, "COVER_IMAGE");
            values.parallelStream().forEach(r -> setter.accept(r, get(getter.apply(r), lookup)));
            values.parallelStream().forEach(r -> handleDefaultImage(r, setter, getterForImage));
        }
    }

    public <E> void handleDefaultImage(E r, BiConsumer<E, String> setter, Function<E, String> getterForImage) {
        if (getterForImage.apply(r) == null) {
            setter.accept(r, defaultImageUrl);
        }
    }

}
