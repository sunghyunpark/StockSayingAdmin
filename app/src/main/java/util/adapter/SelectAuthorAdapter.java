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

public class SelectAuthorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<AuthorModel> authorModelArrayList;
    private SelectAuthorAdapterListener selectAuthorAdapterListener;

    public SelectAuthorAdapter(ArrayList<AuthorModel> authorModelArrayList, SelectAuthorAdapterListener selectAuthorAdapterListener){
        this.authorModelArrayList = authorModelArrayList;
        this.selectAuthorAdapterListener = selectAuthorAdapterListener;
    }

    public interface SelectAuthorAdapterListener{
        void selectAuthor(String authorName);
        void selectAll();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_select_author, parent, false);
            return new Area_VH(v);
        }else if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_header_select_author, parent, false);
            return new Header_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private AuthorModel getItem(int position) {
        return authorModelArrayList.get(position-1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Area_VH) {
            final AuthorModel currentItem = getItem(position);
            final Area_VH VHitem = (Area_VH)holder;

            VHitem.author_name_tv.setText(currentItem.getAuthorName());

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectAuthorAdapterListener.selectAuthor(currentItem.getAuthorName());
                }
            });

        }else if(holder instanceof Header_VH){
            final Header_VH VHitem = (Header_VH)holder;

            VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectAuthorAdapterListener.selectAll();
                }
            });
        }
    }

    public class Area_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup item_layout;
        @BindView(R.id.author_name_tv) TextView author_name_tv;

        private Area_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class Header_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup item_layout;

        private Header_VH(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }
    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return authorModelArrayList.size()+1;
    }
}
