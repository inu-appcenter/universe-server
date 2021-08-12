package org.inu.universe.model.hashtag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashTagSaveRequest {

    private List<String> hashTagList;
}
