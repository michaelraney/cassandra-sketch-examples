package com.dse.se.dao;

import com.dse.se.dto.TopHashTagsDTO;
import com.dse.se.dto.UniqueUsersDTO;

import java.text.ParseException;
import java.util.Date;

public interface ISketchDAO {

     UniqueUsersDTO getUniqueUsersForToday(Date day) throws ParseException;

     UniqueUsersDTO getUniqueUsersRollup(Date day)throws ParseException;

     TopHashTagsDTO getTopTweetsRollup(Date day)throws ParseException;

}
