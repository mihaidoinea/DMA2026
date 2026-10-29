package ro.ase.ie.g1098.model;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
    private String title; //EditText
    private Date release; //EditText date
    private String genre; //Spinner
    private double budget; //EditText number
    private int duration; //SeekBar
    private float rating; //RatingBar
    private boolean watched; //Switch
    private String approval; //RadioGroup | RadioButtons
    private boolean onWatchlist; //Checkbox

    public Movie(String approval, double budget, int duration, String genre, boolean onWatchlist, float rating, Date release, String title, boolean watched) {
        this.approval = approval;
        this.budget = budget;
        this.duration = duration;
        this.genre = genre;
        this.onWatchlist = onWatchlist;
        this.rating = rating;
        this.release = release;
        this.title = title;
        this.watched = watched;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "approval='" + approval + '\'' +
                ", title='" + title + '\'' +
                ", release=" + release +
                ", genre='" + genre + '\'' +
                ", budget=" + budget +
                ", duration=" + duration +
                ", rating=" + rating +
                ", watched=" + watched +
                ", onWatchlist=" + onWatchlist +
                '}';
    }
}

