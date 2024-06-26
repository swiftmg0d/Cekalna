package mk.ukim.finki.wp.chekalna.web.controller;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.chekalna.model.Consultation;
import mk.ukim.finki.wp.chekalna.model.Professor;
import mk.ukim.finki.wp.chekalna.model.Reservation;
import mk.ukim.finki.wp.chekalna.model.Room;
import mk.ukim.finki.wp.chekalna.model.enums.ConsultationType;
import mk.ukim.finki.wp.chekalna.model.utils.Constants;
import mk.ukim.finki.wp.chekalna.repository.ReservationRepository;
import mk.ukim.finki.wp.chekalna.service.interfaces.ConsultationService;
import mk.ukim.finki.wp.chekalna.service.interfaces.ProfessorService;
import mk.ukim.finki.wp.chekalna.service.interfaces.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ProfessorsController {

    private final ProfessorService professorService;

    private final ConsultationService consultationService;
    private final ReservationRepository reservationRepository;

    private final RoomService roomService;

    @GetMapping(path = {"/", "/professors"})
    public String showAllProfessors(@RequestParam(value = "search", required = false) String search,
                                    Model model,
                                    @RequestParam(required = false) String error) {

        if (error!=null) {
            model.addAttribute("error","Веќе имате резервирано термин!");
        }

        List<Professor> professors;
        if (search != null && !search.isEmpty()) {
            professors = professorService.findProfessorsByName(search);
        } else {
            professors = professorService.getAllProfessors();
        }

        professors.forEach(professor -> {
            professor.getConsultations().forEach(consultation -> {
                consultation.setReservations(consultation.getReservations().stream()
                        .sorted(Comparator.comparing(i->i.getNumber().getId()))
                        .collect(Collectors.toList()));
            });
        });

        model.addAttribute("daysOfWeek", Constants.dayOfWeekMap);
        model.addAttribute("professors", professors);
        model.addAttribute("consultations", consultationService.findAll());
        model.addAttribute("timeNow", LocalTime.now());
        model.addAttribute("today", LocalDate.now());
        return "professors";
    }

}
