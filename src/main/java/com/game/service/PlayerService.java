package com.game.service;

import com.game.entity.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {

    Player create (Player player);

    Player get(Long id);

    Player update (Player player, Long id);

    boolean delete(Long id);

    List<Player> getList();

    List<Player> getListWithFilter(Map<String, String> map);
}
