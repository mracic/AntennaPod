package de.danoeh.antennapod.net.download.service.episode.autodownload;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import de.danoeh.antennapod.model.feed.FeedItem;
import de.danoeh.antennapod.model.feed.FeedItemFilter;
import de.danoeh.antennapod.model.feed.FeedMedia;
import de.danoeh.antennapod.model.feed.SortOrder;
import de.danoeh.antennapod.storage.database.DBReader;
import de.danoeh.antennapod.storage.database.DBWriter;

/**
 * A cleanup algorithm that automatically deletes episodes not favorited within 7 days of download.
 * Favorited episodes are always protected from deletion.
 */
public class SevenDayAutoDeleteCleanupAlgorithm extends EpisodeCleanupAlgorithm {

    private static final String TAG = "7DayAutoDeleteAlgo";
    private static final int DAYS_UNTIL_DELETION = 7;

    /**
     * @return the number of episodes that *could* be cleaned up, if needed
     */
    public int getReclaimableItems() {
        return getCandidates().size();
    }

    @Override
    public int performCleanup(Context context, int numberOfEpisodesToDelete) {
        List<FeedItem> candidates = getCandidates();
        List<FeedItem> delete;

        // Sort by download date, oldest first
        Collections.sort(candidates, (lhs, rhs) -> {
            Date l = new Date(lhs.getMedia().getDownloadDate());
            Date r = new Date(rhs.getMedia().getDownloadDate());
            return l.compareTo(r);
        });

        if (candidates.size() > numberOfEpisodesToDelete) {
            delete = candidates.subList(0, numberOfEpisodesToDelete);
        } else {
            delete = candidates;
        }

        for (FeedItem item : delete) {
            try {
                DBWriter.deleteFeedMediaOfItem(context, item.getMedia()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        int counter = delete.size();

        Log.i(TAG, String.format(Locale.US,
                "Auto-delete (7-day) deleted %d episodes (%d requested)", counter,
                numberOfEpisodesToDelete));

        return counter;
    }

    @VisibleForTesting
    Date calcMostRecentDateForDeletion(@NonNull Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_YEAR, -DAYS_UNTIL_DELETION);
        return cal.getTime();
    }

    @NonNull
    private List<FeedItem> getCandidates() {
        List<FeedItem> candidates = new ArrayList<>();
        List<FeedItem> downloadedItems = DBReader.getEpisodes(0, Integer.MAX_VALUE,
                new FeedItemFilter(FeedItemFilter.DOWNLOADED), SortOrder.DATE_NEW_OLD);

        Date mostRecentDateForDeletion = calcMostRecentDateForDeletion(new Date());

        for (FeedItem item : downloadedItems) {
            if (item.hasMedia()
                    && item.getMedia().isDownloaded()
                    && !item.isTagged(FeedItem.TAG_FAVORITE)) {
                FeedMedia media = item.getMedia();
                // Check if episode was downloaded more than 7 days ago
                if (media != null
                        && media.getDownloadDate() > 0) {
                    Date downloadDate = new Date(media.getDownloadDate());
                    if (downloadDate.before(mostRecentDateForDeletion)) {
                        candidates.add(item);
                    }
                }
            }
        }
        return candidates;
    }

    @Override
    public int getDefaultCleanupParameter() {
        // Return the number of episodes eligible for deletion
        return getCandidates().size();
    }
}
