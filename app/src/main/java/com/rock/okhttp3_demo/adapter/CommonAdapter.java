package com.rock.okhttp3_demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rock.okhttp3_demo.R;
import com.rock.okhttp3_demo.bean.main.MeinvBean;
import com.rock.okhttp3_demo.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Rock on 2018/6/26.
 *
 * 普通的Adapter
 */

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.CommomHolder> {

    private Context mContext ;
    private LayoutInflater inflater;
    private List<MeinvBean.ResultsBean> meinvBeen;
    private ImageView imageView;

    public CommonAdapter(Context context,List<MeinvBean.ResultsBean> meinvBeen){

        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.meinvBeen = meinvBeen;
    }

    public void notifyData(List<MeinvBean.ResultsBean> einvBeen){
        this.meinvBeen = einvBeen;
        notifyDataSetChanged();
    }

    @Override
    public CommomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycleview_item_simple_image,parent,false);
        CommomHolder commomHolder = new CommomHolder(view);
        return commomHolder;
    }

    @Override
    public void onBindViewHolder(CommomHolder holder, int position) {

       holder.onbind(meinvBeen.get(position));
    }


    @Override
    public int getItemCount() {
        return meinvBeen == null ? 0: meinvBeen.size();
    }

    public class CommomHolder extends RecyclerView.ViewHolder {


        private final TextView txt;

        public CommomHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.simple_image);
            txt = itemView.findViewById(R.id.simple_title);
        }

        public void onbind(MeinvBean.ResultsBean resultsBean) {
            MeinvBean.ResultsBean b = (MeinvBean.ResultsBean) resultsBean;
//            ImageLoaderUtils.display(mContext,imageView,b.getDesc());
            txt.setText(b.getDesc());
        }
    }
}
