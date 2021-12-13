package com.tezodevi.android.service.worker_services;

import android.content.Context;

import com.tezodevi.android.database.model.LifeChainBlock;
import com.tezodevi.android.database.model.LifeChainRequest;
import com.tezodevi.android.repository.LifeChainRepository;

import java.util.List;

import io.reactivex.Observable;

public class LifeChainService {
    private final LifeChainRepository lifeChainRepository;

    public LifeChainService(Context context) {
        lifeChainRepository = new LifeChainRepository(context);
    }

    public Observable<List<LifeChainBlock>> postLifeChain(LifeChainRequest lifeChainRequest) {
        return lifeChainRepository.postLifeChain(lifeChainRequest);
    }

    public Observable<List<LifeChainBlock>> getLifeChainList() {
        return lifeChainRepository.getLifeChainList();
    }

}
