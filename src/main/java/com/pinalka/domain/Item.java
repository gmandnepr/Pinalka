package com.pinalka.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Represent subject item in the system
 *
 * @author gman
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String label;

    private String description;

    private Date deadline;

    private State state;
    
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Item item = (Item) o;

        if (id != null) {
            return id.equals(item.getId());
        } else {
            return label != null && label.equals(item.getLabel());
        }
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
