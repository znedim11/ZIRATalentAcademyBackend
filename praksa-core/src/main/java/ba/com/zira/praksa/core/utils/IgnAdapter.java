package ba.com.zira.praksa.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;

import ba.com.zira.praksa.api.model.enums.RssAdapterType;
import ba.com.zira.praksa.api.model.rssfeed.RssAdapter;
import ba.com.zira.praksa.api.model.rssfeed.RssFeedExternalReview;

public class IgnAdapter implements RssAdapter {
    @Override
    public List<RssFeedExternalReview> createReviews(List<SyndEntry> entries, Long rssFeedId) {
        List<RssFeedExternalReview> reviews = new ArrayList<>();

        for (SyndEntry entry : entries) {
            RssFeedExternalReview review = new RssFeedExternalReview();
            review.setCreatedBy(entry.getAuthor());
            review.setOrigin(RssAdapterType.IGN.getValue());
            review.setInformation(entry.getContents().get(0).getValue());
            review.setRssFeedId(rssFeedId);
            review.setGameName(entry.getTitle());
            reviews.add(review);
        }
        return reviews;
    }
}
