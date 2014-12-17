package org.aua.aoop.post;

import org.aua.aoop.post.payment.AbstractPayment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ShoppingCartFacade extends Serializable {
    public void addSaleItem(SaleItem item);
    public List<SaleItem> getSaleItems();
    public UUID getSaleID();
    public AbstractPayment getPayment();
    public String getCustomerName();
    public Date getDateTime();
    public double getTotal();
    public void setPayment(AbstractPayment payment);
}
