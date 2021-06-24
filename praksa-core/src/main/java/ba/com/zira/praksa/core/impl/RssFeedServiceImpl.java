package ba.com.zira.praksa.core.impl;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.RssFeedService;
import ba.com.zira.praksa.api.model.rssfeed.RssAdapter;
import ba.com.zira.praksa.api.model.rssfeed.RssFeedExternalReview;
import ba.com.zira.praksa.core.utils.RssAdapterFactory;
import ba.com.zira.praksa.dao.ExternalReviewDAO;
import ba.com.zira.praksa.dao.RssFeedDAO;
import ba.com.zira.praksa.dao.model.ExternalReviewEntity;
import ba.com.zira.praksa.dao.model.RssFeedEntity;

/**
 * @author zira
 *
 */

@Service
@ComponentScan
public class RssFeedServiceImpl implements RssFeedService {

    private RssFeedDAO rssFeedDAO;
    private ExternalReviewDAO externalReviewDAO;

    public RssFeedServiceImpl(RssFeedDAO rssFeedDAO, ExternalReviewDAO externalReviewDAO) {
        this.rssFeedDAO = rssFeedDAO;
        this.externalReviewDAO = externalReviewDAO;
    }

    @Override
    public PayloadResponse<String> rssFeedReader(EntityRequest<Long> request) throws ApiException {

        RssFeedEntity rssFeedEntity = rssFeedDAO.findByPK(request.getEntity());
        if (rssFeedEntity == null) {
            throw ApiException.createFrom(request, ResponseCode.REQUEST_INVALID,
                    "RSS Feed with id " + request.getEntity() + "does not exist!");
        }
        URL feedSource;
        try {
            feedSource = new URL(rssFeedEntity.getUrl());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));
            List<SyndEntry> entries = feed.getEntries();
            RssAdapter adapter = RssAdapterFactory.build(rssFeedEntity.getAdapter());
            List<RssFeedExternalReview> reviews = adapter.createReviews(entries, request.getEntity());

            for (RssFeedExternalReview review : reviews) {
                ExternalReviewEntity newExternalReviewEntity = new ExternalReviewEntity();
                newExternalReviewEntity.setCreated(LocalDateTime.now());
                newExternalReviewEntity.setCreatedBy(review.getCreatedBy());
                newExternalReviewEntity.setGameName(review.getGameName());
                newExternalReviewEntity.setInformation(review.getInformation());
                newExternalReviewEntity.setOrigin(review.getOrigin());
                newExternalReviewEntity.setRssFeed(rssFeedDAO.findByPK(review.getRssFeedId()));
                externalReviewDAO.persist(newExternalReviewEntity);
            }

        } catch (Exception e) {
            throw ApiException.createFrom(request, ResponseCode.REQUEST_INVALID, e.getMessage());
        }

        return new PayloadResponse<>(request, ResponseCode.OK, "Reviews successfully updated");
    }

}
