package util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investmentkorea.android.admin.AdminApplication;
import com.investmentkorea.android.admin.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.SayingModel;
import util.Util;

public class SayingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;

    private Context context;
    private ArrayList<SayingModel> sayingModelArrayList;
    private SayingAdapterListener sayingAdapterListener;

    public SayingAdapter(Context context, ArrayList<SayingModel> sayingModelArrayList, SayingAdapterListener sayingAdapterListener){
        this.sayingModelArrayList = sayingModelArrayList;
        this.sayingAdapterListener = sayingAdapterListener;
        this.context = context;
    }

    public interface SayingAdapterListener{
        void clickItem(SayingModel sayingModel, int pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_saying, parent, false);
            return new Saying_VH(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private SayingModel getItem(int position) {
        return sayingModelArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Saying_VH) {
            final SayingModel currentItem = getItem(position);
            final Saying_VH VHitem = (Saying_VH)holder;

            if(isToday(position)){
                VHitem.monthTv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                VHitem.dayTv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else{
                VHitem.monthTv.setTextColor(context.getResources().getColor(R.color.colorGray));
                VHitem.dayTv.setTextColor(context.getResources().getColor(R.color.colorGray));
            }

            VHitem.monthTv.setText(getCreatedAt(position).split("-")[1]);
            VHitem.dayTv.setText(getCreatedAt(position).split("-")[2]);

            VHitem.contentsTv.setText(currentItem.getContents());
            VHitem.authorTv.setText(currentItem.getAuthorName());

            VHitem.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sayingAdapterListener.clickItem(currentItem, position);
                }
            });

        }
    }

    private String getCreatedAt(int position){
        return Util.parseTimeWithoutTimeByCustom(getItem(position).getCreatedAt());
    }

    private boolean isToday(int position){
        String date = Util.parseTimeWithoutTimeByCustom(getItem(position).getCreatedAt());
        return date.equals(AdminApplication.TODAY_YEAR+"-"+AdminApplication.TODAY_MONTH+"-"+AdminApplication.TODAY_DAY);
    }

    public void onItemDismiss(int position){
        sayingModelArrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class Saying_VH extends RecyclerView.ViewHolder{
        @BindView(R.id.item_layout) ViewGroup itemLayout;
        @BindView(R.id.month_tv) TextView monthTv;
        @BindView(R.id.day_tv) TextView dayTv;
        @BindView(R.id.contents_tv) TextView contentsTv;
        @BindView(R.id.author_tv) TextView authorTv;

        private Saying_VH(View itemView){
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
        return sayingModelArrayList.size();
    }
}

