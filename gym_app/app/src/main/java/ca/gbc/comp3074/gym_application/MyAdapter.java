package ca.gbc.comp3074.gym_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Gyms> mList;
    private Context mContext;
    private LayoutInflater mInflator;
    private Gyms mGyms;
    public RecyclerViewClickListner listener;

    public MyAdapter(Context context, List<Gyms> list, RecyclerViewClickListner listener) {  //needed to pass the context and the list that contains the data
        mContext=context;
        mList=list;
        mInflator=LayoutInflater.from(context);
        this.listener=listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for telling the recycler view how the cell should look like
        View v=mInflator.inflate(R.layout.recyclerview_layout,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder)holder;
        mGyms =mList.get(position);

        myHolder.gymName.setText(mGyms.gymName);
        myHolder.gymDescription.setText(mGyms.gymDescription);
        myHolder.gymImage.setImageResource(mGyms.gymPicId);
        myHolder.gymLongitude.setText(mGyms.gymLongitude);
        myHolder.gymLatitude.setText(mGyms.gymLatitude);


    }



    @Override
    public int getItemCount() {
        return mList.size();
    }
    public interface RecyclerViewClickListner{
        void onClick(View v, int position);
    }

    //Creating a new class that will be responsible to connect the layout we created
    //recyclerview_layout and the class also connects the components of the recyclerview_layout class
    //to this class

    public  class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView gymImage;          //because i want to connect the componenets in the recyclerview_layout.xml
        TextView gymName, gymDescription,gymLongitude,gymLatitude;
        Button gymUpdate, gymDelete;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            gymName =(TextView)itemView.findViewById(R.id.layoutTitle); //R.id.layoutTitle comes from recyclerview_layout.xml ->connecting components now
            gymDescription =(TextView)itemView.findViewById(R.id.layoutDescription);
            gymImage =(ImageView)itemView.findViewById(R.id.layoutImage);
            gymLatitude=(TextView)itemView.findViewById(R.id.layoutLatitude);
            gymLongitude=(TextView)itemView.findViewById(R.id.layoutLongitude);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
