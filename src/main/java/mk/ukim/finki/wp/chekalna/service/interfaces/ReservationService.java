package mk.ukim.finki.wp.chekalna.service.interfaces;

import mk.ukim.finki.wp.chekalna.model.Reservation;

import java.util.List;

public interface ReservationService {
    Reservation save(Reservation reservation);

    void deleteById(List<String> ids);
}