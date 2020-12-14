package ca.gbc.comp3074.gym_application;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
        myHolder.gymLongitude.setText(mGyms.gymId);
        myHolder.gymLatitude.setText(mGyms.gymLatitude);

        myHolder.menuPopUp.setImageResource(mGyms.gymPopUpMenuId);
        myHolder.menuPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(mContext, view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu,popupMenu.getMenu());
                String userRole= mList.get(position).loggedUserRole;
//                Toast.makeText(mContext,userRole, Toast.LENGTH_LONG).show();
                if(userRole.equals("ADMIN")) {
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.popUpEdit:
                                Intent i=new Intent(mContext, MapActivity.class);


                                String id=mList.get(position).gymId;
                                i.putExtra("gymId", id);
                                mContext.startActivity(i);
//                                Toast.makeText(mContext,"gymId  " +  id, Toast.LENGTH_LONG).show();
                                break;
                            case R.id.popUpDelete:

//                                Toast.makeText(mContext,"Role " + ROLE, Toast.LENGTH_LONG).show();
                                deleteItem(position);

                                Toast.makeText(mContext,"Item successfully deleted", Toast.LENGTH_LONG).show();
                                break;
                        }


                        return false;
                    }
                });}else{
                    Toast.makeText(mContext,"Click on the Title for DETAILED Information", Toast.LENGTH_LONG).show();
                }


            }
        });


    }


//    public int editItem(String id){
//        DBHelper DB;
//        Intent i =new Intent(mContext,MapActivity.class);
//
//    }
    public boolean deleteItem(int position ){
        DBHelper DB;
        DB=new DBHelper(mContext);
        boolean result= DB.deleteGym(mList.get(position).gymId);
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mList.size());
        return result;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public interface RecyclerViewClickListner{
        void onClick(View v, int position);
    }
    public  class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView gymImage, menuPopUp;          //because i want to connect the componenets in the recyclerview_layout.xml
        TextView gymName, gymDescription,gymLongitude,gymLatitude;
        Button gymUpdate, gymDelete;

        private Context mContext;
        private ArrayList<ColorSpace.Model>list;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            gymName =(TextView)itemView.findViewById(R.id.layoutTitle); //R.id.layoutTitle comes from recyclerview_layout.xml ->connecting components now
            gymDescription =(TextView)itemView.findViewById(R.id.layoutDescription);
            gymImage =(ImageView)itemView.findViewById(R.id.layoutImage);
            gymLatitude=(TextView)itemView.findViewById(R.id.layoutLatitude);
            gymLongitude=(TextView)itemView.findViewById(R.id.layoutLongitude);
            menuPopUp=(ImageView)itemView.findViewById(R.id.imgMore) ;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
