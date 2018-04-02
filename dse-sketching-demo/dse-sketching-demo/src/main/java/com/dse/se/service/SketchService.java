package com.dse.se.service;

import com.dse.se.dao.ISketchDAO;
import com.dse.se.dto.TopHashTagsDTO;
import com.dse.se.dto.UniqueUsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by michaelraney on 3/18/18.
 */
@Component
public class SketchService {

    @Autowired
    ISketchDAO sketchDAO;

    Integer elapsedTimeInMills = 60 * 60 * 1;//One Hour

    public UniqueUsersDTO getUniqueUsersForToday() throws ParseException {

        Date now = new Date();
        Date relativeElapsedTime = new Date(now.getTime() - elapsedTimeInMills);
        return sketchDAO.getUniqueUsersForToday(now, relativeElapsedTime);

    }

    public UniqueUsersDTO getUniqueUsersRollup() throws ParseException {

        Date now = new Date();
        Date relativeElapsedTime = new Date(now.getTime() - elapsedTimeInMills);

        return sketchDAO.getUniqueUsersRollup(now, relativeElapsedTime);
    }

    public TopHashTagsDTO getTopTweetsRollup() throws ParseException {
        return sketchDAO.getTopTweetsRollup(new Date());

    }
}