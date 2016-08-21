package ru.prognoz.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author:  Туров Данил
 * Дата создания: 16.08.2016
 * Реализует сущность счета клиента.
 * Содержит поля, сеттеры и геттеры для соответствующих полей в таблице.
 * The Prognoz Test Project
 */
@Entity
@Table(name = "clients", schema = "prognoz", catalog = "")
public class ClientsEntity {
    private int id;
    private String name;
    private String address;
    private Integer age;


    @Id
    @GenericGenerator(name="kaugen" , strategy="increment")
    @GeneratedValue(generator="kaugen")
    @Column(name = "id", nullable = false, unique = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientsEntity that = (ClientsEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "address", nullable = false, length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "age", nullable = true)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
