package javawizzards.officespace.controller;

import javawizzards.officespace.dto.Favorite.FavoriteToggleResponseDto;
import javawizzards.officespace.dto.OfficeRoom.OfficeRoomDto;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.OfficeRoom.OfficeRoomMessages;
import javawizzards.officespace.exception.OfficeRoom.OfficeRoomCustomException;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.service.Favorite.FavoriteService;
import javawizzards.officespace.service.User.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    public FavoriteController(FavoriteService favoriteService, UserService userService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
    }

    @PostMapping("/toggle/{roomId}")
    public ResponseEntity<Response<FavoriteToggleResponseDto>> toggleFavorite(@PathVariable UUID roomId) {
        Response<FavoriteToggleResponseDto> response;

        try {
            User currentUser = getCurrentUser();
            boolean isFavorited = favoriteService.toggleFavorite(currentUser.getId(), roomId);

            FavoriteToggleResponseDto dto = new FavoriteToggleResponseDto(roomId, isFavorited);
            String message = isFavorited
                    ? OfficeRoomMessages.FAVORITE_ADDED.getMessage()
                    : OfficeRoomMessages.FAVORITE_REMOVED.getMessage();

            response = new Response<>(dto, HttpStatus.OK, message);
            return ResponseEntity.ok(response);

        } catch (UserCustomException | OfficeRoomCustomException e) {
            response = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Response<List<OfficeRoomDto>>> getUserFavorites() {
        Response<List<OfficeRoomDto>> response;

        try {
            User currentUser = getCurrentUser();
            List<OfficeRoomDto> favorites = favoriteService.getUserFavorites(currentUser.getId());

            response = new Response<>(favorites, HttpStatus.OK, OfficeRoomMessages.FAVORITES_FETCHED.getMessage());
            return ResponseEntity.ok(response);

        } catch (UserCustomException e) {
            response = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.getUserEntityByEmail(email);
    }
}