package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepo playerRepo;

    @Override
    public Player create(Player player) {
        if (player.getName() == null || player.getTitle() == null || player.getRace() == null ||
                player.getProfession() == null || player.getBirthday() == null || player.getExperience() == null ||
        player.getName().isEmpty()){
            return null;
        }
        if (player.getName().length() > 12 || player.getTitle().length() > 30){
            return null;
        }
        if (player.getName().equals("")){
            return null;
        }
        if (player.getExperience() < 0 || player.getExperience() > 10_000_000){
            return null;
        }
        if (player.getBirthday().getTime() < 0){
            return null;
        }
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(player.getBirthday());
        if (calendar.get(Calendar.YEAR) < 2000 || calendar.get(Calendar.YEAR) > 3000){
            return null;
        }
        if (player.getBanned() == null){
            player.setBanned(false);
        }
        player.setLevel(countLevel(player.getExperience()));
        player.setUntilNextLevel(countUntilNextLevel(player.getLevel(), player.getExperience()));
        if (player.getBanned() == null) {
            player.setBanned(false);
        }
        return playerRepo.save(player);

    }

    @Override
    public Player get(Long id) {
        if (playerRepo.existsById(id)){
            return playerRepo.findById(id).orElse(null);
        }
        return null;
    }

    @Override
    public Player update(Player player, Long id) {
        Player idPlayer = get(id);
        if (idPlayer == null){
            return null;
        }
        if (player.getName() != null && player.getName().length() <= 12){
            idPlayer.setName(player.getName());
        }
        if (player.getTitle() != null && player.getTitle().length() <= 30){
            idPlayer.setTitle(player.getTitle());
        }
        if (player.getRace() != null){
            idPlayer.setRace(player.getRace());
        }
        if (player.getProfession() != null){
            idPlayer.setProfession(player.getProfession());
        }
        if (player.getBirthday() != null && player.getBirthday().getTime() > 0){
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(player.getBirthday());
            if (calendar.get(Calendar.YEAR) >= 2000 && calendar.get(Calendar.YEAR) <= 3000){
                idPlayer.setBirthday(player.getBirthday());
            }
        }
        if (player.getBanned() != null){
            idPlayer.setBanned(player.getBanned());
        }
        if (player.getExperience() != null){
            idPlayer.setExperience(player.getExperience());
            idPlayer.setLevel(countLevel(idPlayer.getExperience()));
            idPlayer.setUntilNextLevel(countUntilNextLevel(idPlayer.getLevel(), idPlayer.getExperience()));
        }
        return idPlayer;
    }

    @Override
    public boolean delete(Long id) {
        if (playerRepo.existsById(id)) {
            playerRepo.deleteById(id);
            return true;
        }
        return false;
    }

    private Integer countLevel(Integer exp){
        return  (int) (Math.sqrt(2500+200*exp)-50)/100;
    }

    private Integer countUntilNextLevel(Integer level, Integer exp){
        return 50 * (level + 1) * (level + 2) - exp;
    }

    @Override
    public List<Player> getList() {
        Iterable<Player> iterPlayer = playerRepo.findAll();
        List<Player> players = new ArrayList<>();
        iterPlayer.forEach(players :: add);

        return players;
    }

    @Override
    public List<Player> getListWithFilter(Map<String, String> map) {
        List<Player> listWithFilter = getList();

        if (map.containsKey("name")){
            getListWithNameAndTitleFilter(map.get("name"),"name", listWithFilter);
        }

        if (map.containsKey("title")){
            getListWithNameAndTitleFilter(map.get("title"), "title", listWithFilter);
        }

        if (map.containsKey("race")){
            getListWithRaceFilter(map.get("race"), listWithFilter);
        }

        if (map.containsKey("profession")){
            getListWithProfessionFilter(map.get("profession"),listWithFilter);
        }

        if (map.containsKey("after")){
            getListWithOtherFilter("after", listWithFilter, Long.parseLong(map.get("after")));
        }

        if (map.containsKey("before")){
            getListWithOtherFilter("before", listWithFilter, Long.parseLong(map.get("before")));
        }

        if (map.containsKey("minExperience")){
            getListWithOtherFilter("minExperience", listWithFilter, Long.parseLong(map.get("minExperience")));
        }

        if (map.containsKey("maxExperience")){
            getListWithOtherFilter("maxExperience", listWithFilter, Long.parseLong(map.get("maxExperience")));
        }

        if (map.containsKey("minLevel")){
            getListWithOtherFilter("minLevel", listWithFilter, Long.parseLong(map.get("minLevel")));
        }

        if (map.containsKey("maxLevel")){
            getListWithOtherFilter("maxLevel", listWithFilter, Long.parseLong(map.get("maxLevel")));
        }

        return listWithFilter;
    }

    private void getListWithNameAndTitleFilter(String filter,String changer, List<Player> players){
        Iterator<Player> playerIterator = players.iterator();
        Pattern pattern = Pattern.compile(filter.toLowerCase());

        while (playerIterator.hasNext()){
            if (changer.equals("name")) {
                if (!pattern.matcher(playerIterator.next().getName().toLowerCase()).find()) {
                    playerIterator.remove();
                }
            } else if (changer.equals("title")){
                if (!pattern.matcher(playerIterator.next().getTitle().toLowerCase()).find()) {
                    playerIterator.remove();
                }
            }
        }
    }

    private void getListWithRaceFilter(String filter, List<Player> players){
        Race searchRace;
        switch (filter.toUpperCase()){
            case "HUMAN" : searchRace = Race.HUMAN;
                break;
            case "DWARF" : searchRace = Race.DWARF;
                break;
            case "ELF" : searchRace = Race.ELF;
                break;
            case "GIANT" : searchRace = Race.GIANT;
                break;
            case "ORC" : searchRace = Race.ORC;
                break;
            case "TROLL" : searchRace = Race.TROLL;
                break;
            default: searchRace = Race.HOBBIT;
        }
        players.removeIf(player -> player.getRace() != searchRace);
    }

    private void getListWithProfessionFilter(String filter, List<Player> players){
        Profession searchProfession;
        switch (filter.toUpperCase()){
            case "WARRIOR": searchProfession = Profession.WARRIOR;
                break;
            case "ROGUE": searchProfession = Profession.ROGUE;
                break;
            case "SORCERER": searchProfession = Profession.SORCERER;
                break;
            case "CLERIC": searchProfession = Profession.CLERIC;
                break;
            case "PALADIN": searchProfession = Profession.PALADIN;
                break;
            case "NAZGUL": searchProfession = Profession.NAZGUL;
                break;
            case "WARLOCK": searchProfession = Profession.WARLOCK;
                break;
            default: searchProfession = Profession.DRUID;
        }
        players.removeIf(player -> player.getProfession() != searchProfession);
    }

    private void getListWithOtherFilter(String filter, List<Player> players, Long value){
        switch (filter) {
            case "after":
                players.removeIf(player -> player.getBirthday().getTime() < value);
                break;
            case "before":
                players.removeIf(player -> player.getBirthday().getTime() > value);
                break;
            case "minExperience":
                players.removeIf(player -> player.getExperience() < value);
                break;
            case "maxExperience" :
                players.removeIf(player -> player.getExperience() > value);
                break;
            case "minLevel" :
                players.removeIf(player -> player.getLevel() < value);
                break;
            case "maxLevel" :
                players.removeIf(player -> player.getLevel() > value);
                break;
        }
    }
}
