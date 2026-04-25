package controller.user;

import java.io.IOException;
import java.time.*;
import java.util.List;

import dao.ReservationDao;
import dao.BienDao;
import dao.impl.ReservationDaoImpl;
import dao.impl.BienDaoImpl;

import model.Reservation;
import model.Bien;
import model.Utilisateur;
import model.StatutReservation;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/user/reservations")
public class UserReservationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ReservationDao reservationDao = new ReservationDaoImpl();
    private BienDao bienDao = new BienDaoImpl();

    // ========================= GET =========================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showForm(request, response);
                    break;
                case "cancel":
                    cancelReservation(request, response);
                    break;
                default:
                    listReservations(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // ========================= POST =========================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "create";
        }

        try {
            if ("quickCreate".equals(action)) {
                quickCreateReservation(request, response);
            } else {
                createReservation(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // ========================= SESSION =========================
    private Utilisateur getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (Utilisateur) session.getAttribute("userSession") : null;
    }

    // ========================= LIST =========================
    private void listReservations(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Utilisateur user = getUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Reservation> list = reservationDao.findByUtilisateur(user);
        request.setAttribute("reservations", list);
        request.setAttribute("now", LocalDateTime.now());

        request.getRequestDispatcher("/WEB-INF/views/user/reservations/list.jsp")
               .forward(request, response);
    }

    // ========================= FORM =========================
    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        if (getUser(request) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("salles", bienDao.findAll());

        request.getRequestDispatcher("/WEB-INF/views/user/reservations/form.jsp")
               .forward(request, response);
    }

    // ========================= QUICK CREATE =========================
    private void quickCreateReservation(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Utilisateur user = getUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long bienId = Long.parseLong(request.getParameter("bienId"));

        Bien bien = bienDao.findById(bienId);

        LocalDate date = LocalDate.parse(request.getParameter("date"));
        LocalTime start = LocalTime.parse(request.getParameter("startTime"));
        LocalTime end = LocalTime.parse(request.getParameter("endTime"));

        LocalDateTime debut = LocalDateTime.of(date, start);
        LocalDateTime fin = LocalDateTime.of(date, end);

        Reservation r = new Reservation();
        r.setBien(bien);
        r.setUtilisateur(user);
        r.setDateHeureDebut(debut);
        r.setDateHeureFin(fin);
        r.setDateCreation(LocalDateTime.now());
        r.setStatut(StatutReservation.EN_ATTENTE);
        
        long minutes = Duration.between(debut, fin).toMinutes();
        double heures = minutes / 60.0;
        r.setMontantTotal(heures * bien.getPrixParHeure());

        reservationDao.save(r);

        response.sendRedirect(request.getContextPath() + "/user/reservations");
    }

    // ========================= CREATE =========================
    private void createReservation(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Utilisateur user = getUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String bienIdStr = request.getParameter("bienId");
        String debutStr = request.getParameter("dateHeureDebut");
        String finStr = request.getParameter("dateHeureFin");

        String error = null;

        Long bienId = null;
        LocalDateTime debut = null;
        LocalDateTime fin = null;

        try {
            bienId = Long.parseLong(bienIdStr);
        } catch (Exception e) {
            error = "Salle invalide";
        }

        try {
            debut = LocalDateTime.parse(debutStr);
            fin = LocalDateTime.parse(finStr);
        } catch (Exception e) {
            error = "Date invalide";
        }

        if (error == null && !fin.isAfter(debut)) {
            error = "Fin doit être après début";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("biens", bienDao.findAll());
            request.getRequestDispatcher("/WEB-INF/views/user/reservations/form.jsp")
                   .forward(request, response);
            return;
        }

        Bien bien = bienDao.findById(bienId);

        Reservation r = new Reservation();
        r.setBien(bien);
        r.setUtilisateur(user);
        r.setDateHeureDebut(debut);
        r.setDateHeureFin(fin);
        r.setDateCreation(LocalDateTime.now());
        r.setStatut(StatutReservation.EN_ATTENTE);
        
        long minutes = Duration.between(debut, fin).toMinutes();
        double heures = minutes / 60.0;
        r.setMontantTotal(heures * bien.getPrixParHeure());

        reservationDao.save(r);

        response.sendRedirect(request.getContextPath() + "/user/reservations");
    }

    // ========================= CANCEL =========================
    private void cancelReservation(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Utilisateur user = getUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));

        Reservation r = reservationDao.findById(id);

        if (r == null || !r.getUtilisateur().getId().equals(user.getId())) {
            response.sendRedirect(request.getContextPath() + "/user/reservations");
            return;
        }

        r.setStatut(StatutReservation.ANNULEE);
        reservationDao.save(r);

        response.sendRedirect(request.getContextPath() + "/user/reservations");
    }
}