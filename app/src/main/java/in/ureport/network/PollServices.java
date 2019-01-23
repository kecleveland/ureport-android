package in.ureport.network;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import in.ureport.models.Poll;

/**
 * Created by johncordeiro on 14/09/15.
 */
public class PollServices extends ProgramServices {

    private static final String pollPath = "poll";
    private static final String pollResultPath = "poll_result";

    public void getPolls(ValueEventListener listener) {
        getPollsQuery().addValueEventListener(listener);
    }

    public void removePollsListener(ValueEventListener listener) {
        getPollsQuery().removeEventListener(listener);
    }

    private Query getPollsQuery() {
        return getDefaultRoot().child(pollPath).orderByKey();
    }

    public void getPollResults(Poll poll, ValueEventListener listener) {
        getPollsResultsQuery(poll).addValueEventListener(listener);
    }

    public void removePollsResultsListener(Poll poll, ValueEventListener listener) {
        getPollsResultsQuery(poll).removeEventListener(listener);
    }

    private DatabaseReference getPollsResultsQuery(Poll poll) {
        return getDefaultRoot().child(pollResultPath).child(poll.getKey());
    }

}