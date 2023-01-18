package ru.prognoz.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Table;
import org.springframework.data.annotation.Id;

/**
 * @author:  Туров Данил
 * Дата создания: 16.08.2016
 * Реализует сущность счета клиента.
 * Содержит поля, сеттеры и геттеры для соответствующих полей в таблице.
 * The Prognoz Test Project
 */
@Entity
@Table(appliesTo = "account")
public class AccountEntity {
    private long id; //id счета
    private int client; //id клиента
    private double sum; //сумма на счету

    @Id
    @GenericGenerator(name="kaugen" , strategy="increment")
    @GeneratedValue(generator="kaugen")
    @Column(name = "id", nullable = false, unique = true)
    public long getId() {
        return id;
    }

    public void setId(long accountId) {
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
        result = Math.toIntExact(id);
        result = 31 * result + client;
        temp = Double.doubleToLongBits(sum);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
