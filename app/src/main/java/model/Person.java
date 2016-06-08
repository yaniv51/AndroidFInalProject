package model;

import java.util.LinkedList;
import java.util.List;


public class Person {
    String id;
    String firstName;
    String lastName;
    String phone;
    String email;
    String password;

    private final static Person instance = new Person();
    List<Product> data = new LinkedList<Product>();

    public static Person instance(){
        return instance;
    }

    public Person(String id, String firstName, String lastName, String phone, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        init();
    }

    void init(){
    }

    public void add(Product pr){
        data.add(pr);
    }

    public Product getProduct(String id){
        for (int i=0; i<data.size(); i++) {
            Product pr = data.get(i);

            if(pr.getId().equals(id)){
                return pr;
            }
        }
        return null;
    }

    public void updateProductInformation(String productId, Product newProduct){
        for(int i=0; i<data.size(); i++) {
            if (data.get(i).getId().compareTo(productId) == 0)
            {
                Product pr = data.get(i);
                pr.setType(newProduct.getType());
                pr.setDescription(newProduct.getDescription());
                pr.setPrice(newProduct.getPrice());
                pr.setForWhom(newProduct.getForWhom());
                pr.setImageProduct(newProduct.getImageProduct());
                pr.setId(newProduct.getId());
            }
        }
    }

    public List<Product> getProducts(){
        return data;
    }

    public boolean isIdValid(String productId){
        for(Product pr: data)
        {
            if(pr.getId().compareTo(productId)==0)
                return false;
        }
        return true;
    }

    // *************** GETTERS AND SETTERS ***************


    public String getId() {
        return id;
    }

    public void setId(String value) { this.id = value;}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

