package jp.co.metateam.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.repository.RentalManageRepository;

@Service
public class RentalManageService {
    private final RentalManageRepository rentalManageRepository;

    @Autowired
    public RentalManageService(RentalManageRepository rentalManageRepository) {
        this.rentalManageRepository = rentalManageRepository;
    }

    public void save(RentalManageDto rentalManageDto) {
        rentalManageRepository.save(rentalManageDto);
    }

    public boolean existsRentalPeriod(
            String stockId,
            String rentalDate,
            String returnDate) {
        return rentalManageRepository.existsDuplicateRentalPeriod(
                stockId,
                rentalDate,
                returnDate) > 0;
    }
}
