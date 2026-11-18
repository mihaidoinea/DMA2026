package ro.ase.ie.g1096.adapter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ro.ase.ie.g1096.R;
import ro.ase.ie.g1096.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    /** Optional callbacks so the activity can react to taps. */
    public interface OnMovieListener {
        void onMovieClick(Movie movie, int position);
        void onMovieLongClick(Movie movie, int position);

    }

    private final List<Movie> movies;
    private final OnMovieListener listener;   // may be null

    private static final SimpleDateFormat YEAR = new SimpleDateFormat("yyyy", Locale.US);

    public MovieAdapter(List<Movie> movies, OnMovieListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder h, int position) {
        Movie m = movies.get(position);

        // --- text ---
        h.title.setText(m.getTitle());

        String year = (m.getRelease() != null) ? YEAR.format(m.getRelease()) : "—";
        h.subtitle.setText(m.getGenre() + " · " + year);
        h.rating.setRating(m.getRating());
        h.ratingValue.setText(String.format(Locale.US, "%.1f", m.getRating()));
        h.facts.setText(m.getDuration() + " min  · " + formatBudget(m.getBudget()));
        h.chipApproval.setText(m.getApproval());
        h.chipWatched.setChecked(m.isWatched());
        h.chipWatchlist.setChecked(m.isOnWatchlist());

        //G verde, pg portocaliu, r nc rosu
        if(m.getApproval().toLowerCase().equals("g") || m.getApproval().toLowerCase().equals("pg")){
            h.chipApproval.setChipBackgroundColor(ColorStateList.valueOf(Color.rgb(0, 230, 0)));
        }
        else if(m.getApproval().toLowerCase().equals("pg-13")){
            h.chipApproval.setChipBackgroundColor(ColorStateList.valueOf(Color.rgb(234, 130, 0)));
        }
        else {
            h.chipApproval.setChipBackgroundColor(ColorStateList.valueOf(Color.rgb(234, 0, 0)));
        }

        if(m.isWatched()){
            h.chipWatched.setVisibility(View.VISIBLE);
        } else {
            h.chipWatched.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // ---- helpers ----

    private String formatBudget(double budget) {
        if (budget >= 1_000_000)   return String.format(Locale.US, "$%.0fM", budget / 1_000_000);
        if (budget >= 1_000)       return String.format(Locale.US, "$%.0fK", budget / 1_000);
        return String.format(Locale.US, "$%.0f", budget);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        final ImageView poster;
        final TextView title;
        final TextView subtitle;
        final RatingBar rating;
        final TextView ratingValue;
        final TextView facts;
        final Chip chipApproval;
        final Chip chipWatched;
        final Chip chipWatchlist;

        MovieViewHolder(@NonNull View v) {
            super(v);
            poster       = v.findViewById(R.id.moviePoster);
            title        = v.findViewById(R.id.movieTitle);
            subtitle     = v.findViewById(R.id.movieSubtitle);
            rating       = v.findViewById(R.id.movieRating);
            ratingValue  = v.findViewById(R.id.movieRatingValue);
            facts        = v.findViewById(R.id.movieFacts);
            chipApproval = v.findViewById(R.id.chipApproval);
            chipWatched  = v.findViewById(R.id.chipWatched);
            chipWatchlist= v.findViewById(R.id.chipWatchlist);

            v.setOnClickListener(click -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onMovieClick(movies.get(pos), pos);
                }
            });
            v.setOnLongClickListener(click -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onMovieLongClick(movies.get(pos), pos);
                    return  true;
                }
                return false;
            });


        }
    }
}