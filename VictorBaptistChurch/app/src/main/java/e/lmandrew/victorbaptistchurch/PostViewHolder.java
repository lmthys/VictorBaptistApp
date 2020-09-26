package e.lmandrew.victorbaptistchurch;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public PostViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setTitle(String title) {
        TextView post_title = mView.findViewById(R.id.postTitle);
        post_title.setText(title);
    }

    public void setDesc(String desc) {
        TextView post_desc = mView.findViewById(R.id.postSum);
        post_desc.setText(desc);
    }

    public void setUserName(String userName) {
        TextView postUserName = mView.findViewById(R.id.username);
        postUserName.setText(userName);
    }


}
