/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.camtel.expressunion.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author herma
 */
@Entity
@Table(name = "PAYITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payitem.findAll", query = "SELECT p FROM Payitem p"),
    @NamedQuery(name = "Payitem.findById", query = "SELECT p FROM Payitem p WHERE p.id = :id"),
    @NamedQuery(name = "Payitem.findByTranscid", query = "SELECT p FROM Payitem p WHERE p.transcid = :transcid"),
    @NamedQuery(name = "Payitem.findByAccountid", query = "SELECT p FROM Payitem p WHERE p.accountid = :accountid"),
    @NamedQuery(name = "Payitem.findByInvoiceno", query = "SELECT p FROM Payitem p WHERE p.invoiceno = :invoiceno"),
    @NamedQuery(name = "Payitem.findByAmount", query = "SELECT p FROM Payitem p WHERE p.amount = :amount")})
public class Payitem implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 50)
    @Column(name = "TRANSCID")
    private String transcid;
    @Size(max = 50)
    @Column(name = "ACCOUNTID")
    private String accountid;
    @Size(max = 50)
    @Column(name = "INVOICENO")
    private String invoiceno;
    @Size(max = 50)
    @Column(name = "AMOUNT")
    private String amount;

    public Payitem() {
    }

    public Payitem(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getTranscid() {
        return transcid;
    }

    public void setTranscid(String transcid) {
        this.transcid = transcid;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payitem)) {
            return false;
        }
        Payitem other = (Payitem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.camtel.expressunion.entities.Payitem[ id=" + id + " ]";
    }
    
}
