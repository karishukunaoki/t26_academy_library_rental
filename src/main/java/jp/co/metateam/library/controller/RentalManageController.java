package jp.co.metateam.library.controller;

import java.time.LocalDate;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.Stock;
import jp.co.metateam.library.model.AccountDto;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.service.RentalManageService;
import jp.co.metateam.library.service.AccountService;
import jp.co.metateam.library.service.StockService;
import jp.co.metateam.library.values.RentalStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import lombok.extern.log4j.Log4j2;

/**
 * 貸出管理関連クラスß
 */
@Log4j2
@Controller
public class RentalManageController {

    private final RentalManageService rentalManageService;
    public RentalManageController(RentalManageService rentalManageService) {
        this.rentalManageService = rentalManageService;
        
    }
    @Autowired
    public RentalManageController(RentalManageService rentalManageSrevice, AccountService accountService, StockService stockService){
        this.rentalManageService = rentalManageSrevice;
       
    }
        /**
     * 貸出一覧画面初期表示
     * @param model
     * @return
     */
    @GetMapping("/rental/index")
    public String index(Model model) {
        // 貸出管理テーブルから全件取得
        List<RentalManage> rentalManages = this.rentalManageService.findAll();
        // 貸出一覧画面に渡すデータをmodelに追加
        model.addAttribute("RentalManage", new RentalManage());
        // 貸出一覧画面に遷移
        return "rental/index";
    }

    //貸出登録画面の初期表示
    @GetMapping("/rental/add")
    public String add(Model model) {//Model modelは登録用のControllerとhtmlの共有箱
        List<RentalManage> rentalManages = this.rentalManageService.findAll();//Service側に全件取得を依頼（貸出登録テーブルのEntiti）
        List<Account> accounts = this.rentalManageService.findAllAccounts();//（アカウントテーブルのEntity）
        List<Stock> stocks = this.rentalManageService.findAllStocks();//（在庫テーブルのEntity）
        

        model.addAttribute("rentalManageDto", new RentalManageDto());//実際にControllerからhtmlに"rentalManageDto"のデータを渡す
        //取得したデータをhtmlに渡す
        model.addAttribute("rentalManages", rentalManages);//貸出管理テーブルの全件
        model.addAttribute("rentalStatus", RentalStatus.values());//貸出ステータスの全件
        model.addAttribute("accounts", accounts);//アカウントテーブルの全件
        model.addAttribute("stockList", stocks);//在庫テーブルの全件
       

        return "rental/add";//登録画面に遷移
    }

    //貸出登録処理
    //ユーザーから保存ボタンが押された時に呼び出されるメソッド
    @PostMapping("/rental/add")
    //画面から来た値を受け付け、Serviceに保存処理を依頼
    public String add(@Valid @ModelAttribute RentalManageDto rentalManageDto, BindingResult result, RedirectAttributes ra, Model model) {
            try {

                
                //結果にエラーがあればエラーを投げる
                if (result.hasErrors()) {
                    model.addAttribute("rentalManageDto", rentalManageDto);
                    model.addAttribute("accounts", rentalManageService.findAllAccounts());
                    model.addAttribute("stockList", rentalManageService.findAllStocks());
                    model.addAttribute("rentalStatus", RentalStatus.values());
                    
                    return "rental/add";

                    
                }
                //貸出ステータスが「貸出待ち」又は「貸出中」以外が選択された場合にエラーを投げる
                if(rentalManageDto.getStatus() != null
                && rentalManageDto.getStatus() !=0
                && rentalManageDto.getStatus() !=1){
                    result.rejectValue("status", "error.value","貸出ステータスは「貸出待ち」もしくは「貸出中」を選択して下さい");
                    model.addAttribute("rentalStatus", RentalStatus.values());
                    model.addAttribute("accounts",  rentalManageService.findAllAccounts());
                    model.addAttribute("stockList", rentalManageService.findAllStocks());

                    return "rental/add";
                    
                }
                //貸出予定日が未来日で、貸出ステータスが「貸出待ち」以外が選択された場合エラーを投げる
                if(rentalManageDto.getExpectedRentalOn() != null
                 && rentalManageDto.getStatus() != 0
                 && rentalManageDto.getExpectedRentalOn().isAfter(LocalDate.now())){
                    result.rejectValue("status", "error.value","未来日付では「貸出待ち」を選択してください");
                    model.addAttribute("rentalStatus", RentalStatus.values());
                    model.addAttribute("accounts",  rentalManageService.findAllAccounts());
                    model.addAttribute("stockList", rentalManageService.findAllStocks());
                    
                    return "rental/add";
                }
                //貸出予定日が過去日付で、貸出ステータスが「貸出中」以外が選択された場合エラーを投げる
                if(rentalManageDto.getExpectedRentalOn() != null
                 && rentalManageDto.getStatus() != 1
                 && rentalManageDto.getExpectedRentalOn().isBefore(LocalDate.now())){
                    result.rejectValue("status","error.value","過去日付では「貸出中」を選択してください");
                    //Cnotrollerからhtmlにデータを渡す
                    model.addAttribute("rentalStatus", RentalStatus.values());
                    model.addAttribute("accounts",  rentalManageService.findAllAccounts());
                    model.addAttribute("stockList", rentalManageService.findAllStocks());
                
                    return "rental/add";
                 }
                //同一書籍の貸出履歴取得
                List<RentalManage> rentalManageList = rentalManageService.findByStockIdAndRentalStatusIn(rentalManageDto.getStockId(),List.of(0,1));
                // 重複チェック
                for (RentalManage registeredRentalManage : rentalManageList)  {
                    boolean isDupulicate = !rentalManageDto.getExpectedReturnOn().isBefore(registeredRentalManage.getExpectedRentalOn())
                    && !rentalManageDto.getExpectedRentalOn().isAfter(registeredRentalManage.getExpectedReturnOn());

                    if(isDupulicate) {
                        result.rejectValue("expectedRentalOn", "rental.duplicate", "指定した期間は既存の期間と重複しています");
                        result.rejectValue("expectedReturnOn", "rental.duplicate", "指定した期間は既存の期間と重複しています");
                        model.addAttribute("rentalStatus", RentalStatus.values());
                        model.addAttribute("accounts",rentalManageService.findAllAccounts());
                        model.addAttribute("stockList",rentalManageService.findAllStocks());

                        return "rental/add";
                    }
                    
                }   
                //保存処理をServiceに依頼
                this.rentalManageService.save(rentalManageDto);
                //貸出一覧画面に遷移
                 return "redirect:/rental/index";
                } catch (Exception e){
                    log.error(e.getMessage());

                    ra.addFlashAttribute("rentalManageDto", rentalManageDto);
                    ra.addFlashAttribute("org.springframework.validation.BindingResult.rentalManageDto", result);

                    return "redirect:/rental/add";

                }
        

   

            }
        }        

    
    
