package util.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investmentkorea.android.admin.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.AuthorModel;

public class AuthorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private ArrayList<AuthorModel> authorModelArrayList;

    public AuthorAdapter(ArrayList<AuthorModel> authorModelArrayList){
        this.authorModelArrayList = authorModelArrayList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_author, parent, false);
            return new Author_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private AuthorModel getItem(int position) {
        return authorModelArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Author_VH) {
            final AuthorModel currentItem = getItem(position);
            final Author_VH VHitem = (Author_VH)holder;

            VHitem.authorTv.setText(currentItem.getAuthorName());
        }
    }

    public void onItemDismiss(int position){
        authorModelArrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class Author_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.author_tv) TextView authorTv;

        private Author_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return authorModelArrayList.size();
    }
}

