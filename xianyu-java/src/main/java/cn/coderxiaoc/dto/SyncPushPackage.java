package cn.coderxiaoc.dto;

import lombok.Data;

import java.util.List;
@Data
public class SyncPushPackage {
    private Long maxHighPts;
    private Long startSeq;
    private Long endSeq;
    private List<BodyData> data;
    private Long maxPts;
    private Long hasMore;
    private Long timestamp;
}
