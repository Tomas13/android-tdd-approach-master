package com.example.androidtdd.ui.comment;

import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.ui.base.BaseMvpView;

import java.util.List;

/**
 * Created by quanlt on 23/01/2017.
 */

public interface CommentMvpView extends BaseMvpView {
    void showComments(List<Comment> comments);
}
