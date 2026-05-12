package javawizzards.officespace.dto.Favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteToggleResponseDto {

    @JsonProperty("roomId")
    private UUID roomId;

    @JsonProperty("favorited")
    private boolean favorited;
}