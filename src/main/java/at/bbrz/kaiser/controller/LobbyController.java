package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.model.RoomDto;
import at.bbrz.kaiser.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LobbyController {

    @Autowired
    private RoomService roomService;


    @GetMapping("/lobby")
    public String loadLobby(Model model) {
        List<RoomDto> rooms = roomService.getAllRooms().stream()
                .map(room -> new RoomDto(room.getName(), room.getUsers().size(), room.getUuid()))
                .toList();

        model.addAttribute("rooms", rooms);
        return "lobby";
    }


}
