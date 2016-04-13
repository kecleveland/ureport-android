package in.ureport.views.holders;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Map;

import in.ureport.R;
import in.ureport.fragments.RecordAudioFragment;
import in.ureport.helpers.ImageLoader;
import in.ureport.helpers.TimeFormatter;
import in.ureport.helpers.YoutubePlayer;
import in.ureport.models.ChatMessage;
import in.ureport.models.Media;
import in.ureport.models.User;
import in.ureport.views.adapters.ChatMessagesAdapter;

/**
 * Created by john-mac on 4/11/16.
 */
public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    private ChatMessage chatMessage;

    private ViewGroup parent;

    private TextView message;
    private TextView date;
    private TextView name;
    private TextView youtubeLink;
    private ImageView media;

    private View loadingAudio;
    private ImageView playAudio;
    private TextView durationAudio;
    private SeekBar progressAudio;
    private TextView filename;
    private TextView filetype;

    private OnChatMessageSelectedListener onChatMessageSelectedListener;

    private final DateFormat hourFormatter;
    private final YoutubePlayer youtubePlayer;
    private final Context context;

    private RecordAudioFragment recordAudioFragment;

    public ChatMessageViewHolder(Context context, int viewType) {
        super(LayoutInflater.from(context).inflate(R.layout.item_chat_message, null));
        this.context = context;
        this.hourFormatter = DateFormat.getTimeInstance(DateFormat.SHORT);
        this.youtubePlayer = new YoutubePlayer((Activity)itemView.getContext());
        this.parent = (ViewGroup) itemView.findViewById(R.id.bubble);
        this.name = (TextView) itemView.findViewById(R.id.name);

        this.itemView.setOnLongClickListener(onLongClickListener);
        this.parent.addView(getViewForType(context, viewType));
    }

    @Nullable
    private View getViewForType(Context context, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case ChatMessagesAdapter.TYPE_PICTURE:
                return inflater.inflate(R.layout.item_chat_message_image, parent, false);
            case ChatMessagesAdapter.TYPE_YOUTUBE:
                return inflater.inflate(R.layout.item_chat_message_youtube, parent, false);
            case ChatMessagesAdapter.TYPE_VIDEO:
                return inflater.inflate(R.layout.item_chat_message_video, parent, false);
            case ChatMessagesAdapter.TYPE_AUDIO:
                return inflater.inflate(R.layout.item_chat_message_audio, parent, false);
            case ChatMessagesAdapter.TYPE_FILE:
                return inflater.inflate(R.layout.item_chat_message_file, parent, false);
        }
        return inflater.inflate(R.layout.item_chat_message_text, parent, false);
    }

    public void bindView(User user, ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
        bindContainer(user, chatMessage);

        if(chatMessage.getMedia() != null) {
            media = (ImageView) findIfNeeded(media, R.id.chatMessageMedia);
            switch (chatMessage.getMedia().getType()) {
                case Picture:
                    media.setOnClickListener(onMediaClickListener);
                    ImageLoader.loadGenericPictureToImageViewFit(media, chatMessage.getMedia());
                    break;
                case Video:
                    youtubeLink = (TextView) findIfNeeded(youtubeLink, R.id.chatMessageYoutubeLink);
                    youtubeLink.setText(youtubePlayer.getYoutubeLinkById(chatMessage.getMedia().getId()));

                    media.setOnClickListener(onVideoMediaClickListener);
                    ImageLoader.loadGenericPictureToImageViewFit(media, chatMessage.getMedia());
                    break;
                case VideoPhone:
                    media.setOnClickListener(onMediaClickListener);
                    ImageLoader.loadGenericPictureToImageViewFit(media, chatMessage.getMedia().getThumbnail());
                    break;
                case File:
                    itemView.setOnClickListener(onMediaClickListener);
                    prepareForSpecialContent(user, chatMessage);

                    filename = (TextView) findIfNeeded(filename, R.id.chatMessageFileName);
                    filetype = (TextView) findIfNeeded(filetype, R.id.chatMessageFileType);

                    Map<String, Object> metadata = chatMessage.getMedia().getMetadata();
                    if(metadata != null && metadata.containsKey(Media.KEY_FILENAME)) {
                        String filenameMetadata = (String) chatMessage.getMedia().getMetadata().get(Media.KEY_FILENAME);
                        filename.setText(filenameMetadata);
                        filetype.setText(getFileExt(filenameMetadata));
                    }
                    break;
                case Audio:
                    prepareForSpecialContent(user, chatMessage);

                    loadingAudio = itemView.findViewById(R.id.chatMessageLoadingAudio);
                    playAudio = (ImageView) itemView.findViewById(R.id.chatMessagePlayAudio);
                    progressAudio = (SeekBar) itemView.findViewById(R.id.chatMessageProgressAudio);
                    durationAudio = (TextView) itemView.findViewById(R.id.chatMessageDurationAudio);

                    if(!chatMessage.getMedia().equals(recordAudioFragment.getCurrentMedia())) {
                        resetAudioView();
                        if(recordAudioFragment.isPlayView(playAudio)) {
                            recordAudioFragment.resetPlayback();
                        }
                    } else {
                        recordAudioFragment.setCustomPlayback(playAudio, durationAudio, loadingAudio, progressAudio);
                    }

                    playAudio.setOnClickListener(onPlayCustomClickListener);
            }
        } else {
            message = (TextView) findIfNeeded(message, R.id.chatMessage);
            message.setText(chatMessage.getMessage());
        }

        date = (TextView) findIfNeeded(date, R.id.chatMessageDate);
        date.setText(hourFormatter.format(chatMessage.getDate()));
    }

    private void prepareForSpecialContent(User user, ChatMessage chatMessage) {
        int leftPadding = isUserAuthor(user, chatMessage) ? 0 : getDpDimen(7);
        int rightPadding = isUserAuthor(user, chatMessage) ? getDpDimen(7) : 0;
        parent.setPadding(leftPadding, 0, rightPadding, 0);
        name.setVisibility(View.GONE);
    }

    private void resetAudioView() {
        loadingAudio.setVisibility(View.GONE);
        progressAudio.setProgress(0);
        playAudio.setImageResource(R.drawable.ic_play_arrow_blue_36dp);
        Map<String, Object> metadata = chatMessage.getMedia().getMetadata();
        if(metadata != null && metadata.containsKey(Media.KEY_DURATION)) {
            durationAudio.setText(TimeFormatter.getDurationString((Integer)metadata.get(Media.KEY_DURATION)));
        } else {
            durationAudio.setText(null);
        }
    }

    private void bindContainer(User user, ChatMessage chatMessage) {
        boolean userAuthor = isUserAuthor(user, chatMessage);
        int smallSpace = getDpDimen(10);
        int largeSpace = getDpDimen(40);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        int rule = userAuthor ? RelativeLayout.ALIGN_PARENT_END : RelativeLayout.ALIGN_PARENT_START;
        params.addRule(rule);
        params.leftMargin = userAuthor ? largeSpace : smallSpace;
        params.rightMargin = userAuthor ? smallSpace : largeSpace;
        parent.setLayoutParams(params);

        int drawable = userAuthor ? R.drawable.bubble_me : R.drawable.bubble_other;
        parent.setBackgroundResource(drawable);

        name.setVisibility(userAuthor ? View.GONE : View.VISIBLE);
        name.setText(chatMessage.getUser().getNickname());
    }

    public String getFileExt(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toUpperCase();
        } catch(Exception exception) {
            return "";
        }
    }

    private int getDpDimen(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value
                , context.getResources().getDisplayMetrics());
    }

    private boolean isUserAuthor(User user, ChatMessage chatMessage) {
        return user != null && chatMessage.getUser().equals(user);
    }

    private View findIfNeeded(View view, @IdRes int id) {
        if(view == null) {
            return itemView.findViewById(id);
        }
        return view;
    }
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if(onChatMessageSelectedListener != null) {
                onChatMessageSelectedListener.onChatMessageSelected(chatMessage);
            }
            return false;
        }
    };

    private View.OnClickListener onMediaClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(onChatMessageSelectedListener != null) {
                if(view instanceof ImageView) {
                    onChatMessageSelectedListener.onMediaChatMessageView(chatMessage, (ImageView)view);
                } else {
                    onChatMessageSelectedListener.onMediaChatMessageView(chatMessage, null);
                }
            }
        }
    };

    private View.OnClickListener onVideoMediaClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            youtubePlayer.playVideoMedia(chatMessage.getMedia());
        }
    };

    private View.OnClickListener onPlayCustomClickListener = v -> {
        if (recordAudioFragment.isStopped() || !recordAudioFragment.getCurrentMedia().equals(chatMessage.getMedia())) {
            recordAudioFragment.resetPlayback();
            recordAudioFragment.setCustomPlayback(playAudio, durationAudio, loadingAudio, progressAudio);
            recordAudioFragment.loadNewMedia(chatMessage.getMedia());
        } else {
            recordAudioFragment.togglePlayback();
        }
    };

    public void setRecordAudioFragment(RecordAudioFragment recordAudioFragment) {
        this.recordAudioFragment = recordAudioFragment;
    }

    public void setOnChatMessageSelectedListener(OnChatMessageSelectedListener onChatMessageSelectedListener) {
        this.onChatMessageSelectedListener = onChatMessageSelectedListener;
    }

    public interface OnChatMessageSelectedListener {
        void onMediaChatMessageView(ChatMessage chatMessage, ImageView mediaImageView);
        void onChatMessageSelected(ChatMessage chatMessage);
    }
}