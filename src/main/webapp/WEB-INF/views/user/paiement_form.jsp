<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Formulaire de paiement --%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="pageTitle" value="Paiement Sécurisé - SmartSpace System" scope="request"/>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<style>
    .payment-container {
        max-width: 900px;
        margin: 40px auto;
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 30px;
        background: var(--white);
        border-radius: var(--radius-lg);
        box-shadow: 0 15px 35px rgba(0,0,0,0.05);
        overflow: hidden;
    }

    /* Left Side: Summary */
    .payment-summary {
        background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
        color: var(--white);
        padding: 40px;
    }

    .summary-title {
        font-size: 24px;
        font-weight: 700;
        margin-bottom: 30px;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .summary-item {
        display: flex;
        justify-content: space-between;
        margin-bottom: 15px;
        padding-bottom: 15px;
        border-bottom: 1px solid rgba(255,255,255,0.2);
    }

    .summary-item:last-child {
        border-bottom: none;
        margin-bottom: 0;
        padding-bottom: 0;
    }

    .summary-label {
        opacity: 0.8;
        font-size: 14px;
    }

    .summary-value {
        font-weight: 600;
        font-size: 16px;
    }

    .total-price {
        font-size: 32px;
        font-weight: 700;
        margin-top: 30px;
        padding-top: 20px;
        border-top: 2px solid rgba(255,255,255,0.3);
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    /* Right Side: Form */
    .payment-form-section {
        padding: 40px;
    }

    .form-title {
        font-size: 20px;
        font-weight: 600;
        color: var(--dark);
        margin-bottom: 24px;
    }

    .form-group {
        margin-bottom: 20px;
    }

    .form-label {
        display: block;
        font-size: 13px;
        font-weight: 600;
        color: var(--gray-700);
        margin-bottom: 8px;
    }

    .form-control {
        width: 100%;
        padding: 12px 16px;
        border: 1px solid var(--gray-300);
        border-radius: var(--radius-sm);
        font-family: inherit;
        font-size: 14px;
        transition: var(--transition);
    }

    .form-control:focus {
        border-color: var(--primary);
        box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
        outline: none;
    }

    .row-2 {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 20px;
    }

    .btn-submit-payment {
        width: 100%;
        padding: 14px;
        background: #10B981;
        color: var(--white);
        border: none;
        border-radius: var(--radius-sm);
        font-size: 16px;
        font-weight: 600;
        cursor: pointer;
        transition: var(--transition);
        margin-top: 10px;
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;
    }

    .btn-submit-payment:hover {
        background: #059669;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
    }

    .secure-badge {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        margin-top: 20px;
        color: var(--gray-500);
        font-size: 12px;
    }

    @media (max-width: 768px) {
        .payment-container {
            grid-template-columns: 1fr;
        }
    }
</style>

<div class="payment-container">
    <!-- Left: Summary -->
    <div class="payment-summary">
        <h2 class="summary-title">
            <i class="fas fa-receipt"></i> Résumé de la réservation
        </h2>
        
        <div class="summary-item">
            <span class="summary-label">Espace</span>
            <span class="summary-value">${reservation.bien.nom}</span>
        </div>
        <div class="summary-item">
            <span class="summary-label">Date d'arrivée</span>
            <span class="summary-value">${reservation.dateHeureDebut}</span>
        </div>
        <div class="summary-item">
            <span class="summary-label">Date de départ</span>
            <span class="summary-value">${reservation.dateHeureFin}</span>
        </div>
        <div class="summary-item">
            <span class="summary-label">Client</span>
            <span class="summary-value">${reservation.utilisateur.nomComplet}</span>
        </div>

        <div class="total-price">
            <span style="font-size: 16px; opacity: 0.8; font-weight: normal;">Total à payer</span>
            <span>
                <c:choose>
                    <c:when test="${reservation.montantTotal > 0}">
                        ${reservation.montantTotal} TND
                    </c:when>
                    <c:otherwise>
                        150.0 TND <!-- Valeur par défaut simulée -->
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>

    <!-- Right: Payment Form -->
    <div class="payment-form-section">
        <h3 class="form-title">Détails de paiement</h3>
        
        <form action="${pageContext.request.contextPath}/user/paiement?id=${reservation.id}" method="POST">
            
            <div class="form-group">
                <label class="form-label">Nom sur la carte</label>
                <input type="text" class="form-control" name="cardName" placeholder="ex: Eya Ben Salem" required>
            </div>
            
            <div class="form-group">
                <label class="form-label">Numéro de carte</label>
                <input type="text" class="form-control" name="cardNumber" placeholder="0000 0000 0000 0000" maxlength="19" required>
            </div>
            
            <div class="row-2">
                <div class="form-group">
                    <label class="form-label">Date d'expiration</label>
                    <input type="text" class="form-control" name="cardExpiry" placeholder="MM/AA" maxlength="5" required>
                </div>
                <div class="form-group">
                    <label class="form-label">CVV</label>
                    <input type="password" class="form-control" name="cardCvv" placeholder="123" maxlength="4" required>
                </div>
            </div>
            
            <button type="submit" class="btn-submit-payment">
                <i class="fas fa-lock"></i>
                Payer maintenant
            </button>
            
            <div class="secure-badge">
                <i class="fas fa-shield-alt"></i>
                Paiement 100% sécurisé et chiffré
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
