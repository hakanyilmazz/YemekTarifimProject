package com.tezodevi.android.view.user.fragment.score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.tezodevi.android.database.AppDatabase;
import com.tezodevi.android.database.model.LifeChain;
import com.tezodevi.android.database.model.LifeChainBlock;
import com.tezodevi.android.database.model.LifeChainRequest;
import com.tezodevi.android.database.query.LifeChainDao;
import com.tezodevi.android.databinding.FragmentScoreBinding;
import com.tezodevi.android.model.ScoreFragmentSingletonData;
import com.tezodevi.android.model.ScoreSingleton;
import com.tezodevi.android.service.worker_services.LifeChainService;
import com.tezodevi.android.service.worker_services.LoginService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ScoreFragment extends Fragment {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    LifeChainDao lifeChainDao;
    private FragmentScoreBinding binding;
    private LifeChainService lifeChainService;
    private LoginService loginService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeChainService = new LifeChainService(requireContext());
        loginService = new LoginService(requireContext());

        AppDatabase db = Room.databaseBuilder(requireContext().getApplicationContext(),
                AppDatabase.class, requireActivity().getPackageName()).allowMainThreadQueries().build();

        lifeChainDao = db.lifeChainDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ScoreFragmentSingletonData data = ScoreFragmentSingletonData.getInstance();

        if (data.data.equals("new")) {
            newData();
        } else {
            oldData();
        }
    }

    private void oldData() {
        compositeDisposable.add(
                lifeChainService.getLifeChainList().subscribe(this::handleStarterList, Throwable::printStackTrace)
        );
    }

    private void newData() {
        ScoreSingleton scoreSingleton = ScoreSingleton.getInstance();
        List<LifeChain> all = lifeChainDao.getAll();
        List<LifeChainBlock> result = new ArrayList<>();

        for (LifeChain temp : all) {
            LifeChainBlock tempLifeChain = new LifeChainBlock(loginService.getUsername(), temp.score);
            tempLifeChain.blockHash = temp.blockHash;
            tempLifeChain.previousBlockHash = temp.previousBlockHash;

            result.add(tempLifeChain);
        }

        LifeChainRequest lifeChainRequest = new LifeChainRequest(
                new LifeChainBlock(loginService.getUsername(), scoreSingleton.score),
                result
        );

        compositeDisposable.add(
                lifeChainService.postLifeChain(lifeChainRequest).subscribe(this::handleResponse, Throwable::printStackTrace)
        );
    }

    private void handleStarterList(List<LifeChainBlock> lifeChainBlockList) {
        setUi(lifeChainBlockList);
    }

    private void handleResponse(List<LifeChainBlock> lifeChainWebs) {
        lifeChainDao.delete(lifeChainDao.getAll());

        List<LifeChain> result = new ArrayList<>();
        for (LifeChainBlock temp : lifeChainWebs) {
            LifeChain tempLifeChain = new LifeChain();
            tempLifeChain.score = temp.score;
            tempLifeChain.blockHash = temp.blockHash;
            tempLifeChain.previousBlockHash = temp.previousBlockHash;

            result.add(tempLifeChain);
        }

        lifeChainDao.insertAll(result);

        setUi(lifeChainWebs);
    }

    private void setUi(List<LifeChainBlock> lifeChainWebs) {
        String[] items = new String[lifeChainWebs.size()];

        int i = 0;
        for (LifeChainBlock temp : lifeChainWebs) {
            items[i] = i + 1 + ". " + temp.username + ": " + temp.score;
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items);
        binding.scoreLayoutListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}