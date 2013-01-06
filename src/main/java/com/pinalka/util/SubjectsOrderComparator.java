package com.pinalka.util;

import java.io.Serializable;
import java.util.Comparator;

import com.pinalka.domain.Subject;

/**
 * Sort subjects with respect to priority
 *
 * @author gman
 */
public class SubjectsOrderComparator implements Comparator<Subject>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final Subject o1, final Subject o2) {
        return o2.getPriority() - o1.getPriority();
    }
}
