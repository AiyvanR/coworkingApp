package entities;

import java.io.Serializable;

public class CoworkingSpace implements Serializable {
    private int id;
    private String type;
    private int price;
    private String status;

    public CoworkingSpace(int id, String type, int price) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.status = "Available";
    }

    public CoworkingSpace() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CoworkingSpace{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
