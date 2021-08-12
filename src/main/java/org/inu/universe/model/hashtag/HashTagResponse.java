package org.inu.universe.model.hashtag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.HashTag;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashTagResponse {

    private Long id;

    private String name;

    public static HashTagResponse from(HashTag hashTag) {
        HashTagResponse hashTagResponse = new HashTagResponse();
        hashTagResponse.id = hashTag.getId();
        hashTagResponse.name = hashTag.getName();
        return hashTagResponse;
    }
}
