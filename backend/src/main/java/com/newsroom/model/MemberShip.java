package com.newsroom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "member_ships")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberShip {
    @Field(name = "user_id")
    private String userId;

    @Field(name = "community_id")
    private String communityId;
}
