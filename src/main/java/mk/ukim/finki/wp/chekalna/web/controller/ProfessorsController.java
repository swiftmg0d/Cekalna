package mk.ukim.finki.wp.chekalna.web.controller;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.chekalna.model.Consultation;
import mk.ukim.finki.wp.chekalna.model.Professor;
import mk.ukim.finki.wp.chekalna.model.Room;
import mk.ukim.finki.wp.chekalna.model.enums.ConsultationType;
import mk.ukim.finki.wp.chekalna.service.interfaces.ConsultationService;
import mk.ukim.finki.wp.chekalna.service.interfaces.ProfessorService;
import mk.ukim.finki.wp.chekalna.service.interfaces.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class ProfessorsController {

    private final ProfessorService professorService;

    private final ConsultationService consultationService;

    private final RoomService roomService;

    @GetMapping(path = {"/", "/professors"})
    public String showAllProfessors(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Professor> professors;
        if (search != null && !search.isEmpty()) {
            professors = professorService.findProfessorsByName(search);
        } else {
            professors = professorService.getAllProfessors();
        }
        consultationService.calcualteAverageWaitingTime();
        Map<DayOfWeek, String> dayOfWeekMap = Map.of(
                DayOfWeek.MONDAY, "Понеделник",
                DayOfWeek.TUESDAY, "Вторник",
                DayOfWeek.WEDNESDAY, "Среда",
                DayOfWeek.THURSDAY, "Четврток",
                DayOfWeek.FRIDAY, "Петок",
                DayOfWeek.SATURDAY, "Сабота",
                DayOfWeek.SUNDAY, "Недела"
        );
        model.addAttribute("daysOfWeek", dayOfWeekMap);
        model.addAttribute("professors", professors);
        model.addAttribute("consultations", consultationService.findAll());
        return "professors";
    }

}