
package com.linkb.jstx.listener;

import com.linkb.jstx.model.Comment;
import com.linkb.jstx.component.CommentListView;
import com.linkb.jstx.model.Moment;

public interface OnCommentSelectedListener {

    /**
     * @param commentListView 被评论的文章的评论列表view
     * @param moment
     * @param comment
     */
    void onCommentSelected(CommentListView commentListView, Moment moment,Comment comment);
}
