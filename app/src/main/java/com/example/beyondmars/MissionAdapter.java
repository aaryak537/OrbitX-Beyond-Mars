package com.example.beyondmars;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MissionViewHolder> {


    private Context context;
    private ArrayList<Mission> missionList;
    private OnMissionClickListener listener;



    public interface OnMissionClickListener {

        void onClaimClick(Mission mission, int position);

    }



    public MissionAdapter(Context context,
                          ArrayList<Mission> missionList,
                          OnMissionClickListener listener) {

        this.context = context;
        this.missionList = missionList;
        this.listener = listener;

    }




    @NonNull
    @Override
    public MissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_mission, parent, false);


        return new MissionViewHolder(view);

    }




    @Override
    public void onBindViewHolder(@NonNull MissionViewHolder holder,
                                 int position) {


        Mission mission = missionList.get(position);



        // Title

        holder.txtMissionTitle.setText(
                mission.getTitle()
        );



        // Description

        holder.txtMissionDescription.setText(
                mission.getDescription()
        );



        // Progress Bar

        holder.progressMission.setMax(
                mission.getTarget()
        );


        holder.progressMission.setProgress(
                mission.getProgress()
        );



        holder.txtProgress.setText(

                mission.getProgress()
                        + " / "
                        + mission.getTarget()

        );



        // Rewards

        holder.txtCoinReward.setText(

                "🪙 " + mission.getCoinReward()

        );


        holder.txtGemReward.setText(

                "💎 " + mission.getGemReward()

        );




        // Mission Status

        if(mission.isCompleted()){


            holder.txtStatus.setText(
                    "Completed"
            );


            holder.txtStatus.setTextColor(
                    Color.parseColor("#00FF88")
            );



            if(mission.isClaimed()){


                holder.btnClaim.setText(
                        "Claimed"
                );


                holder.btnClaim.setEnabled(false);


                holder.btnClaim.setBackgroundColor(
                        Color.GRAY
                );


            }

            else {


                holder.btnClaim.setText(
                        "Claim Reward"
                );


                holder.btnClaim.setEnabled(true);


                holder.btnClaim.setOnClickListener(v -> {


                    if(listener != null){

                        listener.onClaimClick(
                                mission,
                                holder.getAdapterPosition()
                        );

                    }


                });


            }



        }


        else {


            holder.txtStatus.setText(
                    "In Progress"
            );


            holder.txtStatus.setTextColor(
                    Color.parseColor("#FFA726")
            );


            holder.btnClaim.setText(
                    "Locked"
            );


            holder.btnClaim.setEnabled(false);



        }



    }





    @Override
    public int getItemCount() {

        return missionList.size();

    }







    public static class MissionViewHolder extends RecyclerView.ViewHolder {



        TextView txtMissionTitle;
        TextView txtMissionDescription;

        TextView txtProgress;

        TextView txtCoinReward;
        TextView txtGemReward;

        TextView txtStatus;


        ProgressBar progressMission;


        Button btnClaim;




        public MissionViewHolder(@NonNull View itemView) {

            super(itemView);



            txtMissionTitle =
                    itemView.findViewById(
                            R.id.txtMissionTitle
                    );


            txtMissionDescription =
                    itemView.findViewById(
                            R.id.txtMissionDescription
                    );


            progressMission =
                    itemView.findViewById(
                            R.id.progressMission
                    );



            txtProgress =
                    itemView.findViewById(
                            R.id.txtProgress
                    );



            txtCoinReward =
                    itemView.findViewById(
                            R.id.txtCoinReward
                    );



            txtGemReward =
                    itemView.findViewById(
                            R.id.txtGemReward
                    );



            txtStatus =
                    itemView.findViewById(
                            R.id.txtStatus
                    );



            btnClaim =
                    itemView.findViewById(
                            R.id.btnClaim
                    );

        }

    }


}