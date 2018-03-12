package com.dse.se.dao;

import com.dse.se.dto.TopHashTagsDTO;
import com.dse.se.dto.UniqueUsersDTO;

import java.text.ParseException;

public interface ISketchDAO {

     UniqueUsersDTO getUniqueUsersForToday() throws ParseException;

     UniqueUsersDTO getUniqueUsersRollup()throws ParseException;

     TopHashTagsDTO getTopTweetsRollup()throws ParseException;

}
