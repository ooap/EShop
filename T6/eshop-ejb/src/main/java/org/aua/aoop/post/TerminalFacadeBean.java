package org.aua.aoop.post;

import org.aua.aoop.post.ex.ProductException;
import org.aua.aoop.post.payment.AbstractPayment;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Startup;
import javax.ejb.Stateless;

@Stateless
@Startup
@Remote(TerminalFacade.class)
public class TerminalFacadeBean implements TerminalFacade {

    private static final Logger logger =
            Logger.getLogger(TerminalFacadeBean.class);

    private Terminal terminal;

    @Override
    public void login(String customerName){
        terminal.login(customerName);
    }

    @Override
    public void startNewSale(){
          terminal.startNewSale();
    }

    @Override
    public void addItem(String UPC, int qty) throws ProductException{
          terminal.addItem(UPC, qty);
    }

    @Override
    public SaleItem getCurrSaleItem() {
        return terminal.getCurrSaleItem();
    }

    @Override
    public boolean processPayment(AbstractPayment.PaymentType paymentType, double amount, String info){
        return terminal.processPayment(paymentType, amount, info);
    }

    @Override
    public String getReceipt(){
        return terminal.getReceipt();
    }

    @Override
    public double getCashBalance() {
        return terminal.getCashBalance();
    }

    @Override
    public void setPayment(double total){
        terminal.setPayment(total);
    }

    @Override
    public ShoppingCart getCurrentSale(){
        return terminal.getCurrentShoppingCart();
    }

    @Override
    public boolean productExists(String UPC){
        return terminal.productExists(UPC);
    }

    @PostConstruct
    private void SetupStore(){
        Store store = new Store();
        terminal = store.getTerminal();
    }
}
