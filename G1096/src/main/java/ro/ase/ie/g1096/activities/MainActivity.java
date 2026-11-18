package ro.ase.ie.g1096.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ro.ase.ie.g1096.R;
import ro.ase.ie.g1096.adapter.MovieAdapter;
import ro.ase.ie.g1096.model.Movie;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieListener {

    protected static final String POSITION_EXTRA = "position";
    protected static final String MOVIE_EXTRA = "movie";
    private FloatingActionButton fabInsert;
    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> launcher;
    private final List<Movie> movies = new ArrayList<>();
    //    private ArrayAdapter<Movie> movieAdapter;
    private MovieAdapter movieAdapter;

    private Button sortingButton;
    private Button ratingButton;
    private Button statisticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addSampleMovies();
        movieAdapter = new MovieAdapter(movies, this);

//        movieAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movies);
        initControls();
        initListeners();

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == RESULT_OK)
                        {
                            Intent intent = o.getData();
                            Movie movie = (Movie) intent.getSerializableExtra(MOVIE_EXTRA);
                            int position = intent.getIntExtra(POSITION_EXTRA, -1);
                            if(position == -1)
                            {
                                movies.add(movie);
                                movieAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                movies.set(position, movie);
                                movieAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void initListeners() {
        fabInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                launcher.launch(intent);
            }
        });

        sortingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movies.sort((o1, o2) -> {
                    return o1.getTitle().compareTo(o2.getTitle());
                });
                movieAdapter.notifyDataSetChanged();
            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movies.sort((o1, o2) -> {
                    return Float.compare(o2.getRating(), o1.getRating());
                });
                movieAdapter.notifyDataSetChanged();
            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float totalBuget = 0.0f;

                float avgRating = 0.0f;

                int numberOfMovies = movies.size();

                for(Movie mov : movies) {
                    totalBuget += mov.getBudget();
                    avgRating += mov.getRating();
                }
                avgRating /= numberOfMovies;
                totalBuget = Math.round(totalBuget);

                new AlertDialog.Builder(MainActivity.this).setTitle("Statistics").setMessage("" +
                        "Total Buget: " + totalBuget +
                        "\n Avg rating: " + avgRating +
                        "\n Nr Movies: " + numberOfMovies)
                        .setNeutralButton("Ok", (dialog, which) ->
                {

                }).show();

            }
        });


       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
    }

    private void initControls() {
        fabInsert = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.rvMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        sortingButton = findViewById(R.id.sortingButton);
        ratingButton = findViewById(R.id.ratingButton);
        statisticsButton = findViewById(R.id.statisticsButton);


    }

    private void addSampleMovies() {
        movies.add(new Movie(
                "Inception",
                new GregorianCalendar(2010, Calendar.JULY, 16).getTime(),
                "Sci-Fi",
                160000000,
                148,
                4.5f,
                true,
                "PG-13",
                false,
                "https://example.com/posters/inception.jpg"));

        movies.add(new Movie(
                "Toy Story",
                new GregorianCalendar(1995, Calendar.NOVEMBER, 22).getTime(),
                "Animation",
                30000000,
                81,
                4f,
                true,
                "G",
                false,
                "https://example.com/posters/toy-story.jpg"));

        movies.add(new Movie(
                "The Matrix",
                new GregorianCalendar(1999, Calendar.MARCH, 31).getTime(),
                "Action",
                63000000,
                136,
                5f,
                false,
                "R",
                true,
                "https://example.com/posters/the-matrix.jpg"));
    }

    @Override
    public void onMovieClick(Movie movie, int position) {
        Intent intent = new Intent(MainActivity.this, ItemActivity.class);
        intent.putExtra(MOVIE_EXTRA, movies.get(position));
        intent.putExtra(POSITION_EXTRA, position);
        launcher.launch(intent);
    }

    public void onMovieLongClick(Movie movie, int position) {
        new AlertDialog.Builder(MainActivity.this).setTitle("Delete movie").setMessage(movie.getTitle())
                .setNegativeButton("Delete", (dialog, which) -> {

                    movies.remove(movie);
                    movieAdapter.notifyDataSetChanged();

                    Snackbar.make(MainActivity.this.findViewById(R.id.main), "Undo delete",  Snackbar.LENGTH_LONG).setAction("Recover", v -> {
                        movies.add(movie);
                        movieAdapter.notifyDataSetChanged();

                        Toast.makeText(this, "Restored changes", Toast.LENGTH_SHORT).show();
                    }).show();


                }).setPositiveButton("Cancel", null).show();
    }
}

/*
new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
@Override
public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder a,
RecyclerView.ViewHolder b) {
return false;
}
@Override
public void onSwiped(RecyclerView.ViewHolder h, int direction) {
deleteAt(h.getBindingAdapterPosition());
}
}).attachToRecyclerView(binding.list);  
*/