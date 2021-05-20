package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepo playerRepo;

    @Override
    public Player create(Player player) {
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
            return playerRepo.findById(id).get();
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
        for (Player player : iterPlayer) {
            players.add(player);
        }
        return players;
    }
}
