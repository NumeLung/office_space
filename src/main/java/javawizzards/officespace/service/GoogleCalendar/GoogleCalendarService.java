package javawizzards.officespace.service.GoogleCalendar;

import javawizzards.officespace.dto.Reservation.CreateReservationDto;
import com.google.api.services.calendar.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface GoogleCalendarService {
    // void createEvent(CalendarEvent dto);
    void createEvent(CreateReservationDto dto);

    List<Event> getEvents(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);
}
