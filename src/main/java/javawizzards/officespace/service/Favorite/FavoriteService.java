package javawizzards.officespace.service.Favorite;

import javawizzards.officespace.dto.OfficeRoom.OfficeRoomDto;

import java.util.List;
import java.util.UUID;

public interface FavoriteService {

    boolean toggleFavorite(UUID userId, UUID roomId);

    List<OfficeRoomDto> getUserFavorites(UUID userId);
}