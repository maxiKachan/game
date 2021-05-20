package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {

    Player create (Player player);

    Player get(Long id);

    Player update (Player player, Long id);

    boolean delete(Long id);

    List<Player> getList();
}
