package ba.com.zira.praksa.core.utils;

import org.springframework.stereotype.Service;

import ba.com.zira.praksa.api.model.enums.RssAdapterType;
import ba.com.zira.praksa.api.model.rssfeed.RssAdapter;

@Service
public class RssAdapterFactory {

    private RssAdapterFactory() {
    }

    public static RssAdapter build(String adapter) {
        if (RssAdapterType.GIANTBOMB.getValue().equalsIgnoreCase(adapter)) {
            return new GBAdapter();
        } else if (RssAdapterType.PCGAMER.getValue().equalsIgnoreCase(adapter)) {
            return new PGAdapter();
        } else if (RssAdapterType.POLYGON.getValue().equalsIgnoreCase(adapter)) {
            return new PolyAdapter();
        } else if (RssAdapterType.IGN.getValue().equalsIgnoreCase(adapter)) {
            return new IgnAdapter();
        }
        return null;
    }
}