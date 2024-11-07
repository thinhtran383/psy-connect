package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    @Query("SELECT c.certificateImage FROM Certificate c WHERE c.userId = :userId")
    List<String> findAllCertificateByUserId(Integer userId);
}