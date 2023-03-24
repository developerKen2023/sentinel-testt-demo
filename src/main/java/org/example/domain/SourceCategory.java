package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SourceCategory {
    //MR_PLUS or other
    private String source;

    private double currentPercent;

    public SourceCategory(String source) {
        this.source = source;
        this.currentPercent = 0.5;
    }
}

