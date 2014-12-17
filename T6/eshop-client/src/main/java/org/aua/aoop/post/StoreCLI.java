package org.aua.aoop.post;

import org.aua.aoop.post.payment.AbstractPayment;
import org.aua.aoop.post.ex.ItemNotFoundException;
import org.aua.aoop.post.ex.NotEnoughItemsException;
import org.aua.aoop.post.ex.ProductException;
import org.aua.aoop.post.util.ClientUtility;

import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Scanner;


public class StoreCLI {

    private static String askUPC(Scanner scanner) {
        System.out.print("Enter UPC: ");
        return scanner.nextLine();
    }

    private static int askQty(Scanner scanner) throws NumberFormatException {
        System.out.print("Enter quantity: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private static String askCustomerName(Scanner scanner) {
        System.out.print("Customer Name: ");
        return scanner.nextLine();
    }

    public static void main(String[] args) {

            while (true) {

                Scanner scanner = new Scanner(System.in);

                try {
                    TerminalFacade service = lookuptheThingy(".TerminalFacade", ".TerminalFacadeBean", false);

                    service.login(askCustomerName(scanner));
                    service.startNewSale();
                    String upc = askUPC(scanner);

                    ShoppingCartFacade cart = lookuptheThingy(".ShoppingCartFacade", ".ShoppingCartFacadeBean", true);

                    while (upc.toLowerCase().compareTo("eos") != 0) {

                        if (!service.productExists(upc)) {
                            try {
                                throw new ItemNotFoundException();
                            } catch (ItemNotFoundException e) {
                                System.out.println(e.getMessage());
                                upc = askUPC(scanner);
                                continue;
                            }
                        }

                        int qty = 0;
                        try {
                            qty = askQty(scanner);
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter valid quantity");
                            continue;
                        }

                        try {
                            service.addItem(upc, qty);
                        } catch (ItemNotFoundException | NotEnoughItemsException e) {
                            System.out.println(e.getMessage());
                        } catch (ProductException e) {
                            e.printStackTrace();
                        }

                        upc = askUPC(scanner);
                    }

                    while (true) {
                        boolean result = false;
                        System.out.print("Payment Type (CASH = 1|CREDIT = 2|CHEQUE = 3): ");
                        int paymentType = Integer.parseInt(scanner.nextLine());
                        switch (paymentType) {

                            case 1:     // Cash payment
                                System.out.println("Total amount to be payed = " + service.getCurrentSale().getTotal());
                                System.out.print("Tendered money:");
                                String tm = scanner.nextLine();
                                double total = 0;

                                try {
                                    total = Double.parseDouble(tm);
                                    result = service.processPayment(AbstractPayment.PaymentType.CASH, total, "");
                                } catch (NumberFormatException e) {
                                    System.out.println("Please enter valid amount of money");
                                }
                                break;

                            case 2:     // Credit card payment
                                System.out.print("Enter card number: ");
                                String creditCardNumber = scanner.nextLine();
                                result = service.processPayment(AbstractPayment.PaymentType.CREDIT_CARD, 0, creditCardNumber);
                                break;

                            case 3:     // Check payment
                                System.out.print("Enter check number: ");
                                String checkNumber = scanner.nextLine();
                                result = service.processPayment(AbstractPayment.PaymentType.CHEQUE, 0, checkNumber);
                                break;
                        }
                        if (result) break;
                    }

                    System.out.println(service.getReceipt());


                } catch (NamingException e) {
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
    }



    /**
     * For more details refer to the link below
     * https://www.facebook.com/l.php?u=https%3A%2F%2Fdocs.jboss.org%2Fauthor%2Fdisplay%2FAS72%2FEJB%2Binvocations%2Bfrom%2Ba%2Bremote%2Bclient%2Busing%2BJNDI&h=uAQFWy1AL
     *
     * Looks up and returns the proxy to remote stateless calculator bean
     *
     * @return TerminalFacade remote EJB
     * @throws NamingException
     */
    private static <K> K lookuptheThingy(String clazz, String clazzBean, boolean full) throws NamingException, ClassNotFoundException {

        Class<?> klass = Class.forName("org.aua.aoop.post" + clazz);
        Class<?> klassBean = Class.forName("org.aua.aoop.post" + clazzBean);
        
        final Context context = ClientUtility.getInitialContext();
        final String appName = "";
        final String moduleName = "eshop-ejb";
        final String distinctName = "";
        final String beanName = klassBean.getSimpleName();
        final String viewClassName = klass.getName();
        final String statefull = full ? "?stateful" : "";
        return (K) context.lookup("ejb:" + appName + "/" + moduleName + "/" +
                distinctName + "/" + beanName + "!" + viewClassName + statefull);
    }

}
