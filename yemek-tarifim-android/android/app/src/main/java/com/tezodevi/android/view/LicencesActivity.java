package com.tezodevi.android.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tezodevi.android.adapter.LicencesAdapter;
import com.tezodevi.android.databinding.ActivityLicencesBinding;

import java.util.ArrayList;
import java.util.List;

public class LicencesActivity extends AppCompatActivity {

    private ActivityLicencesBinding binding;
    private List<String> licencesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLicencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.licencesActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        licencesList = getLicences();
        LicencesAdapter adapter = new LicencesAdapter(licencesList);
        binding.licencesActivityRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private List<String> getLicences() {
        final List<String> tempLicenceList = new ArrayList<>();

        tempLicenceList.add("MIT Licence");
        tempLicenceList.add("Apache License Version 2.0, January 2004");
        tempLicenceList.add("Creative Commons (CC) Attribution (BY)");

        tempLicenceList.add("Android studio icons\n -> Android studio");
        tempLicenceList.add("Lottie files\n -> lottiefiles.com");

        tempLicenceList.add("Loading animation for splash screen\n -> Manmeet Singh: lottiefiles.com/NoobSaiyan");
        tempLicenceList.add("Cooking animation for splash screen\n -> panizk.kazemi: lottiefiles.com/panizk.kazemi");

        tempLicenceList.add("Guava android image library");

        return tempLicenceList;
    }
}