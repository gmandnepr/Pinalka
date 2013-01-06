package com.pinalka.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Represent public subject item in the system
 *
 * @author gman
 */
@Entity
public class PublicItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String label;

    private String description;

    private Date deadline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        if (deadline != null) {
            return new Date(deadline.getTime());
        } else {
            return null;
        }
    }

    public void setDeadline(Date deadline) {
        if (deadline != null) {
            this.deadline = new Date(deadline.getTime());
        } else {
            this.deadline = null;
        }
    }
}
