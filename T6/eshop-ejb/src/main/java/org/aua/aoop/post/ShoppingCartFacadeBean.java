package org.aua.aoop.post;

import org.aua.aoop.post.payment.AbstractPayment;

import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.jboss.logging.Logger;

@Stateful
@Startup
@Remote(ShoppingCartFacade.class)
public class ShoppingCartFacadeBean implements ShoppingCartFacade {

    private static final Logger logger = Logger.getLogger(ShoppingCartFacadeBean.class);

    private ShoppingCart shoppingCart;

    @Override
    public void addSaleItem(SaleItem item) {
        shoppingCart.addSaleItem(item);
    }

    @Override
    public List<SaleItem> getSaleItems() {
        return shoppingCart.getSaleItems();
    }

    @Override
    public UUID getSaleID() {
        return shoppingCart.getSaleID();
    }

    @Override
    public AbstractPayment getPayment() {
        return shoppingCart.getPayment();
    }

    @Override
    public String getCustomerName() {
        return shoppingCart.getCustomerName();
    }

    @Override
    public Date getDateTime() {
        return shoppingCart.getDateTime();
    }

    @Override
    public double getTotal() {
        return shoppingCart.getTotal();
    }

    @Override
    public void setPayment(AbstractPayment payment) {
        shoppingCart.setPayment(payment);
    }

    @PostConstruct
    private void SetupStore(){
        Store store = new Store();
        shoppingCart = store.getTerminal().getCurrentShoppingCart();
    }
}
