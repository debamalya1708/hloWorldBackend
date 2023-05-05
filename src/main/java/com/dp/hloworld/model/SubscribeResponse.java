package com.dp.hloworld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscribeResponse {

    String subscriberName;
    String subsImage;

}
