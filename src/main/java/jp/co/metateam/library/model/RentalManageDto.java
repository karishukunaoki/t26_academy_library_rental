package jp.co.metateam.library.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "rental_manage")
public class RentalManageDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

     @NotBlank(message = "社員番号が入力されていません")
    private String employeeId;
    @NotNull(message = "「貸出予定日」は必須項目です")
    private String expectedRentalOn;
    @NotNull(message = "「返却予定日」は必須項目です")
    private String expectedReturnOn;
    @NotBlank(message = "「在庫管理番号」は必須項目です")
    private String stockId;
    @NotNull(message = "「貸出ステータス」は必須項目です")
    private Integer status;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getExpectedRentalOn() {
        return expectedRentalOn;
    }

    public void setExpectedRentalOn(String expectedRentalOn) {
        this.expectedRentalOn = expectedRentalOn;
    }

    public String getExpectedReturnOn() {
        return expectedReturnOn;
    }

    public void setExpectedReturnOn(String expectedReturnOn) {
        this.expectedReturnOn = expectedReturnOn;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}