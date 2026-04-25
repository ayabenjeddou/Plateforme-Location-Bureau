package controller.user;

import java.io.IOException;
import java.time.LocalDateTime;

import dao.ReservationDao;
import dao.impl.ReservationDaoImpl;
import dao.PaiementDao;
import dao.impl.PaiementDaoImpl;
import model.Reservation;
import model.Paiement;
import model.Utilisateur;
import model.StatutReservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user/paiement")
public class UserPaiementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ReservationDao reservationDao = new ReservationDaoImpl();
    private PaiementDao paiementDao = new PaiementDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("userSession") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/reservations");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            Reservation r = reservationDao.findById(id);

            if (r == null || !r.getUtilisateur().getId().equals(user.getId())) {
                response.sendRedirect(request.getContextPath() + "/user/reservations");
                return;
            }

            if (!StatutReservation.CONFIRMEE.equals(r.getStatut())) {
                session.setAttribute("reservationError", "Cette réservation n'est pas en attente de paiement.");
                response.sendRedirect(request.getContextPath() + "/user/reservations");
                return;
            }

            request.setAttribute("reservation", r);
            // Redirection vers le formulaire de paiement relocalisé
            request.getRequestDispatcher("/WEB-INF/views/user/paiement_form.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("userSession") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/reservations");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            Reservation r = reservationDao.findById(id);

            if (r == null || !r.getUtilisateur().getId().equals(user.getId())) {
                response.sendRedirect(request.getContextPath() + "/user/reservations");
                return;
            }

            if (!StatutReservation.CONFIRMEE.equals(r.getStatut())) {
                session.setAttribute("reservationError", "Le paiement a déjà été effectué ou la réservation n'est pas confirmée.");
                response.sendRedirect(request.getContextPath() + "/user/reservations");
                return;
            }

            // SIMULATION DE PAIEMENT REUSSI
            Paiement paiement = new Paiement();
            paiement.setReservation(r);
            
            // Le montant devrait idéalement être calculé ou stocké. 
            // S'il est dans r.getMontantTotal(), on l'utilise, sinon une valeur par défaut.
            double montant = r.getMontantTotal() > 0 ? r.getMontantTotal() : 150.0;
            paiement.setMontant(montant);
            
            paiement.setMethode("Carte Bancaire");
            paiement.setStatut("SUCCES");
            paiement.setDatePaiement(LocalDateTime.now());

            // Sauvegarder le paiement
            paiementDao.save(paiement);

            // Mettre à jour la réservation
            reservationDao.updateStatus(id, "PAYEE", "Paiement en ligne effectué");

            session.setAttribute("reservationSuccess", "Votre paiement a été validé avec succès !");
            response.sendRedirect(request.getContextPath() + "/user/reservations");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
