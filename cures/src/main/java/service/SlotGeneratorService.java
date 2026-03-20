package service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import model.AvailabilitySchedule;
import repository.AvailabilityRepository;
import repository.DoctorSlotRepository;

@Service
public class SlotGeneratorService {

    @Autowired
    private AvailabilityRepository availabilityRepo;

    @Autowired
    private DoctorSlotRepository slotRepo;
    public SlotGeneratorService() {
        System.out.println("🔥 SlotGeneratorService CREATED");
    }

    // 🔥 RUNS EVERY DAY AT MIDNIGHT
    @Scheduled(cron = "0 0 0 * * ?")
    public void maintainSlotWindow() {

        // ✅ 1. DELETE OLD SLOTS
        slotRepo.deleteOldSlots();

        // ✅ 2. GENERATE NEW DAY (+30)
        LocalDate newDate = LocalDate.now().plusDays(30);

        generateSlotsForDate(newDate);
    }
    
    
    @PostConstruct
    public void init() {
    	 // ✅ 1. DELETE OLD SLOTS
        slotRepo.deleteOldSlots();

        for (int i = 0; i < 31; i++) {
        	 // ✅ 1. DELETE OLD SLOTS
            slotRepo.deleteOldSlots();

          generateSlotsForDate(LocalDate.now().plusDays(i));
        }
    }

    // 🔥 CORE METHOD
    public void generateSlotsForDate(LocalDate date) {

        List<AvailabilitySchedule> doctors =
                availabilityRepo.findAllActiveDoctors();

        
    //    System.out.println("Generating slots for date: " + date + " with " + doctors.size() + " active doctors.");
        for (AvailabilitySchedule da : doctors) {

            // ✅ CHECK DAY AVAILABILITY
            if (!isDoctorAvailable(da, date)) continue;

            LocalTime from = da.getFromTime().toLocalTime();
            LocalTime to = da.getToTime().toLocalTime();

            int duration = da.getSlotDuration();

            LocalDateTime start = LocalDateTime.of(date, from);
            LocalDateTime end = LocalDateTime.of(date, to);
            int gap = 15; // buffer time in minutes
            while (start.plusMinutes(duration).compareTo(end) <= 0) {

                try {
                    slotRepo.insertIgnore(
                            da.getDocId(),
                            start,
                            start.plusMinutes(duration)
                    );
                } catch (Exception e) {
                    // ignore duplicate slots
                }

                start = start.plusMinutes(duration + gap);
            }
        }
    }

    // 🔥 DAY CHECK LOGIC
    private boolean isDoctorAvailable(AvailabilitySchedule da, LocalDate date) {

        DayOfWeek day = date.getDayOfWeek();

        switch (day) {
            case MONDAY:
                return da.getMonAvailability() == 1;
            case TUESDAY:
                return da.getTueAvailability() == 1;
            case WEDNESDAY:
                return da.getWedAvailability() == 1;
            case THURSDAY:
                return da.getThuAvailability() == 1;
            case FRIDAY:
                return da.getFriAvailability() == 1;
            case SATURDAY:
            case SUNDAY:
                return da.getWeekDayOnly() == 0;
            default:
                return false;
        }
    }
}