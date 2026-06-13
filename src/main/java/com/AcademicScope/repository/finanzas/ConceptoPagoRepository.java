package com.AcademicScope.repository.finanzas;

import com.AcademicScope.model.ConceptoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptoPagoRepository extends JpaRepository<ConceptoPago, Long> {
}
