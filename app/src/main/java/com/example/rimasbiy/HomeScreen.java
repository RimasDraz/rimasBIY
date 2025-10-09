package com.example.rimasbiy;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.the_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId()==R.id.itmFavorites)
        {
            Toast.makeText(this,"Favorites",Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId()==R.id.itmSettings)
        {
            Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId()==R.id.itmitem)
        {
            Toast.makeText(this,"itemm",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}