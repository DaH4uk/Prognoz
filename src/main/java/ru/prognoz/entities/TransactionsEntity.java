package ru.prognoz.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author:  Туров Данил
 * Дата создания: 20.08.2016
 * Реализует сущность транзакции.
 * Содержит поля, сеттеры и геттеры для соответствующих полей в таблице.
 * The Prognoz Test Project
 */
@Entity
@Table(name = "transactions", schema = "prognoz")
public class TransactionsEntity {
    private int transactionId;  //id транзакции
    private int writeoffAccountId;  //счет списания
    private Integer refillAccountId;    //Счет зачисления
    private double sum; //сумма
    private Timestamp transactionTime;  //дата/время транзакции
    private String decription;  //описание

    //автоинкремент поля
    @Id
    @GenericGenerator(name="kaugen" , strategy="increment")
    @GeneratedValue(generator="kaugen")
    @Column(name = "TRANSACTION_ID", nullable = false, unique = true)
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "WRITEOFF_ACCOUNT_ID", nullable = false)
    public int getWriteoffAccountId() {
        return writeoffAccountId;
    }

    public void setWriteoffAccountId(int writeoffAccountId) {
        this.writeoffAccountId = writeoffAccountId;
    }

    @Basic
    @Column(name = "REFILL_ACCOUNT_ID", nullable = true)
    public Integer getRefillAccountId() {
        return refillAccountId;
    }

    public void setRefillAccountId(Integer refillAccountId) {
        this.refillAccountId = refillAccountId;
    }

    @Basic
    @Column(name = "SUM", nullable = false, precision = 0)
    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    @Basic
    @Column(name = "TRANSACTION_TIME", nullable = true)
    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Basic
    @Column(name = "DECRIPTION", nullable = true, length = 255)
    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionsEntity that = (TransactionsEntity) o;

        if (transactionId != that.transactionId) return false;
        if (writeoffAccountId != that.writeoffAccountId) return false;
        if (Double.compare(that.sum, sum) != 0) return false;
        if (refillAccountId != null ? !refillAccountId.equals(that.refillAccountId) : that.refillAccountId != null)
            return false;
        if (transactionTime != null ? !transactionTime.equals(that.transactionTime) : that.transactionTime != null)
            return false;
        if (decription != null ? !decription.equals(that.decription) : that.decription != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = transactionId;
        result = 31 * result + writeoffAccountId;
        result = 31 * result + (refillAccountId != null ? refillAccountId.hashCode() : 0);
        temp = Double.doubleToLongBits(sum);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (transactionTime != null ? transactionTime.hashCode() : 0);
        result = 31 * result + (decription != null ? decription.hashCode() : 0);
        return result;
    }
}
