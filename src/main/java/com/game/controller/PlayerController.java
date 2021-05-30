package com.game.controller;

import com.game.entity.Player;

import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping ("/rest/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody Player player){
        Player createPlayer = playerService.create(player);
        if (createPlayer == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Player>> getList(@RequestParam Map<String, String> map){
        int pageNumber = 0;
        int pageSize = 3;
        PlayerOrder playerOrder = PlayerOrder.ID;
        List<Player> responsePlayers = new ArrayList<>();
        List<Player> players = playerService.getList();

        if (map.containsKey("name")){
            String findByName = map.get("name");
            Iterator<Player> playerIterator = players.iterator();
            Pattern pattern = Pattern.compile(findByName.toLowerCase());
            System.out.println("nen");
            while (playerIterator.hasNext()){
                Player nextPlayer = playerIterator.next();
                String lowName = nextPlayer.getName().toLowerCase();
                Matcher matcher = pattern.matcher(lowName);
                if (!matcher.find()){
                        playerIterator.remove();
                }
            }
        }

        if (map.containsKey("title")){
            String findByTitle = map.get("title");
            Iterator<Player> playerIterator = players.iterator();
            Pattern pattern = Pattern.compile(findByTitle.toLowerCase());
            while (playerIterator.hasNext()){
                Player nextPlayer = playerIterator.next();
                String lowTitle = nextPlayer.getTitle().toLowerCase();
                Matcher matcher = pattern.matcher(lowTitle);
                if (!matcher.find()){
                        playerIterator.remove();
                }
            }
        }

        if (map.containsKey("race")){
            Race searchRace;
            String race = map.get("race");
            switch (race.toUpperCase()){
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
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getRace() != searchRace){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("profession")){
            Profession searchProfession;
            String profession = map.get("profession");
            switch (profession.toUpperCase()){
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
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getProfession() != searchProfession){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("after")){
            String afterBirthday = map.get("after");
            long afterTime = Long.parseLong(afterBirthday);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getBirthday().getTime() < afterTime){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("before")){
            String before = map.get("before");
            long beforeTime = Long.parseLong(before);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getBirthday().getTime() > beforeTime){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("minExperience")){
            String sMinExperience = map.get("minExperience");
            int minExperience = Integer.parseInt(sMinExperience);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getExperience() < minExperience){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("maxExperience")){
            String sMaxExperience = map.get("maxExperience");
            int maxExperience = Integer.parseInt(sMaxExperience);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getExperience() > maxExperience){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("minLevel")){
            String sMinLevel = map.get("minLevel");
            int minLevel = Integer.parseInt(sMinLevel);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getLevel() < minLevel){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("maxLevel")){
            String sMaxLevel = map.get("maxLevel");
            int maxLevel = Integer.parseInt(sMaxLevel);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getLevel() > maxLevel){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("banned")){
            String sBanned = map.get("banned");
            boolean banned = Boolean.parseBoolean(sBanned);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getBanned() != banned){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("order")){
            String stringOrder = map.get("order");
            switch (stringOrder.toUpperCase()){
                case "NAME": playerOrder = PlayerOrder.NAME;
                break;
                case "EXPERIENCE": playerOrder = PlayerOrder.EXPERIENCE;
                break;
                case "BIRTHDAY": playerOrder = PlayerOrder.BIRTHDAY;
                break;
                case "LEVEL": playerOrder = PlayerOrder.LEVEL;
                break;
            }
        }

        if (map.containsKey("pageNumber")){
            pageNumber = Integer.parseInt(map.get("pageNumber"));
        }
        if (map.containsKey("pageSize")){
            pageSize = Integer.parseInt(map.get("pageSize"));
        }
        for (int i = pageNumber*pageSize + 1; i < pageNumber*pageSize + 1 + pageSize; i++){
            if (i <= players.size()) {
                responsePlayers.add(players.get(i - 1));
            }
        }
        return new ResponseEntity<>(responsePlayers, HttpStatus.OK);
    }

    @GetMapping(value = "/count")
    public ResponseEntity<Integer> getCount(@RequestParam Map<String, String> map){
        int count;
        List<Player> players = playerService.getList();
        if (map.containsKey("name")){
            String findByName = map.get("name");
            Iterator<Player> playerIterator = players.iterator();
            Pattern pattern = Pattern.compile(findByName.toLowerCase());
            System.out.println("nen");
            while (playerIterator.hasNext()){
                Player nextPlayer = playerIterator.next();
                String lowName = nextPlayer.getName().toLowerCase();
                Matcher matcher = pattern.matcher(lowName);
                if (!matcher.find()){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("title")){
            String findByTitle = map.get("title");
            Iterator<Player> playerIterator = players.iterator();
            Pattern pattern = Pattern.compile(findByTitle.toLowerCase());
            while (playerIterator.hasNext()){
                Player nextPlayer = playerIterator.next();
                String lowTitle = nextPlayer.getTitle().toLowerCase();
                Matcher matcher = pattern.matcher(lowTitle);
                if (!matcher.find()){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("race")){
            Race searchRace;
            String race = map.get("race");
            switch (race.toUpperCase()){
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
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getRace() != searchRace){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("profession")){
            Profession searchProfession;
            String profession = map.get("profession");
            switch (profession.toUpperCase()){
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
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getProfession() != searchProfession){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("after")){
            String afterBirthday = map.get("after");
            long afterTime = Long.parseLong(afterBirthday);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getBirthday().getTime() < afterTime){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("before")){
            String before = map.get("before");
            long beforeTime = Long.parseLong(before);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getBirthday().getTime() > beforeTime){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("minExperience")){
            String sMinExperience = map.get("minExperience");
            int minExperience = Integer.parseInt(sMinExperience);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getExperience() < minExperience){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("maxExperience")){
            String sMaxExperience = map.get("maxExperience");
            int maxExperience = Integer.parseInt(sMaxExperience);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getExperience() > maxExperience){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("minLevel")){
            String sMinLevel = map.get("minLevel");
            int minLevel = Integer.parseInt(sMinLevel);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getLevel() < minLevel){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("maxLevel")){
            String sMaxLevel = map.get("maxLevel");
            int maxLevel = Integer.parseInt(sMaxLevel);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getLevel() > maxLevel){
                    playerIterator.remove();
                }
            }
        }

        if (map.containsKey("banned")){
            String sBanned = map.get("banned");
            boolean banned = Boolean.parseBoolean(sBanned);
            Iterator<Player> playerIterator = players.iterator();
            while (playerIterator.hasNext()){
                Player player = playerIterator.next();
                if (player.getBanned() != banned){
                    playerIterator.remove();
                }
            }
        }
        count = players.size();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPlayer(@PathVariable (name = "id") Long id){
        if (id == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = playerService.get(id);
        if (player != null){
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable(name = "id") Long id, @RequestBody Player player){
        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.getExperience() != null) {
            if (player.getExperience() < 0 || player.getExperience() > 10_000_000) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        if (player.getBirthday() != null) {
            if (player.getBirthday().getTime() > 0){
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(player.getBirthday());
                if (calendar.get(Calendar.YEAR) < 2000 || calendar.get(Calendar.YEAR) > 3000) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player idPlayer = playerService.update(player, id);
        if (idPlayer != null){
            return new ResponseEntity<>(idPlayer, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable(name = "id") Long id){
        //String test = "" + id;
        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return playerService.delete(id) ? new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
