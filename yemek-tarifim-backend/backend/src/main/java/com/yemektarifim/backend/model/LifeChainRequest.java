package com.yemektarifim.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class LifeChainRequest {
    private LifeChainBlock lifeChainBlock;
    private List<LifeChainBlock> lifeChainBlockList;
}
