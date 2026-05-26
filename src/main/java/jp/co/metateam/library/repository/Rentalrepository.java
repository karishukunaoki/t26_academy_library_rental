package jp.co.metateam.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jp.co.metateam.library.model.RentalManage;

public interface Rentalrepository extends JpaRepository<RentalManage, Long> {
}