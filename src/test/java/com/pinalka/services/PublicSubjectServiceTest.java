package com.pinalka.services;

import java.util.Collections;

import com.pinalka.dao.PublicSubjectDAO;
import com.pinalka.domain.PublicSubject;
import com.pinalka.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PublicSubjectServiceTest {
    
    @Mock
    private PublicSubjectDAO publicSubjectDAO;
    
    @InjectMocks
    private PublicSubjectService publicSubjectService;
    
    @Test
    public void testGetPublicSubjects() throws Exception {
        when(publicSubjectDAO.getList()).thenReturn(Collections.<PublicSubject>emptyList());
        
        assertEquals(Collections.<PublicSubject>emptyList(), publicSubjectService.getPublicSubjects());
        
        verify(publicSubjectDAO).getList();
    }

    @Test
    public void testGetRecommendedPublicSubjects() throws Exception {
        when(publicSubjectDAO.getList()).thenReturn(Collections.<PublicSubject>emptyList());
        
        assertEquals(Collections.<PublicSubject>emptyList(), publicSubjectService.getRecommendedPublicSubjects(new User()));

        verify(publicSubjectDAO).getList();
        
    }
}
