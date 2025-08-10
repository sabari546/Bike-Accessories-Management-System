package Employee;

import java.sql.Date;



public class Model 
{
    private int Id;
    private String name;
    private String category;
    private int price;
    private int stock;
    private Date added_date;

    public int getId() 
    {
        return Id;
    }

    public void setId(int Id) 
    {
        this.Id = Id;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getAdded_date() {
        return added_date;
    }

    public void setAdded_date(Date added_date) {
        this.added_date = added_date;
    }
    
    
    
    
}
