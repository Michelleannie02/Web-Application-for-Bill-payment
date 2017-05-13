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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author herma
 */
@Entity
@Table(name = "EXPRESSUNION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expressunion.findAll", query = "SELECT e FROM Expressunion e"),
    @NamedQuery(name = "Expressunion.findById", query = "SELECT e FROM Expressunion e WHERE e.id = :id"),
    @NamedQuery(name = "Expressunion.findByFileName", query = "SELECT e FROM Expressunion e WHERE e.fileName = :fileName"),
    @NamedQuery(name = "Expressunion.findByNumTrans", query = "SELECT e FROM Expressunion e WHERE e.numTrans = :numTrans"),
    @NamedQuery(name = "Expressunion.findByStatutTrans", query = "SELECT e FROM Expressunion e WHERE e.statutTrans = :statutTrans"),
    @NamedQuery(name = "Expressunion.findByLine", query = "SELECT e FROM Expressunion e WHERE e.line = :line"),
    @NamedQuery(name = "Expressunion.findByCommentaire", query = "SELECT e FROM Expressunion e WHERE e.commentaire = :commentaire")})
public class Expressunion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EXPRESSUNION")
    @SequenceGenerator(
            name = "SEQUENCE_EXPRESSUNION",
            sequenceName = "SEQUENCE_EXPRESSUNION",
            initialValue = 000000,
            allocationSize = 1
    )
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 50)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Size(max = 50)
    @Column(name = "NUM_TRANS")
    private String numTrans;
    @Size(max = 50)
    @Column(name = "STATUT_TRANS")
    private String statutTrans;
    @Size(max = 50)
    @Column(name = "LINE")
    private String line;
    @Size(max = 200)
    @Column(name = "COMMENTAIRE")
    private String commentaire;

    public Expressunion() {
    }

    public Expressunion(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNumTrans() {
        return numTrans;
    }

    public void setNumTrans(String numTrans) {
        this.numTrans = numTrans;
    }

    public String getStatutTrans() {
        return statutTrans;
    }

    public void setStatutTrans(String statutTrans) {
        this.statutTrans = statutTrans;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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
        if (!(object instanceof Expressunion)) {
            return false;
        }
        Expressunion other = (Expressunion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.camtel.expressunion.entities.Expressunion[ id=" + id + " ]";
    }
    
}
