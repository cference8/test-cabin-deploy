package com.cf.cabinescaperoom.controllers;


import com.cf.cabinescaperoom.config.oAuth.CustomOAuth2User;
import com.cf.cabinescaperoom.models.ScoreForm;
import com.cf.cabinescaperoom.models.User;
import com.cf.cabinescaperoom.service.ScoreFormService;
import com.cf.cabinescaperoom.service.UserService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ScoreFormController {

    @Autowired
    ScoreFormService service;

    @Autowired
    UserService userService;

    @GetMapping("form")
    public String displayTestScoreForm(Model model) {
        List<ScoreForm> scoreFormList = service.getAll();
        model.addAttribute("scores", scoreFormList);
        return "form";
    }

    @GetMapping("scoreForm")
    public String displayScoreForm(Model model) {return "scoreForm";}

    @PostMapping("addScoreForm")
    public String addScoreForm(HttpServletRequest request) {

        String name1 = request.getParameter("name1");
        String name2 = request.getParameter("name2");
        String name3 = request.getParameter("name3");
        String name4 = request.getParameter("name4");
        String name5 = request.getParameter("name5");
        String name6 = request.getParameter("name6");
        String activity_name = request.getParameter("activity_name");
        LocalDate date = LocalDate.parse(request.getParameter("completion_date"));
        int minutes = Integer.parseInt(request.getParameter("minutes"));
        int help_cards = Integer.parseInt(request.getParameter("help_cards"));

        ScoreForm scoreForm = new ScoreForm();
        if(!Strings.isNullOrEmpty(name1) && !Strings.isNullOrEmpty(name2) && !Strings.isNullOrEmpty(name3)
            && !Strings.isNullOrEmpty(activity_name) && !Strings.isNullOrEmpty(request.getParameter("minutes"))
        && !Strings.isNullOrEmpty(request.getParameter("help_cards"))) {
            scoreForm.setName1(name1);
            scoreForm.setName2(name2);
            scoreForm.setName3(name3);
            scoreForm.setName4(name4);
            scoreForm.setName5(name5);
            scoreForm.setName6(name6);
            scoreForm.setActivity_name(activity_name);
            scoreForm.setCompletion_date(date);
            scoreForm.setMinutes(minutes);
            scoreForm.setHelp_cards(help_cards);
        }

        User user;
        CustomOAuth2User oAuthUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().size() > 2){ // Best way I could think of to check if oAuth2User
            oAuthUser = (CustomOAuth2User) authentication.getPrincipal();
            if(oAuthUser != null) {
                user = userService.getUserByUsername(oAuthUser.getEmail());
                scoreForm.setUser(user);
            }
        } else {
            org.springframework.security.core.userdetails.User localUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            User scoreFormUser = userService.getUserByUsername(localUser.getUsername());
            scoreForm.setUser(scoreFormUser);
        }


        service.addScoreForm(scoreForm);

        return "redirect:/viewAllScores";
    }

    @GetMapping("editScoreForm")
    public String editScoreForm(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        ScoreForm scoreForm = service.getById(id);

        Map<Integer, String> helpCards = new HashMap<>();
        helpCards.put( 0, " No Help Cards");
        helpCards.put( 1, " 1-2 Help Cards");
        helpCards.put( 2, " 3-5 Help Cards");
        helpCards.put( 3, " 6-10 Help Cards");
        helpCards.put( 4, " > 10 Help Cards");

        model.addAttribute("helpCards", helpCards.entrySet());
        model.addAttribute("scoreForm", scoreForm);
        return "editScoreForm";
    }

    @PostMapping("editScoreForm")
    public String preformEditScoreForm(ScoreForm scoreForm) {

        User user;
        CustomOAuth2User oAuthUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().size() > 2){
            oAuthUser = (CustomOAuth2User) authentication.getPrincipal();
            if(oAuthUser != null) {
                user = userService.getUserByUsername(oAuthUser.getEmail());
                scoreForm.setUser(user);
            }
        } else {
            org.springframework.security.core.userdetails.User localUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            user = userService.getUserByUsername(localUser.getUsername());
            scoreForm.setUser(user);
        }

        service.editScoreForm(scoreForm);

        return "redirect:/viewAllScores";
    }

    @GetMapping("viewDetails")
    public String viewDetails(long id, Model model) {
        ScoreForm scoreForm = service.getById(id);
        List<ScoreForm> scoreFormList = service.getAll();

        List<String> names = new ArrayList<>();
        names.add(scoreForm.getName1());
        names.add(scoreForm.getName2());
        names.add(scoreForm.getName3());
        names.add(scoreForm.getName4());
        names.add(scoreForm.getName5());
        names.add(scoreForm.getName6());

        List<String> listCards = new ArrayList<>();
        listCards.add(0,"Zero used");
        listCards.add(1,"1-2 used");
        listCards.add(2,"3-5 used");
        listCards.add(3,"6-10 used");
        listCards.add(4,"> 10 used");

        String help_cards = "error"; // this string value should never show in page
        // create help cards under HTML selected element
        for(int i = 0; i < listCards.size(); i++){
            if(i == scoreForm.getHelp_cards()){
                help_cards = listCards.get(i);
            }
        }
        //  create a ranking order by # of stars
        int i = 1;
        for(ScoreForm item : scoreFormList){
            item.setRank(i);
            i++;
        }
        model.addAttribute("help_cards", help_cards);
        model.addAttribute("score", scoreForm);
        model.addAttribute("names", names);
        model.addAttribute("scores", scoreFormList);

        return "viewDetails";
    }

    @GetMapping("viewAllScores")
    public String viewAllScores(Model model) {
        List<ScoreForm> scoreFormList = service.getAll();
        
        // create a ranking ordered by # of stars
        int i = 1;
        for(ScoreForm item : scoreFormList){
            item.setRank(i);
            i++;
        }
        model.addAttribute("scores", scoreFormList);

        return "viewScores";
    }


/**
 * Save for addScore
 *          String name1 = request.getParameter("name1");
 *         String name2 = request.getParameter("name2");
 *         String name3 = request.getParameter("name3");
 *         String name4 = request.getParameter("name4");
 *         String name5 = request.getParameter("name5");
 *         String name6 = request.getParameter("name6");
 *         String activity_name = request.getParameter("activity_name");
 *         LocalDate date = LocalDate.parse(request.getParameter("date"), formatter);
 *         int minutes = Integer.parseInt(request.getParameter("minutes"));
 *         int help_cards = Integer.parseInt(request.getParameter("help_cards"));
 *
 *         ScoreForm scoreForm = new ScoreForm();
 *         scoreForm.setName1(name1);
 *         scoreForm.setName2(name2);
 *         scoreForm.setName3(name3);
 *         scoreForm.setName4(name4);
 *         scoreForm.setName5(name5);
 *         scoreForm.setName6(name6);
 *         scoreForm.setActivity_name(activity_name);
 *         scoreForm.setDate(date);
 *         scoreForm.setMinutes(minutes);
 *         scoreForm.setHelp_cards(help_cards);
 *         scoreForm.setUser(scoreFormUser);
 */
}
