/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.salesReport;

import java.time.LocalDate;


public class SalesReport 
{
    private int saleId;
    private String productName;
    private int quantitySold;
    private double totalAmount;
    private LocalDate saleDate;

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public SalesReport(int saleId, String productName, int quantitySold, double totalAmount, LocalDate saleDate) 
    {
        this.saleId = saleId;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
    }
    
    
}
