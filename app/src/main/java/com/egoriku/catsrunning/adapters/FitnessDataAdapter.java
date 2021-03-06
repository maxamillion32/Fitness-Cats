package com.egoriku.catsrunning.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.egoriku.catsrunning.R;
import com.egoriku.catsrunning.adapters.interfaces.IOnItemHandlerListener;
import com.egoriku.catsrunning.models.AllFitnessDataModel;
import com.egoriku.catsrunning.utils.ConverterTime;

import java.util.ArrayList;
import java.util.List;

import static com.egoriku.catsrunning.utils.VectorToDrawable.setImageAdapter;

public class FitnessDataAdapter extends AbstractAdapter<AllFitnessDataModel> {

    private List<AllFitnessDataModel> modelList = new ArrayList<>();
    private IOnItemHandlerListener onItemListener;

    public FitnessDataAdapter() {
    }


    public void setOnItemListener(IOnItemHandlerListener onItemListener) {
        this.onItemListener = onItemListener;
    }


    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fitness_data_fragment, parent, false);
        return new AbstractViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }


    @Override
    public void onBind(AbstractViewHolder holder, final AllFitnessDataModel allFitnessDataModel, final int position, int viewType) {
        holder.<TextView>get(R.id.adapter_all_fitness_data_date_text)
                .setText(ConverterTime.convertUnixDateWithoutHours(allFitnessDataModel.getBeginsAt()));

        holder.<TextView>get(R.id.adapter_all_fitness_data_time_text_view)
                .setText(ConverterTime.ConvertTimeAllFitnessData(allFitnessDataModel.getBeginsAt(), allFitnessDataModel.getTime()));

        holder.<TextView>get(R.id.adapter_all_fitness_data_distance_text_view)
                .setText(
                        String.format(
                                holder.<String>getString(R.string.adapter_all_fitness_data_distance_meter),
                                allFitnessDataModel.getDistance()
                        )
                );

        holder.<TextView>get(R.id.adapter_all_fitness_data_calories_text_view);

        holder.<String>getString(R.string.adapter_all_fitness_data_calories);

        switch (allFitnessDataModel.getLiked()) {
            case 0:
                setImageAdapter(holder.<ImageView>get(R.id.adapter_all_fitness_data_image_liked), R.drawable.ic_vec_star_border);
                break;

            case 1:
                setImageAdapter(holder.<ImageView>get(R.id.adapter_all_fitness_data_image_liked), R.drawable.ic_vec_star_black);
                break;
        }

        switch (allFitnessDataModel.getTypeFit()) {
            case 1:
                setImageAdapter(holder.<ImageView>get(R.id.adapter_all_fitness_data_ic_type), R.drawable.ic_vec_directions_walk_40dp);
                break;

            case 2:
                setImageAdapter(holder.<ImageView>get(R.id.adapter_all_fitness_data_ic_type), R.drawable.ic_vec_directions_run_40dp);
                break;

            case 3:
                setImageAdapter(holder.<ImageView>get(R.id.adapter_all_fitness_data_ic_type), R.drawable.ic_vec_directions_bike_40dp);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemListener != null) {
                    onItemListener.onClickItem(allFitnessDataModel, position);
                }
            }
        });

        holder.<ImageView>get(R.id.adapter_all_fitness_data_image_liked).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemListener != null) {
                    onItemListener.onLikedClick(allFitnessDataModel, position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemListener != null) {
                    onItemListener.onLongClick(allFitnessDataModel, position);
                }
                return true;
            }
        });
    }


    @Override
    public AllFitnessDataModel getItem(int position) {
        return modelList.get(position);
    }


    public void setData(List<AllFitnessDataModel> allFitnessDataModels) {
        this.modelList = allFitnessDataModels;
        notifyDataSetChanged();
    }


    public void clear(){
        modelList.clear();
        notifyDataSetChanged();
    }
}
