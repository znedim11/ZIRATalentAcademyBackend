package ba.com.zira.praksa.api.model.rssfeed;

import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;

public interface RssAdapter {

    public List<RssFeedExternalReview> createReviews(List<SyndEntry> entries, Long rssFeedId);
}
