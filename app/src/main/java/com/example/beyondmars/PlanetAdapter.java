package com.example.beyondmars;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder> {

    private final Context context;
    private final ArrayList<PlanetModel> planetList;

    public PlanetAdapter(Context context, ArrayList<PlanetModel> planetList) {
        this.context = context;
        this.planetList = planetList;
    }

    @NonNull
    @Override
    public PlanetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_planet, parent, false);

        return new PlanetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetViewHolder holder, int position) {

        PlanetModel planet = planetList.get(position);

        holder.imgPlanet.setImageResource(planet.getImageResId());
        holder.txtPlanet.setText(planet.getName());
        holder.txtLevel.setText(planet.getStatus());

        if (planet.isLocked()) {

            holder.imgLock.setVisibility(View.VISIBLE);

            holder.btnPlay.setEnabled(false);
            holder.btnPlay.setText("LOCKED");
            holder.btnPlay.setAlpha(0.6f);
            holder.imgPlanet.setAlpha(0.5f);
        } else {
            holder.imgLock.setVisibility(View.GONE);
            holder.btnPlay.setEnabled(true);
            holder.btnPlay.setText("PLAY");
            holder.btnPlay.setAlpha(1f);

            holder.imgPlanet.setAlpha(1f);
            holder.btnPlay.setOnClickListener(v -> {

                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("planet_name", planet.getName());
                context.startActivity(intent);
            });
        }
    }
    @Override
    public int getItemCount() {
        return planetList.size();
    }
    static class PlanetViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPlanet,imgLock;
        TextView txtPlanet,txtLevel;
        Button btnPlay;
        PlanetViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPlanet = itemView.findViewById(R.id.imgPlanet);
            imgLock = itemView.findViewById(R.id.imgLock);

            txtPlanet = itemView.findViewById(R.id.txtPlanet);
            txtLevel = itemView.findViewById(R.id.txtLevel);

            btnPlay = itemView.findViewById(R.id.btnPlay);
        }
    }
}