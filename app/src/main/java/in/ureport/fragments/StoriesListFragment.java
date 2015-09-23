package in.ureport.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;

import in.ureport.R;
import in.ureport.activities.StoryViewActivity;
import in.ureport.helpers.ValueEventListenerAdapter;
import in.ureport.listener.FloatingActionButtonListener;
import in.ureport.listener.OnStoryContributionCountListener;
import in.ureport.helpers.RecyclerScrollListener;
import in.ureport.listener.OnUserStartChattingListener;
import in.ureport.managers.UserManager;
import in.ureport.models.Story;
import in.ureport.models.User;
import in.ureport.network.ContributionServices;
import in.ureport.network.StoryServices;
import in.ureport.network.UserServices;
import in.ureport.helpers.ChildEventListenerAdapter;
import in.ureport.views.adapters.StoriesAdapter;

/**
 * Created by johncordeiro on 7/13/15.
 */
public class StoriesListFragment extends Fragment implements StoriesAdapter.OnStoryViewListener {

    private static final String EXTRA_USER = "user";

    private RecyclerView storiesList;
    private View info;

    private User user;
    protected boolean publicType = true;

    private StoriesAdapter.OnPublishStoryListener onPublishStoryListener;
    private OnUserStartChattingListener onUserStartChattingListener;
    private FloatingActionButtonListener floatingActionButtonListener;

    protected StoriesAdapter storiesAdapter;

    protected StoryServices storyServices;
    protected UserServices userServices;
    protected ContributionServices contributionServices;

    public static StoriesListFragment newInstance(User user) {
        StoriesListFragment storiesListFragment = new StoriesListFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_USER, user);
        storiesListFragment.setArguments(args);

        return storiesListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        if(extras != null && extras.containsKey(EXTRA_USER)) {
            user = extras.getParcelable(EXTRA_USER);
            publicType = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_stories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupObjects();
        setupView(view);
        loadStories();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        storyServices.removeChildEventListener(childEventListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof StoriesAdapter.OnPublishStoryListener) {
            onPublishStoryListener = (StoriesAdapter.OnPublishStoryListener) context;
        }

        if(context instanceof OnUserStartChattingListener) {
            onUserStartChattingListener = (OnUserStartChattingListener) context;
        }

        if(context instanceof FloatingActionButtonListener) {
            floatingActionButtonListener = (FloatingActionButtonListener) context;
        }
    }

    private void setupObjects() {
        storyServices = new StoryServices();
        userServices = new UserServices();
        contributionServices = new ContributionServices();
    }

    public void loadStories() {
        if(publicType) {
            storyServices.addChildEventListener(childEventListener);
        } else {
            storyServices.addChildEventListenerForUser(user, childEventListener);
        }
    }

    private void setupView(View view) {
        storiesList = (RecyclerView) view.findViewById(R.id.storiesList);
        storiesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        storiesList.addOnScrollListener(new RecyclerScrollListener(floatingActionButtonListener));
        setupStoriesAdapter();

        info = view.findViewById(R.id.info);
    }

    private void setupStoriesAdapter() {
        storiesAdapter = new StoriesAdapter();
        storiesAdapter.setHasStableIds(true);

        if(needsUserPublish()) storiesAdapter.setUser(user);

        storiesAdapter.setOnStoryViewListener(this);
        storiesAdapter.setOnPublishStoryListener(onPublishStoryListener);
        storiesAdapter.setOnUserStartChattingListener(onUserStartChattingListener);
        storiesList.setAdapter(storiesAdapter);
    }

    @Override
    public void onStoryViewClick(Story story) {
        Intent storyViewIntent = new Intent(getActivity(), StoryViewActivity.class);
        storyViewIntent.putExtra(StoryViewActivity.EXTRA_STORY, story);
        storyViewIntent.putExtra(StoryViewActivity.EXTRA_USER, user);
        startActivity(storyViewIntent);
    }

    public void updateUser(User user) {
        this.user = user;
        if(storiesAdapter != null && needsUserPublish()) storiesAdapter.setUser(user);
    }

    private void removeStory(Story story) {
        storiesAdapter.removeStory(story);
    }

    private void addStory(Story story) {
        if(!needsUserPublish())
            info.setVisibility(story != null ? View.GONE : View.VISIBLE);

        storiesAdapter.addStory(story);
    }

    private void updateStory(Story story) {
        storiesAdapter.updateStory(story);
    }

    private boolean needsUserPublish() {
        return publicType && UserManager.isUserLoggedIn() && user != null;
    }

    protected ChildEventListener childEventListener = new ChildEventListenerAdapter() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previous) {
            super.onChildAdded(dataSnapshot, previous);
            updateStoryFromSnapshot(dataSnapshot);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            super.onChildRemoved(dataSnapshot);
            Story story = getStoryFromSnapshot(dataSnapshot);
            removeStory(story);
        }
    };

    private void updateStoryFromSnapshot(DataSnapshot dataSnapshot) {
        final Story story = getStoryFromSnapshot(dataSnapshot);

        addStory(story);
        loadStoryData(story);
    }

    @NonNull
    private Story getStoryFromSnapshot(DataSnapshot dataSnapshot) {
        final Story story = dataSnapshot.getValue(Story.class);
        story.setKey(dataSnapshot.getKey());
        return story;
    }

    private void loadStoryData(final Story story) {
        contributionServices.getContributionCount(story, new OnStoryContributionCountListener() {
            @Override
            public void onStoryContributionCountListener(long count) {
                story.setContributions(Long.valueOf(count).intValue());
                loadUsersFromStory(story, onAfterStoryLoadedListener);
            }
        });
    }

    private OnAfterStoryLoadedListener onAfterStoryLoadedListener = new OnAfterStoryLoadedListener() {
        @Override
        public void onAfterStoryLoaded(Story story) {
            updateStory(story);
        }
    };

    private void loadUsersFromStory(final Story story, final OnAfterStoryLoadedListener onAfterStoryLoadedListener) {
        userServices.getUser(story.getUser(), new ValueEventListenerAdapter() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null) {
                    story.setUserObject(user);
                    onAfterStoryLoadedListener.onAfterStoryLoaded(story);
                }
            }
        });
    }

    private interface OnAfterStoryLoadedListener {
        void onAfterStoryLoaded(Story story);
    }
}
