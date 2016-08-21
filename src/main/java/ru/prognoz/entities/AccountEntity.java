package ru.prognoz.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by turov on 06.08.2016.
 */
@Entity
@Table(name = "accounts", schema = "prognoz")
public class AccountEntity {
    private int id;
    private int client;
    private double sum;

    @Id
    @GenericGenerator(name="kaugen" , strategy="increment")
    @GeneratedValue(generator="kaugen")
    @Column(name = "id", nullable = false, unique = true)
    public int getId() {
        return id;
    }

    public void setId(int accountId) {
        this.id = accountId;
    }

    @Basic
    @Column(name = "client", nullable = false)
    public int getClient() {
        return client;
    }

    public void setClient(int userId) {
        this.client = userId;
    }


    @Basic
    @Column(name = "sum", nullable = false, precision = 0)
    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntity that = (AccountEntity) o;

        if (id != that.id) return false;
        if (client != that.client) return false;
        if (Double.compare(that.sum, sum) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + client;
        temp = Double.doubleToLongBits(sum);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
