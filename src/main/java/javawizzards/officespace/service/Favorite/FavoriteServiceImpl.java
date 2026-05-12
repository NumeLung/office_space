package javawizzards.officespace.service.Favorite;

import jakarta.transaction.Transactional;
import javawizzards.officespace.dto.Company.CompanyDto;
import javawizzards.officespace.dto.OfficeRoom.OfficeRoomDto;
import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.exception.OfficeRoom.OfficeRoomCustomException;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.repository.OfficeRoomRepository;
import javawizzards.officespace.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final UserRepository userRepository;
    private final OfficeRoomRepository officeRoomRepository;

    public FavoriteServiceImpl(UserRepository userRepository, OfficeRoomRepository officeRoomRepository) {
        this.userRepository = userRepository;
        this.officeRoomRepository = officeRoomRepository;
    }

    @Override
    @Transactional
    public boolean toggleFavorite(UUID userId, UUID roomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCustomException.UserNotFoundException());

        OfficeRoom room = officeRoomRepository.findById(roomId)
                .orElseThrow(() -> new OfficeRoomCustomException.OfficeRoomNotFoundException());

        boolean alreadyFavorited = user.getFavoriteRooms().stream()
                .anyMatch(r -> r.getId().equals(roomId));

        if (alreadyFavorited) {
            user.getFavoriteRooms().removeIf(r -> r.getId().equals(roomId));
        } else {
            user.getFavoriteRooms().add(room);
        }

        userRepository.save(user);
        return !alreadyFavorited;
    }

    @Override
    @Transactional
    public List<OfficeRoomDto> getUserFavorites(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCustomException.UserNotFoundException());

        return user.getFavoriteRooms().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private OfficeRoomDto mapToDto(OfficeRoom room) {
        OfficeRoomDto dto = new OfficeRoomDto();
        dto.setId(room.getId());
        dto.setOfficeRoomName(room.getName());
        dto.setAddress(room.getAddress());
        dto.setBuilding(room.getBuilding());
        dto.setFloor(room.getFloor());
        dto.setType(room.getType());
        dto.setCapacity(room.getCapacity());
        dto.setStatus(room.getStatus());
        dto.setPictureUrl(room.getPictureUrl());
        dto.setPricePerHour(room.getPricePerHour());
        dto.setReservations(Collections.emptyList());
        dto.setResources(Collections.emptyList());

        if (room.getCompany() != null) {
            CompanyDto companyDto = new CompanyDto();
            companyDto.setId(room.getCompany().getId());
            companyDto.setName(room.getCompany().getName());
            companyDto.setAddress(room.getCompany().getAddress());
            companyDto.setType(room.getCompany().getType());
            dto.setCompany(companyDto);
        }

        return dto;
    }
}